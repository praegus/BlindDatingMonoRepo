import type { Page, Locator } from '@playwright/test';
import { expect } from '@playwright/test';

export class WebSocket {

    readonly page: Page;


    constructor(page: Page) {
        this.page = page;

    }

    async clearMatchStatueses() {
        const response = await this.page.request.delete('http://websocket-service.blinddatingapp.finkingma.com/clear-match-statuses');
        expect(response.status()).toBe(200);
    }
        }

