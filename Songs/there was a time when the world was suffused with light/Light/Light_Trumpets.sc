/*
Monday, November 8th 2021
Light_Trumpets.sc
prm
*/

Light_Trumpets : IM_Module {

  var server, <isLoaded;

  var <distortion, <lpf1, <lpf2, <lpf3;
  var <shift1, <shift2;
  var <splitter, <eq;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, relGroup: relGroup, addAction: \addToHead).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      /*
      distortion = Decapitator.new(eq.inBus, relGroup: group, addAction: \addToHead);
      while({ try { distortion.isLoaded } != true }, { 0.001.wait; });
      */

      lpf1 = LowPassFilter.newStereo(eq.inBus, 1400, relGroup: group, addAction: \addToHead);
      while({ try { lpf1.isLoaded } != true }, { 0.001.wait; });

      lpf2 = LowPassFilter.newStereo(eq.inBus, 1400, relGroup: group, addAction: \addToHead);
      while({ try { lpf2.isLoaded } != true }, { 0.001.wait; });
      shift1 = PitchShifter.newStereo(lpf2.inBus, -12, relGroup: group, addAction: \addToHead);
      while({ try { shift1.isLoaded } != true }, { 0.001.wait; });

      lpf3 = LowPassFilter.newStereo(eq.inBus, 1400, relGroup: group, addAction: \addToHead);
      while({ try { lpf3.isLoaded } != true }, { 0.001.wait; });
      shift2 = PitchShifter.newStereo(lpf3.inBus, -5, relGroup: group, addAction: \addToHead);
      while({ try { shift2.isLoaded } != true }, { 0.001.wait; });

      splitter = Splitter.newStereo(3, [lpf1.inBus, shift1.inBus, shift2.inBus],
        false, group, \addToHead);
      while({ try { splitter.isLoaded } != true }, { 0.001.wait;});

      distortion = Decapitator.new(splitter.inBus, relGroup: group, addAction: \addToHead);
      while({ try { distortion.isLoaded } != true }, { 0.001.wait; });

      server.sync;
      this.prSetInitialParameters;

      isLoaded = true;
    }
  }

  prSetInitialParameters {
    mixer.setPreVol(-12);

    distortion.loadPreset(\light);

    eq.setLowPassCutoff(2500);
    eq.setHighPassCutoff(280);

    lpf1.mixer.setPreVol(-6);
    lpf1.lfo.setWaveform('sampleAndHold');
    lpf1.lfo.setFrequency(12);
    lpf1.setCutoffLFOBottomRatio(0.2);
    lpf1.setCutoffLFOTopRatio(4);
    lpf1.setRQ(0.5);

    lpf2.mixer.setPreVol(-6);
    lpf2.lfo.setWaveform('noise');
    lpf2.lfo.setFrequency(4);
    lpf2.setCutoffLFOBottomRatio(0.2);
    lpf2.setCutoffLFOTopRatio(4);
    lpf3.setRQ(0.5);

    lpf3.mixer.setPreVol(-6);
    lpf3.lfo.setWaveform('noise');
    lpf3.lfo.setFrequency(5);
    lpf3.setCutoffLFOBottomRatio(0.2);
    lpf3.setCutoffLFOTopRatio(4);
    lpf3.setRQ(0.5);

  }

  /////// public functions:

  free {
    splitter.free;
    distortion.free;
    shift1.free; shift2.free;
    lpf1.free; lpf2.free; lpf3.free;
    this.freeModule;
  }

  inBus { ^distortion.inBus }

  setChord1 {
    shift1.setShiftAmount(-7);
    shift2.setShiftAmount(-3);
  }

  setChord2 {
    shift1.setShiftAmount(-7);
    shift2.setShiftAmount(-4);
  }

  setChord3 {
    shift1.setShiftAmount(-9);
    shift2.setShiftAmount(-5);
  }

  setChord4 {
    shift1.setShiftAmount(5);
    shift2.setShiftAmount(-3);
  }

  setChord5 {
    shift1.setShiftAmount(5);
    shift2.setShiftAmount(-3);
  }

  reset {
    shift1.setShiftAmount(12);
    shift2.setShiftAmount(-12);
  }
}


