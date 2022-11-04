
~ches = Chesapeake.new(~prm.audioIn, nil, ~prm.reverb.inBus, ~prm.granulator.inBus);
~ches.isLoaded;

~ches.dataBuf[\ph].plot


~prm.reverb.makeEditor;


x = 1.11234123;

x.trunc(0.01);


s.makeWindow


x = NetAddr.new(

~ches.data[\time].plot
~ches.data[\tide][6000]


~ches.tidalCSV[1]
~ches.dataBuf[\oxygen].plot

~ches.algalBloomEvent;
~ches.dataBus[\oxygen].get({ | val | val.postln });


~ches.bloomCounter
~ches.oxygenCounter
~ches.dataBus[\temp].get({ | val | val.postln });
~ches.severeBloomTrigger
~ches.oxygenCounter

~ches.dataBuf[\chlorophyll].plot


~ches.waterCSV[8]
~ches.dataBus[\chlorophyll].get({ | val | val.postln; });

~ches.algae.granulator.grainDurLow

~ches.algae.granulator.setGrainEnvelope(\hanning);
~ches.algae.granulator.setGrainDur(3, 5);

~ches.algae.granulator.grainEnvelope

~ches.oxygenCrisis
~ches.oxygenStable

~ches.dieOffEvent

~ches.algae.granulator.trigRate

~ches.algae.granulator.rateHigh
~ches.algae.granulator.setRate(2, 2);


~ches.playTidalData(1000);
~ches.playWaterQualityData(1000);

~ches.playTidalData(0);
~ches.playWaterQualityData(0);

~ches.time
~ches.date


~ches.mapRout[\bassTide].play;
~ches.mapRout[\chordTime].play;
~ches.mapRout[\chordChange].play;
~ches.mapRout[\tideMap].play;
~ches.mapRout[\oxygenInst].play;
~ches.mapRout[\turbTrem].play;
~ches.mapRout[\oxygenCounter].play;
~ches.mapRout[\crisisBassVol].play;
~ches.mapRout[\crisisBass].play;
~ches.mapRout[\bloomCounter].play;
~ches.mapRout[\bloomEvent].play;
~ches.mapRout[\chloroAlgae].play;
~ches.mapRout[\crisisVolume].play;
~ches.mapRout[\tempAlgae].play;
~ches.mapRout[\date].play;
~ches.mapRout[\time].play;

~ches.bass.playSequence(\basslinePt1);

~ches.algae.changePitchSet

~ches.wtrCrisis.synth.addKey(\dieOff, \amp, 0.5);
~ches.algae.granulator.setMix(0);

s.meter

SubJuno

isPlaying

~ches.algae.granulator.trigRate
~ches.dataBus[\tideNorm].get({ | val | val.postln });
~ches.dataBuf[\oxygen].plot
~ches.dataBuf[\chlorophyll].plot


~ches.bloomCounter


~ches.dataBus[\chlorophyll].get({ | val | val.postln });



Equalizer
~ches.dataBuf[\tideNorm].plot

~ches.mapRout[\chordChange].stop;
~ches.tide.releaseCurrentChord
~ches.mixer.mute(0);

~ches.tide.cloud.setWidth(0.01, 0.95);

~ches.bass.playSequence(\basslinePt1);

~ches.mixer.setPreVol(1, 0);
~ches.mixer.setVol(1, 0);

~ches.mixer.vol(1);
~ches.bass.mixer.vol

~ches.bass.playNote(220);
~ches.bass.releaseNote(220);

~ches.tide.tremolo.setVolLFODepth(0);

~ches.tide.tremolo.volLFODepth;

~ches.dataBus[\chlorophyllNorm].get({ | val | val.postln });
~ches.dataBuf[\chlorophyllNorm].plot

~ches.dataBus[\chlorophyll].get({ | val | val.postln });


~ches.dataBus[\turbidityNorm].get({ | val | val.postln });
~ches.dataBus[\ph].plot

~ches.dataBuf[\turbidityNorm].plot
~ches.dataBuf[\tide].plot

~ches.dataBuf[\tempNorm].plot

x = ~ches.normData[\turbidity]

b = Buffer.loadCollection(s, x);
b.plot

~ches.normData[\tide].plot

plot


~ches.qualRate

~ches.dataBuf[\tideNorm].plot
~ches.dataBus[\tideNorm].getSynchronous({ | val |  val.postln; });

~ches.dataBus[\oxygen].getSynchronous({ | val |  val.postln; });

~ches.tide.eq.setPeak2Freq(600);
~ches.tide.eq.setPeak2Gain(-5)

~ches.mapRout[\tideMap].play

~ches.mapRout[\chordChange].play



~ches.mapRout[\chordTime].stop;
~ches.mapRout[\chordChange].stop;
~ches.chordTime;

s.meter
~ches.mixer.setPreVol(0, -12);
~ches.mixer.setVol(0, -9);
~ches.mixer.tglMute(0);
~ches.tide.playMarkovChord

~ches.tide.cloud.addNote(60);
~ches.tide.cloud.removeNote(60);

~ches.waterCSV[8][1].interpret.asFloat


~ches.waterCSV[5][2976]

x = SubJuno.new(~prm.audioIn, ~prm.reverb.inBus);

s.meter

~ches.tide.tremolo.setVolLFODepth(1);
~ches.tide.tremolo.setVolLFOFreq(12);
x.mixer.tglMute;

x.mixer.setVol(-24);

x.mixer.setSendVol(0, 0, -15);

x.playNote(110);
x.releaseNote(110);

x.makeSequence('test');
x.addKey(\test, \note, Pseq([0, 2, 4, 5], inf))
x.addKey(\test, \dur, 0.5);
x.addKey(\test, \legato, 1);
x.addKey(\test, \octave, 3);

x.playSequence(\test);

x.stopSequence(\test);

x.makeSequence(\basslinePt1);
x.addKey(\basslinePt1, \note, Prand([-5, 2, 3, 9, 15, [-5, 7], [2, 9]], inf));
x.addKey(\basslinePt1, \legato, 1.2);
x.addKey(\basslinePt1, \dur, Prand([4, 8, 12, 16, 18, 8, 12, 14], inf));
x.addKey(\basslinePt1, \octave, Prand([3, 4], inf));

x.playSequence(\basslinePt1);

x.setReleaseTime(3);
x.setAttackTime(7);

x.setFilterCutoff(500);

x.setOsc1Waveform(\rect);
x.setOsc1PulseWidthLFO1Bottom(-0.3);
x.setOsc1PulseWidthLFO1Bottom(0.3);
x.setLFO1Freq(0.4);
x.setLFO1Waveform('noise');

x.setFilterEnvAttackRatio(2);
x.setFilterEnvAttackTime(3);
x.setFilterEnvSustainRatio(1);
x.setFilterEnvDecayTime(3);
x.setFilterDrive(1);
x.setFilterRes(0.8);
0.5.ampdb

x.writePreset(\chesapeakeBass);
x.readPreset(\chesapeakeBass);

x.mixer.vol(0);

x.mixer.setVol(0, -12);

~rout = {
	{
		~ches.dataBus[\tideNorm].get({ | val |
			var vol = if( val < 0.5, { -inf }, { val.linlin(0.5, 1, 0, 0.5).ampdb });
			x.mixer.setVol(vol);
			//vol.postln;
		});
		0.001.wait;
	}.loop;
}.fork;
~rout.stop;
~ches.dataBus[\tideNorm].getSynchronous({ | val |  val.postln; });
x.mixer.vol(0);


0.25.ampdb

~test =

~bus.getSynchronous({ | val | val.postln; });

0.26558193564415.linlin(0, 1, 0, 0.5).ampdb