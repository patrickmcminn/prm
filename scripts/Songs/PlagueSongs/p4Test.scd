(
~p4 = Plague4.new(~prm.audioIn, ~pyr, ~prm.granulator, ~modInput, ~modOutput,
	~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
	~prm.procGroup, \addToHead);
);


~prm.delay.setDelayTime((60/136)/2);
~prm.delay.setFeedback(0.45);

~prm.reverb.loadPreset('plague4');

~apc.setPage('plague4');
~base.setPage('plague4');

~p4.subtractive;

~p4.isLoaded;



~p4.playChord1;
~p4.releaseChord1

~p4.playChord2;
~p4.releaseChord2;

~p4.playChord3;
~p4.releaseChord3;

~p4.playChord4;
~p4.releaseChord4;



~p4.tremolo.setFrequency((136/60)*2);
~p4.tremolo.setWaveform('sine');
~p4.tremolo.setRange(0, 0.7);
~p4.envelope.setSustainLevel(1);


~r1 = 60.midicps;

~p4.subtractive.playNote(~r1);
~p4.subtractive.playNote(~r1 * (3/2));
~p4.subtractive.playNote(~r1 * (5/4) * 2);
~p4.subtractive.playNote(~r1 * (15/8));


~r2 = 76.midicps;

~p4.subtractive.playNote(~r2 * (3/4) * 0.5);
~p4.subtractive.playNote(~r2 * (5/4) * 0.5);
~p4.subtractive.playNote(~r2 * 5/6);
~p4.subtractive.playNote(~r2);


~p4.subtractive.setLFO1Freq((136/60)*4);

136/60

~p4.mixer.tglMute(4);

	~p4.subtractive.panic

~p4.subtractive.setReleaseTime(1.5);
~p4.subtractive.setLFO1ReleaseTime(5);

~p4.mixer.setPanBal(2, -0.5);

~p4.subtractive.writePreset('plague4-2');


s.meter
Server.killAll;