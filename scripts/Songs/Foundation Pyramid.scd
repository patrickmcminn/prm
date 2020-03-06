
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
var tptSectionChan1 = 4;
var tptSectionChan2 = 5;
var tptSectionChan3 = 6;
var distTptChan = 7;
var endTptChan1 = 8;
var endTptChan1 = 9;
var endTptChan1 = 10;
var chordsChan = 11;
var cellosChan = 12;
var tremChan = 13;
var warpsChan = 14;

//// moog:
moogOnArray = Array.fill(128, { | i |
	MIDIFunc.noteOn({ ~foundation.moog.moog.playNote(i.midicps) }, i, moogChan, pyr);
});
moogOffArray = Array.fill(127, { | i |
	MIDIFunc.noteOff({ ~foundation.moo

x = 5;

*/