-----------------------------------------------
Java/LibGDX 2D Sandbox Game under GPLv3 license
-----------------------------------------------

Here is a video of the game : https://www.youtube.com/watch?v=07fbVSpqpe8
You can download a compiled .jar of it at http://hexabeast.com/TrucLatest.jar
I decided to make it open-source because I don't work a lot on it anymore. People that wanted to help me are now free to do it.
The code wasn't meant to be shared, so it is uncommented and probably not easy to understand.

-----------------------------------------------

HexaTextureTools contains a python script that creates appropriate textures from simple 8*8 tiles
android, core and desktop are part of the Gradle libgdx project, assets are in android and code is in core.

HexBoxServer contains the multiplayer-related server code, it needs kryonet (in java build path/libraries in eclipse), which is included as a jar file.

You should also add kryonet (in "Libraries" in Eclipse) and the HexBoxServer folder (in "Projects" in Eclipse) to the java build path of the core project in order to use the multiplayer part.

The rest of the dependencies (libgdx...) should be handled by Gradle when importing the project.

Please tell if something is broken, it is highly probable since I nearly haven't touched this project in a year and I don't remember everything about this project as well as before.


If you achieve to import it properly, please tell me too :)
