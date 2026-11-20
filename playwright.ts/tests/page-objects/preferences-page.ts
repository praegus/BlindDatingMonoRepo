import type { Page } from '@playwright/test';
import { expect } from '@playwright/test';

export class PreferencesPage {
    constructor(readonly page: Page) {}

    async fillProfileDetails(firstName: string, street: string, streetNumber: string, postalCode: string, gender: string, preferredGender: string, color = '') {
        await this.page.getByRole('textbox', { name: 'First name', exact: true }).fill(firstName);
        await this.page.getByRole('textbox', { name: 'Last name', exact: true }).fill('');
        await this.page.getByRole('textbox', { name: 'Additional info' }).fill('Powered by Playwright');
        // Street and city are read-only and populated by the location service.
        await this.page.getByRole('textbox', { name: 'Street Number', exact: true }).fill(streetNumber);
        await this.page.getByRole('textbox', { name: 'Postal Code', exact: true }).fill(postalCode);
        await this.page.getByTestId(`personal-information-gender-option-${gender}`).click();
        await this.page.getByTestId(`preferences-gender-option-${preferredGender}`).click();
        for (const section of ['Personal Information', 'Preferences']) {
            const fields = this.page.locator('app-characteristics').filter({ has: this.page.getByRole('heading', { name: section, exact: true }) });
            await fields.getByRole('textbox', { name: 'Favorite Color' }).fill(color);
            await fields.getByRole('radio', { name: 'Yes', exact: true }).nth(0).check();
            await fields.getByRole('radio', { name: 'Yes', exact: true }).nth(1).check();
            await fields.getByRole('combobox', { name: 'Hair Color' }).click();
            await this.page.getByRole('option', { name: 'BLOND', exact: true }).click();
            await fields.getByRole('textbox', { name: 'Sports' }).fill('Voetbal');
        }
        const saved = this.page.waitForResponse(response =>
            response.request().method() === 'PUT' && new URL(response.url()).pathname.startsWith('/profiles/'),
        );
        await this.page.getByRole('button', { name: 'Save', exact: true }).click();
        expect((await saved).status()).toBe(200);
        await expect(this.page.getByText('Address is verified', { exact: true })).toBeVisible();
        await expect(this.page.getByRole('textbox', { name: 'Street', exact: true })).toHaveValue(street);
    }
}
