Server.killAll;
~prm = AudioSystem.new(2);

(
~mix = IM_Mixer.new(4, ~prm.audioIn, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus,
  ~prm.delay.inBus, false, ~prm.procGroup, \addToHead);
);

(
~sub = Subtractive.new(~mix.chanStereo(0), relGroup: ~prm.procGroup, addAction: \addToHead);
~melSub = Subtractive.new(~mix.chanStereo(1), relGroup: ~prm.procGroup, addAction: \addToHead);
~modIn1 = IM_HardwareIn.new(2, ~mix.chanMono(2), ~prm.procGroup, \addToHead);
~modIn2 = IM_HardwareIn.new(3, ~mix.chanMono(3), ~prm.procGroup, \addToHead);
)


(

~prm.delay.setDelayTime(1.5);


// sub
~mix.setVol(0, -6);
~mix.setSendVol(0, 0, -3);
~mix.setSendVol(0, 1, -3);

// mel sub
~mix.setVol(1, -9);
~mix.setSendVol(1, 0, -9);
~mix.setSendVol(1, 3, -6);

// plaits
~mix.setVol(2, -9);
~mix.setSendVol(2, 0, -9);

// bells
~mix.setVol(3, -3);
~mix.setSendVol(3, 0, -12);

)

//// make sequences
(
~sub.setAllParameters([ 1, 1, 5, 0.1, 0.5, 0, 0.05, 0.05, 0.11, 0.5, 5, 1, 1, 1, 0, 1, 1, 1, 1, 0.4, 0.4, -0.4, 0.4, 0.4, 1, 1, 1, 0, 1.8, 0, 0, 0, 0.5, 0.5, 0, 2, 0, 1, 1, 0, 1, 1, 1, 1, 0.4, -0.4, 0.4, -0.4, 1, 0.4, 1, 1, 0.3, -0.9, 0, 0, 1, 0.5, 0.25, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 10000, 1, 1, 1, 1, 0.05, 0, 0.05, false, 0.7, 1.3, 1, 1, 0, 0, 0, 0, 1, 1500, 0.3, 0, 1, 1, 1, 1, 10, 0.05, 1, 10, 0, -1, 1, 0, 0 ]);

~melSub.setAllParameters([ 1, 1, 5, 0.1, 0.5, 0, 0.05, 0.05, 0.11, 0.5, 5, 1, 1, 1, 0, 1, 1, 1, 1, 0.4, 0.4, -1, 1, 0.4, 1, 1, 1, 0, 1.8, 0, 0, 3, 0.5, 0.5, 0, 2, 0, 1, 1, 0, 1, 1, 1, 1, 0.4, -0.4, 0.4, -0.4, 1, 0.4, 1, 1, 0.3, -0.9, 0, 0, 1, 0.5, 0.25, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 10000, 1, 1, 1, 1, 0.05, 0, 0.05, false, 0.7, 1.3, 1, 1, 0, 0, 0, 0, 1, 900, 0, 0, 1, 1, 1, 1, 0.3, 0.05, 1, 1.75, 0, -1, 1, 0, 0 ]);

~sub.makeSequence(\D);
~sub.makeSequence(\E);
~sub.makeSequence(\F);
~sub.makeSequence(\G);
~sub.makeSequence(\A);
~sub.makeSequence(\B);
~sub.makeSequence(\C);
~sub.makeSequence(\D2);
~sub.makeSequence(\D0);


~sub.makeSequence(\0);
~sub.makeSequence(\2);
~sub.makeSequence(\3);
~sub.makeSequence(\4);
~sub.makeSequence(\6);
~sub.makeSequence(\8);
~sub.makeSequence(\9);


~melSub.makeSequence(\st);
~melSub.makeSequence(\st2);

~melSub.makeSequence(\tenDrone);

~melSub.makeSequence(\BbDrone);


~scale = Scale.new(#[0, 1, 2, 3, 4, 5, 6, 7, 8, 9], 10, Tuning.et(10), "ET10Chromatic");

~endScale = Scale.mixolydian(\just);

~sub.makeSequence(\EBb);
~sub.makeSequence(\EC);
~sub.makeSequence(\ED);
~sub.makeSequence(\EEb);
~sub.makeSequence(\EF);
~sub.makeSequence(\EG);
~sub.makeSequence(\EAb);
~sub.makeSequence(\ED2);
~sub.makeSequence(\EBb0);


);


//// populate sequences:
(
~sub.addKey(\D, \note, Pseq([2, \r], inf));
~sub.addKey(\D, \dur, Pwhite(15, 25));
~sub.addKey(\D, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\D, \legato, 1);
~sub.addKey(\D, \osc2OctaveMul, Prand([0.5, 1, 1, 2, 4], inf));

~sub.addKey(\E, \note, Pseq([4, \r], inf));
~sub.addKey(\E, \dur, Pwhite(17, 27));
~sub.addKey(\E, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\E, \legato, 1);

~sub.addKey(\F, \note, Pxrand([5, \r], inf));
~sub.addKey(\F, \dur, Pwhite(21, 30));
~sub.addKey(\F, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\F, \legato, 1);

~sub.addKey(\G, \note, Prand([7, \r], inf));
~sub.addKey(\G, \dur, Pwhite(11, 20));
~sub.addKey(\G, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\G, \legato, 1);
~sub.addKey(\G, \osc2DetuneCents, Pwhite(0, 20));

~sub.addKey(\A, \note, Pxrand([9, \r], inf));
~sub.addKey(\A, \dur, Pwhite(15, 25));
~sub.addKey(\A, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\A, \legato, 1);
~sub.addKey(\A, \osc1SubAmp, Prand([0, 0, Pwhite(0.1, 0.6)], inf));

~sub.addKey(\B, \note, Prand([11, \r, \r], inf));
~sub.addKey(\B, \dur, Pwhite(15, 25));
~sub.addKey(\B, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\B, \legato, 1);

~sub.addKey(\C, \note, Prand([12, \r, \r, 0], inf));
~sub.addKey(\C, \dur, Pwhite(15, 25));
~sub.addKey(\C, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\C, \legato, 1);

~sub.addKey(\D2, \note, Pseq([14, \r], inf));
~sub.addKey(\D2, \dur, Pwhite(15, 25));
~sub.addKey(\D2, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\D2, \legato, 1);

~sub.addKey(\D0, \note, Pseq([-10, \r, \r, \r], inf));
~sub.addKey(\D0, \dur, Pwhite(15, 25));
~sub.addKey(\D0, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\D0, \legato, 1);

~sub.addKey(\D, \note, Pseq([2, \r], inf));
~sub.addKey(\D, \dur, Pwhite(15, 25));
~sub.addKey(\D, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\D, \legato, 1);

~sub.addKey(\0, \degree, Pseq([0, \r], inf));
~sub.addKey(\0, \scale, ~scale,);
~sub.addKey(\0, \root, 2);
~sub.addKey(\0, \dur, Pwhite(15, 25));
~sub.addKey(\0, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\0, \legato, 1);
~sub.addKey(\0, \osc2OctaveMul, Prand([0.5, 1, 1, 2, 4], inf));
~sub.addKey(\0, \octave, 5);

~sub.addKey(\2, \degree, Pseq([2, \r], inf));
~sub.addKey(\2, \scale, ~scale,);
~sub.addKey(\2, \root, 2);
~sub.addKey(\2, \dur, Pwhite(17, 27));
~sub.addKey(\2, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\2, \legato, 1);
~sub.addKey(\2, \octave, Prand([4, 4, 5], inf));

~sub.addKey(\3, \degree, Pxrand([3, \r], inf));
~sub.addKey(\3, \scale, ~scale,);
~sub.addKey(\3, \root, 2);
~sub.addKey(\3, \dur, Pwhite(21, 30));
~sub.addKey(\3, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\3, \legato, 1);
~sub.addKey(\3, \octave, Prand([4, 4, 5], inf));

~sub.addKey(\4, \degree, Prand([4, \r], inf));
~sub.addKey(\4, \scale, ~scale,);
~sub.addKey(\4, \root, 2);
~sub.addKey(\4, \dur, Pwhite(11, 20));
~sub.addKey(\4, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\4, \legato, 1);
~sub.addKey(\4, \osc2DetuneCents, Pwhite(0, 20));
~sub.addKey(\4, \octave, Prand([4, 4, 5], inf));

~sub.addKey(\6, \degree, Pxrand([6, \r], inf));
~sub.addKey(\6, \scale, ~scale,);
~sub.addKey(\6, \root, 2);
~sub.addKey(\6, \dur, Pwhite(15, 25));
~sub.addKey(\6, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\6, \legato, 1);
~sub.addKey(\6, \osc1SubAmp, Prand([0, 0, Pwhite(0.1, 0.6)], inf));
~sub.addKey(\6, \octave, Prand([4, 4, 5], inf));

~sub.addKey(\8, \degree, Prand([8, \r, \r], inf));
~sub.addKey(\8, \scale, ~scale,);
~sub.addKey(\8, \root, 2);
~sub.addKey(\8, \dur, Pwhite(15, 25));
~sub.addKey(\8, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\8, \legato, 1);
~sub.addKey(\8, \octave, Prand([4, 4, 5], inf));

~sub.addKey(\9, \degree, Prand([9, \r, \r, 0], inf));
~sub.addKey(\9, \scale, ~scale,);
~sub.addKey(\9, \root, 2);
~sub.addKey(\9, \dur, Pwhite(15, 25));
~sub.addKey(\9, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\9, \legato, 1);
~sub.addKey(\9, \octave, Prand([4, 4, 5], inf));

~melSub.addKey(\st, \scale, ~scale);
~melSub.addKey(\st, \dur, 0.125);
~melSub.addKey(\st, \degree, Pseq([[0, 2], [0, 3], [0, 4], [0, 6], [0, 8,], [0, 9]], inf));
~melSub.addKey(\st, \legato, 0.75);
~melSub.addKey(\st, \amp, 0.3);
~melSub.addKey(\st, \root, 2);
~melSub.addKey(\st, \degree, Pseq([0, 2, 3, 4, 6], inf));
~melSub.addKey(\st, \dur, Pseq([0.5, 0.5, 0.5, 0.5, 1], inf));

~melSub.addKey(\st2, \scale, ~scale);
~melSub.addKey(\st2, \legato, 0.75);
~melSub.addKey(\st2, \amp, 0.3);
~melSub.addKey(\st2, \root, 2);
~melSub.addKey(\st2, \degree, Pseq([6, 4, 3, 2, 0, -1, 0], inf));
~melSub.addKey(\st2, \dur, Pseq([0.5, 0.5, 0.5, 0.5, 1], inf));

~melSub.addKey(\tenDrone, \dur, 8);
~melSub.addKey(\tenDrone, \scale, ~scale);
~melSub.addKey(\tenDrone, \legato, 1);
~melSub.addKey(\tenDrone, \degree, Pseq([0, -1, 2, 6], inf));
~melSub.addKey(\tenDrone, \root, 2);
~melSub.addKey(\tenDrone, \octave, 3);
~melSub.addKey(\tenDrone, \amp, 0.3);

~sub.addKey(\EBb, \root, -2);
~sub.addKey(\EBb, \scale, ~endScale);
~sub.addKey(\EBb, \degree, Pseq([0, \r], inf));
~sub.addKey(\EBb, \dur, Pwhite(15, 25));
~sub.addKey(\EBb, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\EBb, \legato, 1);

~sub.addKey(\EC, \root, -2);
~sub.addKey(\EC, \scale, ~endScale);
~sub.addKey(\EC, \degree, Pseq([1, \r], inf));
~sub.addKey(\EC, \dur, Pwhite(17, 27));
~sub.addKey(\EC, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\EC, \legato, 1);

~sub.addKey(\ED, \root, -2);
~sub.addKey(\ED, \scale, ~endScale);
~sub.addKey(\ED, \degree, Pseq([2, \r], inf));
~sub.addKey(\ED, \dur, Pwhite(15, 25));
~sub.addKey(\ED, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\ED, \legato, 1);

~sub.addKey(\EEb, \root, -2);
~sub.addKey(\EEb, \scale, ~endScale);
~sub.addKey(\EEb, \degree, Pseq([3, \r], inf));
~sub.addKey(\EEb, \dur, Pwhite(21, 30));
~sub.addKey(\EEb, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\EEb, \legato, 1);

~sub.addKey(\EF, \root, -2);
~sub.addKey(\EF, \scale, ~endScale);
~sub.addKey(\EF, \degree, Prand([4, \r], inf));
~sub.addKey(\EF, \dur, Pwhite(11, 20));
~sub.addKey(\EF, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\EF, \legato, 1);

~sub.addKey(\EG, \root, -2);
~sub.addKey(\EG, \scale, ~endScale);
~sub.addKey(\EG, \degree, Prand([5, \r, \r], inf));
~sub.addKey(\EG, \dur, Pwhite(15, 25));
~sub.addKey(\EG, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\EG, \legato, 1);

~sub.addKey(\EAb, \root, -2);
~sub.addKey(\EAb, \scale, ~endScale);
~sub.addKey(\EAb, \degree, Pseq([-1, \r, \r, 8], inf));
~sub.addKey(\EAb, \dur, Pwhite(15, 25));
~sub.addKey(\EAb, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\EAb, \legato, 1);

~sub.addKey(\ED2, \root, -2);
~sub.addKey(\ED2, \scale, ~endScale);
~sub.addKey(\ED2, \degree, Prand([9, \r], inf));
~sub.addKey(\ED2, \dur, Pwhite(15, 25));
~sub.addKey(\ED2, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\ED2, \legato, 1);

~sub.addKey(\EBb0, \root, -2);
~sub.addKey(\EBb0, \scale, ~endScale);
~sub.addKey(\EBb0, \degree, Pseq([-7, \r], inf));
~sub.addKey(\EBb0, \dur, Pwhite(15, 25));
~sub.addKey(\EBb0, \amp, Pwhite(0.2, 0.32));
~sub.addKey(\EBb0, \legato, 1);

~melSub.addKey(\BbDrone, \root, -2);
~melSub.addKey(\BbDrone, \scale, ~endScale);
~melSub.addKey(\BbDrone, \octave, Prand([2, 3], inf));
//~melSub.addKey(\BbDrone, \octave, 3);
//~melSub.addKey(\BbDrone, \octave, 2);
~melSub.addKey(\BbDrone, \dur, 30);
~melSub.addKey(\BbDrone, \degree, 0);
~melSub.addKey(\BbDrone, \amp, 0.5);
~melSub.addKey(\BbDrone, \legato, 1);

);

~apc  = APC40Mk2.new;

(

var page = 'main';

var send0 = 0;
var send1 = 1;
var send2 = 2;
var send3 = 3;

var dIsPlaying = false;
var eIsPlaying = false;
var fIsPlaying = false;
var gIsPlaying = false;
var aIsPlaying = false;
var bIsPlaying = false;
var cIsPlaying = false;
var d2IsPlaying = false;
var d0IsPlaying = false;

var zeroIsPlaying = false;
var twoIsPlaying = false;
var threeIsPlaying = false;
var fourIsPlaying = false;
var sixIsPlaying = false;
var eightIsPlaying = false;
var nineIsPlaying = false;

var stIsPlaying = false;
var st2IsPlaying = false;

var tenDroneIsPlaying = false;

var eBbIsPlaying = false;
var eCIsPlaying = false;
var eDIsPlaying = false;
var eEbIsPlaying = false;
var eFIsPlaying = false;
var eGIsPlaying = false;
var eAbIsPlaying = false;
var eD2IsPlaying = false;
var eBb0IsPlaying = false;

var bbDroneIsPlaying = false;

var activeDBank = 0;

var subBank = 0;
var delBank = 1;

~apc.addDeviceButtonsBanks(1, page);
~apc.addMixerEncodersBanks(3, page);
~apc.addDeviceEncodersBanks(1, page);

////////////////////
/////// mixer: ////
//////////////////

4.do({ | i |
  // vol:
  ~apc.setFaderFunc(i+1, { | val | ~mix.setVol(i, val.ccdbfs); }, page: page);
  // toggle mute:
  ~apc.setTrackSelectFunc(i+1, { ~mix.tglMute(i); }, page: page);
  ~apc.setMixerMonitorFunc("mute"++i.asSymbol, {
    if( ~mix.isMuted(i), { ~apc.turnTrackSelectButtonOff(i+1) }, { ~apc.turnTrackSelectButtonOn(i+1); });
  }, page: page);
  // sends pre/post:
  ~apc.setCrossfaderSelectFunc(i+1, {
    if( ~mix.preOrPost(i) == 'post',
      { ~mix.setSendPre(i); }, { ~mix.setSendPost(i) });
  }, page: page);
  ~apc.setMixerMonitorFunc("preOrPost"++i.asSymbol, {
    if( ~mix.preOrPost(i) == 'post',
      { ~apc.turnCrossfaderSelectButtonOrange(i+1) },
      { ~apc.turnCrossfaderSelectButtonYellow(i+1) });
  }, page: page);

  //// sends:
  ~apc.setMixerEncoderFunc(i+1, { | val | ~mix.setSendVol(i, 0, val.ccdbfs); }, bank: send0, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~mix.setSendVol(i, 1, val.ccdbfs); }, bank: send1, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~mix.setSendVol(i, 2, val.ccdbfs); }, bank: send2, page: page);
  ~apc.setMixerEncoderFunc(i+1, { | val | ~mix.setSendVol(i, 3, val.ccdbfs); }, bank: send3, page: page);

  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~mix.sendVol(i, 0).dbfsCC); }, bank: send0, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~mix.sendVol(i, 1).dbfsCC); }, bank: send1, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~mix.sendVol(i, 2).dbfsCC); }, bank: send2, page: page);
  ~apc.setMixerEncodersMonitorFunc(("mixer"++i++"send").asSymbol, {
    ~apc.setMixerEncoderValue(i+1, ~mix.sendVol(i, 3).dbfsCC); }, bank: send3, page: page);

  //// send bank changes:
  ~apc.setDeviceButtonsMonitorFunc(\sends, {
    if( ~apc.activeMixerEncodersBank == send0,
      { ~apc.turnDeviceButtonOn(1) }, { ~apc.turnDeviceButtonOff(1) });
    if( ~apc.activeMixerEncodersBank  == send1,
      { ~apc.turnDeviceButtonOn(2) }, { ~apc.turnDeviceButtonOff(2) });
    if( ~apc.activeMixerEncodersBank  == send2,
      { ~apc.turnDeviceButtonOn(3) }, { ~apc.turnDeviceButtonOff(3) });
    if( ~apc.activeMixerEncodersBank == send3,
      { ~apc.turnDeviceButtonOn(4) }, { ~apc.turnDeviceButtonOff(4) });
  }, page: page);

  ~apc.setDeviceButtonFunc(1, { ~apc.setActiveMixerEncodersBank(send0); }, page: page);
  ~apc.setDeviceButtonFunc(2, { ~apc.setActiveMixerEncodersBank(send1);  }, page: page);
  ~apc.setDeviceButtonFunc(3, { ~apc.setActiveMixerEncodersBank(send2); }, page: page);
  ~apc.setDeviceButtonFunc(4, { ~apc.setActiveMixerEncodersBank(send3); }, page: page);

});

