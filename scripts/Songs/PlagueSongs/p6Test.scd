


~p6 = Plague6.new(~prm.audioIn, ~pyr, ~prm.granulator, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus, ~prm.procGroup, \addToHead);


~apc.setPage('plague6');
~prm.reverb.loadPreset('plague6-3');



~p6.triggerSection1(~prm.granulator);
~p6.triggerSection2(~prm.granulator);


~prm.granulator.setGrainDur(0.5, 1.5);
~prm.granulator.setTrigRate(7);

s.options.latency;

s.latency;

IM_MultiShift


~p6.concat.setGlitchLength(0.1);
~p6.concat.setGlitchModRange(0.055);
~p6.concat.setGlitchAmpMod(1);

~p6.concat.subMixer.setVol(0, -9);

~p6.concat.lfo.setFrequency(9);

~p6.concat.lfo.setWaveform('sine');
~p6.concat.setGlitchLength(0.5);
~p6.concat.setGlitchModRange(0.49);

~p6.delNet.randomizeDelayTime(0.1, 5);


Plague4

~prm.reverb.makeGUI;
~prm.reverb.savePreset('plague6-3');

~p6.concat.subMixer.setPreVol(1, -9)

~prm.granulator.grainDurHigh
~prm.granulator.grainDurLow
~prm.granulator.trigRate


~p6.concat.subMixer.vol(1);
~p6.mixer.vol(3);

StereoChorus

~p6.chorus.setRateMul(0.1);
~p6.chorus.setDepth(0.7);
~p6.chorus.setTimeMul(0.1);


-18.dbfsCC;
-9.dbfsCC;
-3.dbfsCC;

~p6.mixer.vol(3);
~p6.mixer.muteMaster
