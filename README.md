------------------------------
Java/LibGDX 2D Sandbox Game
------------------------------

Here is a video of the game : https://www.youtube.com/watch?v=07fbVSpqpe8
You can download a compiled .jar of it at http://hexabeast.com/TrucLatest.jar
I decided to make it open-source because I don't work a lot on it anymore. People that wanted to help me are now free to do it.
The code wasn't meant to be shared, so it is uncommented and probably not easy to understand.

I just ask for one thing : If you use parts of my code, or if you work on the whole project, please share it here, don't keep it for yourself.

-----------------------------------------------

HexaTextureTools contains a python script that creates appropriate textures from simple 8*8 tiles
android, core and desktop are part of the Gradle libgdx project (even if android is disabled), assets are in android and code is in core.

HexBoxServer contains the multiplayer-related server code, it needs kryonet (in java build path/libraries in eclipse), which is included as a jar file.

The rest of the dependencies (libgdx...) should be handled by Gradle when importing the project.

To import this project in Eclipse:

-Step 1 : install Gradle for Eclipse in order to setup the project properly

-Step 2 : Import the whole project in Eclipse with Gradle

-Step 3 : Create an empty java project in the HexBox workspace (named "server" or anything else), add the content of the HexBoxServer directory inside this project

-Step 4 : add the jars (gdx/kryonet) from inside the server project in its java build path if you have errors

-Step 5 : add the server project and the kryonet jar to core's java build path

-Step 6 : If you have no errors, right click the desktop project, Run as Java Application, select DesktopLauncher.

-Step 7 : I hope it works :P

Please tell if something is broken, it is highly probable since I nearly haven't touched this project in a year and I don't remember everything about this project as well as before.


If you achieve to import it properly, please tell me too :)
