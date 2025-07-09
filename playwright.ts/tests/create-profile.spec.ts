import { test, expect } from '@playwright/test';

test('Check create button is disabled and name required error', async ({ page }) => {
  await page.goto('http://localhost:8080');
  await expect(page.getByRole('button', { name: 'Create' })).toBeDisabled();
  await page.getByRole('textbox').click();
  await page.keyboard.down('Tab');
  await expect(page.getByText('Name is required')).toBeVisible();
});

test('Create profile', async ({ page, request }) => {
  const response = await request.delete('http://localhost:9080/profiles/Tester')
  expect(response.ok()).toBeTruthy(); // Checks if the status code is in the 2xx range
  expect(response.status()).toBe(204); // Or 200, depending on the API
  await page.goto('http://localhost:8080');
  await expect(page.getByRole('button', { name: 'Create' })).toBeDisabled();
  await page.getByRole('textbox').fill('Tester');
  await page.getByRole('button', { name: 'Create' }).click();
  expect(page.getByText('Welcome Tester')).toBeVisible();
  await page.getByRole('textbox', { name: 'First name'}).fill('Tester');
  await page.getByRole('textbox', { name: 'Last name'}).fill('Tester');
  await page.getByRole('textbox', { name: 'Additional info'}).fill('Power by Playwright');
  await page.getByRole('textbox', { name: 'Street'}).first().fill('Craaienhof');
  await page.getByRole('textbox', { name: 'Street Number'}).fill('9');
  await page.getByRole('textbox', { name: 'City'}).fill('Kesteren');
  await page.getByRole('textbox', { name: 'Postal Code'}).fill('4041BP');
  await page.getByTestId('personal-information-gender-option-MAN').click();
  await page.getByTestId('preferences-gender-option-WOMAN').click();
  await Promise.all([
    page.getByRole('button', { name: 'Save' }).click(),
    page.waitForNavigation(),
  ]);
  expect(page.getByText('Address is verified')).toBeVisible({ timeout: 5000 });
  await page.waitForTimeout(5000);
});