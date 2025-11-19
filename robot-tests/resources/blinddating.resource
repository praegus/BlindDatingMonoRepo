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
    Type Text   role=textbox[name="First name"]          Jim
    Type Text   role=textbox[name="Last name"]           Vogelpoel
    Type Text   role=textbox[name="Additional info"]     dfvbezhbdtg
    Type Text   role=textbox[name="Street Number"]       28
    Type Text   role=textbox[name="Postal Code"]         3832 RC