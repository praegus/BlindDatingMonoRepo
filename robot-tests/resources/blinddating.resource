*** Settings ***
Library     Browser
Library     SeleniumLibrary


*** Variables ***
${URL}  http://localhost:8080/

*** Keywords ***
Open Browser Blinddatingapp
    New Browser    chromium    headless=false
    New Page       http://localhost:8080/

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
    Type Text   role=textbox[name="Street"]              Leusderend
    Type Text   role=textbox[name="Street Number"]       9
    Type Text   role=textbox[name="City"]                Leusden
    Type Text   role=textbox[name="Postal Code"]         weetniet
