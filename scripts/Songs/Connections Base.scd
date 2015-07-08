/*
Wednesday, July 8th 2015
Connections Base.scd
prm
*/

var page = 'connections';

var mainBank = 0;
var airSputtersBank = 1;
var dronerBank = 2;
var basslineBank = 3;
var trumpetGranBank = 4;
var inletBank = 5;
var chordsBank = 6;
var micBank = 7;

~base.makePage(page);

~base.addTouchButtonsBanks(7, page: page);
~base.addFadersBanks(7, page: page);
~base.addGridBanks(7, page: page);