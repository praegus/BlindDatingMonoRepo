import { test, expect } from './fixtures/test-fixture';


test('Create profile', async ({ page, request, profilePage, preferencesPage }) => {
    const response = await request.delete('http://localhost:9080/profiles')
    expect(response.ok()).toBeTruthy(); // Checks if the status code is in the 2xx range
    expect(response.status()).toBe(200); // Or 200, depending on the API

    await profilePage.goto();

    // await expect(profilePage.createButton).toBeDisabled();
    await profilePage.createNewProfile('Jannick');

    await preferencesPage.fillProfileDetails(
        'Jannick',
        'Craaienhof',
        '9',    
        '4041BP',
        'MAN',
        'WOMAN'
    );

});

test('Create second profile', async ({ page, request, profilePage, preferencesPage }) => {
    const response = await request.delete('http://localhost:9080/profiles/Iga')
    expect(response.ok()).toBeTruthy(); // Checks if the status code is in the 2xx range
    expect(response.status()).toBe(204); // Or 200, depending on the API
    
    
    await profilePage.goto();

    // await expect(profilePage.createButton).toBeDisabled();
    await profilePage.createNewProfile('Iga');
    
    await preferencesPage.fillProfileDetails(
        'Iga',
        'Craaienhof',
        '19',    
        '3811HM',
        'WOMAN',
        'MAN'
    );
});