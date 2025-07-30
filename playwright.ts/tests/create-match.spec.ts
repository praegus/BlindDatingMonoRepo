import { test, expect } from './fixtures/test-fixture';

// test goals:
// 1. Create a profile  
// 2. Create a second profile with different details
// 3. verify that profiles are matched successfully

test('Create profile', async ({ page, request, profilePage, preferencesPage }) => {
    const response = await request.delete('http://localhost:9080/profiles')
    expect(response.ok()).toBeTruthy(); // Checks if the status code is in the 2xx range
    expect(response.status()).toBe(204); // Or 200, depending on the API

    await profilePage.goto();

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
    // const response = await request.delete('http://localhost:9080/profiles/Iga')
    // expect(response.ok()).toBeTruthy(); // Checks if the status code is in the 2xx range
    // expect(response.status()).toBe(204); // Or 200, depending on the API
    
    await profilePage.goto();

    await profilePage.createNewProfile('Iga');
    
    await preferencesPage.fillProfileDetails(
        'Iga',
        'Kauwenplein',
        '19',    
        '3811HM',
        'WOMAN',
        'MAN'
    );
});

test('Verify matching profiles', async ({ page, request, profilePage }) => {

    const response = await request.get('http://localhost:9081')
    expect(response.ok()).toBeTruthy(); // Checks if the status code is in the 2xx range
    expect(response.status()).toBe(200); // Or 200, depending on the API

    await profilePage.goto()

    await profilePage.loginExistingProfile('Jannick');  

    await expect(page.getByText('Match gevonden')).toBeEnabled();
    
});