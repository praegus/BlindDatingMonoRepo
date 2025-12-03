*** Settings ***
Resource    ./resources/blinddating.resource

Suite Setup       Clean Test Data
Suite Teardown    Clean Test Data    


*** Variables ***
${USERNAME_JIM}       jim
${USERNAME_SJOERD}    sjoerd

*** Test Cases ***
Personal information aanvullen Jim
    Open Browser Blinddatingapp
    Gebruiker aanmaken     ${USERNAME_JIM}
    New Page               ${URL}
    Login Met Gebruiker    ${USERNAME_JIM}
    Sleep    1s
    Aanvullen Profiel    Jim    Vogelpoel    Ik zoek een date     28    3832 RC
    Sleep    1s
    # arguments: gender, favorite_color, pets, hair_color, tattoos, sports, music_genre
    Aanvullen Personal Information   X    Blauw    Yes    GRAY    mat-radio-23    Rugby    Rock
    Click    role=button[name="Save"]
    Sleep    2s
    

Personal information aanvullen Sjoerd
    Open Browser Blinddatingapp
    Gebruiker aanmaken     ${USERNAME_SJOERD}
    New Page               ${URL}
    Login Met Gebruiker    ${USERNAME_SJOERD}
    Sleep    1s
    Aanvullen Profiel    Sjoerd    Nienhuis    ik hou van daten    1    3744 AA
    Sleep    1s
    # arguments:                     gender,          favorite_color,      pets,         hair_color,     
    Aanvullen Personal Information   X                Rood                 Yes           Bruin       
    ...                              #tattoos,        sports,              music_genre                                                       
    ...                              mat-radio-23     American Football    Rock
    Click    role=button[name="Save"]
    Sleep    2s
    
