/*
Friday, January 9th 2015
Splitter.sc
prm
*/

Splitter {

  var <isLoaded;
  var server;
  var group;
  var <inBus, splitBus, nilBus;
  var inputSynth, outputSynthArray;

  *newMono {
    |
    numOutBusses = 2, outBusArray = nil,
    feedback = false, relGroup = nil, addAction = 'addToHead'
    |
    ^super.new.prInitMono(numOutBusses, outBusArray, feedback, relGroup, addAction);
  }

  *newStereo {
    |
    numOutBusses = 2, outBusArray = nil,
    feedback = false, relGroup = nil, addAction = 'addToHead'
    |
    ^super.new.prInitStereo(numOutBusses, outBusArray, feedback, relGroup, addAction);
  }


  prInitMono {
    |
    numOutBusses = 2, outBusArray = nil,
    feedback = false, relGroup = nil, addAction = 'addToHead'
    |
    server = Server.default;
    server.waitForBoot {
      if( outBusArray.isArray,
        {
          isLoaded = false;
          this.prAddSynthDef;
          group = Group.new(relGroup, addAction);
          server.sync;

          inBus = Bus.audio;
          splitBus = Bus.audio;
          nilBus = Bus.audio;
          server.sync;

          inputSynth = Synth(\prm_Splitter, [\inBus, inBus, \outBus, splitBus], group, \addToHead);

          outputSynthArray = Array.newClear(outBusArray.size);
          outBusArray.size.do({ | i |
            outputSynthArray[i] = Synth(\prm_Splitter, [\inBus, splitBus,
              \outBus, if( outBusArray[i] != nil, { outBusArray[i] }, { nilBus })
              ], group, \addToTail);
          });

          while({ outputSynthArray[outputSynthArray.size - 1] == nil }, { 0.001.wait; });
          isLoaded = true;
        },
        { "outBussArray should be an Array of outBusses".postln; });
    };
  }

  prInitStereo {
     |
    numOutBusses = 2, outBusArray = nil,
    feedback = false, relGroup = nil, addAction = 'addToHead'
    |
    server = Server.default;
    server.waitForBoot {
      if( outBusArray.isArray,
        {
          isLoaded = false;
          this.prAddSynthDef;
          group = Group.new(relGroup, addAction);
          server.sync;

          inBus = Bus.audio(server, 2);
          splitBus = Bus.audio(server, 2);
          nilBus = Bus.audio(server, 2);
          server.sync;

          inputSynth = Synth(\prm_Splitter_Stereo, [\inBus, inBus, \outBus, splitBus], group, \addToHead);

          outputSynthArray = Array.newClear(outBusArray.size);
          outBusArray.size.do({ | i |
            outputSynthArray[i] = Synth(\prm_Splitter_Stereo, [\inBus, splitBus,
              \outBus, outBusArray[i]], group, \addToTail);
          });

          while({ outputSynthArray[outputSynthArray.size - 1] == nil }, { 0.001.wait; });
          isLoaded = true;
        },
        { "outBusArray should be an Array of outBusses".postln; });
    };
  }


  prAddSynthDef {
    SynthDef(\prm_Splitter, {
      | inBus, outBus |
      var sig = In.ar(inBus);
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_Splitter_Stereo, {
      | inBus, outBus |
      var sig = In.ar(inBus, 2);
      Out.ar(outBus, sig);
    }).add;
  }

  //////// public functions:

  free {
    inputSynth.free;
    outputSynthArray.do({ | synth | synth.free; });
    splitBus.free;
    inBus.free;
    group.free;
    nilBus.free;

    inputSynth = nil;
    outputSynthArray = nil;
    splitBus = nil;
    inBus = nil;
    nilBus = nil;
    group = nil;
  }

  setOutBus { | outBusNum = 0, outBus |
    var bus;
    if( outBus != nil, { bus = outBus }, { bus = nilBus });
    outputSynthArray[outBusNum].set(\outBus, bus);
  }


}