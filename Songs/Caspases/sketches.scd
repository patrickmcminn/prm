
10.do({ | i | x.playNote(rrand(880, 1220)) });


~apc.setPage('caspases');

~caspases = Caspases.new(~prm.audioIn, ~micIn, ~pickupIn, ~modInput, ~digiTrackArray, ~pyr,
	~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
	~prm.procGroup, \addToHead);

~caspases.distTpt.delay.setMix(0.75);

~caspases.distTpt.distortion.makeEditor;
~caspases.distTpt.distortion.savePreset('caspasesTpt');

~caspases.isLoaded;
~caspases.mixer.setPreVol(0, -3);

~caspases.synthMel.input.unMute;

~caspases.makeMIDIFuncs;

~prm.reverb.loadPreset('caspases');

~caspases.mixer.tglMute(6);

~caspases.lead.distortion.makeEditor

~caspases.shiftTpt.input.tglMute;

~caspases.texture.playSample(0);
~caspases.texture.releaseSample(0);

x ={ SinOsc.ar(220) *0.2 }.play
x.free;

Caspases_Lead


Caspases_ShiftTpt
MicroSynth

s.meter

MIDIIn.connectAll;
~caspases.pad.mixer.setPreVol(0);

~caspases.mixer.setPreVol(9, -18);

~caspases.mixer.setPreVol(7, 0);

~caspases.mixer.setPreVol(0, -6);

~caspases.lead.distortion.makeEditor
~caspases.lead.distortion.savePreset('caspases');

s.makeWindow

~caspases.texture.mixer.setPreVol(0)
~caspases.mixer.setPreVol(0, -9);
~caspases.mixer.setSendVol(0, 0, -9);

~caspases.distTpt.mixer.setPreVol(-12);

~m = Subtractive.new(~prm.audioIn, ~prm.reverb.inBus, group: ~prm.procGroup, addAction: \addToHead);

~m.playNote(220);
~m.releaseNote(220);

~m.mixer.setSendVol(0, -12);
~m.mixer.setVol(0, -9);

~m.readPreset('caspasesPrickles');

~m.playNote(36.midicps);

~caspases.lead.oc2.setOct1Vol(-3);
~caspases.lead.microsynth.setOctVol(-3);
~caspases.lead.microsynth.setSquareVol(-6);
~caspases.lead.microsynth.setOctVol(-6);

~caspases.lead.distortion.setMix(1);

~caspases.mixer.setSendVol(8, 1, -9);

~caspases.mixer.setVol(8, -9);
~caspases.mixer.tglMute(8);

Caspases_Lead

~m.releaseNote(36.midicps);

~m.playNote((60.midicps) * 5/4)

~m.playNote(84.midicps);

~m.playNote(72.midicps*(15/8), -24)
~m.releaseNote(72.midicps*(15/8))

~m.playNote((72.midicps)*(9/5), -24);
~m.releaseNote((72.midicps)*(9/5));


~caspases.lead.microsynth.setDryVol(0);
~caspases.lead.microsynth.setStartFrequency(18000);
~caspases.lead.microsynth.setEndFrequency(18000);


~caspases.pad.setAttackTime(0.01);
~caspases.pad.setFilterCutoff(800);
~caspases.pad.setFilterCutoffLFO2BottomRatio(0.2);
~caspases.pad.setFilterCutoffLFO2TopRatio(6);
~caspases.pad.setLFO2Waveform('sine');
~caspases.pad.setLFO2Freq(8.8);

~caspases.pad.writePreset('caspasesArrival');


~shift = MicroSynth.newMono(~prm.audioIn);
~in = IM_HardwareIn.new(~pickupIn, ~shift.inBus);

~shift.free;

~caspases.lead.loadCulmination;

s.meter

~caspases.mixer.setPreVol(8, 0);

~falseSelf.mixer.sendVol(9, 0);

FalseSelf_FreezeGtr

~falseSelf.freezeGuitar.eq.setLowPassCutoff(5000);

~scTpt = ~caspases.scTpt.mixer.vol;
if( ~scTpt >= -60, {

	~