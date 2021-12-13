GrainFreeze2


~falseSelf = FalseSelf.new(~prm.audioIn, ~micIn, ~pickupIn, ~modInput, ~modOutput, ~moogDevice, ~moogPort, ~pyr, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
  ~prm.procGroup, \addToHead);

~falseSelf.mixer.setPreVol(4, -4.5);

~apc.setPage('falseSelf');
~falseSelf.isLoaded;
~falseSelf.makeMIDIFuncs;

~falseSelf.sixteenthDrones.playVoice1Sequence;
~falseSelf.sixteenthDrones.playVoice2Sequence
~falseSelf.sixteenthDrones.playVoice3Sequence

~falseSelf.planeNoise.playSample
~falseSelf.planeNoise.releaseSample

~falseSelf.midBuzz.playSequence
~falseSelf.midBuzz.stopSequence;


~apc.setActiveMixerBank(1);
~apc.setActiveDeviceButtonsBank(1);

Light

s.meter

~prm.reverb.loadPreset('falseSelf');

-9.dbfsCC

Server.killAll;

FalseSelf_16thDrones
FalseSelf_MidBuzz

s.makeWindow
~falseSelf.trumpetMelody.shift1.mixer.setPreVol(-5);
~falseSelf.trumpetMelody.shift2.mixer.setPreVol(-5);

~falseSelf.freezeGuitar.sampler.setAttackTime(0.15);
FalseSelf_TrumpetMelody

~pyrOut.control(0, 69, 8);


~pyrPort = MIDIOut.newByName("Pyramid MIDI USB", "Pyramid MIDI USB");
~pyrPort.noteOn(0, 60, 64);
~pyrPort.noteOff(0, 60, 64);

~falseSelf.mixer.vol(1);
-6.dbfsCC

FalseSelf_Kick

~falseSelf.planeNoise.playSample
~falseSelf.planeNoise.releaseSample

~falseSelf.midBuzz.playSequence
~falseSelf.midBuzz.stopSequence

~falseSelf.mixer.setVol(13, -17);
~falseSelf.mixer.setVol(8, 0);

~falseSelf.freezeGuitar.mixer.setPreVol(-3);

FalseSelf_Free