~apc.setMasterFaderFunc({ | val |
  ~prm.systemMixer.setVol(0, val.ccdbfs);
}, page: page);

//////////////////////////
//// Device Control: ////
////////////////////////

// switch:
~apc.setDeviceButtonsMonitorFunc(\dev, {
  if( ~apc.activeDeviceEncodersBank == subBank,
    { ~apc.turnDeviceButtonOn(5) }, { ~apc.turnDeviceButtonOff(5); });
  if( ~apc.activeDeviceEncodersBank == delBank,
    { ~apc.turnDeviceButtonOn(6) }, { ~apc.turnDeviceButtonOff(6) });
});
~apc.setDeviceButtonFunc(5, { ~apc.setActiveDeviceEncodersBank(subBank) });
~apc.setDeviceButtonFunc(6, { ~apc.setActiveDeviceEncodersBank(delBank) });

// detune:
~apc.setDeviceEncodersMonitorFunc(\detune, {
  var detune = ~sub.osc2DetuneCents.linlin(-15, 15, 0, 127);
  ~apc.setDeviceEncoderValue(1, detune);
}, bank: subBank);
~apc.setDeviceEncoderFunc(1, { | val |
  var detune = val.linlin(0, 127, -15, 15);
  ~sub.setOsc2DetuneCents(detune);
}, bank: subBank);
// filter cutoff:
~apc.setDeviceEncodersMonitorFunc(\cutoff, {
  var cutoff = ~sub.filterCutoff.explin(700, 3500, 0, 127);
  ~apc.setDeviceEncoderValue(2, cutoff);
}, bank: subBank);
~apc.setDeviceEncoderFunc(2, { | val |
  var cutoff = val.linexp(0, 127, 700, 3500);
  ~sub.setFilterCutoff(cutoff);
}, bank: subBank);
// filter res:
~apc.setDeviceEncodersMonitorFunc(\res, {
  var res = ~sub.filterRes.linlin(0, 1, 0, 127);
  ~apc.setDeviceEncoderValue(3, res);
}, bank: subBank);
~apc.setDeviceEncoderFunc(3, { | val |
  var res = val.linlin(0, 127, 0, 1);
  ~sub.setFilterRes(res);
}, bank: subBank);
// filter drive:
~apc.setDeviceEncodersMonitorFunc(\drive, {
  var drive = ~sub.filterDrive.explin(1, 100, 0, 127);
  ~apc.setDeviceEncoderValue(4, drive);
}, bank: subBank);
~apc.setDeviceEncoderFunc(4, { | val |
  var drive = val.linexp(0, 127, 1, 100);
  ~sub.setFilterDrive(drive);
}, bank: subBank);
// lfo 1 freq:
~apc.setDeviceEncodersMonitorFunc(\lfo1, {
  var freq = ~sub.lfo1Freq.explin(0.1, 15, 0, 127);
  ~apc.setDeviceEncoderValue(5, freq);
}, bank: subBank);
~apc.setDeviceEncoderFunc(5, { | val |
  var freq = val.linexp(0, 127, 0.1, 15);
  ~sub.setLFO1Freq(freq);
}, bank: subBank);
// lfo 2 freq:
~apc.setDeviceEncodersMonitorFunc(\lfo2, {
  var freq = ~sub.lfo2Freq.explin(0.11, 15.01, 0, 127);
  ~apc.setDeviceEncoderValue(6, freq);
}, bank: subBank);
~apc.setDeviceEncoderFunc(6, { | val |
  var freq = val.linexp(0, 127, 0.11, 15.01);
  ~sub.setLFO2Freq(freq);
}, bank: subBank);

