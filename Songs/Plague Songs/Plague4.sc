/*
Monday, April 5th 2021
PlagueSong4.sc
prm
*/

Plague4 : IM_Module {

	var server, <isLoaded;

	var <kick, <shard, <terr, <gran, <decapitator, <subtractive;
	var kickIn, shardIn, terrIn, granIn;
	var <granEQ;

	var <seq, midiDict, subOnArray, subOffArray;
	var subOnArray9, subOffArray9, subOnArray10, subOffArray10;
	var subOnArray11, subOffArray11, subOnArray12, subOffArray12;

	var <shardEnv, <tremolo, <envelope, <arbharEnv, <lubadhEnv;
	var shardEnvOut, tremoloOut, envelopeOut, arbharEnvOut, lubadhEnvOut;

	var <cvDict;

	var midiFuncsLoaded;

	var chord1, chord2, chord3, chord4;

	var scale;

	*new {
		|
		outBus = 0, sequencer, granulator, inArray, outArray,
		send0Bus, send1Bus, send2Bus, send3Bus,
		relGroup, addAction = 'addToHead'
		|
		^super.new(5, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(inArray, outArray, sequencer, granulator);
	}

	prInit { | inArray, outArray, sequencer, granulator |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			midiFuncsLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			this.prMakeChords;

			mixer.muteMaster;
			server.sync;
			seq = sequencer.uid;

			kickIn = inArray[0];
			shardIn = inArray[1];
			terrIn = inArray[2];
			granIn = inArray[3];

			shardEnvOut = outArray[1];
			tremoloOut = outArray[2];
			envelopeOut = outArray[3];
			arbharEnvOut = outArray[4];
			lubadhEnvOut = outArray[5];

			kick = IM_HardwareIn.new(kickIn, mixer.chanMono(0), group, \addToHead);
			while({ try { kick.isLoaded } != true }, { 0.001.wait; });
			shard = IM_HardwareIn.new(shardIn, mixer.chanMono(1), group, \addToHead);
			while({ try { shard.isLoaded } != true }, { 0.001.wait; });
			terr = IM_HardwareIn.new(terrIn, mixer.chanMono(2), group, \addToHead);
			while({ try { terr.isLoaded } != true }, { 0.001.wait; });
			granEQ = Equalizer.newMono(mixer.chanStereo(3), group, \addToHead);
			while({ try { granEQ.isLoaded } != true }, { 0.001.wait; });
			gran = IM_HardwareIn.new(granIn, granEQ.inBus, group, \addToHead);
			while({ try { gran.isLoaded } != true }, { 0.001.wait; });


			decapitator = Decapitator.newStereo(mixer.chanStereo(4), nil, nil, nil, nil, group, \addToHead);
			while({ try { decapitator.isLoaded } != true }, { 0.001.wait; });
			subtractive = SubJuno.new(decapitator.inBus, nil, nil, nil, nil, group, \addToHead);
			while({ try { subtractive.isLoaded } != true }, { 0.001.wait; });


			server.sync;

			shardEnv = CV_Gate.new(shardEnvOut, group, \addToHead);
			while( { try { shardEnv.isLoaded } != true }, { 0.001.wait; });
			tremolo = CV_LFO.new(tremoloOut, (136/60)*2, 'revSaw', 0, 1, group, \addToHead);
			while({ try { tremolo.isLoaded } != true }, { 0.001.wait; });
			envelope = CV_EnvADSR.new(envelopeOut, group, \addToHead);
			while( { try { envelope.isLoaded } != true }, { 0.001.wait; });
			arbharEnv = CV_EnvADSR.new(arbharEnvOut, group, \addToHead);
			while( { try { arbharEnv.isLoaded } != true }, { 0.001.wait; });
			//lubadhEnv = CV_EnvADSR.new(lubadhEnvOut, group, \addToHead);
			lubadhEnv = CV_Gate.new(lubadhEnvOut, group, \addToHead);
			while( { try { lubadhEnv.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prSetInitialParameters;
			this.prMakeMIDIFuncs(granulator);

			mixer.unMuteMaster;

			isLoaded = true;
		}
	}

	prSetInitialParameters {
		5.do({ | chan | mixer.setPreVol(chan, -9); });
		5.do({ | chan | mixer.mute(chan); });

		// reverb:
		mixer.setSendVol(0, 0, -18);
		mixer.setSendVol(1, 0, 0);
		mixer.setSendVol(2, 0, -12);
		mixer.setSendVol(3, 0, -9);
		mixer.setSendVol(4, 0, -15);

		// delay:
		mixer.setSendVol(2, 3, 0);

		decapitator.loadPreset('plague4');
		subtractive.readPreset('plague4-2');

		tremolo.setRange(0, 0.75);

		envelope.setAttackTime(3);
		envelope.setReleaseTime(3);
		envelope.setSustainLevel(1);

		arbharEnv.setAttackTime(0.01);
		arbharEnv.setReleaseTime(0.75);
		arbharEnv.setSustainLevel(1);

		/*
		lubadhEnv.setAttackTime(3);
		lubadhEnv.setSustainLevel(1);
		lubadhEnv.setReleaseTime(0.75);
		*/
	}

	prMakeMIDIFuncs { | granulator |
		var base = 136/60;

		midiDict = IdentityDictionary.new;

		this.prMakeSubtractiveMIDIFuncs;

		midiDict[\section1] = MIDIFunc.noteOn({ this.triggerSection1(granulator); }, 1, 2, seq);
		midiDict[\section2] = MIDIFunc.noteOn({ this.triggerSection2(granulator); }, 2, 2, seq);
		midiDict[\section3] = MIDIFunc.noteOn({ this.triggerSection3(granulator); }, 3, 2, seq);
		midiDict[\section4] = MIDIFunc.noteOn({ this.triggerSection4(granulator); }, 4, 2, seq);
		midiDict[\section5] = MIDIFunc.noteOn({ this.triggerSection5(granulator); }, 5, 2, seq);
		midiDict[\section6] = MIDIFunc.noteOn({ this.triggerSection6(granulator); }, 6, 2, seq);
		midiDict[\section7] = MIDIFunc.noteOn({ this.triggerSection7(granulator); }, 7, 2, seq);

		midiDict[\chord1On] = MIDIFunc.noteOn({ this.playChord1 }, 1, 1, seq);
		midiDict[\chord1Off] = MIDIFunc.noteOff({ this.releaseChord1; }, 1, 1, seq);
		midiDict[\chord2On] = MIDIFunc.noteOn({ this.playChord2 }, 2, 1, seq);
		midiDict[\chord2Off] = MIDIFunc.noteOff({ this.releaseChord2; }, 2, 1, seq);
		midiDict[\chord3On] = MIDIFunc.noteOn({ this.playChord3 }, 3, 1, seq);
		midiDict[\chord3Off] = MIDIFunc.noteOff({ this.releaseChord3; }, 3, 1, seq);
		midiDict[\chord4On] = MIDIFunc.noteOn({ this.playChord4 }, 4, 1, seq);
		midiDict[\chord5Off] = MIDIFunc.noteOff({ this.releaseChord4; }, 4, 1, seq);

		cvDict = IdentityDictionary.new;
		cvDict[\shardOn] = MIDIFunc.noteOn({ if( shardEnv.gateIsHigh == false, { shardEnv.makeGate });}, 1, 0, seq);
		cvDict[\shardOff] = MIDIFunc.noteOff({ shardEnv.releaseGate; }, 1, 0, seq);
		cvDict[\envOn] = MIDIFunc.noteOn({ if(envelope.isTriggered == false, { envelope.trigger; }); }, 2, 0, seq);
		cvDict[\envOff] = MIDIFunc.noteOff({ envelope.release }, 2, 0, seq);

		cvDict[\tremolo1] = MIDIFunc.noteOn({ tremolo.setFrequency(base); }, 3, 0, seq);
		cvDict[\tremolo2] = MIDIFunc.noteOn({ tremolo.setFrequency(base*4/3); }, 4, 0, seq);
		cvDict[\tremolo3] = MIDIFunc.noteOn({ tremolo.setFrequency(base*2); }, 5, 0, seq);
		cvDict[\tremolo4] = MIDIFunc.noteOn({ tremolo.setFrequency(base*8/3); }, 6, 0, seq);
		cvDict[\tremolo5] = MIDIFunc.noteOn({ tremolo.setFrequency(base*3); }, 7, 0, seq);
		cvDict[\tremolo6] = MIDIFunc.noteOn({ tremolo.setFrequency(base*4); }, 8, 0, seq);

		cvDict[\arbharEnv] = MIDIFunc.noteOn({ if(arbharEnv.isTriggered == false, { arbharEnv.trigger; });}, 9, 0, seq);
		cvDict[\arbharEnv] = MIDIFunc.noteOff({ arbharEnv.release; }, 9, 0, seq);
		cvDict[\lubadhEnv] = MIDIFunc.noteOn({ if(lubadhEnv.gateIsHigh == false, { lubadhEnv.makeGate;}); }, 10, 0, seq);
		cvDict[\lubadhEnv] = MIDIFunc.noteOff({ lubadhEnv.releaseGate; }, 10, 0, seq);

		//this.prAddSubtractiveChordsFuncs;

		midiFuncsLoaded = true;

	}

	prFreeMIDIFuncs {
		midiDict.do({ | i | i.free; });
		subOnArray.do({ | i | i.free; });
		subOffArray.do({ | i | i.free; });
		cvDict.do({ | i | i.free; });
	}

	prMakeSubtractiveMIDIFuncs {

		scale = Scale.chromatic(\just);

		subOnArray = Array.fill(128, { | i |
			MIDIFunc.noteOn({ | vel | subtractive.playNote(i.midicps, vel.ccdbfs);
		}, i, 13, seq); });
		subOffArray = Array.fill(128, { | i |
			MIDIFunc.noteOff({ subtractive.releaseNote(i.midicps); }, i, 13, seq); });

		subOnArray9 = Array.fill(128, { | i |
			MIDIFunc.noteOn({ | vel | subtractive.playNote(scale.degreeToFreq(i, 0.midicps, 0), vel.ccdbfs);
		}, i, 9, seq); });
		subOffArray9 = Array.fill(128, { | i |
			MIDIFunc.noteOff({ subtractive.releaseNote(scale.degreeToFreq(i, 0.midicps, 0));
		}, i, 9, seq); });
		subOnArray10 = Array.fill(128, { | i |
			MIDIFunc.noteOn({ | vel | subtractive.playNote(scale.degreeToFreq(i-4, 4.midicps, 0), vel.ccdbfs);
		}, i, 10, seq); });
		subOffArray10 = Array.fill(128, { | i |
			MIDIFunc.noteOff({ subtractive.releaseNote(scale.degreeToFreq(i-4, 4.midicps, 0));
		}, i, 10, seq); });
		subOnArray11 = Array.fill(128, { | i |
			MIDIFunc.noteOn({ | vel | subtractive.playNote(scale.degreeToFreq(i-1, 1.midicps, 0), vel.ccdbfs);
		}, i, 11, seq); });
		subOffArray11 = Array.fill(128, { | i |
			MIDIFunc.noteOff({ subtractive.releaseNote(scale.degreeToFreq(i-1, 1.midicps, 0));
		}, i, 11, seq); });
		subOnArray12 = Array.fill(128, { | i |
			MIDIFunc.noteOn({ | vel | subtractive.playNote(scale.degreeToFreq(i-9, 9.midicps, 0), vel.ccdbfs);
		}, i, 12, seq); });
		subOffArray12 = Array.fill(128, { | i |
			MIDIFunc.noteOff({ subtractive.releaseNote(scale.degreeToFreq(i-9, 9.midicps, 0));
		}, i, 12, seq); });

	}

	prMakeChords {
		var r1, r2, r3, r4;
		//var c1, c2, c3, c4;
		r1 = 60.midicps;
		r2 = 64.midicps;
		r3 = 61.midicps;
		r4 = 69.midicps;

		chord1 = [r1, (r1*(4/3)), (r1*(3/2)), (r1*(5/4)*2)];
		chord2 = [r2, (r2*(5/4)), (r2*(3/2)), (r2*(15/8))];
		chord3 = [r3, (r3*(3/2)), (r3*(16/9)), (r3*(6/5)*2)];
		chord4 = [r4, (r4*(6/5)*0.5), (r4*(9/8)), (r4*(3/2))];
	}

	//////// public functions:
	free {
		this.prFreeMIDIFuncs;
		kick.free; shard.free; terr.free; gran.free; decapitator.free; subtractive.free;
		kickIn.free; shardIn.free; terrIn.free; granIn.free;
		this.freeModule;
	}

	playChord1 {
		subtractive.playNote(chord1[0]);
		subtractive.playNote(chord1[1]);
		subtractive.playNote(chord1[2]);
		subtractive.playNote(chord1[3]);
	}

	releaseChord1 {
		subtractive.releaseNote(chord1[0]);
		subtractive.releaseNote(chord1[1]);
		subtractive.releaseNote(chord1[2]);
		subtractive.releaseNote(chord1[3]);
	}

	playChord2 {
		subtractive.playNote(chord2[0]);
		subtractive.playNote(chord2[1]);
		subtractive.playNote(chord2[2]);
		subtractive.playNote(chord2[3]);
	}

	releaseChord2 {
		subtractive.releaseNote(chord2[0]);
		subtractive.releaseNote(chord2[1]);
		subtractive.releaseNote(chord2[2]);
		subtractive.releaseNote(chord2[3]);
	}

	playChord3 {
		subtractive.playNote(chord3[0]);
		subtractive.playNote(chord3[1]);
		subtractive.playNote(chord3[2]);
		subtractive.playNote(chord3[3]);
	}

	releaseChord3 {
		subtractive.releaseNote(chord3[0]);
		subtractive.releaseNote(chord3[1]);
		subtractive.releaseNote(chord3[2]);
		subtractive.releaseNote(chord3[3]);
	}

	playChord4 {
		subtractive.playNote(chord4[0]);
		subtractive.playNote(chord4[1]);
		subtractive.playNote(chord4[2]);
		subtractive.playNote(chord4[3]);
	}

	releaseChord4 {
		subtractive.releaseNote(chord4[0]);
		subtractive.releaseNote(chord4[1]);
		subtractive.releaseNote(chord4[2]);
		subtractive.releaseNote(chord4[3]);
	}

	triggerSection1 { | granulator |
		5.do({ | chan | mixer.unMute(chan); });

		5.do({| chan | mixer.setSendVol(chan, 1, -140); });
		//// feedback amount:
		mixer.setSendVol(1, 2, -9);

		// granulator amount:
		mixer.setSendVol(3, 1, 0);
		mixer.setSendVol(0, 1, 0);

		mixer.setSendVol(0, 3, 0);

		granulator.setGrainEnvelope('hanning');
		granulator.setGrainDur(1, 2.5);
		granulator.setTrigRate(0.5);
		granulator.setRate(1, 1);
		granulator.setSync(0);

		arbharEnv.setAttackTime(0.01);
		arbharEnv.setReleaseTime(0.75);
		arbharEnv.setSustainLevel(1);
		granEQ.setHighPassCutoff(20);

		granulator.mixer.setSendVol(0, -28);
	}

	triggerSection2 { | granulator |
		// mute out kick and terrarium:
		mixer.mute(0); mixer.mute(2);
		mixer.unMute(1); mixer.unMute(3); mixer.unMute(4);

		// feedback amount:
		mixer.setSendVol(1, 2, -1.5);

		// granulator:
		mixer.setSendVol(0, 1, -inf);
		mixer.setSendVol(1, 1, -12);
		mixer.setSendVol(2, 1, -inf);
		mixer.setSendVol(3, 1, -6);
		mixer.setSendVol(4, 1, -inf);
		granulator.setPosMod(0.5);
		granulator.setGrainEnvelope('perc');
		granulator.setGrainDur(0.75, 2);
		granulator.setTrigRate(3);
		granulator.setRate(1, 1);
		granulator.setSync(0);

	}

	triggerSection3 { | granulator |

		// mute out all but lubadh, arbhar:
		mixer.mute(0); mixer.mute(1); mixer.mute(2); mixer.mute(4);
		// live: feedback, arbhar:
		mixer.unMute(3);

		// feedback amount:
		mixer.setSendVol(1, 2, -6);

		// granulator:
		mixer.setSendVol(0, 1, -140);
		mixer.setSendVol(1, 1, -3);
		mixer.setSendVol(2, 1, -140);
		mixer.setSendVol(3, 1, 0);
		mixer.setSendVol(4, 1, -140);
		granulator.setPosMod(0.5);
		granulator.setGrainEnvelope('perc');
		granulator.setTrigRate(12);
		granulator.setRate(2, 2);
		granulator.setSync(0);

		mixer.setSendVol(0, 3, -140);

		granEQ.setHighPassCutoff(4500);

	}

	triggerSection4 { | granulator |
		// all live:
		5.do({ | chan | mixer.unMute(chan); });
		//// reverbs:
		mixer.setSendVol(0, 0, -36);
		mixer.setSendVol(1, 0, -18);
		mixer.setSendVol(2, 0, -inf);
		mixer.setSendVol(3, 0, -inf);
		mixer.setSendVol(4, 0, -18);
		// feedback amount:
		mixer.setSendVol(1, 2, -7.5);
		// granulator:
		granulator.setGrainEnvelope('expodec');
		granulator.setTrigRate((136/60)*4);
		granulator.setGrainDur(0.25, 0.25);
		granulator.setSync(1);
		granulator.setPosMod(0);
		granulator.setRate(2, 2);
		granEQ.setHighPassCutoff(20);
	}

	triggerSection5 { | granulator |
		// not sure about mixer yet, but you'll need to figure it out!
		mixer.setSendVol(1, 2, -inf);
		mixer.setSendVol(4, 1, -9);
		granulator.setGrainEnvelope('rexpodec');
		granulator.setTrigRate(40);
		granulator.setGrainDur(0.001, 0.3);
		granulator.setRate(0.5, 0.5);
		granulator.setSync(0);
	}

	triggerSection6 { | granulator |
		mixer.setSendVol(1, 2, 0);
		granulator.setGrainEnvelope('percRev');
		mixer.setSendVol(4, 1, 0);
		granulator.setTrigRate(7);
		granulator.setGrainDur(0.001, 0.3);
		granulator.setRate(0.5, 0.5);
		granulator.setSync(0);
	}

	triggerSection7 { | granulator |
		mixer.setSendVol(1, 2, -18);
		granulator.setGrainEnvelope('perc');
		mixer.setSendVol(4, 1, 0);
		granulator.setTrigRate(20);
		granulator.setGrainDur(1, 1.5);
		granulator.setRate(0.5, 0.5);
		granulator.setSync(0);
	}

}