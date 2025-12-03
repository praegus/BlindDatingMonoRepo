*** Settings ***
Resource    ./resources/blinddating.resource

*** Variables ***
${USERNAME_JIM}       jim
${USERNAME_SJOERD}    sjoerd

*** Test Cases ***
Personal information aanvullen Jim
    Open Browser Blinddatingapp
    Login Met Gebruiker    ${USERNAME_JIM}
    Sleep    1s
    Aanvullen Profiel    Jim    Vogelpoel    Ik zoek een date     28    3832 RC
    Sleep    1s
    Aanvullen Personal Information   X    Blauw    mat-radio-21    Grijs    mat-radio-23    Rugby    Rock
    Click    role=button[name="Save"]
    Sleep    2s
    

Personal information aanvullen Sjoerd
    Open Browser Blinddatingapp
    Login Met Gebruiker    ${USERNAME_SJOERD}
    Sleep    1s
    Aanvullen Profiel    Sjoerd    Nienhuis    ik hou van daten    1    3744 AA
    Sleep    1s
    Aanvullen Personal Information   X    Rood     mat-radio-21    Bruin    mat-radio-23    American Football    Rock
    Click    role=button[name="Save"]
    Sleep    2s
    
