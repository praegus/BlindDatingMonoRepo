import { randomUUID } from 'node:crypto';
import { test, expect } from './fixtures/test-fixture';
import { ProfilePage } from './page-objects/profile-page';
import { PreferencesPage } from './page-objects/preferences-page';

test('Create profiles to setup a date', async ({ browser, profileClient, keycloakClient, webSocket }) => {
    // OpenSpec: authentication.hosted-forms
    // OpenSpec: authentication.own-profile
    // This test resets shared application state: run against an exclusive test environment.
    await profileClient.deleteAllProfiles();
    await keycloakClient.deleteAllAccounts();
    await webSocket.clearMatchStatueses();

    const suffix = randomUUID().replaceAll('-', '').slice(0, 12);
    const usernameOne = `e2ea${suffix}`;
    const usernameTwo = `e2eb${suffix}`;
    const password = randomUUID();
    const contextOne = await browser.newContext();
    const contextTwo = await browser.newContext();
    try {
        const pageOne = await contextOne.newPage();
        const pageTwo = await contextTwo.newPage();
        const profileOne = new ProfilePage(pageOne);
        const profileTwo = new ProfilePage(pageTwo);
        await profileOne.goto();
        await profileOne.createNewProfile(usernameOne, password);
        await profileTwo.goto();
        await profileTwo.createNewProfile(usernameTwo, password);

        const color = 'Pimpel paars met een gouden randje';
        await new PreferencesPage(pageOne).fillProfileDetails('Jannick', 'Craaienhof', '9', '4041BP', 'MAN', 'WOMAN', color);
        await new PreferencesPage(pageTwo).fillProfileDetails('Iga', 'Kauwenplein', '19', '3811HM', 'WOMAN', 'MAN', color);

        await expect(pageOne.getByRole('button', { name: 'Accept!', exact: true })).toBeEnabled({ timeout: 30_000 });
        await expect(pageTwo.getByRole('button', { name: 'Accept!', exact: true })).toBeEnabled({ timeout: 30_000 });
        await pageOne.getByRole('button', { name: 'Accept!', exact: true }).click();
        await pageTwo.getByRole('button', { name: 'Accept!', exact: true }).click();
        await expect(async () => {
            await pageOne.reload();
            await pageTwo.reload();
            for (const page of [pageOne, pageTwo]) {
                await expect(page.getByText('Dwarsweg 63, Overberg')).toBeVisible();
                await expect(page.getByText(`Bring: Sok (${color})`)).toBeVisible();
            }
        }).toPass({ timeout: 30_000 });
    } finally {
        // Finalize each context's trace separately, including when the first close fails.
        try {
            await contextOne.close();
        } finally {
            await contextTwo.close();
        }
    }
});
