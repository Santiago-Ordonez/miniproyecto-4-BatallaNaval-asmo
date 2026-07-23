# Battleship - Mini Project 4

A complete Battleship game developed in Java with JavaFX. Features a graphical interface, AI opponent, save/load system, and timer.

## Description

Implementation of the classic Battleship game against the computer. Includes manual ship placement, turn-based combat, win/lose detection, game saving/loading, and visual effects.

### Main Features

- Graphical user interface using JavaFX
- Manual ship placement with preview and rotation
- Computer AI with smart targeting
- Complete save and load system
- Persistent timer
- Game Over screen
- Visual feedback for hits and sunk ships

## How to Play

1. **New Game**: Enter your name
2. **Placement Phase**: Click on your board to place ships. Right-click or press R to rotate.
3. **Battle Phase**: Click on the enemy board to attack.
4. **Continue/Load**: Resume previous games.

## Technologies

- Java 17+
- JavaFX for the graphical interface
- Serialization for game persistence
- Multithreading for AI and timer

## Project Structure
src/
├── controller/          # JavaFX controllers
├── model/               # Game logic (Board, Ship, Player, Game)
├── utils/               # Utilities (SaveManager, TimeThread, etc.)
├── view/                # FXML and CSS files
└── Main.java

## How to Run

1. Clone the repository
2. Open with IntelliJ IDEA (recommended)
3. Configure JavaFX SDK
4. Run the `Main` class

### Requirements

- JDK 17 or higher
- JavaFX SDK

## Documentation

Javadoc documentation is available in the project.

## Author

Santiago Ordóñez

## License

This project was developed as part of an academic mini-project.

## Project Video
https://youtu.be/vAud4G1w3dk?si=DMLoLWbH_HcHceqS