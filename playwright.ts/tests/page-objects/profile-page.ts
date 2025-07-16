import type { Page, Locator } from '@playwright/test';
import { expect } from '@playwright/test';

export class ProfilePage {

    readonly page: Page;
    readonly createButton: Locator;
    readonly nameInput: Locator;
    readonly firstNameInput: Locator;
    readonly lastNameInput: Locator;
    readonly additionalInfoInput: Locator;
    readonly streetInput: Locator;
    readonly streetNumberInput: Locator;
    readonly cityInput: Locator;
    readonly postalCodeInput: Locator;
    readonly personalInformationGenderOptionMan: Locator;
    readonly preferencesGenderOptionWoman: Locator;

    constructor(page: Page) {
        this.page = page;
        this.createButton = page.getByRole('button', { name: 'Create' });
        this.nameInput = page.getByRole('textbox');
        this.firstNameInput = page.getByRole('textbox', { name: 'First name' });
        this.lastNameInput = page.getByRole('textbox', { name: 'Last name' });
        this.additionalInfoInput = page.getByRole('textbox', { name: 'Additional info' });
        this.streetInput = page.getByRole('textbox', { name: 'Street' }).first();
        this.streetNumberInput = page.getByRole('textbox', { name: 'Street Number' });
        this.cityInput = page.getByRole('textbox', { name: 'City' });
        this.postalCodeInput = page.getByRole('textbox', { name: 'Postal Code' });
        this.personalInformationGenderOptionMan = page.getByTestId('personal-information');
    }
    async goto() {
        await this.page.goto('http://localhost:8080');
    }

    async createNewProfile(profileName: string) {
        await expect(this.page.getByRole('button', { name: 'Create' })).toBeDisabled();
        await this.page.getByRole('textbox').fill(profileName);
        await this.page.getByRole('button', { name: 'Create' }).click();
        expect(this.page.getByText(`Welcome ${profileName}`)).toBeVisible();

    }

}