// sub osc 1:
~apc.setDeviceEncodersMonitorFunc(\sub1, {
  var vol = ~sub.osc1SubAmp.ampdb.dbfsCC;
  ~apc.setDeviceEncoderValue(7, vol);
}, bank: subBank);
~apc.setDeviceEncoderFunc(7, { | val | ~sub.setOsc1SubVol(val.ccdbfs); }, bank: subBank);

// sub osc 2:
~apc.setDeviceEncodersMonitorFunc(\sub2, {
  var vol = ~sub.osc2SubAmp.ampdb.dbfsCC;
  ~apc.setDeviceEncoderValue(8, vol);
}, bank: subBank);
~apc.setDeviceEncoderFunc(8, { | val | ~sub.setOsc2SubVol(val.ccdbfs); }, bank: subBank);

////////////////////
//// DelBank //////
//////////////////
~apc.setDeviceEncoderValue(1, 0.01.explin(0.001, 1, 0, 127), delBank);
~apc.setDeviceEncoderFunc(1, { | val |
  var dur = val.linexp(0, 127, 0.001, 1);
  ~prm.granulator.setGrainDurLow(dur);
  ~apc.setDeviceEncoderValue(1, val);
}, bank: delBank);

~apc.setDeviceEncoderValue(2, 0.19.explin(0.005, 1.5, 0, 127), delBank);
~apc.setDeviceEncoderFunc(2, { | val |
  var dur = val.linexp(0, 127, 0.005, 1.5);
  ~prm.granulator.setGrainDurHigh(dur);
  ~apc.setDeviceEncoderValue(2, val);
}, bank: delBank);

