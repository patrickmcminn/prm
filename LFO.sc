/*
Saturday, September 20th 2014
LFO.sc
prm
*/

// takes direct control over

LFO {

  var <isLoaded;
  var <synth;
  var group, <outBus;
  var <lfoWaveform, <frequency;

  *new { | freq = 1, waveform = 'sine', rangeLow = -1, rangeHigh = 1, relGroup = nil, addAction = 'addToHead' |
    ^super.new.prInit(freq, waveform, rangeLow, rangeHigh, relGroup, addAction);
  }

  prInit {  | freq = 1, waveform = 'sine', rangeLow = -1, rangeHigh = 1, relGroup = nil, addAction = 'addToHead' |
    var server = Server.default;
    var wave;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDef;
      server.sync;
      frequency = freq;
      outBus = Bus.control;
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
      synth = Synth(\prm_LFO,
        [\outBus, outBus, \freq, freq, \lfoWaveform, lfoWaveform,
          \rangeLow, rangeLow, \rangeHigh, rangeHigh], group, \addToHead);
      while({ synth == nil }, { 0.001.wait; });
      this.setWaveform(waveform);
      isLoaded = true;
    };
  }

  prAddSynthDef {
    SynthDef(\prm_LFO, {
      | outBus = 0, lfoWaveform = 0, freq = 1, lfoPulseWidth = 0.5, rangeLow = -1, rangeHigh = 1 |
      var lfo, lfoSine, lfoSaw, lfoRevSaw, lfoRect, lfoNoise0, lfoNoise2;
      lfoSine = SinOsc.kr(freq);
      lfoSaw = LFSaw.kr(freq, 1);
      lfoRevSaw = LFSaw.kr(freq, 1) * -1;
      lfoRect = LFPulse.kr(freq, width: lfoPulseWidth).range(-1, 1).lag2(0.05);
      lfoNoise0 = LFNoise0.kr(freq);
      lfoNoise2 = LFNoise2.kr(freq);
      lfo = SelectX.kr(lfoWaveform, [lfoSine, lfoSaw, lfoRevSaw, lfoRect, lfoNoise0, lfoNoise2]);
      lfo = lfo.range(rangeLow, rangeHigh);
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
    if( waveform.isInteger || waveform.isFloat, {
      lfoWaveform = waveform;
      synth.set(\lfoWaveform, waveform) },
    {
      switch(waveform,
        { 'sine' }, { synth.set(\lfoWaveform, 0); lfoWaveform = 0; },
        { 'saw' }, { synth.set(\lfoWaveform, 1); lfoWaveform = 1 },
        { 'revSaw' }, { synth.set(\lfoWaveform, 2); lfoWaveform = 2 },
        { 'rect' }, { synth.set(\lfoWaveform, 3); lfoWaveform = 3 },
        { 'sampleAndHold' }, { synth.set(\lfoWaveform, 4); lfoWaveform = 4 },
        { 'noise' }, { synth.set(\lfoWaveform, 5); lfoWaveform = 5 }
      );
    });
  }
  setFrequency { | freq = 1 |
    frequency = freq;
    synth.set(\freq, frequency); }
  setPulseWidth { | pw = 0.5 | synth.set(\lfoPulseWidth, pw); }

  setRangeLow { | rangeLo = -1 | synth.set(\rangeLow, rangeLo); }
  setRangeHigh { | rangeHi = 1 | synth.set(\rangeHigh, rangeHi);}

  map { | param = \freq, bus |
    synth.set(param, bus.asMap);
  }

  mapFreq { | bus | this.map(\freq, bus); }
}