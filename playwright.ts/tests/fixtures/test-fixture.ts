import { test as base } from '@playwright/test';
import { ProfilePage } from '../page-objects/profile-page';
import { PreferencesPage } from '../page-objects/preferences-page';


type TestFixtures = {
    profilePage: ProfilePage;
    preferencesPage: PreferencesPage;
}

export const test = base.extend<TestFixtures>({
    profilePage: async ({ page }, use) => {
        const profilePage = new ProfilePage(page);
        await use(profilePage);
    },
    preferencesPage: async ({ page }, use) => {
        const preferencesPage = new PreferencesPage(page);
        await use(preferencesPage);
    }
});

export { expect } from '@playwright/test';