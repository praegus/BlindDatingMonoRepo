*** Settings ***
Suite Setup    Clean Test Data

Resource    ./resources/blinddating.resource

Documentation    In dit bestand gaan we jimmy invullen als nieuw profiel
...
...              instructies:
...              - log in met jim
...              - zorg dat je op http://localhost:8080/ inlogt

*** Variables ***
${USERNAME_SJOERD}           Sjoerd
${USERNAME_JIM}              Jim

*** Test Cases ***
account aanmaken Jim
    Open Browser Blinddatingapp
    Gebruiker aanmaken    ${USERNAME_JIM}
    Aanvullen Profiel    Jim    Vogelpoel    Ik zoek een date     28    3832 RC
    Click    role=button[name="Save"]
    Sleep    2

account aanmaken Sjoerd
    Open Browser Blinddatingapp
    Gebruiker aanmaken    ${USERNAME_SJOERD}
    Aanvullen Profiel    Sjoerd    Nienhuis    ik hou van daten    1    3744 AA
    Click    role=button[name="Save"]
    Sleep    2

