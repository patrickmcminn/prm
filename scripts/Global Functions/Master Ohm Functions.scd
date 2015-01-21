/*
Friday, January 16th 2015
Master Ohm Functions.scd
prm
plane from Sydney to Hobart
*/

~ohm.addGlobalFunction(\audioSystemMixer, {

  var muteBank = 0;
  var recordBank = 1;
  var playToggleBank = 2;
  var clearBank = 3;

  var mixBank = 0;
  var rateBank = 1;
  var divBank = 2;
  var posBank = 3;
  var waveLossBank = 4;

  var knobChangeRoutine;

  ~ohm.addRightButtonsBanks(3);
  ~ohm.addRightKnobsBanks(4);

  // audio system mixer volumes:

  // submixer A:
  ~ohm.setRightSliderFunc(0, { | val |
    var vol = val.linlin(0, 127, -70, 0);
    ~prm.submixerA.mixer.setVol(vol);
  });
  // submixer B:
  ~ohm.setRightSliderFunc(1, { | val |
    var vol = val.linlin(0, 127, -70, 0);
    ~prm.submixerB.mixer.setVol(vol);
  });
  // submixer C:
  ~ohm.setRightSliderFunc(2, { | val |
    var vol = val.linlin(0, 127, -70, 0);
    ~prm.submixerC.mixer.setVol(vol);
  });
  // master volume:
  ~ohm.setRightSliderFunc(3, { | val |
    var vol = val.linlin(0, 127, -70, 0);
    ~prm.systemMixer.setVol(0, vol);
  });

  //////// Bank Change:

  // initial color:
  ~ohm.turnCrossfaderButtonCyan(1);

  ~ohm.setCrossfaderButtonFunc(1, r {
    loop {
      ~ohm.setActiveRightButtonsBank(recordBank);
      ~ohm.turnCrossfaderButtonBlue(1).yield;

      ~ohm.setActiveRightButtonsBank(playToggleBank);
      ~ohm.turnCrossfaderButtonGreen(1).yield;

      ~ohm.setActiveRightButtonsBank(clearBank);
      ~ohm.turnCrossfaderButtonRed(1).yield;

      ~ohm.setActiveRightButtonsBank(muteBank);
      ~ohm.turnCrossfaderButtonCyan(1).yield;

    };
  });

  //////// Mute Bank:

  // initial color:
  if( ~prm.submixerA.mixer.isMuted, { ~ohm.turnRightButtonRed(0, bank: muteBank); }, { ~ohm.turnRightButtonGreen(0, bank: muteBank) });
  if( ~prm.submixerB.mixer.isMuted, { ~ohm.turnRightButtonRed(1, bank: muteBank); }, { ~ohm.turnRightButtonGreen(1, bank: muteBank) });
  if( ~prm.submixerC.mixer.isMuted, { ~ohm.turnRightButtonRed(2, bank: muteBank); }, { ~ohm.turnRightButtonGreen(2, bank: muteBank) });
  if( ~prm.systemMixer.isMuted(0), { ~ohm.turnRightButtonRed(3, bank: muteBank); }, { ~ohm.turnRightButtonGreen(3, bank: muteBank) });

  // submixer A:
  ~ohm.setRightButtonFunc(0, {
    ~prm.submixerA.mixer.tglMute;
    if( ~prm.submixerA.mixer.isMuted, { ~ohm.turnRightButtonRed(0); }, { ~ohm.turnRightButtonGreen(0) });
  }, bank: muteBank);
  // submixer B:
  ~ohm.setRightButtonFunc(1, {
    ~prm.submixerB.mixer.tglMute;
    if( ~prm.submixerB.mixer.isMuted, { ~ohm.turnRightButtonRed(1); }, { ~ohm.turnRightButtonGreen(1) });
  }, bank: muteBank);
  // submixer C:
   ~ohm.setRightButtonFunc(2, {
    ~prm.submixerC.mixer.tglMute;
    if( ~prm.submixerC.mixer.isMuted, { ~ohm.turnRightButtonRed(2); }, { ~ohm.turnRightButtonGreen(2) });
  }, bank: muteBank);
  // master volume:
   ~ohm.setRightButtonFunc(3, {
    ~prm.systemMixer.tglMute(0);
    if( ~prm.systemMixer.isMuted(0), { ~ohm.turnRightButtonRed(3); }, { ~ohm.turnRightButtonGreen(3) });
  }, bank: muteBank);

  //////// Loop Recording Bank:

  //// Initial Colors:
  if( ~prm.submixerA.isRecording, { ~ohm.turnRightButtonGreen(0, bank: recordBank) }, { ~ohm.turnRightButtonBlue(0, bank: recordBank) });
  if( ~prm.submixerB.isRecording, { ~ohm.turnRightButtonGreen(1, bank: recordBank) }, { ~ohm.turnRightButtonBlue(1, bank: recordBank) });
  if( ~prm.submixerB.isRecording, { ~ohm.turnRightButtonGreen(2, bank: recordBank) }, { ~ohm.turnRightButtonBlue(2, bank: recordBank) });
  ~ohm.turnRightButtonOff(3, bank: recordBank);

  // submixer A:
  ~ohm.setRightButtonFunc(0, {
    ~prm.submixerA.loop;
    if( ~prm.submixerA.isRecording, { ~ohm.turnRightButtonGreen(0) }, { ~ohm.turnRightButtonBlue(0) });
  }, bank: recordBank);

  // submixer B:
  ~ohm.setRightButtonFunc(1, {
    ~prm.submixerB.loop;
    if( ~prm.submixerB.isRecording, { ~ohm.turnRightButtonGreen(1) }, { ~ohm.turnRightButtonBlue(1) });
  }, bank: recordBank);

  // submixer C:
  ~ohm.setRightButtonFunc(2, {
    ~prm.submixerB.loop;
    if( ~prm.submixerB.isRecording, { ~ohm.turnRightButtonGreen(2) }, { ~ohm.turnRightButtonBlue(2) });
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

  //////////// Right Knob Change:

  //// bank change:

  knobChangeRoutine = r {
    loop {
      ~ohm.setActiveRightKnobsBank(rateBank);
      4.do({ | bank | ~ohm.turnRightButtonYellow(3, bank: bank); }).yield;

      ~ohm.setActiveRightKnobsBank(divBank);
      4.do({ | bank | ~ohm.turnRightBlue(3, bank: bank); }).yield;

      ~ohm.setActiveRightKnobsBank(posBank);
      4.do({ | bank | ~ohm.turnRightButtonCyan(3, bank: bank); }).yield;

      ~ohm.setActiveRightKnobsBank(waveLossBank);
      4.do({ | bank | ~ohm.turnRightButtonWhite(3, bank: bank); }).yield;

      ~ohm.setActiveRightButtonsBank(mixBank);
      4.do({ | bank | ~ohm.turnRightButtonGreen(3, bank: bank); }).yield;
    }
  };

  //////// Reset Func:

  //// intial colors:

  /// func:
  4.do({  | bank |
    ~ohm.setRightButtonFunc(3, { knobChangeRoutine.next; }, bank: bank);
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
    var rate = val.linlin(0, 127, 0.1, 10);
    ~prm.submixerA.setLoopRate(rate);
  }, bank: rateBank);

  // Submixer B:
  ~ohm.setRightKnobFunc(1, { | val |
    var rate = val.linlin(0, 127, 0.1, 10);
    ~prm.submixerB.setLoopRate(rate);
  }, bank: rateBank);

  // Submixer C:
  ~ohm.setRightKnobFunc(2, { | val |
    var rate = val.linlin(0, 127, 0.1, 10);
    ~prm.submixerC.setLoopRate(rate);
  }, bank: rateBank);

  // reset knob:
  ~ohm.setRightKnobFunc(3, {
    ~prm.submixerA.setLoopRate(1);
    ~prm.submixerB.setLoopRate(1);
    ~prm.submixerC.setLoopRate(1);
  }, bank: rateBank);

  //////// Loop Division Knobs:

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

  //////// Loop Position Knobs:

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

  //////// WaveLoss Knobs:

  // Submixer A:
  ~ohm.setRightKnobFun(0, { | val |
    var loss = val.linlin(0, 127, 0, 100);
    ~prm.submixerA.setWaveLossAmount(loss);
  }, bank: waveLossBank);

  // Submixer B:
  ~ohm.setRightKnobFun(1, { | val |
    var loss = val.linlin(0, 127, 0, 100);
    ~prm.submixerB.setWaveLossAmount(loss);
  }, bank: waveLossBank);

  // Submixer C:
  ~ohm.setRightKnobFun(2, { | val |
    var loss = val.linlin(0, 127, 0, 100);
    ~prm.submixerC.setWaveLossAmount(loss);
  }, bank: waveLossBank);

  //// waveloss mode:
  ~ohm.setRightKnobFunc(3, {  | val |
    var mode = val.linlin(0, 127, 1, 2).asInt;
    ~prm.submixerA.setWaveLossMode(mode);
    ~prm.submixerB.setWaveLossMode(mode);
    ~prm.submixerC.setWaveLossMode(mode);
  }, bank: waveLossBank);

});