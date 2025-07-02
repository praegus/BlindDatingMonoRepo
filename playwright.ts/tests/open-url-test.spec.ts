import { test, expect } from '@playwright/test';

test('has title', async ({ page }) => {
  await page.goto('http://localhost:8080');


await expect(page).toHaveTitle(/Blinddatingapp/);
});

test('welcome landing page', async ({ page }) => {
  await page.goto('http://localhost:8080');


  // Expects page to have a heading to welcome the user.
  await expect(page.getByRole('heading', { name: 'Welcome to the BlindDatingApp' })).toBeVisible();
  await expect(page.getByRole('button', { name: 'Login' })).toBeVisible();
  await expect(page.getByRole('button', { name: 'Create' })).toBeVisible();
  await expect(page.getByRole('textbox')).toBeVisible();
});