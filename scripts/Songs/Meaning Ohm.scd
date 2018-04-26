/*
Wednesday, April 25th 2018
Meaning_Ohm.scd
prm

everything was pregnant with meaning
*/

var page = 'meaning';

~ohm.makePage(page);

~ohm.addPageLoadFunction(\mainGlobalMixer, { ~ohm.storageDict[\loadGlobalSubmixerFunc].value; }, page);

/////////////////
//// Grid //////
///////////////

~ohm.turnAllGridBlue(page: page);

~ohm.setGridMonitorFunc(0, 0, {
  if( ~meaning.hiss.isPlaying == false,
    { ~ohm.turnGridRed(0, 0); }, { ~ohm.turnGridGreen(0, 0) });
}, page: page);
~ohm.setGridFunc(0, 0, {
  if( ~meaning.hiss.isPlaying == false,
    { ~meaning.hiss.playSequences },
    { ~meaning.hiss.stopSequences });
}, page: page);

//////////////////////
//// Left Side: /////
////////////////////

////// Hiss:
//// mute:
~ohm.setLeftButtonMonitorFunc(0, {
  if( ~meaning.mainMixer.isMuted(0),
    { ~ohm.turnLeftButtonRed(0) }, { ~ohm.turnLeftButtonGreen(0) });
}, page: page);
~ohm.setLeftButtonFunc(0, { ~meaning.mainMixer.tglMute(0) }, page: page);

//// volume:
~ohm.setLeftSliderFunc(0, { | val |
  ~meaning.mainMixer.setVol(0, val.ccdbfs);
}, page: page);

//// sends:
// reverb:
~ohm.setLeftKnobFunc(0, 2, { | val |
  ~meaning.mainMixer.setSendVol(0, 0, val.ccdbfs);
}, page: page);
// delay:
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~meaning.mainMixer.setSendVol(0, 1, val.ccdbfs);
}, page: page);
// modular:
~ohm.setLeftKnobFunc(0, 0, { | val |
  ~meaning.mainMixer.setSendVol(0, 2, val.ccdbfs);
}, page: page);

////// Main:
//// mute:
~ohm.setLeftButtonMonitorFunc(1, {
  if( ~meaning.mainMixer.isMuted(1),
    { ~ohm.turnLeftButtonRed(1) }, { ~ohm.turnLeftButtonGreen(1) });
}, page: page);
~ohm.setLeftButtonFunc(1, { ~meaning.mainMixer.tglMute(1) }, page: page);

//// volume:
~ohm.setLeftSliderFunc(1, { | val |
  ~meaning.mainMixer.setVol(1, val.ccdbfs);
}, page: page);

//// sends:
// reverb:
~ohm.setLeftKnobFunc(1, 2, { | val |
  ~meaning.mainMixer.setSendVol(1, 0, val.ccdbfs);
}, page: page);
// delay:
~ohm.setLeftKnobFunc(1, 1, { | val |
  ~meaning.mainMixer.setSendVol(1, 1, val.ccdbfs);
}, page: page);
// modular:
~ohm.setLeftKnobFunc(1, 0, { | val |
  ~meaning.mainMixer.setSendVol(1, 2, val.ccdbfs);
}, page: page);

////// Synth:
//// mute:
~ohm.setLeftButtonMonitorFunc(2, {
  if( ~meaning.mainMixer.isMuted(2),
    { ~ohm.turnLeftButtonRed(2) }, { ~ohm.turnLeftButtonGreen(2) });
}, page: page);
~ohm.setLeftButtonFunc(2, { ~meaning.mainMixer.tglMute(2) }, page: page);

//// volume:
~ohm.setLeftSliderFunc(2, { | val |
  ~meaning.mainMixer.setVol(2, val.ccdbfs);
}, page: page);

//// sends:
// reverb:
~ohm.setLeftKnobFunc(2, 2, { | val |
  ~meaning.mainMixer.setSendVol(2, 0, val.ccdbfs);
}, page: page);
// delay:
~ohm.setLeftKnobFunc(2, 1, { | val |
  ~meaning.mainMixer.setSendVol(2, 1, val.ccdbfs);
}, page: page);
// modular:
~ohm.setLeftKnobFunc(2, 0, { | val |
  ~meaning.mainMixer.setSendVol(2, 2, val.ccdbfs);
}, page: page);

////// Delay:
//// mute:
~ohm.setLeftButtonMonitorFunc(3, {
  if( ~meaning.returns.isMuted(1),
    { ~ohm.turnLeftButtonRed(3) }, { ~ohm.turnLeftButtonGreen(3) });
}, page: page);
~ohm.setLeftButtonFunc(3, { ~meaning.returns.tglMute(1) }, page: page);

//// volume:
~ohm.setLeftSliderFunc(3, { | val |
  ~meaning.returns.setVol(1, val.ccdbfs);
}, page: page);



/////// incidentals:

//// Synth Cutoff:
~ohm.setLeftKnobFunc(3, 0, { | val |
  var cutoff = val.linexp(0, 127, 150, 13000);
  ~meaning.synth.eq.setHighPassCutoff(cutoff);
}, page: page);

//// main low pass:
~ohm.setLeftKnobFunc(3, 1, { | val |
  var cutoff = val.linexp(0, 127, 150, 20000);
  ~meaning.main.sampler.setFilterCutoff(cutoff);
}, page: page);

////



~prm = AudioSystem.new;
~meaning = Meaning.new(0, ~prm.modular.inBus, ~prm.irLibrary.irDict['2.0MediumHall'], ~prm.procGroup, \addToHead);
~ohm = OhmRGB.new;

~ohm.setPage('meaning');


~base = Base.new;
~base.setPage('meaning');

~meaning.synth.eq.setHighPassCutoff(100);

s.meter

~meaning.free;