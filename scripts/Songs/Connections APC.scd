/*
Monday, May 27th 2019
Connections APC.scd
prm
*/

var page = 'connections';


var sendMod = 0;

var send0 = 0;
var send1 = 1;
var send2 = 2;
var send3 = 3;

~apc.makePage(page);

~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);
~apc.addMixerEncodersBanks(3, page);


