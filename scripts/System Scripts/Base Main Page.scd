/*
2017.3.6
Base Main Page.scd
prm
*/

var page = 'main';

var submixVolumeBank = 0;
var submixASendBank = 1;
var submixBSendBank = 2;
var submixCSendBank = 3;

var gridLooperBank = 0;
var gridSubmixSelectBank = 1;

var controlButtonResetFunc;
var activeBank = 0;

var activeSubmixBank = submixASendBank;

~base.addFadersBanks(3, page: page);
~base.addTouchButtonsBanks(3, page: page);
~base.addGridBanks(3, page: page);

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



// 5: looper page:
// 6: EQ Page:
// 7: Reverb Page:
// 8: Granulator Page:

//////// sliders + touch buttons:

////// Submixer Volume Bank:
// fader mode:
3.do({  |fader|
  ~base.setFaderMode(fader+1, 'magentaFill', bank: submixVolumeBank, page: page);
});
//// Submixer A:
// mute:
~base.setTouchButtonMonitorFunc(1, {
  if( ~prm.submixerA.mixer.isMuted, { ~base.turnTouchButtonRed(1); }, { ~base.turnTouchButtonGreen(1); });
}, bank: submixVolumeBank, page: page);
~base.setTouchButtonFunc(1, { ~prm.submixerA.mixer.tglMute; }, bank: submixVolumeBank, page: page);
// volume:
~base.setFaderMonitorFunc(1, {
  ~base.setFaderValue(1, ~prm.submixerA.mixer.vol.dbfsCC);
}, bank: submixVolumeBank, page: page);
~base.setFaderFunc(1, { | val |
  ~prm.submixerA.mixer.setVol(val.ccdbfs(-140, 0));
}, bank: submixVolumeBank, page: page);

//// Submixer B:
// mute:
~base.setTouchButtonMonitorFunc(2, {
  if( ~prm.submixerB.mixer.isMuted, { ~base.turnTouchButtonRed(2); }, { ~base.turnTouchButtonGreen(2); });
}, bank: submixVolumeBank, page: page);
~base.setTouchButtonFunc(2, { ~prm.submixerB.mixer.tglMute; }, bank: submixVolumeBank, page: page);
// volume:
~base.setFaderMonitorFunc(2, {
  ~base.setFaderValue(2, ~prm.submixerB.mixer.vol.dbfsCC);
}, bank: submixVolumeBank, page: page);
~base.setFaderFunc(2, { | val |
  ~prm.submixerB.mixer.setVol(val.ccdbfs(-140, 0));
}, bank: submixVolumeBank, page: page);

//// Submixer C:
// mute:
~base.setTouchButtonMonitorFunc(3, {
  if( ~prm.submixerC.mixer.isMuted, { ~base.turnTouchButtonRed(3); }, { ~base.turnTouchButtonGreen(3); });
}, bank: submixVolumeBank, page: page);
~base.setTouchButtonFunc(3, { ~prm.submixerC.mixer.tglMute; }, bank: submixVolumeBank, page: page);
// volume:
~base.setFaderMonitorFunc(3, {
  ~base.setFaderValue(3, ~prm.submixerC.mixer.vol.dbfsCC);
}, bank: submixVolumeBank, page: page);
~base.setFaderFunc(3, { | val |
  ~prm.submixerC.mixer.setVol(val.ccdbfs(-140, 0));
}, bank: submixVolumeBank, page: page);


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

////// Submix A Sends:
~base.setFaderMode(1, 'magentaFill', bank: submixASendBank, page: page);
3.do({ | i | ~base.setFaderMode(i+2, 'blueFill', bank: submixASendBank, page: page);});
//// Submix A Volume:
~base.setFaderMonitorFunc(1, { ~base.setFaderValue(1, ~prm.submixerA.mixer.vol.dbfsCC); },
  bank: submixASendBank, page: page);
~base.setFaderFunc(1, { | val | ~prm.submixerA.mixer.setVol(val.ccdbfs); },
  bank: submixASendBank, page: page);
//// Submix A Reverb Send:
~base.setFaderMonitorFunc(2, { ~base.setFaderValue(2, ~prm.submixerA.mixer.sendVol(0).dbfsCC); },
  bank: submixASendBank, page: page);
