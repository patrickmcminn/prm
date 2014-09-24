/*
Saturday, September 20th 2014
LFO.sc
prm
*/

LFO { // Low Frequency Oscillator naturally outputting in the range of -1 to 1

  var <isLoaded;
  var synth;
  var group, <outBus;

  *new { | freq = 1, waveform = 'sine', relGroup = nil, addAction = 'addToHead' |
    ^super.new.prInit(freq, waveform, relGroup, addAction);
  }

  prInit {  | freq = 1, waveform = 'sine', relGroup = nil, addAction = 'addToHead' |
    var server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDef;
      server.sync;
      outBus = Bus.control;
      group = Group.new(relGroup, addAction);
      server.sync;
      synth = Synth(\prm_LFO, [\outBus, outBus, \freq, freq], group, \addToHead);
      while({ synth == nil }, { 0.001.wait; });
      this.setWaveform(waveform);
      isLoaded = true;
    };
  }

  prAddSynthDef {
    SynthDef(\prm_LFO, {
      | outBus = 0, lfoWaveform = 0, freq = 1, lfoPulseWidth = 0.5 |
      var lfo, lfoSine, lfoSaw, lfoRevSaw, lfoRect, lfoNoise0, lfoNoise2;
      lfoSine = SinOsc.kr(freq);
      lfoSaw = LFSaw.kr(freq, 1);
      lfoRevSaw = LFSaw.kr(freq, 1) * -1;
      lfoRect = LFPulse.kr(freq, width: lfoPulseWidth);
      lfoNoise0 = LFNoise0.kr(freq);
      lfoNoise2 = LFNoise2.kr(freq);
      lfo = SelectX.kr(lfoWaveform, [lfoSine, lfoSaw, lfoRevSaw, lfoRect, lfoNoise0, lfoNoise2]);
      Out.kr(outBus, lfo);
    }).add;
  }

  //////// public methods:

  free {
    synth.free;
    group.free;
    outBus.free;

    synth = nil;
    group = nil;
    outBus = nil;
  }

  setWaveform { | waveform = 'sine' |
    if( waveform.isInteger || waveform.isFloat, { synth.set(\lfoWaveform, waveform) },
      {
        switch(waveform,
          { 'sine' }, { synth.set(\lfoWaveform, 0); },
          { 'saw' }, { synth.set(\lfoWaveform, 1); },
          { 'revSaw' }, { synth.set(\lfoWaveform, 2); },
          { 'rect' }, { synth.set(\lfoWaveform, 3); },
          { 'sampleAndHold' }, { synth.set(\lfoWaveform, 4); },
          { 'noise' }, { synth.set(\lfoWaveform, 5); }
        );
    });
  }
  setFrequency { | freq = 1 | synth.set(\freq, freq); }
  setPulseWidth { | pw = 0.5 | synth.set(\lfoPulseWidth, pw); }
}