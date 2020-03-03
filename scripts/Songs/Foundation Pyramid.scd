
////// this won't work

/*

/*
Tuesday, March 3rd 2020
Foundation Pyramid.scd
prm
*/

/*
Thinking this out:
0-indexing in SC with the understanding that Pyramid doesn't

0 - moog 1
0 - moog 2
1 - bass 1
2 - bass 2
3 - clean tpt
3 - foundation tpt
4 - tpt section
5 - dist trumpets
6 - end trumpets
7 - chords
8 - cellos
9 - trem
10 - warps

*/

var moogOnArray, moogOffArray;

var pyr = ~pyramid;
var moogChan = 0;
var bass1Chan = 1;
var bass2Chan = 2;
var tptChan = 3;
var tptSectionChan = 4;
var distTptChan = 5;
var endTptChan = 6;
var chordsChan = 7;
var cellosChan = 8;
var tremChan = 9;
var warpsChan = 10;

//// moog:
moogOnArray = Array.fill(128, { | i |
	MIDIFunc.noteOn({ ~foundation.moog.moog.playNote(i.midicps) }, i, moogChan, pyr);
});
moogOffArray = Array.fill(127, { | i |
	MIDIFunc.noteOff({ ~foundation.moo

x = 5;

*/