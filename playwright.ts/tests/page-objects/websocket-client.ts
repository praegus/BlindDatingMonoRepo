import type { Page, Locator } from '@playwright/test';
import { expect } from '@playwright/test';

export class WebSocket {

    readonly page: Page;


    constructor(page: Page) {
        this.page = page;

    }

    async clearMatchStatueses() {
        const baseUrl = (process.env.WEBSOCKET_API_URL ?? 'https://websocket-service.blinddatingapp.finkingma.com').replace(/\/+$/, '');
        const response = await this.page.request.delete(`${baseUrl}/clear-match-statuses`);
        expect(response.status()).toBe(200);
    }
        }

