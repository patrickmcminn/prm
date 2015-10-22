/*
Friday, January 16th 2015
Global Submixer Functions.scd
prm
plane from Sydney to Hobart
*/

var submixerBank = 0;
var returnsBank = 1;

var submixerMuteBank = 0;
var recordBank = 1;
var playToggleBank = 2;
var clearBank = 3;
var returnsMuteBank = 4;
var numRightButtonsBanks = 5;

var mixBank = 0;
var rateBank = 1;
var divBank = 2;
var posBank = 3;
var waveLossBank = 4;


~ohm.storageDict[\globalRightButtonsBank] = submixerMuteBank;
~ohm.storageDict[\globalRightKnobsBank] = mixBank;
~ohm.storageDict[\globalRightSlidersBank] = submixerBank;

~ohm.storageDict[\globalRightKnobsChangeRoutine] = r {
    loop {
    ~ohm.setActiveRightKnobsBank(rateBank);
    ~ohm.storageDict[\globalRightKnobsBank] = rateBank;
    numRightButtonsBanks.do({ | bank | ~ohm.turnRightButtonYellow(3, bank: bank); }).yield;

    // removing:
    /*
    ~ohm.setActiveRightKnobsBank(divBank);
    ~ohm.storageDict[\globalRightKnobsBank] = divBank;
    numRightButtonsBanks.do({ | bank | ~ohm.turnRightButtonBlue(3, bank: bank); }).yield;
    */

    // removing:
    /*
    ~ohm.setActiveRightKnobsBank(posBank);
    ~ohm.storageDict[\globalRightKnobsBank] = posBank;
    numRightButtonsBanks.do({ | bank | ~ohm.turnRightButtonCyan(3, bank: bank); }).yield;
    */

    ~ohm.setActiveRightKnobsBank(waveLossBank);
    ~ohm.storageDict[\globalRightKnobsBank] = waveLossBank;
    numRightButtonsBanks.do({ | bank | ~ohm.turnRightButtonWhite(3, bank: bank); }).yield;


    ~ohm.setActiveRightKnobsBank(mixBank);
    ~ohm.storageDict[\globalRightKnobsBank] = mixBank;
    numRightButtonsBanks.do({ | bank | ~ohm.turnRightButtonGreen(3, bank: bank); }).yield;
  }
};

~ohm.storageDict[\globalRightChangeRoutine] =  r {
    loop {
    ~ohm.setActiveRightButtonsBank(recordBank);
    ~ohm.setActiveRightSlidersBank(submixerBank);
    ~ohm.storageDict[\globalRightButtonsBank] = recordBank;
    ~ohm.storageDict[\globalRightSlidersBank] = submixerBank;
    ~ohm.turnCrossfaderButtonBlue(1).yield;

    ~ohm.setActiveRightButtonsBank(playToggleBank);
    ~ohm.setActiveRightSlidersBank(submixerBank);
    ~ohm.storageDict[\globalRightButtonsBank] = playToggleBank;
    ~ohm.storageDict[\globalRightSlidersBank] = submixerBank;
    ~ohm.turnCrossfaderButtonGreen(1).yield;

    ~ohm.setActiveRightButtonsBank(clearBank);
    ~ohm.setActiveRightSlidersBank(submixerBank);
    ~ohm.storageDict[\globalRightButtonsBank] = clearBank;
    ~ohm.storageDict[\globalRightSlidersBank] = submixerBank;
    ~ohm.turnCrossfaderButtonRed(1).yield;

    ~ohm.setActiveRightButtonsBank(returnsMuteBank);
    ~ohm.setActiveRightSlidersBank(returnsBank);
    ~ohm.storageDict[\globalRightButtonsBank] = returnsMuteBank;
    ~ohm.storageDict[\globalRightSlidersBank] = returnsBank;
    ~ohm.turnCrossfaderButtonWhite(1).yield;

    ~ohm.setActiveRightButtonsBank(submixerMuteBank);
    ~ohm.setActiveRightSlidersBank(submixerBank);
    ~ohm.storageDict[\globalRightButtonsBank] = submixerMuteBank;
    ~ohm.storageDict[\globalRightSlidersBank] = submixerBank;
    ~ohm.turnCrossfaderButtonCyan(1).yield;

  };
};


