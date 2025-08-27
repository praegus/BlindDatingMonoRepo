import { test, expect } from './fixtures/test-fixture';
import { ProfilePage } from './page-objects/profile-page';

// test goals:
// 1. Create a profile  
// 2. Create a second profile with different details
// 3. verify that profiles are matched successfully

test('Create profiles to setup a date', async ({ page, request, profilePage, preferencesPage, context, profileClient, webSocket}) => {
    //GIVEN dat er 2 profiles zijn die die "matchen".
    
    await webSocket.clearMatchStatueses();
    await profileClient.deleteAllProfiles();

    await profileClient.createProfile(
            'Jannick',
            'Craaienhof',
            '9',    
            '4041BP',
            'MAN',
            'WOMAN',
            'Pimpel paars met een gouden randje'
    );

    await profileClient.createProfile(
        'Iga',
        'Kauwenplein',
        '19',    
        '3811HM',
        'WOMAN',
        'MAN',
        'Pimpel paars met een gouden randje'
    );

    //WHEN beide profielen de match accepteren


    const pageOne = await context.newPage();
    const pageTwo = await context.newPage();

    const profilePage1 = new ProfilePage(pageOne)
    const profilePage2 = new ProfilePage(pageTwo)

    await profilePage1.goto();
    await profilePage1.loginExistingProfile('Jannick'); 
    
    await profilePage2.goto();
    await profilePage2.loginExistingProfile('Iga');

    await expect.poll(async () => {
            const buttonOneEnabled = await pageOne.getByRole('button', { name: 'Accept!' }).isEnabled();
            const buttonTwoEnabled = await pageTwo.getByRole('button', { name: 'Accept!' }).isEnabled();
            return buttonOneEnabled && buttonTwoEnabled;
    }, {
       timeout:  15000 
    }).toBe(true);
    await pageOne.getByRole('button', { name: 'Accept!' }).click();
    await pageTwo.getByRole('button', { name: 'Accept!' }).click();


    //THEN hebben beide profielen een date gepland.

        await pageTwo.waitForTimeout(1000);
    await pageOne.reload();
    await pageTwo.reload();

    await expect(pageOne.getByText('Dwarsweg 63, Overberg')).toBeVisible();
    await expect(pageOne.getByText('Bring: Sok (Pimpel paars met een gouden randje)')).toBeVisible();
    await expect(pageTwo.getByText('Dwarsweg 63, Overberg')).toBeVisible();
    await expect(pageTwo.getByText('Bring: Sok (Pimpel paars met een gouden randje)')).toBeVisible();
});