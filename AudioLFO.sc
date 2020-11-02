/*
Saturday, September 20th 2014
LFO.sc
prm
*/

// class for sending LFO out of a DC-coupled audio interface for CV Control

CV_LFO {

  var <isLoaded;
  var <synth;
  var group;
  var lfoWaveform;

  *new { | outBus = 0, freq = 1, waveform = 'sine', rangeLow = -1, rangeHigh = 1, relGroup = nil, addAction = 'addToHead' |
    ^super.new.prInit(outBus, freq, waveform, rangeLow, rangeHigh, relGroup, addAction);
  }

  prInit {
		| outBus, freq = 1, waveform = 'sine', rangeLow = -0.25, rangeHigh = 0.25, relGroup = nil, addAction = 'addToHead' |
    var server = Server.default;
    var wave;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDef;
      server.sync;
      group = Group.new(relGroup, addAction);
      switch ( waveform,
        'sine', { lfoWaveform = 0 },
        'saw', { lfoWaveform = 1 },
        'revSaw', { lfoWaveform = 2 },
        'rect', { lfoWaveform = 3 },
        'sampleAndHold', { lfoWaveform = 4 },
        'noise', { lfoWaveform = 5 }
      );
      server.sync;
      synth = Synth(\prm_AudioLFO,
        [\outBus, outBus, \freq, freq, \lfoWaveform, lfoWaveform,
          \rangeLow, rangeLow, \rangeHigh, rangeHigh], group, \addToHead);
      while({ synth == nil }, { 0.001.wait; });
      this.setWaveform(waveform);
      isLoaded = true;
    };
  }

  prAddSynthDef {
    SynthDef(\prm_AudioLFO, {
      | outBus = 0, lfoWaveform = 0, freq = 1, lfoPulseWidth = 0.5, rangeLow = -1, rangeHigh = 1 |
      var lfo, lfoSine, lfoSaw, lfoRevSaw, lfoRect, lfoNoise0, lfoNoise2;
      lfoSine = SinOsc.ar(freq);
      lfoSaw = LFSaw.ar(freq, 1);
      lfoRevSaw = LFSaw.ar(freq, 1) * -1;
      lfoRect = LFPulse.ar(freq, width: lfoPulseWidth).range(-1, 1).lag2(0.05);
      lfoNoise0 = LFNoise0.ar(freq);
      lfoNoise2 = LFNoise2.ar(freq);
      lfo = SelectX.ar(lfoWaveform, [lfoSine, lfoSaw, lfoRevSaw, lfoRect, lfoNoise0, lfoNoise2]);
      lfo = lfo.range(rangeLow, rangeHigh);
      Out.ar(outBus, lfo);
    }).add;
  }

  //////// public methods:

  free {
    synth.free;
    group.free;

    synth = nil;
    group = nil;
  }

  setOutBus { | outBus = 0 | synth.set(\outBus, outBus); }

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

  setRange { | rangeLo = -1, rangeHi = 1 |
    this.setRangeLow(rangeLo);
    this.setRangeHigh(rangeHi);
  }
  setRangeLow { | rangeLo = -1 | synth.set(\rangeLow, rangeLo); }
  setRangeHigh { | rangeHi = 1 | synth.set(\rangeHigh, rangeHi);}
}