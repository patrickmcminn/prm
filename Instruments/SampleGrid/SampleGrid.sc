SampleGrid : IM_Processor {

	var server, <isLoaded;
	var <numSlots, <samplerArray, <parameterArray;

	var <masterPresetDict;

	var <presetPath, presetDict;

	*new { | outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup = nil, addAction = 'addToHead' |
		^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
	}

	prInit {
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			numSlots = 16;

			samplerArray = Array.fill(numSlots, { SampleGrid_Voice.new(mixer.chanStereo, group, \addToHead); });
			while({ try { samplerArray[15].isLoaded } != true}, { 0.001.wait; });
			parameterArray = Array.fill(numSlots, { Dictionary.new; });
			parameterArray.do({ | dict, i | dict[\SampleName] = "sample"++i; });

			masterPresetDict = IdentityDictionary.new;
			presetPath = "~/Library/Application Support/SuperCollider/Extensions/prm/Instruments/SampleGrid/Sample Grid Presets.scd".standardizePath;

			this.prBuildPresetDict;

			isLoaded = true;
		}
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
			this.loadSampleByPath(i, dict[\samplePath]);
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
		/*
		dictArray[0].postln;
		("
		//this.interpret("samplerArray[0].setGrainPanHigh(-0.5)");
		/*
		dictArray[0].keysValuesDo({ | key, value |
		var val;
		[key, value].postln;
		if( value.isNumber == false, { val = "'"++value++"'" }, { val = value; });
		//("this.set"++key++"(0, "++val++");").interpret;
		});
		*/
		//this.setGrainPanHigh(0, -0.5);
		/*
		dictArray.do({ | dict |
		dict.keysValuesDo({ | key, value |
		var val;
		//[key, value].postln;
		if( value.isNumber == false, { val = "'"++value++"'" }, { val = value; });
		("this.set"++key++"(0, "++val++")").interpret;
		});
		});
		*/
		*/
	}

	//////// public functions:

	free {

	}

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
				array.do({ | array | file.write(array.asString++";"); });
				file.close;
				this.prBuildPresetDict;
		});
	}

	readPreset { | name |
		var pArray;
		this.prBuildPresetDict;
		this.prSetAllParameters(masterPresetDict[name]);

	}


	getSampleName { | slot | ^parameterArray[slot][\SampleName]; }
	setSampleName { | slot, name | parameterArray[slot][\SampleName] = name; }
	setSampleNameGUI { | slot |
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
	}

	/////////// convenience functions:

	loadSample { | slot | samplerArray[slot].loadSample; }
	loadSampleByPath { | slot, path | samplerArray[slot].loadSampleByPath; }
	setPosGUI { | slot | samplerArray[slot].setPosGUI(parameterArray[slot][\name]); }

	playSample { | slot | samplerArray[slot].playSample }
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

	//////// presets:


}

