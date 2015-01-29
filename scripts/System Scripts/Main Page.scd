/*
Thursday, January 22nd 2015
Main Page.scd
prm
Sydney, Australia
*/

var page = 'main';

~ohm.globalStorage[\globalSubmixerFunc].value;
~ohm.setPageLoadFunction({
  ~ohm.globalStorage[\globalSubmixerFunc].value;
}, page);

//////////// Grid Functions:

// Master EQ Page:
~ohm.turnGridPurple(6, 7, page: page);
~ohm.setGridFunc(6, 7, { ~ohm.setPage('masterEQ'); }, page: page);





