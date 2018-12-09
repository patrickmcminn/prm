/*
2017.3.6
Base Main Page.scd
prm
main base page
*/

var page = 'main';

var submixVolumeBank = 0;
var submixASendBank = 1;
var submixBSendBank = 2;
var submixCSendBank = 3;

var inputsBank = 4;
var modularInputBank = 5;
var moogInputBank = 6;
var microphoneInputBank = 7;

var gridLooperBank = 0;
var gridSubmixSelectBank = 1;

var controlButtonResetFunc;
var activeBank = 0;

var activeSubmixBank = submixVolumeBank;
var activeInputsBank = inputsBank;


~base.addFadersBanks(7, page: page);
~base.addTouchButtonsBanks(7, page: page);
~base.addGridBanks(5, page: page);

//////// control buttons:

~base.setControlButtonMonitorFunc(1, {
  ~base.turnControlButtonMagenta(1);
  ~base.turnControlButtonMagenta(1, 'right');
  ~base.turnControlButtonBlue(2);
  ~base.turnControlButtonBlue(2, 'right');
  ~base.turnControlButtonYellow(3);
  ~base.turnControlButtonYellow(3, 'right');
  ~base.turnControlButtonRed(4);
  ~base.turnControlButtonRed(4, 'right');
  ~base.turnControlButtonGreen((activeBank + 1), 'right');
}, page: page);

// 1 : Normal Page:
~base.setControlButtonFunc(1, {
  activeBank = 0;
  ~base.setActiveFadersBank(submixVolumeBank);
  ~base.setActiveTouchButtonsBank(submixVolumeBank);
  ~base.setActiveGridBank(gridLooperBank);
}, page: page);

// 2 : Submix Sends Bank:
~base.setControlButtonFunc(2, {
  activeBank = 1;
  ~base.setActiveFadersBank(activeSubmixBank);
  ~base.setActiveTouchButtonsBank(activeSubmixBank);
  ~base.setActiveGridBank(gridSubmixSelectBank);
}, page: page);

// 3: main inputs Bank:
~base.setControlButtonFunc(3, {
  activeBank = 2;
  ~base.setActiveFadersBank(activeInputsBank);
  ~base.setActiveFadersBank(activeInputsBank);
  ~base.setActiveGridBank(inputsBank);
}, page: page);



// 5: looper page:
// 6: EQ Page:
// 7: Reverb Page:
// 8: Granulator Page:

//////// sliders + touch buttons:


////// Returns:
2.do({ | fader | ~base.setFaderMode(fader +4, 'blueFill', bank: submixVolumeBank, page: page); });

//// Return A (Reverb);
// mute:
~base.setTouchButtonMonitorFunc(4, {
  if( ~prm.reverb.mixer.isMuted, { ~base.turnTouchButtonRed(4); }, { ~base.turnTouchButtonGreen(4) });
}, bank: submixVolumeBank, page: page);
~base.setTouchButtonFunc(4, { ~prm.reverb.mixer.tglMute; }, bank: submixVolumeBank, page: page);
// volume:
~base.setFaderMonitorFunc(4, {
  ~base.setFaderValue(4, ~prm.reverb.mixer.vol.dbfsCC);
}, bank: submixVolumeBank, page: page);
~base.setFaderFunc(4, { | val |
  ~prm.reverb.mixer.setVol(val.ccdbfs(-140, 0));
}, bank: submixVolumeBank, page: page);

//// Return B (Granulator);
// mute:
~base.setTouchButtonMonitorFunc(5, {
  if( ~prm.granulator.mixer.isMuted, { ~base.turnTouchButtonRed(5); }, { ~base.turnTouchButtonGreen(5) });
}, bank: submixVolumeBank, page: page);
~base.setTouchButtonFunc(5, { ~prm.granulator.mixer.tglMute; }, bank: submixVolumeBank, page: page);
// volume:
~base.setFaderMonitorFunc(5, {
  ~base.setFaderValue(5, ~prm.granulator.mixer.vol.dbfsCC);
}, bank: submixVolumeBank, page: page);
~base.setFaderFunc(5, { | val |
  ~prm.granulator.mixer.setVol(val.ccdbfs(-140, 0));
}, bank: submixVolumeBank, page: page);





//////// main grid







