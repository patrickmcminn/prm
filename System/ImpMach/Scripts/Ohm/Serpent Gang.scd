/*
Tuesday, February 18th 2014
Serpent Gang.scd
*/

var page = 'serpentGang';
var serpentBank = 0;
var dryBank = 1;


// make Serpent Gang page and banks:
~ohm.makePage(page);
~ohm.addLeftSlidersBanks(1, page: page);
~ohm.addLeftButtonsBanks(1, page: page);
~ohm.addLeftKnobsBanks(1, page: page);

~ohm.turnAllGridWhite(page: page);

~ohmFunc.addMasterFaders(page);
~ohmFunc.addDryFaders(page, dryBank);
// go home:
~ohm.turnControlButtonGreen(3, 0, page: page);
~ohm.setControlButtonFunc(3, 0, { ~ohm.setPage('main'); }, page: page);


// create functions to record and toggle playback of the loop

~ohm.turnGridRed(0, 0, page: page);
~ohm.setGridFunc(0, 0, { ~im.getSong('SerpentGang').toggleRecordLoop; { ~im.getSong('SerpentGang').toggleRecordLoop; }.defer(10); }, page: page);
~ohm.turnGridBlue(1, 0, page: page);
~ohm.setGridFunc(1, 0, r {
  loop {
    ~im.getSong('SerpentGang').playLoop;
    ~ohm.turnGridGreen(1, 0).yield;

    ~im.getSong('SerpentGang').stopLoop;
    ~ohm.turnGridBlue(1, 0).yield;
  };
}, page: page);

// change banks:
~ohm.turnGridPurple(0, 7, page: page);
~ohm.setGridFunc(0, 7, r {
  loop {
    ~ohm.setActiveLeftKnobsBank(dryBank);
    ~ohm.setActiveLeftSlidersBank(dryBank);
    ~ohm.setActiveLeftButtonsBank(dryBank);
    ~ohm.turnGridYellow(0, 7).yield;

    ~ohm.setActiveLeftKnobsBank(serpentBank);
    ~ohm.setActiveLeftSlidersBank(serpentBank);
    ~ohm.setActiveLeftButtonsBank(serpentBank);
    ~ohm.turnGridPurple(0, 7).yield;
  };
}, page: page);


//////// SerpentBank Functions:

// Serpent Gang Looper:

// Volume Slider:
~ohm.setLeftSliderFunc(0, { | val |
  ~im.getSong('SerpentGang').setVol(2, val.ccdbfs(-70, 0), 0.1);
}, bank: serpentBank, page: page);

// Mute Button:
~ohm.turnLeftButtonGreen(0, bank: serpentBank, page: page);
~ohm.setLeftButtonFunc(0, {
  if( ~im.getSong('SerpentGang').isMuted(2),
    { ~im.getSong('SerpentGang').unMute(2); ~ohm.turnLeftButtonGreen(0); },
    { ~im.getSong('SerpentGang').mute(2); ~ohm.turnLeftButtonRed(0); }
  );
}, bank: serpentBank, page: page);

// trumpet multiHarm:

// volume slider:
~ohm.setLeftSliderFunc(1, { | val |
  ~im.getSong('SerpentGang').mixer.setVol(0, val.ccdbfs(-70, 0), 0.1);
}, bank: serpentBank, page: page);

// mute toggle:
~ohm.turnLeftButtonGreen(1, bank: serpentBank, page: page);
~ohm.setLeftButtonFunc(1, {
if( ~im.getSong('SerpentGang').isMuted(0),
    { ~im.getSong('SerpentGang').unMute(0); ~ohm.turnLeftButtonGreen(1); },
    { ~im.getSong('SerpentGang').mute(0); ~ohm.turnLeftButtonRed(1); }
  );
}, bank: serpentBank, page: page);


// flugel multiHarm:

// Volume Slider:
~ohm.setLeftSliderFunc(2, { | val |
  ~im.getSong('SerpentGang').mixer.setVol(1, val.ccdbfs(-70, 0), 0.1);
}, bank: serpentBank, page: page);

// Mute Button:
~ohm.turnLeftButtonGreen(2, bank: serpentBank, page: page);
~ohm.setLeftButtonFunc(2, {
  if( ~im.getSong('SerpentGang').mixer.isMuted(1),
    { ~im.getSong('SerpentGang').mixer.unMute(1); ~ohm.turnLeftButtonGreen(2); },
    { ~im.getSong('SerpentGang').mixer.mute(1); ~ohm.turnLeftButtonRed(2); }
  );
}, bank: serpentBank, page:page);

// reverb sends:
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~im.getSong('SerpentGang').mixer.setSendVol(2, 1, val.ccdbfs(-70, 0));
}, bank: serpentBank, page: page);

~ohm.setLeftKnobFunc(1, 1, { | val |
  ~im.getSong('SerpentGang').mixer.setSendVol(0, 1, val.ccdbfs(-70, 0));
}, bank: serpentBank, page: page);

~ohm.setLeftKnobFunc(2, 1, { | val |
  ~im.getSong('SerpentGang').mixer.setSendVol(1, 1, val.ccdbfs(-70, 0));
}, bank: serpentBank, page: page);

// granulator sends:
~ohm.setLeftKnobFunc(0, 0, { | val |
  ~im.getSong('SerpentGang').mixer.setSendVol(2, 2, val.ccdbfs(-70, 0));
}, bank: serpentBank, page: page);

~ohm.setLeftKnobFunc(1, 0, { | val |
  ~im.getSong('SerpentGang').mixer.setSendVol(0, 2, val.ccdbfs(-70, 0));
}, bank: serpentBank, page: page);

~ohm.setLeftKnobFunc(2, 0, { | val |
  ~im.getSong('SerpentGang').mixer.setSendVol(1, 2, val.ccdbfs(-70, 0));
}, bank: serpentBank, page: page);

//////// Song Presets:

~ohm.turnGridGreen(5, 7, page: page);
~ohm.turnGridPurple(6, 7, page: page);
~ohm.turnGridPurple(7, 7, page: page);

~ohm.setGridFunc(5, 7, {
  ~ohm.turnGridGreen(5, 7);
  ~ohm.turnGridPurple(6, 7);
  ~ohm.turnGridPurple(7, 7);
  3.do({ | index |
    ~im.getSong('SerpentGang').mixer.setSendVol(index, 1, -140);
    ~directIns.mixer.setSendVol(index + 1, 1, -140);
    ~im.getSong('SerpentGang').mixer.unMute(index);
  });
}, page: page);

~ohm.setGridFunc(6, 7, {
  ~ohm.turnGridGreen(6, 7);
  ~ohm.turnGridPurple(5, 7);
  ~ohm.turnGridPurple(7, 7);
  2.do({ | index |
    ~directIns.mixer.setSendVol(index + 1, 1, -6);
    ~im.getSong('SerpentGang').mixer.setSendVol(index, 1, -6);
    ~im.getSong('SerpentGang').mixer.mute(index);
  });
}, page: page);

~ohm.setGridFunc(7, 7, {
  ~ohm.turnGridGreen(7, 7);
  ~ohm.turnGridPurple(5, 7);
  ~ohm.turnGridPurple(6, 7);
  2.do({ | index |
    ~im.getSong('SerpentGang').mixer.unMute(index);
    ~im.getSong('SerpentGang').mixer.setSendVol(index, 1, -6);
    ~directIns.mixer.setSendVol(index + 1, 1, -6);
  });
}, page: page);