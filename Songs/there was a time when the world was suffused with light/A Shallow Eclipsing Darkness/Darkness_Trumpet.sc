/*
Tuesday, April 10th 2018
Darkness_Trumpet.sc
A Shallow Eclipsing Darkness
prm
*/

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

      multiHarmonizer;


      isLoaded = true;
    }
  }

}
