*** Settings ***
Resource    ./resources/blinddating.resource

Documentation    In dit bestand gaan we jimmy invullen als nieuw profiel
...
...              instructies:
...              - log in met jim
...              - zorg dat je op http://localhost:8080/ inlogt

*** Variables ***
${USERNAME_JIM}       jim
${USERNAME_SJOERD}    sjoerd


*** Test Cases ***
inloggen en profiel aanvullen Jim
    Open Browser Blinddatingapp
    Login Met Gebruiker     ${USERNAME_JIM}
    Aanvullen Profiel
    Click    role=button[name="Save"]
    Sleep    7
   #  Clean Test Data (Dit keyword kan uiteindelijk in een suite/test teardown, maar voor de rest van de flow moet dit hier weg)
   
inloggen en profiel aanvullen Sjoerd
    Open Browser Blinddatingapp
    Login Met Gebruiker     ${USERNAME_SJOERD}
    Aanvullen Profiel
    Click    role=button[name="Save"]
    Sleep    7
   #  Clean Test Data (Dit keyword kan uiteindelijk in een suite/test teardown, maar voor de rest van de flow moet dit hier weg)