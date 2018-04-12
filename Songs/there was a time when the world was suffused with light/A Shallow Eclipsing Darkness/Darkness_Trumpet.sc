/*
Tuesday, April 10th 2018
Darkness_Trumpet.sc
A Shallow Eclipsing Darkness
prm
*/


Darkness_Trumpet : IM_Processor {

  var <isLoaded;
  var server;
  var <input, <distortion, <microLooper, <modularOut, <modularIn;
  var <eq, <multiHarmonizer, <splitter, <lowPassFilter, <delay, <pitchShift;

  *new {  | outBus = 0, modularOut = 2, modularIn = 2, relGroup, addAction = 'addToHead' |
    ^super.new(1, 2, outBus, relGroup: relGroup, addAction: addAction).prInit(modularOut, modularIn);
  }

  prInit { | modularOut = 2, modularIn = 2 |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      //// second trumpet line. to be muted in initial state
      pitchShift = PitchShifter.newStereo(mixer.chanStereo(1), 12, relGroup: group, addAction: \addToHead);
      while({ try { pitchShift.isLoaded } != true }, { 0.001.wait; });

      delay = SimpleDelay.newStereo(pitchShift.inBus, 5, 0.1, 5, relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });

      ///// main trumpet line (only effect post split)
      lowPassFilter = LowPassFilter.newStereo(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { lowPassFilter.isLoaded } != true }, { 0.001.wait; });

      // splitter ( to get to the two trumpet lines ):
      splitter = Splitter.newStereo(2, [lowPassFilter.inBus, delay.inBus], relGroup: group, addAction: \addToHead);
      while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

      multiHarmonizer = IM_MultiShift.newStereo(splitter.inBus, [-12, 7, 12, 14, 15, 17, ], 0, group, \addToHead);
      while({ try { multiHarmonizer.isLoaded } != true }, { 0.001.wait; });

      eq = Equalizer.newMono(multiHarmonizer.inBus, group, \addToHead);
      while({ try { eq.isLoaded } != true }, { 0.001.wait; });

      modularIn = IM_HardwareIn.new(modularIn, eq.inBus, group, \addToHead);
      while ({ try { modularIn.isLoaded } != true }, { 0.001.wait; });

      modularOut = MonoHardwareSend.new(2, relGroup: group, addAction: \addToHead);
      while ({ try { modularOut.isLoaded } != true }, { 0.001.wait; });

      // needs to be adapted for dry/wet. Might be cool to add later.
      //microLooper = GlitchLooper.newStereo

      distortion = Distortion.newStereo(modularOut.inBus, 3, relGroup: group, addAction: \addToHead);
      while({ try { distortion.isLoaded } != true }, { 0.001.wait; });

      input = IM_Mixer_1Ch.new(distortion.inBus, relGroup: group, addAction: \addToHead);
      while({ try { input.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prSetInitialParameters;

      isLoaded = true;
    }
  }

  prSetInitialParameters {
    // second trumpet starts muted:
    mixer.mute(1);

    // multi harmonizer starts bypassed:
    multiHarmonizer.bypass;

    // equalizer:
    eq.setHighPassCutoff(100);
    eq.setLowPassCutoff(12000);

    // distortion:
    distortion.preEQ.setHighPassCutoff(200);
    distortion.preEQ.setLowPassCutoff(5350);
    distortion.postEQ.setHighPassCutoff(100);
    distortion.postEQ.setLowPassCutoff(4570);

    // delay:
    delay.setMix(1);


  }

  //////// public functions:

  inBus { ^input.chanStereo(0) }

  free {
    pitchShift.free;
    delay.free;
    lowPassFilter.free;
    splitter.free;
    multiHarmonizer.free;
    eq.free;
    modularIn.free;
    modularOut.free;
    distortion.free;
    input.free;
    this.freeProcessor;
    isLoaded = false;
  }
}

/*
input

Distortion


MicroLooper

out TO MODULAR

in FROM modular

EQ

MultiHarmonizer

SPLIT:


1 -- normal
-- LowPassFilter
--(LOWPASS filter needs to be modulated)

2 -- 8va w/ DELAY


*/
