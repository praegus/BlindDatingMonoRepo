import type { Page, Locator } from '@playwright/test';
import { expect } from '@playwright/test';

export class ProfileClient {

    readonly page: Page;


    constructor(page: Page) {
        this.page = page;

    }

    async deleteAllProfiles() {
        const response = await this.page.request.delete('http://localhost:9080/profiles');
        expect(response.status()).toBe(204);
    }

    async createProfile(username: string, street: string, streetNumber: string, postalCode: string, gender: string, preferredGender: string, color: string = '') {
        const requestBody = {
            "username": username,
            "firstname": username,
            "lastname": null,
            "additionalInfo": "Power by Playwright",
            "address": {
                "street": street,
                "streetNumber": streetNumber,
                "postalCode": postalCode,
            },
            "personalInformation": {
                "gender": gender,
                "favoriteColor": color,
                "pets": false,
                "hairColor": null,
                "tattoos": false,
                "sports": null,
                "musicGenres": null
            },
            "dislikes": {
                "gender": null,
                "favoriteColor": null,
                "pets": false,
                "hairColor": null,
                "tattoos": false,
                "sports": null,
                "musicGenres": null
            },
            "preferences": {
                "gender": preferredGender,
                "favoriteColor": color,
                "pets": false,
                "hairColor": null,
                "tattoos": false,
                "sports": null,
                "musicGenres": null
            },
        };


        const response = await this.page.request.post('http://localhost:9080/profiles', {
            data: requestBody
        }
        );
        expect(response.status()).toBe(201);
    }
}
