## Goal
The chess software that is developed for fun.

## Creator
Initial creator is Leonid Rozenblyum
(the last name could also be spelled as Rozenblium according to Ukrainian transliteration rules)

## Milestone
Main goals for version1 are:
* support Chess Engine Communication Protocol (http://home.hccnet.nl/h.g.muller/engine-intf.html)
This protocol is one of de facto standards in communicating between a chess GUI and an engine.
The well-known GUI implementations are XBoard/Winboard.
 + Probably the fastest way to implement is to take into account the 'Minimal protocol'
document http://home.hccnet.nl/h.g.muller/interfacing.txt
* know about all chess rules (including ambiguities with the 3'd position repetition and insufficient material)
* be able to choose at least random 'correct according to rules' move.

## How to run
* Git clone
* Set up Winboard
* Modify variables.bat:
 * Set up correct WINBOARD_INSTALLATION_PATH
 * Set up correct JAVA_PATH
 
1. Default (recommended) way
   1. `mvn clean package -Dproject.deployDirectory="path to set up"`
    `-DskipTests=true` if you feel brave;
     avoid `clean` phase if your IDE has already compiled the code.
   2. runEngine.bat
2. New way (requires M3_HOME variable set up)
   1. runEngineDev.bat

## Continuous Integration
Public continuous integration is accessible via 
https://travis-ci.org/lrozenblyum/chess/builds

Current status of master branch: [![Build Status](https://travis-ci.org/lrozenblyum/chess.svg?branch=master)](https://travis-ci.org/lrozenblyum/chess)

## License
The license is described in LICENSE file.
Please read it carefully before forking the repository.
