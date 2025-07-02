import { test, expect } from '@playwright/test';

test('has title', async ({ page }) => {
  await page.goto('http://localhost:8080');
  await page.getByRole('button', { name: 'Create' }).click();
  await page.getByText('Something unexpected occurred, please try again').isVisible();
});