//////////// MAIN GLOBAL FUNCTION FOR RIGHT SIDE OF OHM

~ohm.storageDict[\loadGlobalSubmixerFunc] =  {

  ~ohm.addRightSlidersBanks(1);
  ~ohm.addRightButtonsBanks(4);
  ~ohm.addRightKnobsBanks(4);

  //// go home:
  ~ohm.turnControlButtonGreen(3, 0);
  ~ohm.setControlButtonFunc(3, 0, { ~ohm.setPage('main') });

  //// audio system mixer volumes:

  // submixer A:
  ~ohm.setRightSliderFunc(0, { | val |
    ~prm.submixerA.mixer.setVol(val.ccdbfs(-70, 0));
  }, bank: submixerBank);
  // submixer B:
  ~ohm.setRightSliderFunc(1, { | val |
    ~prm.submixerB.mixer.setVol(val.ccdbfs(-70, 0));
  }, bank: submixerBank);
  // submixer C:
  ~ohm.setRightSliderFunc(2, { | val |
    ~prm.submixerC.mixer.setVol(val.ccdbfs(-70, 0));
  }, bank: submixerBank);
  // master volume:
  ~ohm.setRightSliderFunc(3, { | val |
    ~prm.systemMixer.setVol(0, val.ccdbfs(-70, 0));
  }, bank: submixerBank);
  //// returns volumes:

  // Return 0:
  ~ohm.setRightSliderFunc(0, { | val |
    ~prm.reverb.mixer.setVol(val.ccdbfs(-70, 0));
  }, bank: returnsBank);

  // Return 1:
  ~ohm.setRightSliderFunc(1, { | val |
    ~prm.granulator.mixer.setVol(val.ccdbfs(-70, 6));
  }, bank: returnsBank);

  // Return 2:
  ~ohm.setRightSliderFunc(2, { | val |
    ~prm.modular.setVol(val.ccdbfs(-70, 0));
  }, bank: returnsBank);

  // Return 3:


  ///////////// Buttons:

  //////// Bank Change:

  // initial state:
  ~ohm.setActiveRightButtonsBank(~ohm.storageDict[\globalRightButtonsBank]);
  ~ohm.setActiveRightSlidersBank(~ohm.storageDict[\globalRightSlidersBank]);
  switch(~ohm.storageDict[\globalRightButtonsBank],
    submixerMuteBank, { ~ohm.turnCrossfaderButtonCyan(1) },
    recordBank, { ~ohm.turnCrossfaderButtonBlue(1) },
    playToggleBank, { ~ohm.turnCrossfaderButtonGreen(1) },
    clearBank, { ~ohm.turnCrossfaderButtonRed(1) },
    returnsMuteBank, { ~ohm.turnCrossfaderButtonWhite(1) }
  );

  // function:
  ~ohm.setCrossfaderButtonFunc(1, { ~ohm.storageDict[\globalRightChangeRoutine].next });

  //////// Mute Bank:
  // mutes submixes

  // initial color:
  if( ~prm.submixerA.mixer.isMuted, { ~ohm.turnRightButtonRed(0, bank: submixerMuteBank); }, { ~ohm.turnRightButtonGreen(0, bank: submixerMuteBank) });
  if( ~prm.submixerB.mixer.isMuted, { ~ohm.turnRightButtonRed(1, bank: submixerMuteBank); }, { ~ohm.turnRightButtonGreen(1, bank: submixerMuteBank) });
  if( ~prm.submixerC.mixer.isMuted, { ~ohm.turnRightButtonRed(2, bank: submixerMuteBank); }, { ~ohm.turnRightButtonGreen(2, bank: submixerMuteBank) });
  //if( ~prm.systemMixer.isMuted(0), { ~ohm.turnRightButtonRed(3, bank: submixerMuteBank); }, { ~ohm.turnRightButtonGreen(3, bank: submixerMuteBank) });
  // submixer A:
  ~ohm.setRightButtonFunc(0, {
    ~prm.submixerA.mixer.tglMute;
    if( ~prm.submixerA.mixer.isMuted, { ~ohm.turnRightButtonRed(0); }, { ~ohm.turnRightButtonGreen(0) });
  }, bank: submixerMuteBank);
  // submixer B:
  ~ohm.setRightButtonFunc(1, {
    ~prm.submixerB.mixer.tglMute;
    if( ~prm.submixerB.mixer.isMuted, { ~ohm.turnRightButtonRed(1); }, { ~ohm.turnRightButtonGreen(1) });
  }, bank: submixerMuteBank);
  // submixer C:
   ~ohm.setRightButtonFunc(2, {
    ~prm.submixerC.mixer.tglMute;
    if( ~prm.submixerC.mixer.isMuted, { ~ohm.turnRightButtonRed(2); }, { ~ohm.turnRightButtonGreen(2) });
  }, bank: submixerMuteBank);
  /*
  // master volume:
   ~ohm.setRightButtonFunc(3, {
    ~prm.systemMixer.tglMute(0);
    if( ~prm.systemMixer.isMuted(0), { ~ohm.turnRightButtonRed(3); }, { ~ohm.turnRightButtonGreen(3) });
  }, bank: submixerMuteBank);
  */

  //////// Loop Recording Bank:

  //// Initial Colors:
  if( ~prm.submixerA.isRecording, { ~ohm.turnRightButtonGreen(0, bank: recordBank) }, { ~ohm.turnRightButtonBlue(0, bank: recordBank) });
  if( ~prm.submixerB.isRecording, { ~ohm.turnRightButtonGreen(1, bank: recordBank) }, { ~ohm.turnRightButtonBlue(1, bank: recordBank) });
  if( ~prm.submixerB.isRecording, { ~ohm.turnRightButtonGreen(2, bank: recordBank) }, { ~ohm.turnRightButtonBlue(2, bank: recordBank) });
  ~ohm.turnRightButtonOff(3, bank: recordBank);

  // submixer A:
  ~ohm.setRightButtonFunc(0, {
    ~prm.submixerA.loop;
    if( ~prm.submixerA.isRecording,
      {  ~ohm.turnRightButtonGreen(0); if( ~prm.submixerA.isPlaying, { ~ohm.turnRightButtonGreen(0, bank: playToggleBank) });},
      { ~ohm.turnRightButtonBlue(0); if( ~prm.submixerA.isPlaying, { ~ohm.turnRightButtonGreen(0, bank: playToggleBank) }); })
  }, bank: recordBank);

  // submixer B:
  ~ohm.setRightButtonFunc(1, {
    ~prm.submixerB.loop;
    if( ~prm.submixerB.isRecording,
      {  ~ohm.turnRightButtonGreen(1); if( ~prm.submixerB.isPlaying, { ~ohm.turnRightButtonGreen(1, bank: playToggleBank) });},
      { ~ohm.turnRightButtonBlue(1); if( ~prm.submixerB.isPlaying, { ~ohm.turnRightButtonGreen(1, bank: playToggleBank) }); })
  }, bank: recordBank);

  // submixer C:
  ~ohm.setRightButtonFunc(2, {
    ~prm.submixerC.loop;
    if( ~prm.submixerC.isRecording,
      {  ~ohm.turnRightButtonGreen(2); if( ~prm.submixerC.isPlaying, { ~ohm.turnRightButtonGreen(2, bank: playToggleBank) });},
      { ~ohm.turnRightButtonBlue(2); if( ~prm.submixerC.isPlaying, { ~ohm.turnRightButtonGreen(2, bank: playToggleBank) }); })
  }, bank: recordBank);

  //////// Play Toggle Bank:

  //// Initial Colors:
  if( ~prm.submixerA.isPlaying, { ~ohm.turnRightButtonGreen(0, bank: playToggleBank) }, { ~ohm.turnRightButtonRed(0, bank: playToggleBank); });
  if( ~prm.submixerB.isPlaying, { ~ohm.turnRightButtonGreen(1, bank: playToggleBank) }, { ~ohm.turnRightButtonRed(1, bank: playToggleBank); });
  if( ~prm.submixerC.isPlaying, { ~ohm.turnRightButtonGreen(2, bank: playToggleBank) }, { ~ohm.turnRightButtonRed(2, bank: playToggleBank); });
  ~ohm.turnRightButtonOff(3, bank: playToggleBank);

  // submixer A:
  ~ohm.setRightButtonFunc(0, {
    ~prm.submixerA.togglePlayLoop;
    if( ~prm.submixerA.isPlaying, { ~ohm.turnRightButtonGreen(0) }, { ~ohm.turnRightButtonRed(0) })
  }, bank: playToggleBank);

  // submixer B:
  ~ohm.setRightButtonFunc(1, {
    ~prm.submixerB.togglePlayLoop;
    if( ~prm.submixerB.isPlaying, { ~ohm.turnRightButtonGreen(1) }, { ~ohm.turnRightButtonRed(1) })
  }, bank: playToggleBank);

  // submixer C:
  ~ohm.setRightButtonFunc(2, {
    ~prm.submixerC.togglePlayLoop;
    if( ~prm.submixerC.isPlaying, { ~ohm.turnRightButtonGreen(2) }, { ~ohm.turnRightButtonRed(2) })
  }, bank: playToggleBank);

  //////// Clear Bank:

  //// intial Colors:
  3.do({ | button | ~ohm.turnRightButtonRed(button, bank: clearBank); });
  ~ohm.turnRightButtonOff(3, bank: clearBank);

  // submixer A:
  ~ohm.setRightButtonFunc(0, { ~prm.submixerA.clearLoop(30); }, bank: clearBank);

  // submixer B:
  ~ohm.setRightButtonFunc(1, { ~prm.submixerB.clearLoop(30); }, bank: clearBank);

  // submixer C:
  ~ohm.setRightButtonFunc(2, { ~prm.submixerC.clearLoop(30); }, bank: clearBank);

  //////// Returns Mute Bank:

  //// initial colors:
  if( ~prm.reverb.mixer.isMuted, { ~ohm.turnRightButtonRed(0, bank: returnsMuteBank); }, { ~ohm.turnRightButtonGreen(0, bank: returnsMuteBank); });
  if( ~prm.granulator.mixer.isMuted, { ~ohm.turnRightButtonRed(1, bank: returnsMuteBank); }, { ~ohm.turnRightButtonGreen(1, bank: returnsMuteBank); });
  if( ~prm.modular.isMuted, { ~ohm.turnRightButtonRed(1, bank: returnsMuteBank); }, {
    ~ohm.turnRightButtonGreen(1, bank: returnsMuteBank); });

  // reverb:
  ~ohm.setRightButtonFunc(0, {
    ~prm.reverb.mixer.tglMute;
    if( ~prm.reverb.mixer.isMuted, { ~ohm.turnRightButtonRed(0); }, { ~ohm.turnRightButtonGreen(0); });
  }, bank: returnsMuteBank);

  // granulator:
  ~ohm.setRightButtonFunc(1, {
    ~prm.granulator.mixer.tglMute;
     if( ~prm.granulator.mixer.isMuted, { ~ohm.turnRightButtonRed(1); }, { ~ohm.turnRightButtonGreen(1); });
  }, bank: returnsMuteBank);

  // modular:
  ~ohm.setRightButtonFunc(2, {
    ~prm.modular.tglMute;
    if( ~prm.granulator.mixer.isMuted, { ~ohm.turnRightButtonRed(2); }, { ~ohm.turnRightButtonGreen(2); });
  }, bank: returnsMuteBank);

  //////////// Right Knob Change:

  //// bank change:

  // intial state:
  ~ohm.setActiveRightKnobsBank(~ohm.storageDict[\globalRightKnobsBank]);
  case
  { ~ohm.storageDict[\globalRightKnobsBank] == mixBank } { ~ohm.turnRightButtonGreen(3) }
  { ~ohm.storageDict[\globalRightKnobsBank] == rateBank } { ~ohm.turnRightButtonYellow(3); }
  { ~ohm.storageDict[\globalRightKnobsBank] == divBank } { ~ohm.turnRightButtonBlue(3) }
  { ~ohm.storageDict[\globalRightKnobsBank] == posBank } { ~ohm.turnRightButtonCyan(3) }
  { ~ohm.storageDict[\globalRightKnobsBank] == waveLossBank } { ~ohm.turnRightButtonWhite(3) };
  /// func:
  4.do({  | bank |
    ~ohm.setRightButtonFunc(3, { ~ohm.storageDict[\globalRightKnobsChangeRoutine].next; }, bank: bank);
  });

  //////// Loop Mix Knobs:

  // submixer A:
  ~ohm.setRightKnobFunc(0, { | val |
    var mix = val.linlin(0, 127, -1, 1);
    ~prm.submixerA.setMix(mix);
  }, bank: mixBank);

  // submixer B:
  ~ohm.setRightKnobFunc(1, { | val |
    var mix = val.linlin(0, 127, -1, 1);
    ~prm.submixerB.setMix(mix);
  }, bank: mixBank);

  // submixer C:
  ~ohm.setRightKnobFunc(2, { | val |
    var mix = val.linlin(0, 127, -1, 1);
    ~prm.submixerC.setMix(mix);
  }, bank: mixBank);

  //////// Rate Knobs:

  // Submixer A:
  ~ohm.setRightKnobFunc(0, { | val |
    var rate = val.linlin(0, 127, 0.1, 4);
    ~prm.submixerA.setLoopRate(rate);
  }, bank: rateBank);

  // Submixer B:
  ~ohm.setRightKnobFunc(1, { | val |
    var rate = val.linlin(0, 127, 0.1, 4);
    ~prm.submixerB.setLoopRate(rate);
  }, bank: rateBank);

  // Submixer C:
  ~ohm.setRightKnobFunc(2, { | val |
    var rate = val.linlin(0, 127, 0.1, 4);
    ~prm.submixerC.setLoopRate(rate);
  }, bank: rateBank);

  // reset knob:
  ~ohm.setRightKnobFunc(3, {
    ~prm.submixerA.setLoopRate(1);
    ~prm.submixerB.setLoopRate(1);
    ~prm.submixerC.setLoopRate(1);
  }, bank: rateBank);

  //////// Loop Division Knobs:

  // gonna remove these, I think:
  /*

  // Submixer A:
  ~ohm.setRightKnobFunc(0, { | val |
    var div = val.linlin(0, 127, 1, 15).asInt;
    ~prm.submixerA.setLoopDivison(div);
  }, bank: divBank);

  // Submixer B:
  ~ohm.setRightKnobFunc(1, { | val |
    var div = val.linlin(0, 127, 1, 15).asInt;
    ~prm.submixerB.setLoopDivison(div);
  }, bank: divBank);

  // Submixer C:
  ~ohm.setRightKnobFunc(2, { | val |
    var div = val.linlin(0, 127, 1, 15).asInt;
    ~prm.submixerC.setLoopDivison(div);
  }, bank: divBank);
  */

  //////// Loop Position Knobs:

  // also gonna remove these:
  /*
  // Submixer A:
  ~ohm.setRightKnobFunc(0, { | val |
    var pos = val.linlin(0, 127, 0, 1);
    ~prm.submixerA.setLoopPosition(pos);
  }, bank: posBank);

  // Submixer B:
  ~ohm.setRightKnobFunc(1, { | val |
    var pos = val.linlin(0, 127, 0, 1);
    ~prm.submixerB.setLoopPosition(pos);
  }, bank: posBank);

  // Submixer C:
  ~ohm.setRightKnobFunc(2, { | val |
    var pos = val.linlin(0, 127, 0, 1);
    ~prm.submixerC.setLoopPosition(pos);
  }, bank: posBank);
  */

  //////// WaveLoss Knobs:

  // Submixer A:
  ~ohm.setRightKnobFunc(0, { | val |
    var loss = val.linlin(0, 127, 0, 85);
    ~prm.submixerA.setWaveLossAmount(loss);
  }, bank: waveLossBank);

  // Submixer B:
  ~ohm.setRightKnobFunc(1, { | val |
    var loss = val.linlin(0, 127, 0, 85);
    ~prm.submixerB.setWaveLossAmount(loss);
  }, bank: waveLossBank);

  // Submixer C:
  ~ohm.setRightKnobFunc(2, { | val |
    var loss = val.linlin(0, 127, 0, 85);
    ~prm.submixerC.setWaveLossAmount(loss);
  }, bank: waveLossBank);

  //// waveloss mode:
  // this doesn't work exactly as I'd like it to, but I've gotten used to its functionality
  // values < 127, mode 1
  // value = 127, mode 2
  ~ohm.setRightKnobFunc(3, {  | val |
    var mode = val.linlin(0, 127, 1, 2).asInt;
    ~prm.submixerA.setWaveLossMode(mode);
    ~prm.submixerB.setWaveLossMode(mode);
    ~prm.submixerC.setWaveLossMode(mode);
  }, bank: waveLossBank);

};