~apc.setDeviceEncoderValue(3, 32.linlin(1, 64, 0, 128), delBank);
~apc.setDeviceEncoderFunc(3, { | val |
  var rate = val.linlin(0, 127, 1, 64);
  ~prm.granulator.setTrigRate(rate);
  ~apc.setDeviceEncoderValue(3, val);
}, bank: delBank);

~apc.setDeviceEncodersMonitorFunc(\delayTime, {
  var delay = ~prm.delay.delayTime.linlin(0.01, 5, 0, 127);
  ~apc.setDeviceEncoderValue(5, delay);
}, bank: delBank);
~apc.setDeviceEncoderFunc(5, { | val |
  var delay = val.linlin(0, 127, 0.01, 5);
  ~prm.delay.setDelayTime(delay);
}, bank: delBank);

~apc.setDeviceEncodersMonitorFunc(\feedback, {
  var feedback = ~prm.delay.feedback.linlin(0, 1.1, 0, 127);
  ~apc.setDeviceEncoderValue(6, feedback);
}, bank: delBank);
~apc.setDeviceEncoderFunc(6, { | val |
  var feedback = val.linlin(0, 127, 0, 1.1);
  ~prm.delay.setFeedback(feedback);
}, bank: delBank);

// low/high pass
~apc.turnSceneLaunchButtonPink(4);
~apc.setSceneLaunchFunc(4, {
  if( ~sub.filterType == 0,
    { ~sub.setFilterType('highPass'); }, { ~sub.setFilterType('lowPass') });
});
~apc.setSceneLaunchMonitorFunc(\filterType, {
  if( ~sub.filterType == 0, { ~apc.turnSceneLaunchButtonPink(4) }, { ~apc.turnSceneLaunchButtonYellow(4) });
});


