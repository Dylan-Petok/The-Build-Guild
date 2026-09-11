const { test, expect } = require('@playwright/test');

test('logging in shows successful login banner and brings you to home page', async ({ page }) => {
    const username = 'dylantest1';
    const password = 'Test1234'

    await page.goto('/signin');

    await page.fill('input[name="username"]', username);
    await page.fill('input[name="password"]', password );

    await page.click('button[type="submit"]');

    await expect(page).toHaveURL('/');

    await expect(page.getByText('Logout')).toBeVisible();
});