~base.setFaderFunc(2, { | val | ~prm.submixerA.mixer.setSendVol(0, val.ccdbfs(-70, 0)); },
  bank: submixASendBank, page: page);
//// Submix A Granulator Send:
~base.setFaderMonitorFunc(3, { ~base.setFaderValue(3, ~prm.submixerA.mixer.sendVol(1).dbfsCC); },
  bank: submixASendBank, page: page);
~base.setFaderFunc(3, { | val | ~prm.submixerA.mixer.setSendVol(1, val.ccdbfs(-70, 0));
}, bank: submixASendBank, page: page);
//// Submix A Modular Send:
~base.setFaderMonitorFunc(4, { ~base.setFaderValue(4, ~prm.submixerA.mixer.sendVol(2).dbfsCC); },
  bank: submixASendBank, page: page);
~base.setFaderFunc(4, { | val | ~prm.submixerA.mixer.setSendVol(2, val.ccdbfs(-70, 0));
}, bank: submixASendBank, page: page);

////// Submix B Sends:
~base.setFaderMode(1, 'magentaFill', bank: submixBSendBank, page: page);
3.do({ | i | ~base.setFaderMode(i+2, 'blueFill', bank: submixBSendBank, page: page);});
//// Submix A Volume:
~base.setFaderMonitorFunc(1, { ~base.setFaderValue(1, ~prm.submixerB.mixer.vol.dbfsCC); },
  bank: submixBSendBank, page: page);
~base.setFaderFunc(1, { | val | ~prm.submixerB.mixer.setVol(val.ccdbfs); },
  bank: submixBSendBank, page: page);
//// Submix A Reverb Send:
~base.setFaderMonitorFunc(2, { ~base.setFaderValue(2, ~prm.submixerB.mixer.sendVol(0).dbfsCC); },
  bank: submixBSendBank, page: page);
~base.setFaderFunc(2, { | val | ~prm.submixerB.mixer.setSendVol(0, val.ccdbfs(-70, 0)); },
  bank: submixBSendBank, page: page);
//// Submix A Granulator Send:
~base.setFaderMonitorFunc(3, { ~base.setFaderValue(3, ~prm.submixerB.mixer.sendVol(1).dbfsCC); },
  bank: submixBSendBank, page: page);
~base.setFaderFunc(3, { | val | ~prm.submixerB.mixer.setSendVol(1, val.ccdbfs(-70, 0));
}, bank: submixBSendBank, page: page);
//// Submix A Modular Send:
~base.setFaderMonitorFunc(4, { ~base.setFaderValue(4, ~prm.submixerB.mixer.sendVol(2).dbfsCC); },
  bank: submixBSendBank, page: page);
~base.setFaderFunc(4, { | val | ~prm.submixerB.mixer.setSendVol(2, val.ccdbfs(-70, 0));
}, bank: submixBSendBank, page: page);


////// Submix C Sends:
~base.setFaderMode(1, 'magentaFill', bank: submixCSendBank, page: page);
3.do({ | i | ~base.setFaderMode(i+2, 'blueFill', bank: submixCSendBank, page: page);});
//// Submix A Volume:
~base.setFaderMonitorFunc(1, { ~base.setFaderValue(1, ~prm.submixerC.mixer.vol.dbfsCC); },
  bank: submixCSendBank, page: page);
~base.setFaderFunc(1, { | val | ~prm.submixerC.mixer.setVol(val.ccdbfs); },
  bank: submixCSendBank, page: page);
//// Submix A Reverb Send:
~base.setFaderMonitorFunc(2, { ~base.setFaderValue(2, ~prm.submixerC.mixer.sendVol(0).dbfsCC); },
  bank: submixCSendBank, page: page);
~base.setFaderFunc(2, { | val | ~prm.submixerC.mixer.setSendVol(0, val.ccdbfs(-70, 0)); },
  bank: submixCSendBank, page: page);
//// Submix A Granulator Send:
~base.setFaderMonitorFunc(3, { ~base.setFaderValue(3, ~prm.submixerC.mixer.sendVol(1).dbfsCC); },
  bank: submixCSendBank, page: page);
