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
  var posBank = 2;

  ~ohm.addRightButtonsBanks(3);
  ~ohm.addRightKnobsBanks(2);


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
    ~ohm.setActiveRightButtonsBank(recordBank);
    ~ohm.turnCrossfaderButtonBlue(1).yield;

    ~ohm.setActiveRightButtonsBank(playToggleBank);
    ~ohm.turnCrossfaderButtonGreen(1).yield;

    ~ohm.setActiveRightButtonsBank(clearBank);
    ~ohm.turnCrossfaderButtonRed(1).yield;

    ~ohm.setActiveRightButtonsBank(muteBank);
    ~ohm.turnCrossfaderButtonCyan(1).yield;
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

});