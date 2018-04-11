/*
Monday, December 30th 2013
MultiShift.sc
prm
*/

MultiShift {

  var server;
  var shiftBus, faderBus;
  var inputSynth, shiftSynthArray, faderSynth;
  var <pitchShiftIsMuted;

  *new { | inBus = 0, outBus = 0, amp = 1, dryAmp = 1, pan = 0, shiftArray = 0, group = nil, addAction = 'addToHead' |
    ^super.new.prInit(inBus, outBus, amp, dryAmp, pan, shiftArray, group, addAction);
  }

  prInit { | inBus = 0, outBus = 0, amp = 1, dryAmp = 1, pan = 0, shiftArray = 0, group = nil, addAction = 'addToHead' |
    server = Server.default;
    server.waitForBoot {
      this.prAddSynthDefs;
      server.sync;
      this.prMakeBusses;
      server.sync;
      this.prMakeSynths(inBus, outBus, amp, dryAmp, pan, shiftArray, group, addAction);
      server.sync;
    };
  }

  prAddSynthDefs {

    SynthDef(\prm_MultiShift_input, {
      | inBus, dryOutBus, wetOutBus, dryAmp = 1, dryMute = 1, wetAmp = 1, wetMute = 1 |
      var input, sig;
      input = In.ar(inBus);
      Out.ar(dryOutBus, input * dryAmp * dryMute);
      Out.ar(wetOutBus, input * wetAmp * wetMute);
    }).add;

    SynthDef(\prm_MultiShift_fader, {
      | inBus, outBus, amp = 0.5, pan = 0, mute = 1 |
      var input, panner, sig;
      input = In.ar(inBus);
      panner = Pan2.ar(input, pan);
      sig = panner * amp * mute;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_MultiShift_pitchShifter, {
      | inBus, outBus, amp = 1, shift = 0, windowSize = 0.2, pitchDispersion = 0, timeDispersion = 0.05 |
      var input, interval, pitchShift, sig;
      input = In.ar(inBus);
      interval = exp(0.057762265 * shift);
      pitchShift = PitchShift.ar(input, windowSize, interval, pitchDispersion, timeDispersion);
      sig = pitchShift * amp;
      Out.ar(outBus, sig);
    }).add;

  }

  prMakeBusses {
    shiftBus = Bus.audio;
    faderBus = Bus.audio;
  }

  prFreeBusses {
    shiftBus.free;
    faderBus.free;
  }

  prMakeSynths { | inBus = 0, outBus = 0, amp = 1, dryAmp = 1, pan = 0, shiftArray = 0, group = nil, addAction = 'addTdoTail' |
    faderSynth = Synth(\prm_MultiShift_fader, [\inBus, faderBus, \outBus, outBus, \amp, amp], group, addAction);
    inputSynth = Synth(\prm_MultiShift_input, [\inBus, inBus, \wetOutBus, shiftBus, \dryOutBus, faderBus, \dryAmp, dryAmp],
      faderSynth, \addBefore);
    if( shiftArray.isArray, {
      shiftSynthArray = Array.fill(shiftArray.size, { | index |
        Synth(\prm_MultiShift_pitchShifter, [\inBus, shiftBus, \outBus, faderBus, \amp, 1/shiftArray.size, \shift, shiftArray.at(index)],
          inputSynth, \addAfter)
      });
      },
      {
        shiftSynthArray = Synth(\prm_MultiShift_pitchShifter, [\inBus, shiftBus, \outBus, faderBus, \amp, 1, \shift, shiftArray],
          inputSynth, \addAfter);
    }
    );
  }

  prFreeSynths {
    faderSynth.free;
    shiftSynthArray.do({ | synth | synth.free; });
    inputSynth.free;
  }

  //////// Public Functions

  setAmp { | amp = 1 |
    faderSynth.set(\amp, amp);
    ^amp;
  }

  setVol { | vol = 0 |
    this.setAmp(vol.dbamp);
    ^vol;
  }

  setPan { | pan = 0 |
    faderSynth.set(\pan, pan);
    ^pan;
  }

  setShift { | synth = 0, shift = 0 |
    shiftSynthArray.at(synth).set(\shift, shift);
    ^shift;
  }

  setShiftArray { | shiftArray = 0 |
    shiftSynthArray.do({ | synth, index | synth.set(\shift, shiftArray.at(index)); });
  }

  addPitchShift { | shift = 0 |
    //shiftArray.add(shift);
    shiftSynthArray.add(
      Synth(\prm_MultiShift_pitchShifter, [\inBus, shiftBus, \outBus, faderBus, \amp, 1/shiftSynthArray.size, \shift, shift],
        inputSynth, \addAfter);
    );
    shiftSynthArray.do({ | synth | synth.set(\amp, 1/shiftSynthArray.size) });
  }

  removePitchShift { | synth = 0 |
    shiftSynthArray.at(synth).free;
  }

  free {
    this.prFreeSynths;
    this.prFreeBusses;
  }

  tglMutePitchShift { }

  mutePitchShift { }

  unMutePitchShift { }

}