/*
Wednesday, April 25th 2018
wasntKnownBase.scd
prm

that which wasn't known
*/

var page = 'wasntKnown';

~ohm.makePage(page);

~ohm.addPageLoadFunction(\mainGlobalMixer, { ~ohm.storageDict[\loadGlobalSubmixerFunc].value; }, page);

~ohm.turnAllGridMagenta(page: page);


