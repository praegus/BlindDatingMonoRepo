import { test as base } from '@playwright/test';
import { ProfilePage } from '../page-objects/profile-page';
import { PreferencesPage } from '../page-objects/preferences-page';
import { ProfileClient } from '../page-objects/profile-client';
import { WebSocket } from '../page-objects/websocket-client';



type TestFixtures = {
    profilePage: ProfilePage;
    preferencesPage: PreferencesPage;
    profileClient: ProfileClient;
    webSocket: WebSocket;
}

export const test = base.extend<TestFixtures>({
    profilePage: async ({ page }, use) => {
        const profilePage = new ProfilePage(page);
        await use(profilePage);
    },
    preferencesPage: async ({ page }, use) => {
        const preferencesPage = new PreferencesPage(page);
        await use(preferencesPage);
    },
    profileClient: async ({ page }, use) => {
        const profileClient = new ProfileClient(page);
        await use(profileClient);
    },
    webSocket: async ({ page }, use) => {
        const webSocket = new WebSocket(page);
        await use(webSocket);
    }
});

export { expect } from '@playwright/test';