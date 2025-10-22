*** Settings ***
Library    OperatingSystem
Library    SeleniumLibrary
Library    Collections

*** Variables ***
${URL}    https://www.google.com/ncr

*** Test Cases ***
Search For A Term
    [Tags]    BDD
    Given Google is open
    When I search for "Robot Framework"
    Then The results should contain "robotframework.org"

*** Keywords ***
Google is open
    Open Browser    ${URL}    chrome
    Maximize Browser Window
    Wait Until Element Is Visible    xpath=//button[.='Accept all']    timeout=10s
    Click Button    xpath=//button[.='Accept all']
    Title Should Be    Google

I search for "${term}"
    Input Text    name=q    ${term}
    Press Keys    name=q    RETURN
    Wait Until Page Contains    ${term}

The results should contain "${expected}"
    Page Should Contain    ${expected}