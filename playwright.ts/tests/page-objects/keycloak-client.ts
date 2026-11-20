import type { APIRequestContext } from '@playwright/test';

type KeycloakUser = {
    id: string;
    username: string;
    serviceAccountClientId?: string;
};

export class KeycloakClient {
    constructor(private readonly request: APIRequestContext) {}

    private requiredEnv(name: string): string {
        const value = process.env[name];
        if (!value?.trim()) throw new Error(`Missing required environment variable: ${name}`);
        return value;
    }

    private get usersUrl(): string {
        const baseUrl = this.requiredEnv('KEYCLOAK_URL').replace(/\/+$/, '');
        const realm = encodeURIComponent(this.requiredEnv('KEYCLOAK_REALM'));
        return `${baseUrl}/admin/realms/${realm}/users`;
    }

    private async authorization(): Promise<Record<string, string>> {
        const baseUrl = this.requiredEnv('KEYCLOAK_URL').replace(/\/+$/, '');
        const realm = encodeURIComponent(this.requiredEnv('KEYCLOAK_REALM'));
        const response = await this.request.post(
            `${baseUrl}/realms/${realm}/protocol/openid-connect/token`,
            {
                form: {
                    grant_type: 'client_credentials',
                    client_id: this.requiredEnv('KEYCLOAK_CLEANUP_CLIENT_ID'),
                    client_secret: this.requiredEnv('KEYCLOAK_CLEANUP_CLIENT_SECRET'),
                },
            },
        );
        if (!response.ok()) {
            throw new Error(`Keycloak token request failed: HTTP ${response.status()}`);
        }
        const { access_token } = await response.json();
        if (typeof access_token !== 'string' || !access_token) {
            throw new Error('Keycloak token response did not contain an access token');
        }
        return { Authorization: `Bearer ${access_token}` };
    }

    async getAccounts(): Promise<KeycloakUser[]> {
        const headers = await this.authorization();
        const accounts: KeycloakUser[] = [];
        const pageSize = 100;
        for (let first = 0; ; first += pageSize) {
            const response = await this.request.get(this.usersUrl, {
                headers,
                params: { first, max: pageSize },
            });
            if (!response.ok()) {
                throw new Error(`Keycloak user retrieval failed: HTTP ${response.status()}`);
            }
            const page: KeycloakUser[] = await response.json();
            accounts.push(...page);
            if (page.length < pageSize) return accounts;
        }
    }

    // Deletes test accounts only; normal users and service accounts are preserved.
    async deleteAllAccounts(): Promise<number> {
        // Finish pagination before deleting so offsets cannot shift during cleanup.
        const accounts = (await this.getAccounts()).filter(user =>
            user.username.startsWith('e2e') && !user.serviceAccountClientId,
        );
        if (!accounts.length) return 0;
        const headers = await this.authorization();
        for (const user of accounts) {
            const response = await this.request.delete(
                `${this.usersUrl}/${encodeURIComponent(user.id)}`,
                { headers },
            );
            // A user already removed by a previous cleanup is also a successful outcome.
            if (response.status() !== 204 && response.status() !== 404) {
                throw new Error(`Keycloak user deletion failed for ${user.id}: HTTP ${response.status()}`);
            }
        }
        return accounts.length;
    }
}
