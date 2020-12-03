# 2D Game Redo

2D game written in Java/OpenJFX. Intended for Windows. 

## Requirements

You need to have Java 15 installed in order to run this game. This game is intended for Windows. The game has a fixed resolution of 1280x800 because it's intended to be run on a particular tablet computer. It can be run in either fullscreen or windowed mode.

You can play the game with either the keyboard or touchscreen controls, configurable in the settings menu. 

## How to run it

Double click on run.vbs in order to run the game

## About

This is a 2D RPG. It's not finished yet.

The game supports 1280x720, 1280x800, and 1920x1080 resolutions, with 40x40 pixel tiles. It has a tile-based movement system. 

All you need in order to run this program is Java. But this game itself has two dependencies (aside from Java, that is): OpenJFX and the Apache Commons IO library. However, it contains both of those dependencies within it, so there is no need to install them separately.

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
