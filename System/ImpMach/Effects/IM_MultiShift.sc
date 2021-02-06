/*
Monday, December 30th 2013
MultiShift.sc
prm

converted to new system 6/19/2014 prm

TO DO 9/16/2014:

take out dedicated shiftMixer
fix waits
-- fixed 9/20/2014 - prm


// changed 4/11/2018
*/

IM_MultiShift : IM_Processor {

  var <isLoaded;
  var <synthsLoaded;
  var <shiftSynthArray, <shiftMixer, server;

  var <isBypassed, <drySynth;

  *new { | outBus = 0, shiftArray = 0, dryAmp = 0, relGroup = nil, addAction = 'addToTail' |
    if( shiftArray.isArray,
      { ^super.new(1, shiftArray.size, outBus, relGroup: relGroup, addAction: addAction).prInit(shiftArray, dryAmp); },
      { ^super.new(1, 1, outBus, relGroup: relGroup, addAction: addAction).prInit(shiftArray, dryAmp); }
    );
  }

  *newStereo { | outBus = 0, shiftArray = 0, dryAmp = 0, relGroup = nil, addAction = 'addToTail' |
    if( shiftArray.isArray,
      { ^super.new(1, shiftArray.size, outBus, relGroup: relGroup, addAction: addAction).prInitStereo(shiftArray, dryAmp); },
      { ^super.new(1, 1, outBus, relGroup: relGroup, addAction: addAction).prInitStereo(shiftArray, dryAmp); }
    );
  }

  prInit { |  shiftArray = 0, dryAmp = 0 |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while( { try { mixer.isLoaded } != true }, { 0.001.wait; });
      server.sync;
      this.prAddSynthDef;
      server.sync;
      this.prMakeSynths(shiftArray, dryAmp);
      while({ try { synthsLoaded } != true }, { 0.001.wait; });
      isBypassed = false;
      isLoaded = true;
    };
  }

  prInitStereo { |  shiftArray = 0, dryAmp = 0 |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while( { try { mixer.isLoaded } != true }, { 0.001.wait; });
      server.sync;
      this.prAddSynthDef;
      server.sync;
      this.prMakeSynthsStereo(shiftArray, dryAmp);
      while({ try { synthsLoaded } != true }, { 0.001.wait; });
      isLoaded = true;
    };
  }

  prAddSynthDef {

    SynthDef(\prm_MultiShift_pitchShifter, {
      | inBus, outBus, amp = 1, shift = 0, windowSize = 0.1, pitchDispersion = 0, timeDispersion = 0.05, mute = 1|
      var input, interval, pitchShift, sig;
      input = In.ar(inBus);
      interval = exp(0.057762265 * shift);
      pitchShift = PitchShift.ar(input, windowSize, interval, pitchDispersion, timeDispersion);
      sig = pitchShift * amp * 0.75;
      sig = sig * mute;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_MultiShift_pitchShifter_stereo, {
      | inBus, outBus, amp = 1, shift = 0, windowSize = 0.1, pitchDispersion = 0, timeDispersion = 0.05, mute = 1|
      var input, interval, pitchShift, sig;
      input = In.ar(inBus, 2);
      interval = exp(0.057762265 * shift);
      pitchShift = PitchShift.ar(input, windowSize, interval, pitchDispersion, timeDispersion);
      sig = pitchShift * amp * 0.9;
      sig = sig * mute;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_drySynth_mono, {
      | inBus, outBus, amp = 1, mute = 0 |
      var input, sig;
      input = In.ar(inBus);
      sig = input * amp;
      sig = sig * mute;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_drySynth_stereo, {
      | inBus, outBus, amp = 1, mute = 0 |
      var input, sig;
      input = In.ar(inBus, 2);
      sig = input * amp;
      sig = sig * mute;
      Out.ar(outBus, sig);
    }).add;


  }

  prMakeSynths { | shiftArray = 0, dryAmp = 0 |
    synthsLoaded = false;
    if( shiftArray.isArray,
      {
        {
          shiftSynthArray = Array.fill(shiftArray.size, { | index |
            Synth(\prm_MultiShift_pitchShifter, [\inBus, inBus, \outBus, mixer.chanMono(index),
              \shift, shiftArray.at(index)], group, \addToHead);
          });
          while({ shiftSynthArray[shiftArray.size-1] == nil}, { 0.001.wait; });
          server.sync;
          synthsLoaded = true;
        }.fork;
      },
      {
        {
          shiftSynthArray = Synth(\prm_MultiShift_pitchShifter, [\inBus, inBus, \outBus, mixer.chanMono,
            \shift, shiftArray], group, \addToHead);
          while({ shiftSynthArray == nil }, { 0.001.wait; });
          server.sync;
          synthsLoaded = true;
        }.fork;
      }
    );


    /*
        {
          shiftMixer = IM_Mixer.new(shiftArray.size, mixer.chanMono(0), relGroup: group, addAction: \addToHead);
          server.sync;
          //while( { try { shiftMixer.chanMono(shiftArray.size-1) } == nil }, { 0.01.wait } );
          //server.sync;
          //0.1.wait;
          while( { shiftMixer.isLoaded.not }, { 0.001.wait; });
          shiftSynthArray = Array.fill(shiftArray.size, { | index |
            Synth(\prm_MultiShift_pitchShifter, [\inBus, inBus, \outBus, shiftMixer.chanMono(index),
              \shift, shiftArray.at(index)],
              group, \addToHead);
          });
          while( { shiftSynthArray[shiftArray.size-1] == nil }, { 0.001.wait; });
          synthsLoaded = true;
        }.fork;
      },

      {
        shiftSynthArray = Synth(\prm_MultiShift_pitchShifter, [\inBus, inBus, \outBus, mixer.chanMono(0),
          \shift, shiftArray], group, \addToTail);
        while( { shiftSynthArray == nil }, { 0.001.wait; });
        synthsLoaded = true;
      }
    );
    */

    drySynth = Synth(\prm_drySynth_mono, [\inBus, inBus, \outBus, mixer.chanMono, \amp, dryAmp], group, \addToHead);
  }

  prMakeSynthsStereo { | shiftArray = 0, dryAmp = 0 |
    synthsLoaded = false;
    if( shiftArray.isArray,
      {
        {
          shiftSynthArray = Array.fill(shiftArray.size, { | index |
            Synth(\prm_MultiShift_pitchShifter_stereo, [\inBus, inBus, \outBus, mixer.chanMono(index),
              \shift, shiftArray.at(index)], group, \addToHead);
          });
          while({ shiftSynthArray[shiftArray.size-1] == nil}, { 0.001.wait; });
          server.sync;
          synthsLoaded = true;
        }.fork;
      },
      {
        {
          shiftSynthArray = Synth(\prm_MultiShift_pitchShifter_stereo, [\inBus, inBus, \outBus, mixer.chanMono,
            \shift, shiftArray], group, \addToHead);
          while({ shiftSynthArray == nil }, { 0.001.wait; });
          server.sync;
          synthsLoaded = true;
        }.fork;
      }
    );
    drySynth = Synth(\prm_drySynth_stereo, [\inBus, inBus, \outBus, mixer.chanMono, \amp, dryAmp], group, \addToHead);
  }

  //////// Public Functions

  free {
    shiftSynthArray.do({ | synth | synth.free; });
    shiftSynthArray = nil;
    drySynth.free;
    this.freeProcessor;
    isLoaded = false;
  }

  setShift { | synth = 0, shift = 0 |
    shiftSynthArray.at(synth).set(\shift, shift);
    ^shift;
  }

	setShiftVol { | synth = 0, vol = 0 |
		shiftSynthArray.at(synth).set(\amp, vol.dbamp);
	}

	setDryVol { | vol = 0 | drySynth.set(\amp, vol.dbamp); }

  setShiftArray { | shiftArray = 0 |
    shiftSynthArray.do({ | synth, index | synth.set(\shift, shiftArray.at(index)); });
  }

  tglBypass {
    if( isBypassed == true, { this.unBypass; }, { this.bypass });
  }

  bypass {
    shiftSynthArray.do({ | synth | synth.set(\mute, 0); });
    drySynth.set(\mute, 1);
    isBypassed = true;
  }

  unBypass {
    shiftSynthArray.do({ | synth | synth.set(\mute, 1); });
    drySynth.set(\mute, 0);
    isBypassed = false;
  }

  /*
  addPitchShift { | shift = 0 |
    //shiftArray.add(shift);
    shiftSynthArray.add(
      Synth(\prm_MultiShift_pitchShifter, [\inBus, shiftBus, \outBus, faderBus, \amp, 1/shiftSynthArray.size, \shift, shift],
        inputSynth, \addAfter);
    );
    shiftSynthArray.do({ | synth | synth.set(\amp, 1/shiftSynthArray.size) });
  }

  asshat
  */

}