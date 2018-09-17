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

  *new { | outBus = 0, send0Bus = 0, send1Bus = 0, send2Bus = 0, send3Bus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit;
  }

  prInit {
    var path;
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
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

  sweepFilter { | startFreq = 1000, endFreq = 100, time =  1 |
    sampler.sweepFilter(startFreq, endFreq, time);
    { filterCutoff = endFreq; }.defer(time);
  }

  //////// sequencer:

  makeSequence { | name |
    fork {
      sampler.makeSequence(name, 'sustaining');
      server.sync;
      sampler.addKey(name, \attackTime, attackTime);
      sampler.addKey(name, \decayTime, decayTime);
      sampler.addKey(name, \sustainLevel, sustainLevel);
      sampler.addKey(name, \releaseTime, releaseTime);
      sampler.addKey(name, \rate, Pkey(\freq)/44.midicps);
    };
  }

  addKey {  | name, key, action |
    sampler.addKey(name, key, action);
  }

  playSequence { | name, clock = 'internal', quant = 'nil' |
    sampler.playSequence(name, clock, quant);
  }


  resetSequence { | name | sampler.resetSequence(name); }
  stopSequence { | name | sampler.stopSequence(name); }
  pauseSequence { | name | sampler.pauseSequence(name); }
  resumeSequence { | name | sampler.resumeSequence(name); }
  isSequencePlaying { | name |^sampler.isSequencePlaying }
  setSequenceQuant { | name, quant = 0 | sampler.setSequenceQuant(name, quant); }

  setSequencerClockTempo { | bpm = 60 |
    sampler.setSequencerClockTempo(bpm);
  }
}