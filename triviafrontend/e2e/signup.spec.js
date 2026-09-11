
const { test, expect } = require('@playwright/test');

test('signing up logs the user in and shows the authenticated header', async ({ page }) => {

  const uniqueUsername = `e2euser_${Date.now()}`;

  await page.goto('/signup');


  await page.fill('input[name="username"]', uniqueUsername);
  await page.fill('input[name="email"]', `${uniqueUsername}@example.com`);
  await page.fill('input[name="password"]', 'password123');
  await page.fill('input[name="confirmPassword"]', 'password123');


  await page.click('button[type="submit"]');


  await expect(page).toHaveURL('/');

  await expect(page.getByText('Logout')).toBeVisible();
});
