*** Settings ***
Library    Browser
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
    New Browser     chromium    headless=${False}
    New Page    ${URL}
    Click    text="Accept all"    # Cookie-popup
    Get Title   Should Be   Google

I search for "${term}"
    Fill Text    xpath=//textarea[@name="q"]    ${term}
    Press Keys    xpath=//textarea[@name="q"]    Enter
    Wait For Elements State    text=${term}    visible

The results should contain "${expected}"
    Get Text    //body    Should Contain    ${expected}