*** Settings ***
Library     Browser
Library     SeleniumLibrary
Library     RequestsLibrary


*** Variables ***
${URL}  http://localhost:8080/
${BASE_URL}    http://localhost:9080/

*** Keywords ***
Open Browser Blinddatingapp
    New Browser    chromium    headless=false
    New Page       http://localhost:8080/

Clean Test Data
    Create Session    api    ${BASE_URL}
    ${response}=    Delete Request    api    /profiles
    Should Be Equal As Integers    ${response.status_code}    204

Gebruiker aanmaken
    [Arguments]    ${username}
    ${veld}=    Get Element By Role    textbox
    Type Text    ${veld}    ${username}
    ${create_knop}=    Get Element By Role    button    name=Create
    Click    ${create_knop}
    Sleep    1s

Login Met Gebruiker
    [Arguments]    ${username}
    ${veld}=    Get Element By Role    textbox
    Type Text    ${veld}    ${username}
    ${login_knop}=    Get Element By Role    button    name=Login
    Click    ${login_knop}
    Sleep    1s

Aanvullen Profiel
    [Arguments]    ${first}    ${last}    ${info}    ${street}    ${postal}

    Type Text   role=textbox[name="First name"]      ${first}
    Type Text   role=textbox[name="Last name"]       ${last}
    Type Text   role=textbox[name="Additional info"]  ${info}
    Type Text   role=textbox[name="Street Number"]   ${street}
    Type Text   role=textbox[name="Postal Code"]     ${postal}


Aanvullen Personal Information
    [Arguments]    ${gender}    ${favorite_color}    ${pets}    ${hair_color}    ${tattoos}    ${sports}    ${music_genre}

    # Gender radio – visible mat-radio-button
    Click    css=[data-testid="personal-information-gender-option-${gender}"]

    # Favorite Color – first input field
    Type Text    css=input[formcontrolname="favoriteColor"] >> nth=0    ${favorite_color}

    # Pets radio – visible mat-radio-button
    ${pet_id}=    Run Keyword If    '${pets}'=='Yes'    Set Variable    mat-radio-21    ELSE    Set Variable    mat-radio-22
    Click    css=#${pet_id}

    # Hair Color dropdown
    Click    css=mat-select[formcontrolname="hairColor"]
    Click    text="${hair_color}"

    # Tattoos radio – visible mat-radio-button
    ${tattoo_id}=    Run Keyword If    '${tattoos}'=='Yes'    Set Variable    mat-radio-23    ELSE    Set Variable    mat-radio-24
    Click    css=#${tattoo_id}

    # Sports input
    Type Text    css=input[formcontrolname="sports"]    ${sports}

    # Music Genre dropdown
    Click    css=mat-select[formcontrolname="musicGenres"]
    Click    text="${music_genre}"







