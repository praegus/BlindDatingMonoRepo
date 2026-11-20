import type { Page } from '@playwright/test';
import { expect } from '@playwright/test';
import { KeycloakClient } from './keycloak-client';

export class ProfileClient {
    constructor(readonly page: Page) {}

    async deleteAllProfiles() {
        const headers = await new KeycloakClient(this.page.request).authorization();
        const baseUrl = (process.env.PROFILE_API_URL ?? 'https://profile-service.blinddatingapp.finkingma.com').replace(/\/+$/, '');
        const response = await this.page.request.delete(`${baseUrl}/profiles`, { headers });
        expect(response.status(), 'Profile deletion requires a valid token with profile-service audience').toBe(204);
    }
}
