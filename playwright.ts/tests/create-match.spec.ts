import { test, expect } from '@playwright/test';
import { ProfilePage } from './page-objects/profile-page';



test('Create profile', async ({ page, request }) => {
    const response = await request.delete('http://localhost:9080/profiles/Jannick')
    expect(response.ok()).toBeTruthy(); // Checks if the status code is in the 2xx range
    expect(response.status()).toBe(204); // Or 200, depending on the API

    const profilePage = new ProfilePage(page);
    profilePage.goto();

    // await expect(profilePage.createButton).toBeDisabled();
    profilePage.createNewProfile('Jannick');

    await page.getByRole('textbox', { name: 'First name' }).fill('Tennisert');
    await page.getByRole('textbox', { name: 'Last name' }).fill('');
    await page.getByRole('textbox', { name: 'Additional info' }).fill('Power by Playwright');
    await page.getByRole('textbox', { name: 'Street' }).first().fill('Craaienhof');
    await page.getByRole('textbox', { name: 'Street Number' }).fill('9');
    await page.getByRole('textbox', { name: 'City' }).fill('Kesteren');
    await page.getByRole('textbox', { name: 'Postal Code' }).fill('4041BP');
    await page.getByTestId('personal-information-gender-option-MAN').click();
    await page.getByTestId('preferences-gender-option-WOMAN').click();
    await Promise.all([
        page.getByRole('button', { name: 'Save' }).click(),
        page.waitForNavigation(),
    ]);
    expect(page.getByText('Address is verified')).toBeVisible({ timeout: 5000 });
    await page.waitForTimeout(5000);
});

test('Create second profile', async ({ page, request }) => {
    const response = await request.delete('http://localhost:9080/profiles/Iga')
    expect(response.ok()).toBeTruthy(); // Checks if the status code is in the 2xx range
    expect(response.status()).toBe(204); // Or 200, depending on the API
    
    const profilePage = new ProfilePage(page);
    profilePage.goto();

    // await expect(profilePage.createButton).toBeDisabled();
    profilePage.createNewProfile('Iga');

    await page.getByRole('textbox', { name: 'First name' }).fill('Iga');
    await page.getByRole('textbox', { name: 'Last name' }).fill('Tenniser');
    await page.getByRole('textbox', { name: 'Additional info' }).fill('Power by Playwright');
    await page.getByRole('textbox', { name: 'Street' }).first().fill('Craaienhof');
    await page.getByRole('textbox', { name: 'Street Number' }).fill('9');
    await page.getByRole('textbox', { name: 'City' }).fill('Kesteren');
    await page.getByRole('textbox', { name: 'Postal Code' }).fill('4041BP');
    await page.getByTestId('personal-information-gender-option-WOMAN').click();
    await page.getByTestId('preferences-gender-option-MAN').click();
    await Promise.all([
        page.getByRole('button', { name: 'Save' }).click(),
        page.waitForNavigation(),
    ]);
    expect(page.getByText('Address is verified')).toBeVisible({ timeout: 5000 });
    await page.waitForTimeout(5000);
});