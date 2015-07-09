/*
Monday, July 6th 2015
Connections_AirSputters.sc
prm
*/

Connections_AirSputters : IM_Processor {

  var <isLoaded, server;
  var <granulator, <sampler, onsetDetector, <bufferArray, oscFunc;
  var <sputterNum, recordRoutine;
  var <isPlayingArray;

  *new { | outBus = 0, clock, relGroup = nil, addAction = 'addToTail' |
    ^super.new(1, 1, outBus, nil, nil, nil, nil, false, relGroup, addAction).prInit(clock);
  }

  prInit { | clock |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      isPlayingArray = Array.fill(4, { false; });
      sputterNum = 0;
      this.prAddSynthDefs;

      server.sync;

      bufferArray = Buffer.allocConsecutive(4, server, server.sampleRate * 0.4, 1);

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      granulator = GranularDelay.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { granulator.isLoaded } != true }, { 0.001.wait; });

      sampler = Sampler.newMono(granulator.inBus, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });
      server.sync;

      sampler.setBufferArray(bufferArray);
      this.prMakePatterns;
      server.sync;
      this.prMakeRecordRoutine(clock);

      onsetDetector = Synth(\prm_AirSputters_OnsetDetectorTrig, [\inBus, inBus, \thresh, 0.05, \fastMul, 0.65, \inputAmp, 2.5],
        group, \addToHead);
      server.sync;

      oscFunc = OSCFunc({ recordRoutine.next; }, '/tr');
      server.sync;
      this.prMakePatternParameters;

      granulator.setGrainDur(0.01, 0.16);
      granulator.setTrigRate(45);
      granulator.setGrainEnvelope('rexpodec');
      granulator.granulator.setPan(-0.7, 0.7);
      granulator.setDelayMix(0);
      granulator.setGranulatorCrossfade(1);

      mixer.setPreVol(18);
      isLoaded = true;
    };
  }

  prAddSynthDefs {

    SynthDef(\prm_AirSputters_OnsetDetectorTrig, {
      |inBus = 0, id = 0, inputAmp = 1
      trackFall = 0.2, slowLag = 0.2, fastLag = 0.01, fastMul = 0.5, thresh = 0.005, minDur = 0.1 |
      var input, detect, trigger, sig;
      input = In.ar(inBus);
      input = input  * inputAmp;
      detect = Coyote.kr(input, trackFall, slowLag, fastLag, fastMul, thresh, minDur);
      sig = SendTrig.kr(detect, id, 1);
    }).add;

    SynthDef(\prm_AirSputters_RecordBuf, {
      |inBus, buffer, loop = 0, preLevel = 0 |
      var input, record, sig;
      input = In.ar(inBus);
      record = RecordBuf.ar(input, buffer, preLevel: preLevel, loop: loop, trigger: 1, doneAction: 2);
    }).add;

  }

  prMakePatterns {
    sampler.makeSequence(\sputterOne, 'oneShot');
    sampler.makeSequence(\sputterTwo, 'oneShot');
    sampler.makeSequence(\sputterThree, 'oneShot');
    sampler.makeSequence(\sputterFour, 'oneShot');
  }

  prMakePatternParameters {

    // sputter 1:
    sampler.addKey(\sputterOne, \buffer, bufferArray[0]);
    //sampler.addKey(\sputterOne, \amp, 0.6);
    sampler.addKey(\sputterOne, \amp, Pseq([1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0], inf));
    sampler.addKey(\sputterOne, \dur, 0.125);
    sampler.addKey(\sputterOne, \sustainTime, 0.125);
    sampler.addKey(\sputterOne, \rate, 1);
    sampler.addKey(\sputterOne, \rate,Pstep(
			Prand([-1, -1, -1, -1, -1, -1, -0.5, -0.5, -0.5, 0.5, 1, 1,
				1.1, 1.1, 1.1, 1.1, 1.2, 1.2, 1.2, 1.3, 1.3, 1.3, 1.3, 1.3, 2, 2, 2, 2, 2], inf),
      1, inf));
    sampler.addKey(\sputterOne, \startPos, Pif(Pkey(\rate).isPositive, 0, server.sampleRate*0.4));
    sampler.addKey(\sputterOne, \loop, 0);

    // sputter 2:
    sampler.addKey(\sputterTwo, \buffer, bufferArray[1]);
    //sampler.addKey(\sputterTwo, \amp, 0.6);
    sampler.addKey(\sputterTwo, \amp, Pseq([1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1], inf));
    sampler.addKey(\sputterTwo, \dur, 0.125);
    sampler.addKey(\sputterTwo, \sustainTime, 0.125);
    //sampler.addKey(\sputterTwo, \note, Pseq([1, 1, \r, \r, \r, \r, \r, 1, 1, \r, \r, \r, \r, \r, 1, 1], inf));
    sampler.addKey(\sputterTwo, \rate, Pstep(
			Prand([-2, -2, -2, -2, -2, -2, -0.5, -0.5, -0.5, 3, 1.7, 1.7, 1.1, 1.1, 1.1, 1.1, 2.4, 2.4, 2.4,
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2], inf),
      1, inf));
    sampler.addKey(\sputterTwo, \startPos, Pif(Pkey(\rate).isPositive, 0, server.sampleRate*0.4));

    // sputter 3:
    sampler.addKey(\sputterThree, \buffer, bufferArray[2]);
    //sampler.addKey(\sputterThree, \amp, 0.6);
    sampler.addKey(\sputterThree, \amp, Pseq([1, 0, 0, 0], inf));
    sampler.addKey(\sputterThree, \dur, 0.125);
    sampler.addKey(\sputterThree, \sustainTime, 0.125);
    //sampler.addKey(\sputterThree, \note, Pseq([1, \r, \r, \r], inf));
    sampler.addKey(\sputterThree, \rate, Pstep(
			Prand([0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, -0.5, -0.5, -0.5, 1, 1, 1.1, 1.1, 1.1, 1.1,
				0.6, 0.6, 0.6, 0.4, 0.4, 0.4, 0.4], inf),
      1, inf));
    sampler.addKey(\sputterThree, \startPos, Pif(Pkey(\rate).isPositive, 0, server.sampleRate*0.4));

    // sputter 4:
    sampler.addKey(\sputterFour, \buffer, bufferArray[3]);
    //sampler.addKey(\sputterFour, \amp, 0.6);
    sampler.addKey(\sputterFour, \amp, Pseq([1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0], inf));
    sampler.addKey(\sputterFour, \dur, 0.125);
    sampler.addKey(\sputterFour, \sustainTime, 0.12);
    //sampler.addKey(\sputterFour, \note, Pseq([1, \r, \r, 1, \r, \r, 1, \r, \r, 1, \r, \r, \r], inf));
    sampler.addKey(\sputterFour, \rate, 0.25);

  }


  prMakeRecordRoutine { | clock |
    recordRoutine = r {

      Synth(\prm_AirSputters_RecordBuf, [\inBus, inBus, \buffer, bufferArray[0]], group, \addToTail);
      { sampler.playSequence('sputterOne', clock); }.defer(0.4);
      isPlayingArray[0] = true;
      "Air Sputter 1".postln;
      (sputterNum = 1).yield;

      Synth(\prm_AirSputters_RecordBuf, [\inBus, inBus, \buffer, bufferArray[1]], group, \addToTail);
      { sampler.playSequence(\sputterTwo, clock); }.defer(0.4);
      isPlayingArray[1] = true;
      "Air Sputter 2".postln;
      (sputterNum = 2).yield;

      Synth(\prm_AirSputters_RecordBuf, [\inBus, inBus, \buffer, bufferArray[2]], group, \addToTail);
      { sampler.playSequence(\sputterThree, clock); }.defer(0.4);
      isPlayingArray[2] = true;
      "Air Sputter 3".postln;
      (sputterNum = 3).yield;

      Synth(\prm_AirSputters_RecordBuf, [\inBus, inBus, \buffer, bufferArray[3]], group, \addToTail);
      { sampler.playSequence(\sputterFour, clock); }.defer(0.4);
      isPlayingArray[3] = true;
      "Air Sputter 4".postln;
      (sputterNum = 4).yield;

      oscFunc.free;
      "Air Sputters Recorded".postln.yield;
    }
  }

  //////// public functions:

  free {
    this.stopAllPatterns;
    bufferArray.do({ | i | i.free; });
    oscFunc.free;
    sampler.free;
    granulator.free;
    this.freeProcessor;

    bufferArray = nil;
    oscFunc = nil;
    sampler = nil;
    granulator = nil;
  }

  resetPatterns {
    this.stopAllPatterns;
    oscFunc.free;
    bufferArray.do({ | buf | buf.clear; });
    oscFunc = OSCFunc({ recordRoutine.next; });
    recordRoutine.reset;
  }

  //// start/stop patterns:

  playPattern1 { | clock |
    sampler.playSequence('sputterOne', clock);
    isPlayingArray[0] = true;
  }
  stopPattern1 {
    sampler.stopSequence('sputterOne');
  }
  togglePattern1Play { | clock |
    if( isPlayingArray[0] == false,
      {
        sampler.playSequence(\sputterOne, clock);
        isPlayingArray[0] = true;
      },
      {
        sampler.stopSequence(\sputterOne);
        isPlayingArray[0] = false;
      }
    );
  }

  playPattern2 { | clock |
    sampler.playSequence('sputterTwo', clock);
    isPlayingArray[1] = true;
  }
  stopPattern2 {
    sampler.stopSequence('sputterTwo');
  }
  togglePattern2Play { | clock |
    if( isPlayingArray[1] == false,
      {
        sampler.playSequence(\sputterTwo, clock);
        isPlayingArray[1] = true;
      },
      {
        sampler.stopSequence(\sputterTwo);
        isPlayingArray[1] = false;
      }
    );
  }

  playPattern3 { | clock |
    sampler.playSequence('sputterThree', clock);
    isPlayingArray[2] = true;
  }
  stopPattern3 {
    sampler.stopSequence('sputterThree');
  }
  togglePattern3Play { | clock |
    if( isPlayingArray[2] == false,
      {
        sampler.playSequence(\sputterThree, clock);
        isPlayingArray[2] = true;
      },
      {
        sampler.stopSequence(\sputterThree);
        isPlayingArray[2] = false;
      }
    );
  }

  playPattern4 { | clock |
    sampler.playSequence('sputterFour', clock);
    isPlayingArray[3] = true;
  }
  stopPattern4 {
    sampler.stopSequence('sputterFour');
  }
  togglePattern4Play { | clock |
    if( isPlayingArray[3] == false,
      {
        sampler.playSequence(\sputterFour, clock);
        isPlayingArray[3] = true;
      },
      {
        sampler.stopSequence(\sputterFour);
        isPlayingArray[3] = false;
      }
    );
  }

  playAllPatterns {
    this.playPattern1;
    this.playPattern2;
    this.playPattern3;
    this.playPattern4;
  }

  stopAllPatterns {
    this.stopPattern1;
    this.stopPattern2;
    this.stopPattern3;
    this.stopPattern4;
  }



}