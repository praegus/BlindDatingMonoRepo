import { test, expect } from './fixtures/test-fixture';
import { ProfilePage } from './page-objects/profile-page';

// test goals:
// 1. Create a profile  
// 2. Create a second profile with different details
// 3. verify that profiles are matched successfully

test('Create profiles to setup a date', async ({ page, request, profilePage, preferencesPage, context, profileClient}) => {
    //GIVEN dat er 2 profiles zijn die die "matchen".
    await request.delete('http://localhost:9082/clear-match-statuses');
    await request.delete('http://localhost:9080/profiles');

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
        return await pageOne.getByRole('button', { name: 'Accept!' }).isEnabled();
    }, {
       timeout:  15000 
    }).toBe(true);
    await expect(pageOne.getByText('Match gevonden')).toBeVisible();
    await pageOne.getByRole('button', { name: 'Accept!' }).click();

    await expect.poll(async () => {
        return await pageTwo.getByRole('button', { name: 'Accept!' }).isEnabled();
    }, {
       timeout:  15000 
    }).toBe(true);
    await expect(pageTwo.getByText('Match gevonden')).toBeVisible();
    await pageTwo.getByRole('button', { name: 'Accept!' }).click();
    await pageOne.waitForTimeout(1000);
    //await pageTwo.waitForTimeout(1000);

    //THEN hebben beide profielen een date gepland.

    await pageOne.reload();
    await pageTwo.reload();

    await expect(pageOne.getByText('Dwarsweg 63, Overberg')).toBeVisible();
    await expect(pageOne.getByText('Bring: Sok (Pimpel paars met een gouden randje)')).toBeVisible();
    await expect(pageTwo.getByText('Dwarsweg 63, Overberg')).toBeVisible();
    await expect(pageTwo.getByText('Bring: Sok (Pimpel paars met een gouden randje)')).toBeVisible();
});