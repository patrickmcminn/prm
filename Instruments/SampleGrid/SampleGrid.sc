SampleGrid : IM_Processor {

	var server, <isLoaded;
	var <isRecording;
	var <numSlots, <samplerArray, <parameterArray;

	var <masterPresetDict;

	var <presetPath, presetDict, clipboard;

	var <recordPath, <recordBufferStereo, <recordBufferMono, <bufferLength, recSynth;

	var recFileName;

	*new { | outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
		^super.new(2, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
	}

	prInit {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			this.prAddSynthDef;

			server.sync;

			isRecording = false;
			numSlots = 16;
			bufferLength = 2097152;

			samplerArray = Array.fill(numSlots, { SampleGrid_Voice.new(mixer.chanStereo, group, \addToHead); });
			while({ try { samplerArray[15].isLoaded } != true}, { 0.001.wait; });
			parameterArray = Array.fill(numSlots, { Dictionary.new; });
			parameterArray.do({ | dict, i | dict[\SampleName] = "sample"++i; });

			masterPresetDict = IdentityDictionary.new;
			presetPath = "~/Library/Application Support/SuperCollider/Extensions/prm/Instruments/SampleGrid/Sample Grid Presets.scd".standardizePath;
			recordPath = "~/Library/Application Support/SuperCollider/Extensions/prm/Instruments/SampleGrid/Recorded Samples/".standardizePath;

			recordBufferStereo = Buffer.alloc(server, 2097152, 2);
			recordBufferMono = Buffer.alloc(server, 2097152, 1);


			this.prPopulatePresetDictionaries;

			isLoaded = true;
		}
	}

	prAddSynthDef {
		SynthDef(\prm_SampleGrid_Record_Stereo, { | inBus, buffer |
			var input, record;
			input = InFeedback.ar(inBus, 2);
			DiskOut.ar(buffer, input);
		}).add;

		SynthDef(\prm_SampleGrid_Record_Mono, { | inBus, buffer |
			var input, record;
			input = InFeedback.ar(inBus, 1);
			DiskOut.ar(buffer, input);
		}).add;
	}

	prMakePresetDictionary { | dict, sampler |
		//dict[\SampleName]
		dict[\SamplePath] = sampler.samplePath;
		dict[\SampleVol] = sampler.sampleVol;
		dict[\PlayMode] = sampler.playMode;
		dict[\LowPassCutoff] = sampler.lowPassCutoff;
		dict[\HighPassCutoff] = sampler.highPassCutoff;
		dict[\StartPos] = sampler.startPos;
		dict[\EndPos] = sampler.endPos;
		dict[\SamplePan] = sampler.samplePan;
		dict[\AttackTime] = sampler.attackTime;
		dict[\DecayTime] = sampler.decayTime;
		dict[\SustainLevel] = sampler.sustainLevel;
		dict[\ReleaseTime] = sampler.releaseTime;
		dict[\Rate] = sampler.rate;
		dict[\GrainPanLow] = sampler.panLow;
		dict[\GrainPanHigh] = sampler.panHigh;
		dict[\GrainDurLow] = sampler.grainDurLow;
		dict[\GrainDurHigh] = sampler.grainDurHigh;
		dict[\TrigRate] = sampler.trigRate;
	}

	prPopulatePresetDictionaries {
		numSlots = samplerArray.size;
		numSlots.do({ | i | this.prMakePresetDictionary(parameterArray[i], samplerArray[i]); });
	}

	prBuildPresetDict {
		var presetArray = SemiColonFileReader.read(this.presetPath);
		presetArray.do({ | array |
			masterPresetDict[array[0].asSymbol] = Array.fill(numSlots, { | i | Dictionary.newFrom(array[i+1].interpret) });
		});
	}


	prSetAllParameters { | dictArray |
		dictArray.do({ | dict, i |
			this.loadSampleByPath(i, dict[\SamplePath]);
			this.setPos(i, dict[\StartPos], dict[\EndPos]);
			this.setPlayMode(i, dict[\PlayMode]);
			this.setSampleVol(i, dict[\SampleVol]);
			this.setSamplePan(i, dict[\SamplePan]);
			this.setAttackTime(i, dict[\AttackTime]);
			this.setDecayTime(i, dict[\DecayTime]);
			this.setSustainLevel(i, dict[\SustainLevel]);
			this.setReleaseTime(i, dict[\ReleaseTime]);
			this.setLowPassCutoff(i, dict[\LowPassCutoff]);
			this.setHighPassCutoff(i, dict[\HighPassCutoff]);
			this.setRate(i, dict[\Rate]);
			this.setGrainPan(i, dict[\GrainPanLow], dict[\GrainPanHigh]);
			this.setGrainDur(i, dict[\GrainDurLow], dict[\GrainDurHigh]);
			this.setTrigRate(i, dict[\TrigRate]);
		});
	}

	prSetSlotParameters { | slot |
		var dict = parameterArray.at(slot);
		this.loadSampleByPath(slot, dict[\SamplePath]);
		this.setPos(slot, dict[\StartPos], dict[\EndPos]);
		this.setPlayMode(slot, dict[\PlayMode]);
		this.setSampleVol(slot, dict[\SampleVol]);
		this.setSamplePan(slot, dict[\SamplePan]);
		this.setAttackTime(slot, dict[\AttackTime]);
		this.setDecayTime(slot, dict[\DecayTime]);
		this.setSustainLevel(slot, dict[\SustainLevel]);
		this.setReleaseTime(slot, dict[\ReleaseTime]);
		this.setLowPassCutoff(slot, dict[\LowPassCutoff]);
		this.setHighPassCutoff(slot, dict[\HighPassCutoff]);
		this.setRate(slot, dict[\Rate]);
		this.setGrainPan(slot, dict[\GrainPanLow], dict[\GrainPanHigh]);
		this.setGrainDur(slot, dict[\GrainDurLow], dict[\GrainDurHigh]);
		this.setTrigRate(slot, dict[\TrigRate]);
	}

	//////// public functions:

	free {
		samplerArray.do({ | samp | samp.free; });
		this.freeProcessor;
	}

	copySlot { | slot |
		this.prMakePresetDictionary(parameterArray[slot], samplerArray[slot]);
		clipboard = parameterArray[slot];
	}

	pasteSlot { | slot |
		if( clipboard.notNil, {
			parameterArray[slot] = clipboard;
			this.prSetSlotParameters(slot);
		});
	}

	//////// recording:

	recordSampleStereo {
		if( isRecording == false, {
			recFileName = recordPath++"sampleRec"++1000000.rand++".aiff";
			recordBufferStereo.write(recFileName, "aiff", "int16", 0, 0, true);
			//server.sync;
			recSynth = Synth(\prm_SampleGrid_Record_Stereo, [\inBus, inBus, \buffer, recordBufferStereo], group, \addToHead);
			isRecording = true;
			recFileName.postln;
		}, { "already recording".postln; });
	}

	stopRecordingStereo {
		{
			recSynth.free;
			recordBufferStereo.close;
			recordBufferStereo.free;
			server.sync;
			recordBufferStereo = Buffer.alloc(server, 2097152, 2);
			isRecording = false;
		}.fork;
	}

	stopRecordingLoadSlot { | slot = 0, monoOrStereo = 'stereo' |
		if( monoOrStereo == 'stereo', { this.stopRecordingStereo }, { this.stopRecordingMono });
		this.loadRecordedToSlot(slot);

	}

	recordSampleMono {
		if( isRecording == false, {
			recFileName = recordPath++"sampleRec"++1000000.rand++".aiff";
			recordBufferMono.write(recFileName, "aiff", "int16", 0, 0, true);
			recSynth = Synth(\prm_SampleGrid_Record_Mono, [\inBus, inBus, \buffer, recordBufferMono], group, \addToHead);
			isRecording = true;
		}, { "already recording".postln; });
	}

	stopRecordingMono {
		{
			recSynth.free;
			recordBufferMono.close;
			recordBufferMono.free;
			server.sync;
			recordBufferMono = Buffer.alloc(server, 2097152, 1);
			isRecording = false;
		}.fork;
	}

	loadRecordedToSlot { | slot |
		this.loadSampleByPath(slot, recFileName);
	}

	setRecordPath { | path | recordPath = path; }

	getSampleName { | slot | ^parameterArray[slot][\SampleName]; }
	setSampleName { | slot, name | parameterArray[slot][\SampleName] = name; }
	setSampleNameGUI { | slot |
		{
			var window, colorPicker, text, colorText, textField, goButton;
			window = Window.new("sampler name at slot"+slot, Rect((100.rand+400),(100.rand+400),225,50));
			text = StaticText.new(window).string_("sample name:");
			textField = TextField.new(window);
			goButton = Button.new(window).action_({
				[textField.string].postln;
				this.setSampleName(slot, textField.string);
				window.close;
			});
			goButton.string = "Add Name";
			window.layout = VLayout(VLayout(text, textField, goButton));
			window.front;
		}.fork(AppClock);
	}

	/////////// convenience functions:

	loadSample { | slot | samplerArray[slot].loadSample; }
	loadSampleByPath { | slot, path | samplerArray[slot].loadSampleByPath(path); }
	setPosGUI { | slot | samplerArray[slot].setPosGUI(parameterArray[slot][\name]); }

	playSample { | slot, vol = -3 | samplerArray[slot].playSample(vol); }
	releaseSample { | slot | samplerArray[slot].releaseSample; }

	setPos { | slot, startPos, endPos | samplerArray[slot].setPos(startPos, endPos); }
	setStartPos { | slot, pos | samplerArray[slot].setStartPos(pos); }
	setEndPos { | slot, pos | samplerArray[slot].setEndPos(pos); }

	setPlayMode { | slot, mode | samplerArray[slot].setPlayMode(mode); }
	setSampleVol { | slot, vol = -6 | samplerArray[slot].setSampleVol(vol); }
	setSamplePan { | slot, pan = 0 | samplerArray[slot].setSamplePan(pan); }

	setAttackTime { | slot, time = 0.01 | samplerArray[slot].setAttackTime(time); }
	setDecayTime { | slot, time = 0.01 | samplerArray[slot].setDecayTime(time); }
	setSustainLevel { | slot, level = 1 | samplerArray[slot].setSustainLevel(level) }
	setReleaseTime { | slot, time = 0.01 | samplerArray[slot].setReleaseTime(time); }

	setLowPassCutoff { | slot, cutoff = 20000 | samplerArray[slot].setLowPassCutoff(cutoff); }
	setHighPassCutoff { | slot, cutoff = 20 | samplerArray[slot].setHighPassCutoff(cutoff); }

	setRate { | slot, rate = 1 | samplerArray[slot].setRate(rate); }

	setGrainPan { | slot, panLow = -0.5, panHigh = 0.5 | samplerArray[slot].setGrainPan(panLow, panHigh) }
	setGrainPanLow { | slot, pan = -0.5 | samplerArray[slot].setGrainPanLow(pan); }
	setGrainPanHigh { | slot, pan = 0.5 | samplerArray[slot].setGrainPanHigh(pan); }

	setGrainDur { | slot, durLow = 1, durHigh = 2 | samplerArray[slot].setGrainDur(durLow, durHigh) }
	setGrainDurLow { | slot, dur = 1 | samplerArray[slot].setGrainDurLow(dur); }
	setGrainDurHigh { | slot, dur = 2 | samplerArray[slot].setGrainDurHigh(dur); }

	setTrigRate { | slot, rate = 3 | samplerArray[slot].setTrigRate(rate); }

	////////// convenience getters:

	samplePath { | slot | ^samplerArray[slot].samplePath; }
	sampleVol { | slot | ^samplerArray[slot].sampleVol; }
	playMode { | slot | ^samplerArray[slot].playMode; }
	lowPassCutoff { | slot | ^samplerArray[slot].lowPassCutoff }
	highPassCutoff { | slot | ^samplerArray[slot].highPassCutoff }
	startPos { | slot | ^samplerArray[slot].startPos }
	endPos { | slot | ^samplerArray[slot].endPos }
	samplePan { | slot | ^samplerArray[slot].samplePan }
	attackTime { | slot | ^samplerArray[slot].attackTime }
	decayTime { | slot | ^samplerArray[slot].decayTime }
	sustainLevel { | slot | ^samplerArray[slot].sustainLevel }
	releaseTime { | slot | ^samplerArray[slot].releaseTime }
	rate { | slot | ^samplerArray[slot].rate; }
	panLow { | slot | ^samplerArray[slot].panLow; }
	panHigh { | slot | ^samplerArray[slot].panHigh }
	grainDurLow { | slot | ^samplerArray[slot].grainDurLow }
	grainDurHigh { | slot | ^samplerArray[slot].grainDurHigh }
	trigRate { | slot | ^samplerArray[slot].trigRate }
	buffer { | slot | ^samplerArray[slot].buffer }
	isPlaying { | slot | ^samplerArray[slot].isPlaying }
	monoOrStereo { | slot | ^samplerArray[slot].monoOrStereo }

	//////// presets:

	printPresetFilePath { this.presetPath.postln; }

	writePreset { | name |
		var array = Array.newClear(parameterArray.size);
		var file = File(this.presetPath, "a");
		this.prBuildPresetDict;
		this.prPopulatePresetDictionaries;
		if( masterPresetDict.includesKey(name),
			{ ^"preset already exists" },
			{
				// separate Dictionaries into Pairs in Arrays:
				parameterArray.do({ | dict, i | array[i] = dict.getPairs; });
				// make into psuedo-symbols so that when they're a String later they can be interpreted:
				array.do({ | subArray, i |
					subArray.do({ | item, i |
						if( item.isNumber == false, { subArray[i] = "'"++item++"'"; });
					});
				});
				file.write("\n"++name++";");
				//array.do({ | subArray | subArray.do({ | i |  file.write(array.asString++";"); });
				array.do({ | subArray |
					file.write("[");
					subArray.do({ | item | file.write(item.asString++","); });
					file.write("];");
				});
				file.close;
				this.prBuildPresetDict;
		});
	}

	readPreset { | name |
		var pArray;
		this.prBuildPresetDict;
		this.prSetAllParameters(masterPresetDict[name]);

	}

}

