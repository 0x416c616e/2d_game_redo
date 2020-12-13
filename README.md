# 2D Game Redo (Work in Progress)

2D game written in Java/OpenJFX. Intended for Windows. It isn't finished yet.

## Requirements

You need to have Java 15 installed in order to run this game. This game is intended for Windows. It can be run in either fullscreen or windowed mode.

You can play the game with either the keyboard or touchscreen controls, configurable in the settings menu. 

## How to run it

1. Install Java **JDK 15**. This will *not* work with Java 8.

2. Clone or download this repo.

3. Double click on run.vbs in order to run the game in the normal mode.

## About

This is a 2D RPG. It's not finished yet.

The game supports 1280x720, 1280x800, and 1920x1080 resolutions, with 40x40 pixel tiles for 720p and 800p, and 60x60 tiles in 1080p. It has a tile-based movement system. 

**Please note**: if you are using a laptop or tablet, please put the display scaling to 100%, or else things will be spaced out incorrectly.

All you need in order to run this program is Java. But this game itself has two dependencies (aside from Java, that is): OpenJFX and the Apache Commons IO library. However, it contains both of those dependencies within it, so there is no need to install them separately.

-------

### Creating JAR when working on this project

See my repo about the JavaFX 15 template for more info.

In order to edit the JAR, meaning you can change the game and then run it with the VBscript, you need to do the following (only for development, not for playing):

1. In the left-hand tab of IntelliJ, navigate to the Main file in the src folder
2. Write your code in the Main class
3. Run to make sure it works
4. Build -> Build Artifacts -> Build
5. Double click on copy_new_artifact.bat
6. Double click the run.vbs script
7. You are now running the JAR

### Debug mode

Double click on run_debug_mode.bat if you want to run the game with the debug mode flag, which will allow you to enable debug mode and/or debug mode logging.
