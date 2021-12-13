/*
Sunday, April 16th 2017
FalseSelfBase.scd
prm

updated 12/11/2021
*/

var page = 'falseSelf';

var main = 0;

~base.makePage(page);

// bass section:
~base.setFaderMode(1, 'yellowFill', main, page);
~base.setFaderMode(2, 'yellowFill', main, page);
~base.setFaderMode(3, 'yellowFill', main, page);

// mixers:
5.do({ | i | ~base.setFaderMode(i+4, 'whiteFill', main, page); });

~base.setFaderMonitorFunc(1, {
  var fb, gtr, moog;
  var kick, tpt, tptMel, mod, modNoise;
  var kickMute, tptMute, tptMelMute, modMute, modNoiseMute;
  var tptRec, tptMelRec, modRec, modNoiseRec;

  fb = ~falseSelf.bassSection.mixer.vol(0);
  gtr = ~falseSelf.bassSection.mixer.vol(1);
  moog = ~falseSelf.bassSection.mixer.vol(2);
  kick = ~falseSelf.mixer.vol(4);
  tpt = ~falseSelf.mixer.vol(5);
  tptMel = ~falseSelf.mixer.vol(8);
  mod = ~falseSelf.mixer.vol(14);
  modNoise = ~falseSelf.mixer.vol(15);

  kickMute = ~falseSelf.mixer.isMuted(4);
  tptMute = ~falseSelf.mixer.isMuted(5);
  tptMelMute = ~falseSelf.mixer.isMuted(8);
  modMute = ~falseSelf.mixer.isMuted(14);
  modNoiseMute = ~falseSelf.mixer.isMuted(15);

  tptRec = ~falseSelf.mainTrumpetInput.isMuted;
  tptMelRec = ~falseSelf.trumpetMelodyInput.isMuted;
  modRec = ~falseSelf.modular.isMuted;
  modNoiseRec = ~falseSelf.modNoise.isMuted;

  ~base.setFaderValue(1, fb.dbfsCC, main, page);
  ~base.setFaderValue(2, gtr.dbfsCC, main, page);
  ~base.setFaderValue(3, moog.dbfsCC, main, page);
  ~base.setFaderValue(4, kick.dbfsCC, main, page);
  ~base.setFaderValue(5, tpt.dbfsCC, main, page);
  ~base.setFaderValue(6, tptMel.dbfsCC, main, page);
  ~base.setFaderValue(7, mod.dbfsCC, main, page);
  ~base.setFaderValue(8, modNoise.dbfsCC, main, page);

  if( kickMute, { ~base.turnTouchButtonRed(4) }, { ~base.turnTouchButtonGreen(4) });
  if( tptMute, { ~base.turnTouchButtonRed(5) }, { ~base.turnTouchButtonGreen(5) });
  if( tptMelMute, { ~base.turnTouchButtonRed(6) }, { ~base.turnTouchButtonGreen(6) });
  if( modMute, { ~base.turnTouchButtonRed(7) }, { ~base.turnTouchButtonGreen(7) });
  if( modNoiseMute, { ~base.turnTouchButtonRed(8) }, { ~base.turnTouchButtonGreen(8) });

  if( tptRec, { ~base.turnGridBlue(4, 3) }, { ~base.turnGridRed(4, 3) });
  if( tptMelRec, { ~base.turnGridBlue(5, 3) }, { ~base.turnGridRed(5, 3) });
  if( modRec, { ~base.turnGridBlue(6, 3) }, { ~base.turnGridRed(6, 3) });
  if( modNoiseRec, { ~base.turnGridBlue(7, 3) }, { ~base.turnGridRed(7, 3) });
}, bank: main, page: page);

~base.setFaderFunc(1, { | val | ~falseSelf.bassSection.mixer.setVol(0, val.ccdbfs);
}, bank: main, page: page);
~base.setFaderFunc(2, { | val | ~falseSelf.bassSection.mixer.setVol(1, val.ccdbfs);
}, bank: main, page: page);
~base.setFaderFunc(3, { | val | ~falseSelf.bassSection.mixer.setVol(2, val.ccdbfs);
}, bank: main, page: page);

