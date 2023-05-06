Instructions for running
------------------------

1) Before running the SNePS-2_7 EXE, configure the system by opening the sneps_config.lisp file in the SNePS 2.7 EXE home directory. 
2) Configure the SNEPS-2_7.bat to take in the configuration file as an argument
   a) Open the SNEPS-2_7.bat file in a text editor
   b) Change the "-config-file" argument path to point to the configuration 
      file
3) Double click the batch file to start the SNEPS-2.7 system


Note: The system starts in the system tray as an allegro process called "Allegro Common Lisp Console".


Instructions for the JavaSnepsAPI
---------------------------------

In order for a user to use the JavaSnepsAPI the configuration file for the API needs to be modified. Go to the JavaSnepsAPI subdirectory and modify the java_sneps_config.config file. Instructions are given in that file. See the external documentation at http://www.cse.buffalo.edu/sneps/Docs/javadocs/ for class usage.