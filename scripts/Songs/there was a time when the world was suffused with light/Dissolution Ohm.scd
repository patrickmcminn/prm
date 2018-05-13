/*
Sunday, May 6th 2018
Dissolution Ohm.scd
prm
*/

var page = 'dissolution';

~ohm.makePage(page);

~ohm.addPageLoadFunction(\mainGlobalMixer, { ~ohm.storageDict[\loadGlobalSubmixerFunc].value; }, page);

~ohm.turnAllGridBlue(page: page);

~ohm.turnGridWhite(0, 0, page: page);
~ohm.setGridFunc(0, 0, {
  ~dissolution.playSample;
  ~ohm.turnGridGreen(0, 0);
}, page: page);

~ohm.setLeftSliderFunc(0, { | val |
  ~dissolution.mixer.setVol(val.ccdbfs);
}, page: page);