*** Settings ***
Suite Setup    Clean Test Data

Resource    ./resources/blinddating.resource

Documentation    In dit bestand gaan we jimmy invullen als nieuw profiel
...
...              instructies:
...              - log in met jim
...              - zorg dat je op http://localhost:8080/ inlogt

*** Variables ***
${USERNAME}           jim

*** Test Cases ***
account aanmaken
    Open Browser Blinddatingapp
    Gebruiker aanmaken    ${USERNAME}
    Aanvullen Profiel
    Click    role=button[name="Save"]
    Sleep    7