~base.setFaderFunc(3, { | val | ~prm.submixerC.mixer.setSendVol(1, val.ccdbfs(-70, 0));
}, bank: submixCSendBank, page: page);
//// Submix A Modular Send:

~base.setFaderMonitorFunc(4, { ~base.setFaderValue(4, ~prm.submixerC.mixer.sendVol(2).dbfsCC); },
  bank: submixCSendBank, page: page);
~base.setFaderFunc(4, { | val | ~prm.submixerC.mixer.setSendVol(2, val.ccdbfs(-70, 0));
}, bank: submixCSendBank, page: page);




//////// main grid:

////// Submix Volume Bank:

//// Looper:

/// Submixer A:

// Recording:
~base.setGridMonitorFunc(0, 0, {
  if( ~prm.submixerA.isRecording,
    { ~base.turnGridWhite(0, 0); },
    { ~base.turnGridBlue(0, 0); }
  );
}, bank: gridLooperBank, page: page);
~base.setGridFunc(0, 0, {  ~prm.submixerA.loop }, bank: gridLooperBank, page: page);

// Playing:
~base.setGridMonitorFunc(0, 1, {
  if( ~prm.submixerA.isPlaying,
    { ~base.turnGridGreen(0, 1); },
    { ~base.turnGridRed(0, 1); }
  );
}, bank: gridLooperBank, page: page);
~base.setGridFunc(0, 1, { ~prm.submixerA.togglePlayLoop; }, bank: gridLooperBank, page: page);

// Reset:
~base.turnGridRed(0, 2, bank: gridLooperBank, page: page);
~base.setGridFunc(0, 2, { ~prm.submixerA.clearLoop(30); }, bank: gridLooperBank, page: page);

/// Submixer B:

// Recording:
~base.setGridMonitorFunc(1, 0, {
  if( ~prm.submixerB.isRecording,
    { ~base.turnGridWhite(1, 0); },
    { ~base.turnGridBlue(1, 0); }
  );
}, bank: gridLooperBank, page: page);
~base.setGridFunc(1, 0, {  ~prm.submixerB.loop }, bank: gridLooperBank, page: page);

// Playing:
~base.setGridMonitorFunc(1, 1, {
  if( ~prm.submixerB.isPlaying,
    { ~base.turnGridGreen(1, 1); },
    { ~base.turnGridRed(1, 1); }
  );
}, bank: gridLooperBank, page: page);
~base.setGridFunc(1, 1, { ~prm.submixerB.togglePlayLoop; }, bank: gridLooperBank, page: page);

// Reset:
~base.turnGridRed(1, 2, bank: gridLooperBank, page: page);
~base.setGridFunc(1, 2, { ~prm.submixerB.clearLoop(30); }, bank: gridLooperBank, page: page);

/// Submixer C:

// Recording:
~base.setGridMonitorFunc(2, 0, {
  if( ~prm.submixerB.isRecording,
    { ~base.turnGridWhite(2, 0); },
    { ~base.turnGridBlue(2, 0); }
  );
}, bank: gridLooperBank, page: page);
~base.setGridFunc(2, 0, {  ~prm.submixerC.loop }, bank: gridLooperBank, page: page);

// Playing:
~base.setGridMonitorFunc(2, 1, {
  if( ~prm.submixerC.isPlaying,
    { ~base.turnGridGreen(2, 1); },
    { ~base.turnGridRed(2, 1); }
  );
}, bank: gridLooperBank, page: page);
~base.setGridFunc(2, 1, { ~prm.submixerC.togglePlayLoop; }, bank: gridLooperBank, page: page);

// Reset:
~base.turnGridRed(2, 2, bank: gridLooperBank, page: page);
~base.setGridFunc(2, 2, { ~prm.submixerC.clearLoop(30); }, bank: gridLooperBank, page: page);




////// Submix Send Bank:

//// Submix Select:
~base.setGridMonitorFunc(0, 3, {
  ~base.turnGridMagenta(0, 3);
  ~base.turnGridWhite(1, 3);
  ~base.turnGridYellow(2, 3);
  ~base.turnGridGreen(activeSubmixBank -1, 3);
}, bank: gridSubmixSelectBank, page: page);

