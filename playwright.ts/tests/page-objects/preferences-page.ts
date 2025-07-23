import type { Page } from '@playwright/test';
import { expect } from '@playwright/test';

export class PreferencesPage {

    readonly page: Page;
    
    constructor(page: Page) {
        this.page = page;
    }

    async fillProfileDetails(firstName: string, street: string, streetNumber: string, postalCode: string, gender: string, preferredGender: string) {
        await this.page.getByRole('textbox', { name: 'First name' }).fill(firstName);
        await this.page.getByRole('textbox', { name: 'Last name' }).fill('');
        await this.page.getByRole('textbox', { name: 'Additional info' }).fill('Power by Playwright');
        await this.page.getByRole('textbox', { name: 'Street' }).first().fill(street);
        await this.page.getByRole('textbox', { name: 'Street Number' }).fill(streetNumber);
        await this.page.getByRole('textbox', { name: 'City' }).fill('Kesteren');
        await this.page.getByRole('textbox', { name: 'Postal Code' }).fill(postalCode);
        await this.page.getByTestId(`personal-information-gender-option-${gender}`).click();
        await this.page.getByTestId(`preferences-gender-option-${preferredGender}`).click();
        await Promise.all([
            this.page.getByRole('button', { name: 'Save' }).click(),
            this.page.waitForNavigation(),
        ]);
        expect(this.page.getByText('Address is verified')).toBeVisible({ timeout: 5000 });
        await this.page.waitForTimeout(5000);
    }

}
