/*
Thursday, January 22nd 2015
Main Page.scd
prm
Sydney, Australia
*/

var page = 'main';

~ohm.storageDict[\loadGlobalSubmixerFunc].value;
~ohm.setPageLoadFunction({
  ~ohm.storageDict[\loadGlobalSubmixerFunc].value;
}, page);

//////////// Grid Functions:


// Returns Page:
~ohm.turnGridBlue(5, 7, page: page);
~ohm.setGridFunc(5, 7, { ~ohm.setPage('returns'); }, page: page);
// Master EQ Page:
~ohm.turnGridPurple(6, 7, page: page);
~ohm.setGridFunc(6, 7, { ~ohm.setPage('masterEQ'); }, page: page);
// Submixer Effects Page:
~ohm.turnGridCyan(7, 7, page: page);
~ohm.setGridFunc(7, 7, { ~ohm.setPage('submixer'); }, page: page);





