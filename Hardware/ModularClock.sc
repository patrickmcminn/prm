/*
Monday, May 27th 2019
ModularClock.sc
prm

Class and SynthDef for sending clock signals to the modular
*/

ModularClock {

  var <isLoaded, server;
  var <tempo, <division;
  var <isPlaying;
  var synth;
  var out, rg, addAct;

  *new { | outBus = 0, temp = 120, div = 24, relGroup, addAction = 'addToHead' |
    ^super.new.prInit(outBus, temp, div, relGroup, addAction);
  }

  prInit { | outBus = 0, temp = 120, div = 16, relGroup, addAction |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      isPlaying = false;
      tempo = temp;
      division = div;
      isPlaying = false;
      out = outBus;
      rg = relGroup;
      addAct = addAction;

      this.prAddSynthDef;

      server.sync;
      isLoaded = true;
    }
  }

  prAddSynthDef {
    SynthDef(\prm_modularClock, {
      | outBus = 0, freq = 1, amp = 0.5 |
      var sig = Impulse.ar(freq) * amp;
      Out.ar(outBus, sig);
    }).add;
  }

  //////// public functions:
  free {
    this.stop;
  }

  togglePlay {
    if( isPlaying == true, { this.stop }, { this.play });
  }

  play {
    var freq = (tempo/60) * division;
    synth = Synth(\prm_modularClock, [\outBus, out, \freq, freq], rg, addAct);
    isPlaying = true;
  }

  stop {
    synth.free;
    isPlaying = false;
  }

  setTempo { | temp = 120 |
    tempo = temp;
    if( isPlaying == true, { synth.set(\freq, (tempo/60) * division) });
  }

  setDivision { | div = 16 |
    division = div;
    if( isPlaying == true, { synth.set(\freq, (tempo/60) * division) });
  }



}