/*
Thursday, December 3rd 2015
FeedbackSynth.sc
prm
driving between Portland, ME and Ithaca, NY
*/

FeedbackSynth : IM_Module {

  var server, <isLoaded;
  var <sampler;
  var <attackTime, <decayTime, <sustainLevel, <releaseTime;
  var <filterCutoff;
  var sequencerDict, sequencerClock, <tempo;

  *new { | outBus = 0, send0Bus = 0, send1Bus = 0, send2Bus = 0, send3Bus = 0, relGroup = nil, addAction = 'addToTail' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    var path;
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      sequencerDict = IdentityDictionary.new;
      path = "~/Library/Application Support/SuperCollider/Extensions/prm/Instruments/FeedbackSynth/samples/feedbackSynthSample.wav";
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      sampler = SamplePlayer.newStereo(mixer.chanStereo(0), path, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });
      server.sync;

      attackTime = 0.05;
      decayTime = 0;
      sustainLevel = 0;
      releaseTime = 0.05;

      filterCutoff = 5720;
      isLoaded = true;
    }
  }

  /////// public functions:

  free {
    sampler.free;
    sequencerDict.free;
    this.freeModule;
  }

  /////// root note: Ab 1
  playNote { | freq = 220, vol = 0 |
    var rate = freq/44.midicps;
    sampler.playSampleSustaining(freq.asSymbol, vol, rate);
  }

  releaseNote { | freq |
    sampler.releaseSampleSustaining(freq.asSymbol);
  }

  setAttackTime { | attack = 0.05 |
    attackTime = attack;
    sampler.setAttackTime(attackTime);
  }
  setDecayTime { | decay = 0 |
    decayTime = decay;
    sampler.setDecayTime(decay);
  }
  setSustainLevel { | sustain = 1 |
    sustainLevel = sustain;
    sampler.setSustainLevel(sustainLevel);
  }
  setReleaseTime { | release = 0.05 |
    releaseTime = release;
    sampler.setReleaseTime(releaseTime);
  }

  setFilterCutoff { | cutoff = 5720 |
    filterCutoff = cutoff;
    sampler.setFilterCutoff(cutoff);
  }

  //////// sequencer:

  makeSequence { | name |
    fork {
      sequencerDict[name] = IM_PatternSeq.new(name, group, \addToHead);
      sequencerDict[name].stop;
      server.sync;
      //sequencerDict[name].addKey(\instrument, \prm_Sampler_Stereo_OneShot);
      sequencerDict[name].addKey(\instrument, \prm_SamplePlayer_Stereo_ADSR);
      sequencerDict[name].addKey(\outBus, mixer.chanStereo(0));
      //sequencerDict[name].addKey(\attackTime, Pfunc({ sampler.attackTime }));
      //sequencerDict[name].addKey(\decayTime, Pfunc({ sampler.decayTime }));
      //sequencerDict[name].addKey(\sustainLevel, Pfunc({ sampler.sustainLevel }));
      //sequencerDict[name].addKey(\releaseTime, Pfunc({ sampler.releaseTime }));
      //sequencerDict[name].addKey(\filterCutoff, Pfunc({ sampler.filterCutoff }));
      sequencerDict[name].addKey(\amp, 1);
      sequencerDict[name].addKey(\freq, 1);
      sequencerDict[name].addKey(\rate, Pfunc( { (Pkey(\freq)/44.midicps) }));
    };
  }

  addKey {  | name, key, action |
    sequencerDict[name].addKey(key, action);
  }

  playSequence { | name, clock = 'internal', quant = 'nil' |
    var playClock;
    if( clock == 'internal', { playClock = sequencerClock }, { playClock = clock });
    sequencerDict[name].play(playClock);
  }

  resetSequence { | name | sequencerDict[name].reset; }
  stopSequence { | name | sequencerDict[name].stop; }
  pauseSequence { | name | sequencerDict[name].pause }
  resumeSequence { | name | sequencerDict[name].resume; }
  isSequencePlaying { | name | ^sequencerDict[name].isPlaying }
  setSequenceQuant { | name, quant = 0 | sequencerDict[name].setQuant(quant) }

  setSequencerClockTempo { | bpm = 60 |
    var bps = bpm/60;
    tempo = bps;
    sequencerClock.tempo = tempo;
  }


}