/////////////////////
//// SEQUENCES: ////
///////////////////

/////////////////
///// 12TET ////
///////////////

~apc.turnGridBlue(0, 4);
~apc.setGridFunc(0, 4, {
  if( dIsPlaying == false,
    { ~sub.playSequence(\D);
      ~apc.turnGridGreen(0, 4);
      dIsPlaying = true;
    },
    { ~sub.stopSequence(\D);
      ~apc.turnGridBlue(0, 4);
      dIsPlaying = false;
    }
  );
});

~apc.turnGridPink(1, 4);
~apc.setGridFunc(1, 4, {
  if( eIsPlaying == false,
    { ~sub.playSequence(\E);
      ~apc.turnGridGreen(1, 4);
      eIsPlaying = true;
    },
    { ~sub.stopSequence(\E);
      ~apc.turnGridPink(1, 4);
      eIsPlaying = false;
    }
  );
});

~apc.turnGridMagenta(2, 4);
~apc.setGridFunc(2, 4, {
  if( fIsPlaying == false,
    { ~sub.playSequence(\F);
      ~apc.turnGridGreen(2, 4);
      fIsPlaying = true;
    },
    { ~sub.stopSequence(\F);
      ~apc.turnGridMagenta(2, 4);
      fIsPlaying = false;
    }
  );
});

