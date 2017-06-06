/*
Sunday, April 16th 2017
False Self Ohm.scd
prm
*/

var page = 'falseSelf';

var mixerBank1 = 0;
var mixerBank2 = 1;
var mixerBank3 = 2;
var mixerBank4 = 3;

var activeGridBank = mainGridBank;

var mainGridBank = 0;
var endGridBank = 1;

var activeLeftBank = mixerBank1;

~ohm.makePage(page);

~ohm.addGridBanks(3, page: page);
~ohm.addLeftButtonsBanks(3, page: page);
~ohm.addLeftSlidersBanks(3, page: page);
~ohm.addLeftKnobsBanks(3, page: page);

~ohm.addPageLoadFunction(\mainGlobalMixer, { ~ohm.storageDict[\loadGlobalSubmixerFunc].value; }, page);
//////// Metronome:
~ohm.addPageLoadFunction(\metronome, {
  ~falseSelf.clock.schedAbs(~falseSelf.clock.beats.ceil, { | beat |
    //~falseSelf.clock.beatInBar.postln;
    ~falseSelf.clock.playNextBar({
      ~ohm.turnControlButtonWhite(1, 0, page: page);
      { ~ohm.turnControlButtonOff(1, 0, page: page); }.defer(0.1);
    });
    ~ohm.turnControlButtonGreen(2, 0, page: page);
    { ~ohm.turnControlButtonOff(2, 0, page: page); }.defer(0.1);
    //~falseSelf.bar.postln;
    1;
  },page);
},page: page);

/////// crossfader bank:

//// grid bank change:
~ohm.setCrossfaderButtonMonitorFunc(0, {
  if( activeGridBank == mainGridBank,
    { ~ohm.turnCrossfaderButtonCyan(0) }, { ~ohm.turnCrossfaderButtonWhite(0) });
}, page: page);
~ohm.setCrossfaderButtonFunc(0, {
  if( activeGridBank == mainGridBank,
    { activeGridBank = endGridBank; ~ohm.setActiveGridBank(activeGridBank); },
    { activeGridBank = mainGridBank; ~ohm.setActiveGridBank(activeGridBank) });
}, page: page);


//////// main grid:


