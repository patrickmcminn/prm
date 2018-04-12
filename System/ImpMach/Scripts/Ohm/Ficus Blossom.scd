/*
Tuesday, March 4th 2014
Ficus Blossum.scd
prm
*/

var page = 'ficusBlossom';
var bank = 'active';

~ohm.makePage(page);

~ohm.turnAllGridBlue(page: page);

~ohmFunc.addMasterFaders(page);
// go home:
~ohm.turnControlButtonGreen(3, 0, page: page);
~ohm.setControlButtonFunc(3, 0, { ~ohm.setPage('main'); }, page: page);

// sustainer:
~ohm.turnGridPurple(0, 0, page: page);
~ohm.setGridFunc(0, 0, r {
  loop {
    ~im.getSong('FicusBlossom').makeSustainers(0.5);
    ~ohm.turnGridGreen(0, 0, page: page).yield;

    ~im.getSong('FicusBlossom').freeSustainers;
    ~ohm.turnGridRed(0, 0, page: page).yield;
  };
}, page: page);

//////// Faders:

//////// Sustainer Fader:

~ohm.setLeftSliderFunc(0, { | val |
  ~im.getSong('FicusBlossom').mixer.setVol(0, val.ccdbfs(-70, 3));
  ~im.getSong('FicusBlossom').mixer.setVol(1, val.ccdbfs(-70, 3));
}, page: page);

// verb send:
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~im.getSong('FicusBlossom').mixer.setSendVol(0, 1, val.ccdbfs(-140, 0));
}, page: page);

// granulator send:
~ohm.setLeftKnobFunc(0, 0, { | val |
  ~im.getSong('FicusBlossom').mixer.setSendVol(0, 2, val.ccdbfs(-140, 0));
}, page: page);

// set sustainer mix:
~ohm.setLeftKnobFunc(0, 2, { | val |
  var mix = val.linlin(0, 127, 0, 1);
  ~im.getSong('FicusBlossom').setMix(mix);
}, page: page);

~ohm.setLeftButtonFunc(0, {
  if( ~im.getSong('FicusBlossom').isMuted(0),
    { ~im.getSong('FicusBlossom').unMute(0); ~ohm.turnLeftButtonGreen(0); },
    { ~im.getSong('FicusBlossom').mute(0); ~ohm.turnLeftButtonRed(0); }
  );
}, page: page);


//////// Dry Faders:

3.do({ | index | ~ohm.setLeftSliderFunc(index + 1, { | val |
  ~directIns.setVol(index + 1, val.ccdbfs(-140, 0), 0.1);
  }, page: page);
});

// Buttons for Input Muting:
4.do({ | index | ~ohm.turnLeftButtonGreen(index, page: page, bank: bank) });

3.do({ | index | ~ohm.setLeftButtonFunc(index + 1,  {
  if( ~directIns.isMuted(index + 1),
    { ~directIns.unMute; ~ohm.turnLeftButtonGreen(index + 1); },
    { ~directIns.mute; ~ohm.turnLeftButtonRed(index + 1); }
  );
  }, page: page);
});

// Reverb Sends for Input Channels:
3.do({ | index | ~ohm.setLeftKnobFunc(index + 1, 1, { | val |
  ~directIns.mixer.setSendVol(index + 1, 1, val.ccdbfs(-140, 0));
  }, page: page);
});

// Granulator Sends for Input Channels:
3.do({ | index | ~ohm.setLeftKnobFunc(index + 1, 0, { | val |
  ~directIns.mixer.setSendVol(index + 1, 2, val.ccdbfs(-140, 0));
  }, page: page);
});