~base.setFaderFunc(4, { | val | ~falseSelf.mixer.setVol(4, val.ccdbfs) }, main, page);
~base.setFaderFunc(5, { | val | ~falseSelf.mixer.setVol(5, val.ccdbfs) }, main, page);
~base.setFaderFunc(6, { | val | ~falseSelf.mixer.setVol(8, val.ccdbfs) }, main, page);
~base.setFaderFunc(7, { | val | ~falseSelf.mixer.setVol(14, val.ccdbfs) }, main, page);
~base.setFaderFunc(8, { | val | ~falseSelf.mixer.setVol(15, val.ccdbfs) }, main, page);

~base.setTouchButtonFunc(4, { ~falseSelf.mixer.tglMute(4) }, bank: main, page: page);
~base.setTouchButtonFunc(5, { ~falseSelf.mixer.tglMute(5) }, bank: main, page: page);
~base.setTouchButtonFunc(6, { ~falseSelf.mixer.tglMute(8) }, bank: main, page: page);
~base.setTouchButtonFunc(7, { ~falseSelf.mixer.tglMute(14) }, bank: main, page: page);
~base.setTouchButtonFunc(8, { ~falseSelf.mixer.tglMute(15) }, bank: main, page: page);

~base.setGridFunc(4, 3, { ~falseSelf.mainTrumpetInput.tglMute; }, bank: main, page: page);
~base.setGridFunc(5, 3, { ~falseSelf.trumpetMelodyInput.tglMute; }, bank: main, page: page);
~base.setGridFunc(6, 3, { ~falseSelf.modular.tglMute; }, bank: main, page: page);
~base.setGridFunc(7, 3, { ~falseSelf.modNoise.tglMute; }, bank: main, page: page);

// bell:
~base.turnGridMagenta(0, 1, main, page);
~base.setGridFunc(0, 1, { ~falseSelf.bellSection.playNote(73.midicps, -3, 0);
}, bank: main, page: page);

// kicks:
5.do({ | i | ~base.turnGridGreen(i, 0, main, page); });

~base.setGridFunc(0, 0, { | vel |
  ~falseSelf.drums.playDrum0(vel.ccdbfs);
  ~base.turnGridWhite(0, 0);
}, 'noteOn', main, page);
~base.setGridFunc(0, 0, { ~base.turnGridGreen(0, 0) }, 'noteOff', main, page);

~base.setGridFunc(1, 0, { | vel |
  ~falseSelf.drums.playDrum1(vel.ccdbfs);
  ~base.turnGridWhite(1, 0);
}, 'noteOn', main, page);
~base.setGridFunc(1, 0, { ~base.turnGridGreen(1, 0) }, 'noteOff', main, page);

~base.setGridFunc(2, 0, { | vel |
  ~falseSelf.drums.playDrum2(vel.ccdbfs);
  ~base.turnGridWhite(2, 0);
}, 'noteOn', main, page);
~base.setGridFunc(2, 0, { ~base.turnGridGreen(2, 0) }, 'noteOff', main, page);

~base.setGridFunc(3, 0, { | vel |
  ~falseSelf.drums.playDrum3(vel.ccdbfs);
  ~base.turnGridWhite(3, 0);
}, 'noteOn', main, page);
~base.setGridFunc(3, 0, { ~base.turnGridGreen(3, 0) }, 'noteOff', main, page);

~base.setGridFunc(4, 0, { | vel |
  ~falseSelf.drums.playMDrum(vel.ccdbfs, 0);
  ~base.turnGridWhite(4, 0);
}, 'noteOn', main, page);
~base.setGridFunc(4, 0, { ~base.turnGridGreen(4, 0) }, 'noteOff', main, page);


~base.setGridMonitorFunc(7, 0, {
  var feedback = ~falseSelf.mainTrumpet.delay.feedback;
  if( feedback == 1, { ~base.turnGridWhite(7, 0) }, { ~base.turnGridCyan(7, 0) });
}, main, page);
~base.setGridFunc(7, 0, {
  ~falseSelf.mainTrumpet.delay.setFeedback(1)
}, 'noteOn', main, page);
~base.setGridFunc(7, 0, {
  ~falseSelf.mainTrumpet.delay.setFeedback(0.22);
}, 'noteOff', main, page);