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
*

## Continuous Integration
Public continuous integration is accessible via 
https://travis-ci.org/lrozenblyum/chess/builds

## License
The license is described in LICENSE file.
Please read it carefully before forking the repository.
