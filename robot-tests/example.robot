*** Settings ***
Library    OperatingSystem

*** Test Cases ***
Check File Exists
    [Documentation]    Controleer of een bestand bestaat
    Create File    testfile.txt
    File Should Exist    testfile.txtrobot example.robot