~apc.turnGridPurple(3, 4);
~apc.setGridFunc(3, 4, {
  if (gIsPlaying == false,
    { ~sub.playSequence(\G);
      ~apc.turnGridGreen(3, 4);
      gIsPlaying = true;
    },
    { ~sub.stopSequence(\G);
      ~apc.turnGridPurple(3, 4);
      gIsPlaying = false;
  });
});

~apc.turnGridCyan(4, 4);
~apc.setGridFunc(4, 4, {
  if (aIsPlaying == false,
    { ~sub.playSequence(\A);
      ~apc.turnGridGreen(4, 4);
      aIsPlaying = true;
    },
    { ~sub.stopSequence(\A);
      ~apc.turnGridCyan(4, 4);
      aIsPlaying = false;
  });
});

~apc.turnGridLightBlue(5, 4);
~apc.setGridFunc(5, 4, {
  if ( bIsPlaying == false,
    { ~sub.playSequence(\B);
      ~apc.turnGridGreen(5, 4);
      bIsPlaying = true;
    },
    { ~sub.stopSequence(\B);
      ~apc.turnGridLightBlue(5, 4);
      bIsPlaying = false;
  });
});

~apc.turnGridYellowGreen(6, 4);
~apc.setGridFunc(6, 4, {
  if( cIsPlaying == false,
    { ~sub.playSequence(\C);
      ~apc.turnGridGreen(6, 4);
      cIsPlaying = true;
    },
    { ~sub.stopSequence(\C);
      ~apc.turnGridYellowGreen(6, 4);
      cIsPlaying = false;
  });
});

~apc.turnGridWhite(7, 4);
~apc.setGridFunc(7, 4, {
  if( d2IsPlaying == false,
    { ~sub.playSequence(\D2);
      ~apc.turnGridGreen(7, 4);
      d2IsPlaying = true;
    },
    { ~sub.stopSequence(\D2);
      ~apc.turnGridWhite(7, 4);
      d2IsPlaying = false;
  });
});

~apc.turnSceneLaunchButtonBlue(0);
~apc.setSceneLaunchFunc(0, {
  if ( d0IsPlaying == false,
    { ~sub.playSequence(\D0);
      ~apc.turnSceneLaunchButtonGreen(0);
      d0IsPlaying = true;
    },
    {
      ~sub.stopSequence(\D0);
      ~apc.turnSceneLaunchButtonBlue(0);
      d0IsPlaying = false;
  });
});


/////////////////
///// 10TET ////
///////////////

~apc.turnGridYellowGreen(0, 3);
~apc.setGridFunc(0, 3, {
  if( zeroIsPlaying == false,
    { ~sub.playSequence(\0);
      ~apc.turnGridGreen(0, 3);
      zeroIsPlaying = true;
    },
    { ~sub.stopSequence(\0);
      ~apc.turnGridYellowGreen(0, 3);
      zeroIsPlaying = false;
  });
});

~apc.turnGridYellow(1, 3);
~apc.setGridFunc(1, 3, {
  if( twoIsPlaying == false,
    { ~sub.playSequence(\2);
      ~apc.turnGridGreen(1, 3);
      twoIsPlaying = true;
    },
    { ~sub.stopSequence(\2);
      ~apc.turnGridYellow(1, 3);
      twoIsPlaying = false;
  });
});

~apc.turnGridWhite(2, 3);
~apc.setGridFunc(2, 3, {
  if ( threeIsPlaying == false,
    { ~sub.playSequence(\3);
      ~apc.turnGridGreen(2, 3);
      threeIsPlaying = true;
    },
    { ~sub.stopSequence(\3);
      ~apc.turnGridWhite(2, 3);
      threeIsPlaying = false;
  });
});

~apc.turnGridLightBlue(3, 3);
~apc.setGridFunc(3, 3, {
  if ( fourIsPlaying == false,
    { ~sub.playSequence(\4);
      ~apc.turnGridGreen(3, 3);
      fourIsPlaying = true;
    },
    { ~sub.stopSequence(\4);
      ~apc.turnGridLightBlue(3, 3);
      fourIsPlaying = false;
  });
});

~apc.turnGridBlue(4, 3);
~apc.setGridFunc(4, 3, {
  if( sixIsPlaying == false,
    { ~sub.playSequence(\6);
      ~apc.turnGridGreen(4, 3);
      sixIsPlaying = true;
    },
    { ~sub.stopSequence(\6);
      ~apc.turnGridBlue(4, 3);
      sixIsPlaying = false;
  });
});

