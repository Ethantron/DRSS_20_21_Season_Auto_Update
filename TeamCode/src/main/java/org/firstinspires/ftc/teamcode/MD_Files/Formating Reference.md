Formatting Reference Sheet

    This file is to inform you on how to properly format your code.
    
    Firstly, there are a few words that are spelled in multiple ways. The following list contains
    the correct spellings so that all of our files can be consistent.
        
        1. TelyOp
        
    
    At the top of your TelyOp program, include a comment that informs of all buttons being used.
    
    When you are putting the following code into your program, format it as such:
    @TeleOp(name = "Name_of_file", group= "Name_of_folder")
    
    When naming things like motors, servos, and variables, the name should start with the first word
    being lowercase, with the following word being uppercase. (Ex. motorFrontLeft, armSpeed)
    
    When formatting your code, it should look like this:
        
        if (condition){
        then do
        } else (condition){
        do
        }
    
    Code should be sectioned into blocks. THe sections should look like this:
    
        /** Gamepad 1 controls (drive train) ==> **/
        
        /** Mechanum controls  ==> **/
        
        body
        
        /** <== End of mechanum drive controls **/
        /** <== End of gamepad 1 controls (drive train) **/

