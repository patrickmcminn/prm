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

	var <out, <frequency, <waveform, <pulseWidth, <rangeLow, <rangeHigh;

  *new { | outBus = 0, freq = 1, wave = 'sine', rangeLo = -0.25, rangeHi = 0.25, relGroup = nil, addAction = 'addToHead' |
    ^super.new.prInit(outBus, freq, wave, rangeLo, rangeHi, relGroup, addAction);
  }

  prInit {  | outBus, freq = 1, wave = 'sine', rangeLo = -1, rangeHi = 1, relGroup = nil, addAction = 'addToHead' |
    var server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDef;
      server.sync;
      group = Group.new(relGroup, addAction);

			this.prSetInitialParameters(outBus, freq, wave, rangeLo, rangeHi);

      server.sync;

      synth = Synth(\prm_AudioLFO,
        [\outBus, out, \freq, frequency, \lfoWaveform, waveform,
          \rangeLow, rangeLow, \rangeHigh, rangeHigh], group, \addToHead);
      while({ synth == nil }, { 0.001.wait; });

			isLoaded = true;
    };
  }

	prSetInitialParameters { | outBus, freq, wave, rangeLo, rangeHi |

		out = outBus;
		frequency = freq;
		switch ( wave,
        'sine', { waveform = 0 },
        'saw', { waveform = 1 },
        'revSaw', { waveform = 2 },
        'rect', { waveform = 3 },
        'sampleAndHold', { waveform = 4 },
        'noise', { waveform = 5 }
      );
		pulseWidth = 0.5;
		rangeLow = rangeLo;
		rangeHigh = rangeHi;
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

  setOutBus { | outBus = 0 | out = outBus; synth.set(\outBus, out); }

  setWaveform { | wave = 'sine' |
    if( wave.isInteger || wave.isFloat, { waveform = wave; synth.set(\lfoWaveform, waveform) },
      {
        switch(wave,
          { 'sine' }, { waveform = 0; synth.set(\lfoWaveform, waveform); },
          { 'saw' }, { waveform = 1; synth.set(\lfoWaveform, waveform); },
          { 'revSaw' }, { waveform = 2;  synth.set(\lfoWaveform, waveform); },
          { 'rect' }, {  waveform = 3; synth.set(\lfoWaveform, waveform); },
          { 'sampleAndHold' }, { waveform = 4; synth.set(\lfoWaveform, waveform); },
          { 'noise' }, { waveform = 5; synth.set(\lfoWaveform, waveform); }
        );
    });
  }
  setFrequency { | freq = 1 | frequency = freq; synth.set(\freq, frequency); }
  setPulseWidth { | pw = 0.5 | pulseWidth = pw; synth.set(\lfoPulseWidth, pulseWidth); }

  setRange { | rangeLo = -1, rangeHi = 1 |
    this.setRangeLow(rangeLo);
    this.setRangeHigh(rangeHi);
  }
  setRangeLow { | rangeLo = -0.25 | rangeLow = rangeLo; synth.set(\rangeLow, rangeLow); }
  setRangeHigh { | rangeHi = 0.25 | rangeHigh = rangeHi; synth.set(\rangeHigh, rangeHigh);}
}