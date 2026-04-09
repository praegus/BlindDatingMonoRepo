import type { Page, Locator } from '@playwright/test';
import { expect } from '@playwright/test';

export class ProfilePage {

    readonly page: Page;


    constructor(page: Page) {
        this.page = page;

    }
    async goto() {
        await this.page.goto('http://frontend.blinddatingapp.finkingma.com/');
    }

    async createNewProfile(profileName: string) {
        await expect(this.page.getByRole('button', { name: 'Create' })).toBeDisabled();
        await this.page.getByRole('textbox').fill(profileName);
        await this.page.getByRole('button', { name: 'Create' }).click();
        expect(this.page.getByText(`Welcome ${profileName}`)).toBeVisible();

    }

    async loginExistingProfile(profileName: string) {
        await expect(this.page.getByRole('button', { name: 'Create' })).toBeDisabled();
        await this.page.getByRole('textbox').fill(profileName);
        await this.page.getByRole('button', { name: 'Login' }).click();
        expect(this.page.getByText(`Welcome ${profileName}`)).toBeVisible();

    }
}
