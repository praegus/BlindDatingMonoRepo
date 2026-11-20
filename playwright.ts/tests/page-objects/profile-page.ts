import type { Page } from '@playwright/test';
import { expect } from '@playwright/test';

export class ProfilePage {
    constructor(readonly page: Page) {}

    async goto() {
        await this.page.goto(process.env.FRONTEND_URL ?? 'https://frontend.blinddatingapp.finkingma.com/');
    }

    async createNewProfile(username: string, password: string) {
        await this.page.getByRole('button', { name: 'Register', exact: true }).click();
        await this.page.locator('#username').fill(username);
        await this.page.locator('#password').fill(password);
        await this.page.locator('#password-confirm').fill(password);
        await this.page.getByRole('button', { name: 'Register', exact: true }).click();
        await expect(this.page.getByText(`Welcome ${username}`, { exact: true })).toBeVisible();
    }

    async loginExistingProfile(username: string, password: string) {
        await this.page.getByRole('button', { name: 'Login', exact: true }).click();
        await this.page.locator('#username').fill(username);
        await this.page.locator('#password').fill(password);
        await this.page.locator('#kc-login').click();
        await expect(this.page.getByText(`Welcome ${username}`, { exact: true })).toBeVisible();
    }
}
