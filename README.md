## Goal
**Leokom Chess** is a Java-based chess engine.
The engine is using Chess Engine Communication Protocol for communication with any existing GUI that supports the protocol.

## Not a goal
**Leokom Chess** doesn't include any GUI.
If you are a bot you can play with it using command line :)
If you are a human being you should deploy it to your favourite Chess GUI software. 

## Motivation
The chess software is developed for fun.
It should proof that OOP and TDD principles can lead to quality game software.

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
   (`-DskipTests=true` if you feel brave; avoid `clean` phase if your IDE has already compiled the code).
   2. runEngine.bat
2. New way (requires M3_HOME variable set up)
   1. runEngineDev.bat
   
## How to develop
### Branch naming convention
Due to SonarCloud analyzing of pull requests, the branch names should follow the pattern: they can only contain 'A-Z', 'a-z', '0-9', '-', '_', '.', and '/'.
   
## How to release
### Preparation
1) settings.xml must contain valid credentials for release uploading to GitHub
```xml 
<servers>
    <server>
        <id>github</id>
        <username>...</username>
        <password>...</password>
    </server>
</servers>
```
2) pom.xml:
github-release-plugin configuration contains meaningful description for the current release

3) current release tag name is the same as Maven version (so in release 0.3 the tag name must be 0.3)

### Execution
mvn clean deploy

### Release artifacts
The release consists of:
* source code in *.zip
* source code in *.tar.gz
* binaries in *.zip   

## Continuous Integration
### Travis CI
Builds: https://travis-ci.com/lrozenblyum/chess/builds

[![Build Status](https://travis-ci.com/lrozenblyum/chess.svg?branch=master)](https://travis-ci.com/lrozenblyum/chess)

### SonarCloud
Full overview: https://sonarcloud.io/dashboard?id=leokom-chess

[![Technical debt](https://sonarcloud.io/api/project_badges/measure?project=leokom-chess&metric=sqale_index)](https://sonarcloud.io/component_measures?id=leokom-chess&metric=sqale_index)

[![Maintainability rating](https://sonarcloud.io/api/project_badges/measure?project=leokom-chess&metric=sqale_rating)](https://sonarcloud.io/component_measures?id=leokom-chess&metric=Maintainability)


## License
See the [LICENSE](LICENSE) and [NOTICE](NOTICE) files.
