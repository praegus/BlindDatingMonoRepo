*** Settings ***
Documentation    API-test om de Location service te controleren.
Library     RequestsLibrary

*** Variables ***
${base_url}      http://location-service.blinddatingapp.finkingma.com/location


*** Test Cases ***
Doe check met postcode en huisnummer
    [Documentation]     voer een request uit voor een geldige postcode en huisnummer

    Create Session   test   ${base_url}
    &{request_body }        Create Dictionary   streetNumber=1141     postalCode=6545NB
    ${response}             POST On Session     test        /get       json=${request_body}

