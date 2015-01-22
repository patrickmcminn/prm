/*
Thursday, January 22nd 2015
Main Page.scd
prm
Sydney, Australia
*/

var page = 'main';

~ohm.setPageLoadFunction({
  ~ohm.callGlobalFunction(\audioSystemMixer);
}, page);

//////////// Grid Functions:

// Master EQ Page:
~ohm.turnGridPurple(6, 7, page: page);
~ohm.setGridFunc(6, 7, { ~ohm.setPage('masterEQ'); }, page: page);