~base.setGridFunc(0, 3, {
  activeSubmixBank = submixASendBank;
  ~base.setActiveFadersBank(activeSubmixBank);
  ~base.setActiveTouchButtonsBank(activeSubmixBank);
}, bank: gridSubmixSelectBank, page: page);
~base.setGridFunc(1, 3, {
  activeSubmixBank = submixBSendBank;
  ~base.setActiveFadersBank(activeSubmixBank);
  ~base.setActiveTouchButtonsBank(activeSubmixBank);
}, bank: gridSubmixSelectBank, page: page);
~base.setGridFunc(2, 3, {
  activeSubmixBank = submixCSendBank;
  ~base.setActiveFadersBank(activeSubmixBank);
  ~base.setActiveTouchButtonsBank(activeSubmixBank);
}, bank: gridSubmixSelectBank, page: page);


//// Looper:

/// Submixer A:

// Recording:
~base.setGridMonitorFunc(0, 0, {
  if( ~prm.submixerA.isRecording,
    { ~base.turnGridWhite(0, 0); },
    { ~base.turnGridBlue(0, 0); }
  );
}, bank: gridSubmixSelectBank, page: page);
~base.setGridFunc(0, 0, {  ~prm.submixerA.loop }, bank: gridSubmixSelectBank, page: page);

// Playing:
~base.setGridMonitorFunc(0, 1, {
  if( ~prm.submixerA.isPlaying,
    { ~base.turnGridGreen(0, 1); },
    { ~base.turnGridRed(0, 1); }
  );
}, bank: gridSubmixSelectBank, page: page);
~base.setGridFunc(0, 1, { ~prm.submixerA.togglePlayLoop; }, bank:gridSubmixSelectBank , page: page);

// Reset:
~base.turnGridRed(0, 2, bank: gridSubmixSelectBank, page: page);
~base.setGridFunc(0, 2, { ~prm.submixerA.clearLoop(30); }, bank: gridSubmixSelectBank, page: page);

/// Submixer B:

// Recording:
~base.setGridMonitorFunc(1, 0, {
  if( ~prm.submixerB.isRecording,
    { ~base.turnGridWhite(1, 0); },
    { ~base.turnGridBlue(1, 0); }
  );
}, bank: gridSubmixSelectBank, page: page);
~base.setGridFunc(1, 0, {  ~prm.submixerB.loop }, bank: gridSubmixSelectBank, page: page);

// Playing:
~base.setGridMonitorFunc(1, 1, {
  if( ~prm.submixerB.isPlaying,
    { ~base.turnGridGreen(1, 1); },
    { ~base.turnGridRed(1, 1); }
  );
}, bank: gridSubmixSelectBank, page: page);
~base.setGridFunc(1, 1, { ~prm.submixerB.togglePlayLoop; }, bank: gridSubmixSelectBank, page: page);

// Reset:
~base.turnGridRed(1, 2, bank: gridSubmixSelectBank, page: page);
~base.setGridFunc(1, 2, { ~prm.submixerB.clearLoop(30); }, bank: gridSubmixSelectBank, page: page);

/// Submixer C:

// Recording:
~base.setGridMonitorFunc(2, 0, {
  if( ~prm.submixerB.isRecording,
    { ~base.turnGridWhite(2, 0); },
    { ~base.turnGridBlue(2, 0); }
  );
}, bank: gridSubmixSelectBank, page: page);
~base.setGridFunc(2, 0, {  ~prm.submixerC.loop }, bank: gridSubmixSelectBank, page: page);

// Playing:
~base.setGridMonitorFunc(2, 1, {
  if( ~prm.submixerC.isPlaying,
    { ~base.turnGridGreen(2, 1); },
    { ~base.turnGridRed(2, 1); }
  );
}, bank: gridSubmixSelectBank, page: page);
~base.setGridFunc(2, 1, { ~prm.submixerC.togglePlayLoop; }, bank: gridSubmixSelectBank, page: page);

// Reset:
~base.turnGridRed(2, 2, bank: gridSubmixSelectBank, page: page);
~base.setGridFunc(2, 2, { ~prm.submixerC.clearLoop(30); }, bank: gridSubmixSelectBank, page: page);