~apc.turnGridPink(5, 3);
~apc.setGridFunc(5, 3, {
  if( eightIsPlaying == false,
    { ~sub.playSequence(\8);
      ~apc.turnGridGreen(5, 3);
      eightIsPlaying = true;
    },
    { ~sub.stopSequence(\8);
      ~apc.turnGridPink(5, 3);
      eightIsPlaying = false;
  });
});

~apc.turnGridPurple(6, 3);
~apc.setGridFunc(6, 3, {
  if( nineIsPlaying == false,
    { ~sub.playSequence(\9);
      ~apc.turnGridGreen(6, 3);
      nineIsPlaying = true;
    },
    { ~sub.stopSequence(\9);
      ~apc.turnGridPurple(6, 3);
      nineIsPlaying = false;
  });
});

// tenDrone:
~apc.turnGridCyan(0, 2);
~apc.setGridFunc(0, 2, {
  if ( tenDroneIsPlaying == false,
    { ~melSub.playSequence(\tenDrone);
      ~apc.turnGridGreen(0, 2);
      tenDroneIsPlaying = true;
    },
    { ~melSub.stopSequence(\tenDrone);
      ~apc.turnGridCyan(0, 2);
      tenDroneIsPlaying = false;
  });
});

// st:
~apc.turnGridWhite(3, 2);
~apc.setGridFunc(3, 2, {
  if( stIsPlaying == false,
    { ~melSub.playSequence(\st);
      ~apc.turnGridGreen(3, 2);
      stIsPlaying = true;
    },
    { ~melSub.stopSequence(\st);
      ~apc.turnGridWhite(3, 2);
      stIsPlaying = false;
  });
});

// st2:
~apc.turnGridMagenta(4, 2);
~apc.setGridFunc(4, 2, {
  if( st2IsPlaying == false,
    { ~melSub.playSequence(\st2);
      ~apc.turnGridGreen(4, 2);
      st2IsPlaying = true;
    },
    { ~melSub.stopSequence(\st2);
      ~apc.turnGridMagenta(4, 2);
      st2IsPlaying = false;
  });
});

////////////////
///// JUST ////
//////////////

~apc.turnGridLightBlue(0, 1);
~apc.setGridFunc(0, 1, {
  if( eBbIsPlaying == false,
    {
      ~sub.playSequence(\EBb);
      ~apc.turnGridGreen(0, 1);
      eBbIsPlaying = true;
    },
    {
      ~sub.stopSequence(\EBb);
      ~apc.turnGridLightBlue(0, 1);
      eBbIsPlaying = false;
  });
});

~apc.turnGridBlue(1, 1);
~apc.setGridFunc(1, 1, {
  if( eCIsPlaying == false,
    {
      ~sub.playSequence(\EC);
      ~apc.turnGridGreen(1, 1);
      eCIsPlaying = true;
    },
    {
      ~sub.stopSequence(\EC);
      ~apc.turnGridBlue(1, 1);
      eCIsPlaying = false;
  });
});

~apc.turnGridCyan(2, 1);
~apc.setGridFunc(2, 1, {
  if( eDIsPlaying == false,
    {
      ~sub.playSequence(\ED);
      ~apc.turnGridGreen(2, 1);
      eDIsPlaying = true;
    },
    { ~sub.stopSequence(\ED);
      ~apc.turnGridCyan(2, 1);
      eDIsPlaying = false;
  });
});

~apc.turnGridPurple(3, 1);
~apc.setGridFunc(3, 1, {
  if( eEbIsPlaying == false,
    { ~sub.playSequence(\EEb);
      ~apc.turnGridGreen(3, 1);
      eEbIsPlaying = true;
    },
    { ~sub.stopSequence(\EEb);
      ~apc.turnGridPurple(3, 1);
      eEbIsPlaying = false;
  });
});

~apc.turnGridMagenta(4, 1);
~apc.setGridFunc(4, 1, {
  if( eFIsPlaying == false,
    { ~sub.playSequence(\EF);
      ~apc.turnGridGreen(4, 1);
      eFIsPlaying = true;
    },
    { ~sub.stopSequence(\EF);
      ~apc.turnGridMagenta(4, 1);
      eFIsPlaying = false;
  });
});

~apc.turnGridPink(5, 1);
~apc.setGridFunc(5, 1, {
  if (eGIsPlaying == false,
    { ~sub.playSequence(\EG);
      ~apc.turnGridGreen(5, 1);
      eGIsPlaying = true;
    },
    { ~sub.stopSequence(\EG);
      ~apc.turnGridPink(5, 1);
      eGIsPlaying = false;
  });
});

~apc.turnGridYellowGreen(6, 1);
~apc.setGridFunc(6, 1, {
  if( eAbIsPlaying == false,
    { ~sub.playSequence(\EAb);
      ~apc.turnGridGreen(6, 1);
      eAbIsPlaying = true;
    },
    { ~sub.stopSequence(\EAb);
      ~apc.turnGridYellowGreen(6, 1);
      eAbIsPlaying = false;
  });
});

~apc.turnGridWhite(7, 1);
~apc.setGridFunc(7, 1, {
  if( eD2IsPlaying == false,
    { ~sub.playSequence(\ED2);
      ~apc.turnGridGreen(7, 1);
      eD2IsPlaying = true;
    },
    { ~sub.stopSequence(\ED2);
      ~apc.turnGridWhite(7, 1);
      eD2IsPlaying = false;
  });
});

