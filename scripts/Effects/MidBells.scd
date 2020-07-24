/*
Friday, March 27th 2020
MidBells page.scd
prm
*/

var page = 'midBells';

var gridDrifting = 0;
var gridOneShot = 1;

var sendsBank = 0;

var hold = false;

var mainEncodersBank = 0;

var makeDriftingMIDIFuncs, freeDriftingMIDIFuncs;
var makeOneShotMIDIFuncs, freeOneShotMIDIFuncs;
var driftOn, driftOff;

var midiFuncsLoaded = false;

var driftRate = 3;

~apc.makePage(page);

~apc.addGridBanks(1, page);

makeDriftingMIDIFuncs = { | dev, chan = 12 |
	driftOn = Array.fill(128, { | i |
		MIDIFunc.noteOn({ | vel | ~inst[\midBells].playDriftingNote(i.midicps, vel.ccdbfs, driftRate,
			0, 0.2, 0.03, 0.09, rrand(-1, 1)); }, i, chan, nil);
	});
	driftOff = Array.fill(128, { | i |
		MIDIFunc.noteOff({ ~inst[\midBells].releaseDriftingNote(i.midicps); }, i, chan, nil); });
};

freeDriftingMIDIFuncs = {
	driftOn.do({ | i | i.free; });
	driftOff.do({ | i | i.free; });
};

~apc.turnSceneLaunchButtonBlue(4, page: page);
~apc.setSceneLaunchFunc(4, { if( midiFuncsLoaded == false,
	{ makeDriftingMIDIFuncs.value; ~apc.turnSceneLaunchButtonGreen(4); midiFuncsLoaded = true; },
	{ freeDriftingMIDIFuncs.value; ~apc.turnSceneLaunchButtonBlue(4); midiFuncsLoaded = false; });
}, page: page);

/////// mixer:

~apc.setFaderFunc(1, { | val |
	~inst[\midBells].mixer.setVol(val.ccdbfs) }, page: page);
~apc.setMixerMonitorFunc('main', {
	if( ~inst[\midBells].mixer.isMuted,
		{ ~apc.turnTrackSelectButtonOff(1) }, { ~apc.turnTrackSelectButtonOn(1) });
	if( ~inst[\midBells].mixer.preOrPost == 'post',
		{ ~apc.turnCrossfaderSelectButtonOrange(1) }, { ~apc.turnCrossfaderSelectButtonYellow(1) });
}, page: page);
~apc.setCrossfaderSelectFunc(1, {
	if( ~inst[\midBells].mixer.preOrPost(0) == 'post',
		{ ~inst[\midBells].mixer.setSendPre(0); }, { ~inst[\midBells].mixer.setSendPost(0); });
}, page: page);
~apc.setTrackSelectFunc(1, { ~inst[\midBells].mixer.tglMute }, page: page);

// sends:
~apc.setMixerEncodersMonitorFunc(\sends, {
	var reverb, granulator, modular, delay;
	reverb = ~inst[\midBells].mixer.sendVol(0);
	granulator = ~inst[\midBells].mixer.sendVol(1);
	modular = ~inst[\midBells].mixer.sendVol(2);
	delay = ~inst[\midBells].mixer.sendVol(3);
	~apc.setMixerEncoderValue(1, reverb.dbfsCC, page: page);
	~apc.setMixerEncoderValue(2, granulator.dbfsCC, page: page);
	~apc.setMixerEncoderValue(3, modular.dbfsCC, page: page);
	~apc.setMixerEncoderValue(4, delay.dbfsCC, page: page);
}, sendsBank, page);
~apc.setMixerEncoderFunc(1, { | val | ~inst[\midBells].mixer.setSendVol(0, val.ccdbfs) }, sendsBank, page);
~apc.setMixerEncoderFunc(2, { | val | ~inst[\midBells].mixer.setSendVol(1, val.ccdbfs) }, sendsBank, page);
~apc.setMixerEncoderFunc(3, { | val | ~inst[\midBells].mixer.setSendVol(2, val.ccdbfs) }, sendsBank, page);
~apc.setMixerEncoderFunc(4, { | val | ~inst[\midBells].mixer.setSendVol(3, val.ccdbfs) }, sendsBank, page);

//////// knobs:

// adsr monitor:
~apc.setDeviceEncodersMonitorFunc(\mainEncodersBank, {
	var attack = ~inst[\midBells].attackTime.explin(0.01, 10, 0, 127);
	var decay = ~inst[\midBells].decayTime.explin(0.01, 10, 0, 127);
	var sustain = ~inst[\midBells].sustainLevel.linlin(0, 1, 0, 127);
	var release = ~inst[\midBells].releaseTime.explin(0.01, 15, 0, 127);
	var cutoff = ~inst[\midBells].sampler.filterCutoff.explin(200, 20000, 0, 127);
	var tremRate = ~inst[\midBells].sampler.tremoloRate.explin(0.001, 25, 0, 127);
	var tremDepth = ~inst[\midBells].sampler.tremoloDepth.linlin(0, 1, 0, 127);
	var tremWaveform = ~inst[\midBells].sampler.tremoloWaveform.linlin(0, 5, 0, 127);
	~apc.setDeviceEncoderValue(1, attack);
	~apc.setDeviceEncoderValue(2, decay);
	~apc.setDeviceEncoderValue(3, sustain);
	~apc.setDeviceEncoderValue(4, release);
	~apc.setDeviceEncoderValue(5, cutoff);
	~apc.setDeviceEncoderValue(6, tremRate);
	~apc.setDeviceEncoderValue(7, tremDepth);
	~apc.setDeviceEncoderValue(8, tremWaveform);
}, mainEncodersBank, page);

// attack:
~apc.setDeviceEncoderFunc(1, { | val |
	var attack = val.linexp(0, 127, 0.01, 10);
	~inst[\midBells].setAttackTime(attack);
}, mainEncodersBank, page);

// decay:
~apc.setDeviceEncoderFunc(2, { | val |
	var decay = val.linexp(0, 127, 0.01, 10);
	~inst[\midBells].setDecayTime(decay);
}, mainEncodersBank, page);

// sustain:
~apc.setDeviceEncoderFunc(3, { | val |
	var sustain = val.linlin(0, 127, 0, 1);
	~inst[\midBells].setSustainLevel(sustain);
}, mainEncodersBank, page);

// release:
~apc.setDeviceEncoderFunc(4, { | val |
	var release = val.linexp(0, 127, 0.01, 15);
	~inst[\midBells].setReleaseTime(release);
}, mainEncodersBank, page);

// cutoff:
~apc.setDeviceEncoderFunc(5, { | val |
	var cutoff = val.linexp(0, 127, 200, 20000);
	~inst[\midBells].sampler.setFilterCutoff(cutoff);
}, mainEncodersBank, page);

// trem rate:
~apc.setDeviceEncoderFunc(6, { |  val |
	var rate = val.linexp(0, 127, 0.001, 25);
	~inst[\midBells].sampler.setTremoloRate(rate);
}, mainEncodersBank, page);

// trem depth:
~apc.setDeviceEncoderFunc(7, { | val |
	var depth = val.linlin(0, 127, 0, 1);
	~inst[\midBells].sampler.setTremoloDepth(depth);
}, mainEncodersBank, page);

// trem waveform:
~apc.setDeviceEncoderFunc(8, { | val |
	var waveform = val.linlin(0, 127, 0, 5.9).asInt;
	//var waveform = val;
	waveform.postln;
	~inst[\midBells].sampler.setTremoloWaveform(waveform);
}, mainEncodersBank, page);