//// Bar Numbers:
~ohm.setGridMonitorFunc(0, 0, { if( ~falseSelf.bar >= 1, { ~ohm.turnGridMagenta(0, 0); }, { ~ohm.turnGridOff(0, 0) });
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(1, 0, { if( ~falseSelf.bar >= 13, { ~ohm.turnGridMagenta(1, 0); }, { ~ohm.turnGridOff(1, 0); })
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(2, 0, { if( ~falseSelf.bar >= 21, { ~ohm.turnGridMagenta(2, 0); }, { ~ohm.turnGridOff(2, 0);})
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(3, 0, { if( ~falseSelf.bar >= 25, { ~ohm.turnGridMagenta(3, 0); }, { ~ohm.turnGridOff(3, 0); })
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(4, 0, { if( ~falseSelf.bar >= 53, { ~ohm.turnGridMagenta(4, 0); }, { ~ohm.turnGridOff(4, 0); })
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(5, 0, { if( ~falseSelf.bar >= 55, { ~ohm.turnGridMagenta(5, 0); }, { ~ohm.turnGridOff(5, 0); })
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(6, 0, { if( ~falseSelf.bar >= 68, { ~ohm.turnGridMagenta(6, 0); }, { ~ohm.turnGridOff(6, 0); })
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(7, 0, { if( ~falseSelf.bar >= 81, { ~ohm.turnGridMagenta(7, 0); }, { ~ohm.turnGridOff(7, 0); })
}, bank: mainGridBank, page: page);

//// countdowns:

// grid play funcs:
~ohm.setGridMonitorFunc(0, 2, { if( ~falseSelf.section1IsPlaying == true,
  { ~ohm.turnGridGreen(0, 2) }, { ~ohm.turnGridBlue(0, 2) });
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(1, 2, { if( ~falseSelf.chorus1IsPlaying == true,
  { ~ohm.turnGridGreen(1, 2) }, { ~ohm.turnGridOff(1, 2) });
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(2, 2, { if( ~falseSelf.chorus2IsPlaying == true,
  { ~ohm.turnGridGreen(2, 2) }, { ~ohm.turnGridOff(2, 2) });
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(3, 2,{ if( ~falseSelf.canonIsPlaying == true,
  { ~ohm.turnGridGreen(3, 2) }, { ~ohm.turnGridOff(3, 2) });
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(4, 2, { if( ~falseSelf.limboIsPlaying == true,
  { ~ohm.turnGridGreen(4, 2) }, { ~ohm.turnGridOff(4, 2) });
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(5, 2, { if (~falseSelf.melodyIsPlaying == true,
  { ~ohm.turnGridGreen(5, 2) }, { ~ohm.turnGridOff(5, 2) });
}, bank: mainGridBank, page: page);
~ohm.setGridMonitorFunc(6, 2, { if( ~falseSelf.endIsPlaying == true,
  { ~ohm.turnGridGreen(6, 2) }, { ~ohm.turnGridOff(6, 2) });
}, bank: mainGridBank, page: page);

// start song:
~ohm.setGridFunc(0, 2, { ~falseSelf.startSong; }, bank: mainGridBank, page: page);

// Fake Guitar:
~ohm.setGridMonitorFunc(0, 3, {
  if( ~falseSelf.fakeGuitar.isLoaded, { ~ohm.turnGridGreen(0, 3) }, { ~ohm.turnGridRed(0, 3) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(0, 3, {
  if( ~falseSelf.fakeGuitar.isLoaded, { ~falseSelf.fakeGuitar.free; });
}, bank: mainGridBank, page: page);

// Main Trumpet:
~ohm.setGridMonitorFunc(0, 4, {
  if( ~falseSelf.mainTrumpet.isLoaded, { ~ohm.turnGridGreen(0, 4) }, { ~ohm.turnGridRed(0, 4) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(0, 4, {
  if( ~falseSelf.mainTrumpet.isLoaded, { ~falseSelf.mainTrumpet.free; });
}, bank: mainGridBank, page: page);

// Bells:
~ohm.setGridMonitorFunc(0, 5, {
  if( ~falseSelf.bellSection.isLoaded, { ~ohm.turnGridGreen(0, 5) }, { ~ohm.turnGridRed(0, 5) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(0, 5, {
  if( ~falseSelf.bellSection.isLoaded, { ~falseSelf.bellSection.free; });
}, bank: mainGridBank, page: page);

// Melody Synth:
~ohm.setGridMonitorFunc(0, 6, {
  if( ~falseSelf.melodySynth.isLoaded, { ~ohm.turnGridGreen(0, 6) }, { ~ohm.turnGridRed(0, 6) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(0, 6, {
  if( ~falseSelf.melodySynth.isLoaded, { ~falseSelf.melodySynth.free; });
}, bank: mainGridBank, page: page);

//// Drums:
// loaded:
~ohm.setGridMonitorFunc(4, 3, {
  if( ~falseSelf.drums.isLoaded, { ~ohm.turnGridWhite(4, 3) }, { ~ohm.turnGridRed(4, 3) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(4, 3, {
  if( ~falseSelf.drums.isLoaded, { ~falseSelf.drums.free; });
}, bank: mainGridBank, page: page);
// beginning:
~ohm.setGridMonitorFunc(5, 3, {
  if( ~falseSelf.drums.drums.isSequencePlaying(\section1),
    { ~ohm.turnGridGreen(5, 3) }, { ~ohm.turnGridRed(5, 3) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(5, 3, {
  if( ~falseSelf.drums.drums.isSequencePlaying(\section1),
    { ~falseSelf.drums.stopSection1 }, { ~falseSelf.drums.playSection1(~falseSelf.clock) });
}, bank: mainGridBank, page: page);
// chorus 1:
~ohm.setGridMonitorFunc(6, 3, {
  if( ~falseSelf.drums.drums.isSequencePlaying(\chorus1),
    { ~ohm.turnGridGreen(6, 3) }, { ~ohm.turnGridRed(6, 3) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(6, 3, {
  if( ~falseSelf.drums.drums.isSequencePlaying(\chorus1),
    { ~falseSelf.drums.stopChorus1 }, { ~falseSelf.drums.playChorus1(~falseSelf.clock); });
}, bank: mainGridBank, page: page);
// chorus 2:
~ohm.setGridMonitorFunc(7, 3, {
  if( ~falseSelf.drums.drums.isSequencePlaying(\chorus2),
    { ~ohm.turnGridGreen(7, 3) }, { ~ohm.turnGridRed(7, 3) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(7, 3, {
  if( ~falseSelf.drums.drums.isSequencePlaying(\chorus2),
    { ~falseSelf.drums.stopChorus2 }, { ~falseSelf.drums.playChorus2(~falseSelf.clock) });
}, bank: mainGridBank, page: page);
// canon:
~ohm.setGridMonitorFunc(4, 4, {
  if( ~falseSelf.drums.drums.isSequencePlaying(\canon),
    { ~ohm.turnGridGreen(4, 4) }, { ~ohm.turnGridRed(4, 4) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(4, 4, {
  if( ~falseSelf.drums.drums.isSequencePlaying(\canon),
    { ~falseSelf.drums.stopCanon }, { ~falseSelf.drums.playCanon(~falseSelf.clock) });
}, bank: mainGridBank, page: page);
// end:
~ohm.setGridMonitorFunc(5, 4, {
  if( ~falseSelf.drums.drums.isSequencePlaying(\ending),
    { ~ohm.turnGridGreen(5, 4) }, { ~ohm.turnGridRed(5, 4) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(5, 4, {
  if( ~falseSelf.drums.drums.isSequencePlaying(\ending),
    { ~falseSelf.drums.stopEnding }, { ~falseSelf.drums.playEnding(~falseSelf.clock) });
}, bank: mainGridBank, page: page);

//// Basses:
// active:
~ohm.setGridMonitorFunc(4, 6, {
  if( ~falseSelf.bassSection.isLoaded, { ~ohm.turnGridCyan(4, 6) }, { ~ohm.turnGridRed(4, 6) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(4, 6, {
  if( ~falseSelf.bassSection.isLoaded, { ~falseSelf.bassSection.free; });
}, bank: mainGridBank, page: page);
///patterns:
// pre-chorus:
~ohm.setGridMonitorFunc(5, 6, {
  if( ~falseSelf.bassSection.preChorusIsPlaying,
    { ~ohm.turnGridGreen(5, 6) }, { ~ohm.turnGridRed(5, 6) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(5, 6, { if( ~falseSelf.bassSection.preChorusIsPlaying,
  { ~falseSelf.bassSection.stopPreChorus }, { ~falseSelf.bassSection.playPreChorus });
}, bank: mainGridBank, page: page);
// chorus:
~ohm.setGridMonitorFunc(6, 6, {
  if( ~falseSelf.bassSection.chorusIsPlaying,
    { ~ohm.turnGridGreen(6, 6,) }, { ~ohm.turnGridRed(6, 6) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(6, 6, {
  if( ~falseSelf.bassSection.chorusIsPlaying,
    { ~falseSelf.bassSection.stopChorus }, { ~falseSelf.bassSection.playChorus });
}, bank: mainGridBank, page: page);
// post-Chorus:
~ohm.setGridMonitorFunc(7, 6, {
  if( ~falseSelf.bassSection.postChorusIsPlaying,
    { ~ohm.turnGridGreen(7, 6) }, { ~ohm.turnGridRed(7, 6) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(7, 6, {
  if( ~falseSelf.bassSection.postChorusIsPlaying,
    { ~falseSelf.bassSection.stopPostChorus }, { ~falseSelf.bassSection.playPostChorus });
}, bank: mainGridBank, page: page);
// chorale:
~ohm.setGridMonitorFunc(5, 7, {
  if( ~falseSelf.bassSection.endIsPlaying,
    { ~ohm.turnGridGreen(5, 7) }, { ~ohm.turnGridRed(5, 7) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(5, 7, {
  if( ~falseSelf.bassSection.endIsPlaying,
    { ~falseSelf.bassSection.stopEnd }, { ~falseSelf.bassSection.playEnd });
}, bank: mainGridBank, page: page);
// end:
~ohm.setGridMonitorFunc(6, 7, {
  if( ~falseSelf.bassSection.codaIsPlaying,
    { ~ohm.turnGridGreen(6, 7) }, { ~ohm.turnGridRed(6, 7) });
}, bank: mainGridBank, page: page);
~ohm.setGridFunc(6, 7, {
  if( ~falseSelf.bassSection.codaIsPlaying,
    { ~falseSelf.bassSection.stopCoda }, { ~falseSelf.bassSection.playCoda });
}, bank: mainGridBank, page: page);

//////// End Grid Bank:

//// tempo:
// monitor:
~ohm.setGridMonitorFunc(0, 0, {
  if( ~falseSelf.clock.tempo == 2.37, { ~ohm.turnGridGreen(0, 0); }, { ~ohm.turnGridBlue(0, 0); });
}, bank: endGridBank, page: page);
// change:
~ohm.setGridFunc(0, 0, { ~falseSelf.clock.tempo = 142.2/60; }, bank: endGridBank, page: page);

//// End Sequence:
// monitor:
~ohm.setGridMonitorFunc(1, 0, {
  if( ~falseSelf.melodyIsPlaying == true,
    { ~ohm.turnGridGreen(1, 0); }, { ~ohm.turnGridMagenta(1, 0) });
}, bank: endGridBank, page: page);
// start sequence:
~ohm.setGridFunc(1, 0, { ~falseSelf.startMelody; }, bank: endGridBank, page: page);


//// Trumpet Melody:
// active:
~ohm.setGridMonitorFunc(0, 2, {
  if( ~falseSelf.trumpetMelody.isLoaded,
    { ~ohm.turnGridCyan(0, 2) }, { ~ohm.turnGridRed(0, 2) });
}, bank: endGridBank, page: page);
~ohm.setGridFunc(0, 2, {
  if( ~falseSelf.trumpetMelody.isLoaded, { ~falseSelf.trumpetMelody.free; });
}, bank: endGridBank, page: page);
// shift 1:
~ohm.setGridMonitorFunc(1, 2, {
  if( ~falseSelf.trumpetMelody.shift1.mixer.isMuted,
    { ~ohm.turnGridRed(1, 2) }, { ~ohm.turnGridGreen(1, 2) });
}, bank: endGridBank, page: page);
~ohm.setGridFunc(1, 2, { ~falseSelf.trumpetMelody.shift1.mixer.tglMute;
}, bank: endGridBank, page: page);
// shift 2:
~ohm.setGridMonitorFunc(2, 2, {
  if(~falseSelf.trumpetMelody.shift2.mixer.isMuted,
    { ~ohm.turnGridRed(2, 2) }, { ~ohm.turnGridGreen(2, 2) });
}, bank: endGridBank, page: page);
~ohm.setGridFunc(2, 2, { ~falseSelf.trumpetMelody.shift2.mixer.tglMute;
}, bank: endGridBank, page: page);
// dry:
~ohm.setGridMonitorFunc(3, 2, {
  if( ~falseSelf.trumpetMelody.dry.isMuted,
    { ~ohm.turnGridRed(3, 2) }, { ~ohm.turnGridGreen(3, 2) })
}, bank: endGridBank, page: page);
~ohm.setGridFunc(3, 2, { ~falseSelf.trumpetMelody.dry.tglMute;
}, bank: endGridBank, page: page);


//// Plane Noise:
// active:
~ohm.setGridMonitorFunc(0, 3, { if( ~falseSelf.planeNoise.isLoaded,
  { ~ohm.turnGridGreen(0, 3) }, { ~ohm.turnGridYellow(0, 3) });
}, bank: endGridBank, page: page);
~ohm.setGridFunc(0, 3, { if( ~falseSelf.planeNoise.isLoaded,
  { ~falseSelf.planeNoise.free; });
}, bank: endGridBank, page: page);
// playing:
~ohm.setGridMonitorFunc(1, 3, {
  if( ~falseSelf.planeNoise.isPlaying,
    { ~ohm.turnGridGreen(1, 3) }, { ~ohm.turnGridRed(1, 3) });
}, bank: endGridBank, page: page);
~ohm.setGridFunc(1, 3, {
  if( ~falseSelf.planeNoise.isPlaying,
    { ~falseSelf.planeNoise.releaseSample }, { ~falseSelf.planeNoise.playSample; });
}, bank: endGridBank, page: page);

//// Mid Buzz:
// active:
~ohm.setGridMonitorFunc(0, 4, { if( ~falseSelf.midBuzz.isLoaded,
  { ~ohm.turnGridGreen(0, 4) }, { ~ohm.turnGridWhite(0, 4) });
}, bank: endGridBank, page: page);
~ohm.setGridFunc(1, 4, { if( ~falseSelf.midBuzz.isLoaded,
  { ~falseSelf.midBuzz.free; });
}, bank: endGridBank, page: page);
// playing:
~ohm.setGridMonitorFunc(1, 4, {
  if( ~falseSelf.midBuzz.sampler.isSequencePlaying(\midBuzz),
    { ~ohm.turnGridGreen(1, 4) }, { ~ohm.turnGridRed(1, 4) });
}, bank: endGridBank, page: page);
~ohm.setGridFunc(1, 4, {
  if( ~falseSelf.midBuzz.sampler.isSequencePlaying(\midBuzz),
    { ~falseSelf.midBuzz.stopSequence }, { ~falseSelf.midBuzz.playSequence(~falseSelf.clock); });
}, bank: endGridBank, page: page);

//// Crude Drones:
// active:
~ohm.setGridMonitorFunc(0, 5, { if( ~falseSelf.drones.isLoaded,
  { ~ohm.turnGridGreen(0, 5) }, { ~ohm.turnGridCyan(0, 5) });
}, bank: endGridBank, page: page);
~ohm.setGridFunc(0, 5, { if( ~falseSelf.drones.isLoaded,
  { ~falseSelf.drones.free });
}, bank: endGridBank, page: page);
// voice 1:
~ohm.setGridMonitorFunc(1, 5, { if( ~falseSelf.drones.sampler.isSequencePlaying(\voice1),
  { ~ohm.turnGridGreen(1, 5) }, { ~ohm.turnGridRed(1, 5) })
}, bank: endGridBank, page: page);
~ohm.setGridFunc(1, 5, { if( ~falseSelf.drones.sampler.isSequencePlaying(\voice1),
  { ~falseSelf.drones.stopVoice1Sequence }, { ~falseSelf.drones.playVoice1Sequence(~falseSelf.clock); });
}, bank: endGridBank, page: page);
// voice 2:
~ohm.setGridMonitorFunc(2, 5, { if( ~falseSelf.drones.sampler.isSequencePlaying(\voice2),
  { ~ohm.turnGridGreen(2, 5) }, { ~ohm.turnGridRed(2, 5) })
}, bank: endGridBank, page: page);
~ohm.setGridFunc(2, 5, { if( ~falseSelf.drones.sampler.isSequencePlaying(\voice2),
  { ~falseSelf.drones.stopVoice2Sequence }, { ~falseSelf.drones.playVoice2Sequence(~falseSelf.clock) });
}, bank: endGridBank, page: page);
// voice 3:
~ohm.setGridMonitorFunc(3, 5, { if( ~falseSelf.drones.sampler.isSequencePlaying(\voice3),
  { ~ohm.turnGridGreen(3, 5) }, { ~ohm.turnGridRed(3, 5) })
}, bank: endGridBank, page: page);
~ohm.setGridFunc(3, 5, { if( ~falseSelf.drones.sampler.isSequencePlaying(\voice3),
  { ~falseSelf.drones.stopVoice3Sequence }, { ~falseSelf.drones.playVoice3Sequence(~falseSelf.clock) });
}, bank: endGridBank, page: page);

// 16th Drones:
// active:
~ohm.setGridMonitorFunc(0, 6, { if( ~falseSelf.sixteenthDrones.isLoaded,
  { ~ohm.turnGridGreen(0, 6) }, { ~ohm.turnGridCyan(0, 6) });
}, bank: endGridBank, page: page);
~ohm.setGridFunc(0, 6, { if( ~falseSelf.sixteenthDrones.isLoaded,
  { ~falseSelf.sixteenthDrones.free });
}, bank: endGridBank, page: page);
// voice 1:
~ohm.setGridMonitorFunc(1, 6, { if( ~falseSelf.sixteenthDrones.sampler.isSequencePlaying(\voice1),
  { ~ohm.turnGridGreen(1, 6) }, { ~ohm.turnGridRed(1, 6) })
}, bank: endGridBank, page: page);
~ohm.setGridFunc(1, 6, { if( ~falseSelf.sixteenthDrones.sampler.isSequencePlaying(\voice1),
  { ~falseSelf.sixteenthDrones.stopVoice1Sequence }, { ~falseSelf.sixteenthDrones.playVoice1Sequence(~falseSelf.clock); });
}, bank: endGridBank, page: page);
// voice 2:
~ohm.setGridMonitorFunc(2, 6, { if( ~falseSelf.sixteenthDrones.sampler.isSequencePlaying(\voice2),
  { ~ohm.turnGridGreen(2, 6) }, { ~ohm.turnGridRed(2, 6) })
}, bank: endGridBank, page: page);
~ohm.setGridFunc(2, 6, { if( ~falseSelf.sixteenthDrones.sampler.isSequencePlaying(\voice2),
  { ~falseSelf.sixteenthDrones.stopVoice2Sequence }, { ~falseSelf.sixteenthDrones.playVoice2Sequence(~falseSelf.clock) });
}, bank: endGridBank, page: page);
// voice 3:
~ohm.setGridMonitorFunc(3, 6, { if( ~falseSelf.sixteenthDrones.sampler.isSequencePlaying(\voice3),
  { ~ohm.turnGridGreen(3, 6) }, { ~ohm.turnGridRed(3, 6) })
}, bank: endGridBank, page: page);
~ohm.setGridFunc(3, 6, { if( ~falseSelf.sixteenthDrones.sampler.isSequencePlaying(\voice3),
  { ~falseSelf.sixteenthDrones.stopVoice3Sequence }, { ~falseSelf.sixteenthDrones.playVoice3Sequence(~falseSelf.clock) });
}, bank: endGridBank, page: page);


//// Orchestra:
//active:
~ohm.setGridMonitorFunc(4, 3, { if( ~falseSelf.orchestra.isLoaded,
  { ~ohm.turnGridGreen(4, 3) }, { ~ohm.turnGridMagenta(4, 3) });
}, bank: endGridBank, page: page);
~ohm.setGridFunc(4, 3, { if( ~falseSelf.orchestra.isLoaded,
  { ~falseSelf.orchestra.free; })
}, bank: endGridBank, page: page);
// play:
~ohm.turnGridBlue(5, 3, bank: endGridBank, page: page);
~ohm.setGridFunc(5, 3, { ~falseSelf.orchestra.playMahlerPhrase }, bank: endGridBank, page: page);

//// Guitars:
// active:
~ohm.setGridMonitorFunc(4, 4, { if( ~falseSelf.freezeGuitar.isLoaded,
  { ~ohm.turnGridGreen(4, 4) }, { ~ohm.turnGridCyan(4, 4) });
}, bank: endGridBank, page: page);
~ohm.setGridFunc(4, 4, { if( ~falseSelf.freezeGuitar.isLoaded,
  { ~falseSelf.freezeGuitar.free; });
}, bank: endGridBank, page: page);
// chord progression:
~ohm.setGridMonitorFunc(5, 4, { if( ~falseSelf.freezeGuitar.sampler.isSequencePlaying(\chordProgression),
  { ~ohm.turnGridGreen(5, 4); }, { ~ohm.turnGridRed(5, 4) });
}, bank: endGridBank, page: page);
~ohm.setGridFunc(5, 4, { if( ~falseSelf.freezeGuitar.sampler.isSequencePlaying(\chordProgression),
  { ~falseSelf.freezeGuitar.stopChordProgression }, { ~falseSelf.freezeGuitar.playChordProgression(~falseSelf.clock) });
}, bank: endGridBank, page: page);
// end progression:
~ohm.setGridMonitorFunc(6, 4, { if( ~falseSelf.freezeGuitar.sampler.isSequencePlaying(\voice1),
  { ~ohm.turnGridGreen(6, 4) }, { ~ohm.turnGridRed(6, 4) });
}, bank: endGridBank, page: page);
~ohm.setGridFunc(6, 4, { if( ~falseSelf.freezeGuitar.sampler.isSequencePlaying(\voice1),
  { ~falseSelf.freezeGuitar.stopEndProgression }, { ~falseSelf.freezeGuitar.playEndProgression(~falseSelf.clock) })
}, bank: endGridBank, page: page);

//// end trumpet:
// active:
~ohm.setGridMonitorFunc(4, 5, { if( ~falseSelf.endTrumpet.isLoaded,
  { ~ohm.turnGridMagenta(4, 5) }, { ~ohm.turnGridRed(4, 5) });
}, bank: endGridBank, page: page);
~ohm.setGridFunc(4, 5, { if( ~falseSelf.endTrumpet.isLoaded,
  { ~falseSelf.endTrumpet.free; });
}, bank: endGridBank, page: page);
// input is muted:
~ohm.setGridMonitorFunc(5, 5, { if( ~falseSelf.endTrumpet.input.isMuted,
  { ~ohm.turnGridRed(5, 5) }, { ~ohm.turnGridGreen(5, 5) });
}, bank: endGridBank, page: page);
~ohm.setGridFunc(5, 5, { ~falseSelf.endTrumpet.input.tglMute; }, bank: endGridBank, page: page);


//// Bank Change:
~ohm.setGridMonitorFunc(0, 7, {
  4.do({ | i | ~ohm.turnGridCyan(i, 7) });
  ~ohm.turnGridGreen(activeLeftBank, 7);
}, bank: mainGridBank, page: page);

// mix bank 1:
~ohm.setGridFunc(0, 7, {
  activeLeftBank = mixerBank1;
  ~ohm.setActiveLeftButtonsBank(activeLeftBank);
  ~ohm.setActiveLeftSlidersBank(activeLeftBank);
  ~ohm.setActiveLeftKnobsBank(activeLeftBank);
}, bank: mainGridBank, page: page);

// mix bank 2:
~ohm.setGridFunc(1, 7, {
  activeLeftBank = mixerBank2;
  ~ohm.setActiveLeftButtonsBank(activeLeftBank);
  ~ohm.setActiveLeftSlidersBank(activeLeftBank);
  ~ohm.setActiveLeftKnobsBank(activeLeftBank);
}, bank: mainGridBank, page: page);

// mix bank 3:
~ohm.setGridFunc(2, 7, {
  activeLeftBank = mixerBank3;
  ~ohm.setActiveLeftButtonsBank(activeLeftBank);
  ~ohm.setActiveLeftSlidersBank(activeLeftBank);
  ~ohm.setActiveLeftKnobsBank(activeLeftBank);
}, bank: mainGridBank, page: page);

// mix bank 4:
~ohm.setGridFunc(3, 7, {
  activeLeftBank = mixerBank4;
  ~ohm.setActiveLeftButtonsBank(activeLeftBank);
  ~ohm.setActiveLeftSlidersBank(activeLeftBank);
  ~ohm.setActiveLeftKnobsBank(activeLeftBank);
}, bank: mainGridBank, page: page);

//// Bank Change (End Grid Bank):
~ohm.setGridMonitorFunc(0, 7, {
  4.do({ | i | ~ohm.turnGridCyan(i, 7) });
  ~ohm.turnGridGreen(activeLeftBank, 7);
}, bank: endGridBank, page: page);

// mix bank 1:
~ohm.setGridFunc(0, 7, {
  activeLeftBank = mixerBank1;
  ~ohm.setActiveLeftButtonsBank(activeLeftBank);
  ~ohm.setActiveLeftSlidersBank(activeLeftBank);
  ~ohm.setActiveLeftKnobsBank(activeLeftBank);
}, bank: endGridBank, page: page);

// mix bank 2:
~ohm.setGridFunc(1, 7, {
  activeLeftBank = mixerBank2;
  ~ohm.setActiveLeftButtonsBank(activeLeftBank);
  ~ohm.setActiveLeftSlidersBank(activeLeftBank);
  ~ohm.setActiveLeftKnobsBank(activeLeftBank);
}, bank: endGridBank, page: page);

// mix bank 3:
~ohm.setGridFunc(2, 7, {
  activeLeftBank = mixerBank3;
  ~ohm.setActiveLeftButtonsBank(activeLeftBank);
  ~ohm.setActiveLeftSlidersBank(activeLeftBank);
  ~ohm.setActiveLeftKnobsBank(activeLeftBank);
}, bank: endGridBank, page: page);

// mix bank 4:
~ohm.setGridFunc(3, 7, {
  activeLeftBank = mixerBank4;
  ~ohm.setActiveLeftButtonsBank(activeLeftBank);
  ~ohm.setActiveLeftSlidersBank(activeLeftBank);
  ~ohm.setActiveLeftKnobsBank(activeLeftBank);
}, bank: endGridBank, page: page);


//////////////////////
//// Left Side: /////
////////////////////

/////// Main Bank:

//// Fake Guitar:
// mute:
~ohm.setLeftButtonMonitorFunc(0, { if( ~falseSelf.mixerA.isMuted(0),
  { ~ohm.turnLeftButtonRed(0); }, { ~ohm.turnLeftButtonGreen(0) });
}, bank: mixerBank1, page: page);
~ohm.setLeftButtonFunc(0, { ~falseSelf.mixerA.tglMute(0); }, bank: mixerBank1, page: page);
// volume:
~ohm.setLeftSliderFunc(0, { | val |
  ~falseSelf.mixerA.setVol(0, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);
// sends:
~ohm.setLeftKnobFunc(0, 2, { | val |
  ~falseSelf.mixerA.setSendVol(0, 0, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~falseSelf.mixerA.setSendVol(0, 1, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);
~ohm.setLeftKnobFunc(0, 0, { | val |
  ~falseSelf.mixerA.setSendVol(0, 2, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);

//// Drums:
//mute:
~ohm.setLeftButtonMonitorFunc(1, { if (~falseSelf.mixerB.isMuted(2),
  { ~ohm.turnLeftButtonRed(1); }, { ~ohm.turnLeftButtonGreen(1) });
}, bank: mixerBank1, page: page);
~ohm.setLeftButtonFunc(1, { ~falseSelf.mixerB.tglMute(2); }, bank: mixerBank1, page: page);
// volume:
~ohm.setLeftSliderFunc(1, { | val |
  ~falseSelf.mixerB.setVol(2, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);
// sends:
~ohm.setLeftKnobFunc(1, 2, { | val |
  ~falseSelf.mixerB.setSendVol(2, 0, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);
~ohm.setLeftKnobFunc(1, 1, { | val |
  ~falseSelf.mixerB.setSendVol(2, 1, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);
~ohm.setLeftKnobFunc(1, 0, { | val |
  ~falseSelf.mixerB.setSendVol(2, 2, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);

////Bass:
//mute:
~ohm.setLeftButtonMonitorFunc(2, { if (~falseSelf.mixerB.isMuted(1),
  { ~ohm.turnLeftButtonRed(2); }, { ~ohm.turnLeftButtonGreen(2) });
}, bank: mixerBank1, page: page);
~ohm.setLeftButtonFunc(2, { ~falseSelf.mixerB.tglMute(1); }, bank: mixerBank1, page: page);
// volume:
~ohm.setLeftSliderFunc(2, { | val |
  ~falseSelf.mixerB.setVol(1, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);
// sends:
~ohm.setLeftKnobFunc(2, 2, { | val |
  ~falseSelf.mixerB.setSendVol(1, 0, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);
~ohm.setLeftKnobFunc(2, 1, { | val |
  ~falseSelf.mixerB.setSendVol(1, 1, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);
~ohm.setLeftKnobFunc(2, 0, { | val |
  ~falseSelf.mixerB.setSendVol(1, 2, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);


//// Trumpet:
//mute:
~ohm.setLeftButtonMonitorFunc(3, { if (~falseSelf.mixerA.isMuted(2),
  { ~ohm.turnLeftButtonRed(3); }, { ~ohm.turnLeftButtonGreen(3) });
}, bank: mixerBank1, page: page);
~ohm.setLeftButtonFunc(3, { ~falseSelf.mixerA.tglMute(2); }, bank: mixerBank1, page: page);
// volume:
~ohm.setLeftSliderFunc(3, { | val |
  ~falseSelf.mixerA.setVol(2, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);
// sends:
~ohm.setLeftKnobFunc(3, 2, { | val |
  ~falseSelf.mixerA.setSendVol(2, 0, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);
~ohm.setLeftKnobFunc(3, 1, { | val |
  ~falseSelf.mixerA.setSendVol(2, 1, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);
~ohm.setLeftKnobFunc(3, 0, { | val |
  ~falseSelf.mixerA.setSendVol(2, 2, val.ccdbfs(-70, 0));
}, bank: mixerBank1, page: page);

//// Bells:
// mute:
~ohm.setLeftButtonMonitorFunc(0, { if( ~falseSelf.mixerA.isMuted(1),
  { ~ohm.turnLeftButtonRed(0) }, { ~ohm.turnLeftButtonGreen(0) });
}, bank: mixerBank2, page: page);
~ohm.setLeftButtonFunc(0, { ~falseSelf.mixerA.tglMute(1); }, bank: mixerBank2, page: page);
// volume:
~ohm.setLeftSliderFunc(0, { | val |
  ~falseSelf.mixerA.setVol(1, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);
// sends:
~ohm.setLeftKnobFunc(0, 2, { | val |
  ~falseSelf.mixerA.setSendVol(1, 0, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~falseSelf.mixerA.setSendVol(1, 1, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);
~ohm.setLeftKnobFunc(0, 0, { | val |
  ~falseSelf.mixerA.setSendVol(1, 2, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);

//// Melody Synth:
// mute:
~ohm.setLeftButtonMonitorFunc(1, { if( ~falseSelf.mixerB.isMuted(0),
  { ~ohm.turnLeftButtonRed(1) }, { ~ohm.turnLeftButtonGreen(1) });
}, bank: mixerBank2, page: page);
~ohm.setLeftButtonFunc(1, { ~falseSelf.mixerB.tglMute(0); }, bank: mixerBank2, page: page);
// volume:
~ohm.setLeftSliderFunc(1, { | val |
  ~falseSelf.mixerB.setVol(0, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);
// sends:
~ohm.setLeftKnobFunc(1, 2, { | val |
  ~falseSelf.mixerB.setSendVol(0, 0, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);
~ohm.setLeftKnobFunc(1, 1, { | val |
  ~falseSelf.mixerB.setSendVol(0, 1, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);
~ohm.setLeftKnobFunc(1, 0, { | val |
  ~falseSelf.mixerB.setSendVol(0, 2, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);


//// Modular:
// mute:
~ohm.setLeftButtonMonitorFunc(2, { if( ~falseSelf.mixerC.isMuted(0),
  { ~ohm.turnLeftButtonRed(2) }, { ~ohm.turnLeftButtonGreen(2) });
}, bank: mixerBank2, page: page);
~ohm.setLeftButtonFunc(2, { ~falseSelf.mixerC.tglMute(0); }, bank: mixerBank2, page: page);
// volume:
~ohm.setLeftSliderFunc(2, { | val |
  ~falseSelf.mixerC.setVol(0, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);
// sends:
~ohm.setLeftKnobFunc(2, 2, { | val |
  ~falseSelf.mixerC.setSendVol(0, 0, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);
~ohm.setLeftKnobFunc(2, 1, { | val |
  ~falseSelf.mixerC.setSendVol(0, 1, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);
~ohm.setLeftKnobFunc(2, 0, { | val |
  ~falseSelf.mixerC.setSendVol(0, 2, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);

//// trumpet canon:
// mute:
~ohm.setLeftButtonMonitorFunc(3, { if( ~falseSelf.mixerA.isMuted(3),
  { ~ohm.turnLeftButtonRed(3) }, { ~ohm.turnLeftButtonGreen(3) });
}, bank: mixerBank2, page: page);
~ohm.setLeftButtonFunc(3, { ~falseSelf.mixerA.tglMute(3); }, bank: mixerBank2, page: page);
// volume:
~ohm.setLeftSliderFunc(3, { | val |
  ~falseSelf.mixerA.setVol(3, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);
// sends:
~ohm.setLeftKnobFunc(3, 2, { | val |
  ~falseSelf.mixerA.setSendVol(3, 0, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);
~ohm.setLeftKnobFunc(3, 1, { | val |
  ~falseSelf.mixerA.setSendVol(3, 1, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);
~ohm.setLeftKnobFunc(3, 0, { | val |
  ~falseSelf.mixerA.setSendVol(3, 2, val.ccdbfs(-70, 0));
}, bank: mixerBank2, page: page);

//// Plane Noise:
// mute:
~ohm.setLeftButtonMonitorFunc(0, { if( ~falseSelf.mixerC.isMuted(3),
  { ~ohm.turnLeftButtonRed(0) }, { ~ohm.turnLeftButtonGreen(0) });
}, bank: mixerBank3, page: page);
~ohm.setLeftButtonFunc(0, { ~falseSelf.mixerC.tglMute(3); }, bank: mixerBank3, page: page);
//volume:
~ohm.setLeftSliderFunc(0, { | val |
  ~falseSelf.mixerC.setVol(3, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);
//sends:
~ohm.setLeftKnobFunc(0, 2, { | val |
  ~falseSelf.mixerC.setSendVol(3, 0, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~falseSelf.mixerC.setSendVol(3, 1, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);
~ohm.setLeftKnobFunc(0, 0, { | val |
  ~falseSelf.mixerC.setSendVol(3, 2, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);

//// Mid Buzz:
// mute:
~ohm.setLeftButtonMonitorFunc(1, { if( ~falseSelf.mixerC.isMuted(4),
  { ~ohm.turnLeftButtonRed(1) }, { ~ohm.turnLeftButtonGreen(1) });
}, bank: mixerBank3, page: page);
~ohm.setLeftButtonFunc(1, { ~falseSelf.mixerC.tglMute(4); }, bank: mixerBank3, page: page);
//volume:
~ohm.setLeftSliderFunc(1, { | val |
  ~falseSelf.mixerC.setVol(4, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);
//sends:
~ohm.setLeftKnobFunc(1, 2, { | val |
  ~falseSelf.mixerC.setSendVol(4, 0, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);
~ohm.setLeftKnobFunc(1, 1, { | val |
  ~falseSelf.mixerC.setSendVol(4, 1, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);
~ohm.setLeftKnobFunc(1, 0, { | val |
  ~falseSelf.mixerC.setSendVol(4, 2, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);

//// drones:
// mute:
~ohm.setLeftButtonMonitorFunc(2, { if( ~falseSelf.mixerC.isMuted(1),
  { ~ohm.turnLeftButtonRed(2) }, { ~ohm.turnLeftButtonGreen(2) });
}, bank: mixerBank3, page: page);
~ohm.setLeftButtonFunc(2, { ~falseSelf.mixerC.tglMute(1); }, bank: mixerBank3, page: page);
//volume:
~ohm.setLeftSliderFunc(2, { | val |
  ~falseSelf.mixerC.setVol(1, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);
//sends:
~ohm.setLeftKnobFunc(2, 2, { | val |
  ~falseSelf.mixerC.setSendVol(1, 0, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);
~ohm.setLeftKnobFunc(2, 1, { | val |
  ~falseSelf.mixerC.setSendVol(1, 1, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);
~ohm.setLeftKnobFunc(2, 0, { | val |
  ~falseSelf.mixerC.setSendVol(1, 2, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);

//// orchestra:
// mute:
~ohm.setLeftButtonMonitorFunc(3, { if( ~falseSelf.mixerB.isMuted(3),
  { ~ohm.turnLeftButtonRed(3) }, { ~ohm.turnLeftButtonGreen(3) });
}, bank: mixerBank3, page: page);
~ohm.setLeftButtonFunc(3, { ~falseSelf.mixerB.tglMute(3); }, bank: mixerBank3, page: page);
//volume:
~ohm.setLeftSliderFunc(3, { | val |
  ~falseSelf.mixerB.setVol(3, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);
//sends:
~ohm.setLeftKnobFunc(3, 2, { | val |
  ~falseSelf.mixerB.setSendVol(3, 0, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);
~ohm.setLeftKnobFunc(3, 1, { | val |
  ~falseSelf.mixerB.setSendVol(3, 1, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);
~ohm.setLeftKnobFunc(3, 0, { | val |
  ~falseSelf.mixerB.setSendVol(3, 2, val.ccdbfs(-70, 0));
}, bank: mixerBank3, page: page);

//// 16th drones:
// mute:
~ohm.setLeftButtonMonitorFunc(0, { if( ~falseSelf.mixerC.isMuted(2),
  { ~ohm.turnLeftButtonRed(0) }, { ~ohm.turnLeftButtonGreen(0) });
}, bank: mixerBank4, page: page);
~ohm.setLeftButtonFunc(0, { ~falseSelf.mixerC.tglMute(2); }, bank: mixerBank4, page: page);
//volume:
~ohm.setLeftSliderFunc(0, { | val |
  ~falseSelf.mixerC.setVol(2, val.ccdbfs(-70, 0));
}, bank: mixerBank4, page: page);
//sends:
~ohm.setLeftKnobFunc(0, 2, { | val |
  ~falseSelf.mixerC.setSendVol(2, 0, val.ccdbfs(-70, 0));
}, bank: mixerBank4, page: page);
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~falseSelf.mixerC.setSendVol(2, 1, val.ccdbfs(-70, 0));
}, bank: mixerBank4, page: page);
~ohm.setLeftKnobFunc(0, 0, { | val |
  ~falseSelf.mixerC.setSendVol(2, 2, val.ccdbfs(-70, 0));
}, bank: mixerBank4, page: page);

//// trumpet melody:
// mute:
~ohm.setLeftButtonMonitorFunc(1, { if( ~falseSelf.mixerA.isMuted(4),
  { ~ohm.turnLeftButtonRed(1) }, { ~ohm.turnLeftButtonGreen(1) });
}, bank: mixerBank4, page: page);
~ohm.setLeftButtonFunc(1, { ~falseSelf.mixerA.tglMute(4); }, bank: mixerBank4, page: page);
//volume:
~ohm.setLeftSliderFunc(1, { | val |
  ~falseSelf.mixerA.setVol(4, val.ccdbfs(-70, 0));
}, bank: mixerBank4, page: page);
//sends:
~ohm.setLeftKnobFunc(1, 2, { | val |
  ~falseSelf.mixerA.setSendVol(4, 0, val.ccdbfs(-70, 0));
}, bank: mixerBank4, page: page);
~ohm.setLeftKnobFunc(1, 1, { | val |
  ~falseSelf.mixerA.setSendVol(4, 1, val.ccdbfs(-70, 0));
}, bank: mixerBank4, page: page);
~ohm.setLeftKnobFunc(1, 0, { | val |
  ~falseSelf.mixerA.setSendVol(4, 2, val.ccdbfs(-70, 0));
}, bank: mixerBank4, page: page);

//// freeze guitars:
// mute:
~ohm.setLeftButtonMonitorFunc(2, { if( ~falseSelf.mixerB.isMuted(4),
  { ~ohm.turnLeftButtonRed(2) }, { ~ohm.turnLeftButtonGreen(2) });
}, bank: mixerBank4, page: page);
~ohm.setLeftButtonFunc(2, { ~falseSelf.mixerB.tglMute(4); }, bank: mixerBank4, page: page);
//volume:
~ohm.setLeftSliderFunc(2, { | val |
  ~falseSelf.mixerB.setVol(4, val.ccdbfs(-70, 0));
}, bank: mixerBank4, page: page);
//sends:
~ohm.setLeftKnobFunc(2, 2, { | val |
  ~falseSelf.mixerB.setSendVol(4, 0, val.ccdbfs(-70, 0));
}, bank: mixerBank4, page: page);
~ohm.setLeftKnobFunc(2, 1, { | val |
  ~falseSelf.mixerB.setSendVol(4, 1, val.ccdbfs(-70, 0));
}, bank: mixerBank4, page: page);
~ohm.setLeftKnobFunc(2, 0, { | val |
  ~falseSelf.mixerB.setSendVol(4, 2, val.ccdbfs(-70, 0));
}, bank: mixerBank4, page: page);

//// end trumpet:
// mute:
~ohm.setLeftButtonMonitorFunc(3, { if( ~falseSelf.mixerA.isMuted(5),
  { ~ohm.turnLeftButtonRed(3) }, { ~ohm.turnLeftButtonGreen(3) });
}, bank: mixerBank4, page: page);
~ohm.setLeftButtonFunc(3, { ~falseSelf.mixerA.tglMute(5) },
  bank: mixerBank4, page: page);
// volume:
~ohm.setLeftSliderFunc(3, { | val |
  ~falseSelf.mixerA.setVol(5, val.ccdbfs(-70, 0));
}, bank: mixerBank4, page: page);
// sends:
~ohm.setLeftKnobFunc(3, 2, { | val |
  ~falseSelf.mixerA.setSendVol(5, 0, val.ccdbfs(-70, 0));
}, bank: mixerBank4, page: page);
~ohm.setLeftKnobFunc(3, 1, { | val |
  ~falseSelf.mixerA.setSendVol(5, 1, val.ccdbfs(-70, 0));
}, bank: mixerBank4, page: page);
~ohm.setLeftKnobFunc(3, 0, { | val |
  ~falseSelf.mixerA.setSendVol(5, 2, val.ccdbfs(-70, 0));
}, bank: mixerBank4, page: page);






/*



~ohm.setPage('falseSelf');
~base.setPage('falseSelf');


~falseSelf.clock.schedAbs(~falseSelf.clock.beats.ceil, { | beat | beat.postln; 1 });

~falseSelf.clock.tempo = 160/60

~falseSelf.clock.schedAbs(~falseSelf.clock.nextBar, { ~falseSelf.clock.beatsPerBar_(4) });

~falseSelf.startSong;

~falseSelf.mixerA.tglMute(0);

~falseSelf.startMelody

*/