~apc.turnSceneLaunchButtonBlue(3);
~apc.setSceneLaunchFunc(3, {
  if( eBb0IsPlaying == false,
    { ~sub.playSequence(\EBb0);
      ~apc.turnSceneLaunchButtonGreen(3);
      eBb0IsPlaying = true;
    },
    { ~sub.stopSequence(\EBb0);
      ~apc.turnSceneLaunchButtonBlue(3);
      eBb0IsPlaying = false;
  });
});

~apc.turnGridLightBlue(0, 0);
~apc.setGridFunc(0, 0, {
  if( bbDroneIsPlaying == false,
    { ~melSub.playSequence(\BbDrone);
      ~apc.turnGridGreen(0, 0);
      bbDroneIsPlaying = true;
    },
    { ~melSub.stopSequence(\BbDrone);
      ~apc.turnGridLightBlue(0, 0);
      bbDroneIsPlaying = false;
  });
});

);





~sub.playSequence(\D);
~sub.playSequence(\E);
~sub.playSequence(\F);
~sub.playSequence(\G);
~sub.playSequence(\A);
~sub.playSequence(\B);
~sub.playSequence(\C);
~sub.playSequence(\D2);
~sub.playSequence(\D0);

(
~sub.playSequence(\0);
~sub.stopSequence(\D);
)

(
~sub.playSequence(\2);
~sub.stopSequence(\E);
)

(
~sub.playSequence(\3);
~sub.stopSequence(\F);
)

(
~sub.playSequence(\4);
~sub.stopSequence(\G);
)

(
~sub.playSequence(\6);
~sub.stopSequence(\A);
)


(
~sub.playSequence(\8);
~sub.stopSequence(\B);
)


(
~sub.playSequence(\9);
~sub.stopSequence(\C);
)


~sub.stopSequence(\D);
~sub.stopSequence(\E);
~sub.stopSequence(\F);
~sub.stopSequence(\G);
~sub.stopSequence(\A);
~sub.stopSequence(\B);
~sub.stopSequence(\C);
~sub.stopSequence(\D2);
~sub.stopSequence(\D0);


~sub.setOsc1SubVol(-3);
~sub.setOsc2SubVol(-6);


/*
//~sub = Subtractive.new(~prm.submixA, ~prm.reverb.inBus, ~prm.granulator.inBus);
(
~sub.mixer.setSendVol(0, -12);
~sub.mixer.setSendVol(1, -100);

~sub.setOsc1Waveform('sine');
~sub.setOsc2Waveform('tri');
~sub.setOsc2Octave(4);


~sub.setAttackTime(10);
~sub.setReleaseTime(10);

~sub.setLFO1Waveform('noise');
~sub.setLFO1Freq(0.1);

~sub.setPanLFO1Bottom(-1);
~sub.setPanLFO1Top(1);

~sub.setFilterCutoff(1500);
~sub.setFilterCutoffLFO1BottomRatio(0.7);
~sub.setFilterCutoffLFO1TopRatio(1.3);
~sub.setLFO2Freq(0.11);
~sub.setLFO2Waveform('noise');

~sub.setFilterDrive(10);
~sub.setFilterRes(0.3);

~sub.setOsc1AmpLFO1Bottom(0.4);
~sub.setOsc2AmpLFO1Top(0.4);

~sub.setOsc1WaveformLFO1Bottom(0);
~sub.setOsc1WaveformLFO1Top(1.8);

~sub.setOsc2WaveformLFO1Bottom(0.3);
~sub.setOsc2WaveformLFO1Top(-0.9);


~sub.setOsc1PulseWidthLFO2Bottom(-0.4);
~sub.setOsc1PulseWidthLFO2Top(0.4);
~sub.setOsc1PulseWidthLFO1Bottom(0.4);
~sub.setOsc1PulseWidthLFO1Top(0.4);

~sub.setOsc2PulseWidthLFO2Top(-0.4);
~sub.setOsc2PulseWidthLFO2Bottom(0.4);
~sub.setOsc2PulseWidthLFO1Top(-0.4);
~sub.setOsc2PulseWidthLFO1Bottom(0.4);

~sub.setFilterType('lowPass');
~sub.setFilterType('highPass');
);
*/

~sub.setOsc2DetuneCents(10);

~sub.printAllParameters;

~sub.setOsc1PulseWidthLFO1Bottom(-0.4);
~sub.osc1PulseWidthLFO1Bottom;


~sub.setOsc1WaveformLFO2Bottom(4);
~sub.setOsc1WaveformLFO2Top(0);


~melSub.stopSequence(\st);


~melSub.stopSequence(\st2);

~scale.ratios

~melSub.playSequence(\tenDrone);
~melSub.stopSequence(\tenDrone);

(
~melSub.playSequence(\st2);
~melSub.playSequence(\st);
)

(
~sub.stopSequence(\0);
~sub.stopSequence(\2);
~sub.stopSequence(\3);
~sub.stopSequence(\4);
~sub.stopSequence(\6);
~sub.stopSequence(\8);
~sub.stopSequence(\9);
)







~melSub.setReleaseTime(1.75);
~melSub.setOsc2Octave(4);
~melSub.setOsc1Octave(3);
~melSub.setFilterRes(0);
~melSub.setAttackTime(0.3);
~melSub.setFilterCutoffLFO2BottomRatio(1);
~melSub.setFilterCutoff(900);
~melSub.setOsc1Waveform('rect');
~melSub.setOsc1PulseWidthLFO2Bottom(-1);
~melSub.setOsc1PulseWidthLFO2Top(1);

~melSub.printAllParameters;



s.meter




