/*
July 13th 2017
Metronome.sc
prm
*/

Metronome :IM_Module {

  var <isLoaded;
  var server;
  var metro;

  var <downbeatFreq, <beatFreq, <subdivisionFreq;

  *new { | outBus, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      downbeatFreq = 660;
      beatFreq = 440;
      subdivisionFreq = 550;

      this.prAddSynthDef;

      server.sync;

      isLoaded = true;
    }
  }

  prAddSynthDef {
    SynthDef(\prm_metronome, {
      | outBus = 0, amp = 0.2, freq = 440 |
      var osc, env, sig;
      osc = SinOsc.ar(freq);
      env = EnvGen.kr(Env.perc(0.01, 0.1, 1), 1, doneAction: 2);
      sig = osc * env;
      sig = sig * amp;
      Out.ar(outBus, sig);
    }).add;
  }

  //////// public functions:

  free {
    this.freeModule;
    isLoaded = false;
  }

  playDownbeat {
    Synth(\prm_metronome, [\outBus, mixer.chanMono(0), \freq, downbeatFreq], group, \addToHead);
  }

  playBeat {
    Synth(\prm_metronome, [\outBus, mixer.chanMono(0), \freq, beatFreq], group, \addToHead);
  }

  playSubdivision {
     Synth(\prm_metronome, [\outBus, mixer.chanMono(0), \freq, subdivisionFreq], group, \addToHead);
  }

  setDownbeatFreq { | freq | downbeatFreq = freq }
  setBeatFreq { | freq | beatFreq = freq }
  setSubdivisionFreq { | freq | subdivisionFreq = freq }
}