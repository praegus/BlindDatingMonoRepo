*** Settings ***
Documentation    Simpele controles om te kijken of Robot Framework werkt.

*** Variables ***
${BOODSCHAP}     Mandt!!!

*** Test Cases ***
Controleer Console Log; Robot Framework werkt ;)
    [Documentation]    Schrijft een bericht naar de console.
    Log To Console     \n--- ${BOODSCHAP} ---

Controleer Basis Logica
    [Documentation]    Doe een rekensom.
    Should Be Equal As Integers    ${123 + 321}    ${444}
