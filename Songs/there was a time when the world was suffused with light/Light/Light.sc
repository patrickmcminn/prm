/*
Saturday, April 22nd, 2017
Light.sc
prm
*/

/*
3 4 4 3
3 3 4 5
*/

Light : IM_Module {

  var server, <isLoaded;

  var <main, <bass, <saturBass, <arps;
  var <chorale, <trumpet;

  var <midiEnabled, midiDict;

  var <noiseTrumpetInput, <noiseTrumpet;

  var <modularReturn, <noiseFilter, <noise, <revPlayer;

  var sequencer;

  *new { | outBus, micIn, pickupIn, moogIn, modularIn, plaitsIn, noiseIn,
    moogDevice, moogPort, seq,
    send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
    ^super.new(10, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false,
      relGroup, addAction).prInit(micIn, pickupIn, moogIn, modularIn, plaitsIn, noiseIn, moogDevice,
      moogPort, seq);
  }

  prInit {
    | micIn, pickupIn, moogIn, modularIn, plaitsIn, noiseIn, moogDevice, moogPort, seq |
    var path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/there was a time when the world was suffused with light/dissolution of color onto an infinite plane/samples/b04_2.wav";

    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      server.sync;
      mixer.muteMaster;


      midiDict = IdentityDictionary.new;
      sequencer = seq.uid;

      main = Light_Main.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { main.isLoaded } != true }, { 0.001.wait; });

      arps = Light_Arps.new(mixer.chanStereo(1), plaitsIn, group, \addToHead);
      while({ try { arps.isLoaded } != true }, { 0.001.wait; });

      /*
      bass = IM_HardwareIn.new(moogIn, mixer.chanMono(2), group, \addToHead);
      while({ try{ bass.isLoaded } != true }, { 0.001.wait; });
      */
      bass = Mother32.new(moogIn, mixer.chanStereo(2), moogDevice, moogPort,
        relGroup: group, addAction: \addToHead);
      while({ try{ bass.isLoaded } != true }, { 0.001.wait; });


      saturBass = SaturSynth.new(mixer.chanStereo(3), relGroup: group, addAction: \addToHead);
      while({ try { saturBass.isLoaded } != true }, { 0.001.wait; });

      noiseTrumpet = Light_Trumpets.new(mixer.chanStereo(4), group, \addToHead);
      while({ try { noiseTrumpet.isLoaded } != true }, { 0.001.wait; });
      noiseTrumpetInput = IM_HardwareIn.new(pickupIn, noiseTrumpet.inBus, group, \addToHead);
      while({ try { noiseTrumpetInput.isLoaded } != true }, { 0.001.wait; });

      noiseFilter = LowPassFilter.newMono(mixer.chanStereo(5),
        relGroup: group, addAction: \addToHead);
      while({ try { noiseFilter.isLoaded } != true }, { 0.001.wait; });

      noise = IM_HardwareIn.new(noiseIn, noiseFilter.inBus, group, \addToHead);
      while({ try { noise.isLoaded } != true }, { 0.001.wait; });

      trumpet = IM_HardwareIn.new(micIn, mixer.chanMono(6), group, \addToHead);
      while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });

      chorale = Light_Chorale.new(mixer.chanStereo(7), group, \addToHead);
      while({ try { chorale.isLoaded } != true }, { 0.001.wait;});

      modularReturn = IM_HardwareIn.new(modularIn, mixer.chanMono(8), group, \addToHead);
      while({ try { modularReturn.isLoaded } != true }, { 0.001.wait; });

      revPlayer = SamplePlayer.newStereo(mixer.chanStereo(9), path,
        relGroup: group, addAction: \addToHead);
      while({ try { revPlayer.isLoaded } != true }, { 0.001.wait; });

      /*
      bass = Light_Bass.new(mixer.chanStereo(2), "UMC404HD 192k", "UMC404HD 192k", group, \addToHead);
      while({ try { bass.isLoaded } != true }, { 0.001.wait; });
      */

      server.sync;

      this.prSetInitialConditions;

      server.sync;

      mixer.unMuteMaster;
      isLoaded = true;
    }
  }

  prSetInitialConditions {

    midiEnabled = false;

    // send 4 - light delays

    // main:
    mixer.setVol(0, -3);
    mixer.setPreVol(0, -6);
    mixer.setSendVol(0, 0, 0);
    mixer.setSendVol(0, 3, 0);
    main.setFilterCutoff(150);


    // arps:
    mixer.setVol(1, -6);
    mixer.setSendVol(1, 0, -15);
    mixer.setSendVol(1, 3, -9);
    //arps.setFilterCutoff(150);

    // bass:
    mixer.setPreVol(2, -6);
    mixer.setVol(2, -6);
    mixer.setSendVol(2, 0, -12);

    // satur bass:
    //mixer.setPreVol(3, -3);
    mixer.setVol(3, -18);
    mixer.setSendVol(3, 0, -15);

    // noise trumpet:
    mixer.mute(4);
    mixer.setPreVol(4, -9);
    mixer.setSendVol(4, 0, -15);
    mixer.setSendVol(4, 1, -12);
    mixer.setSendVol(4, 3, -9);

    // modular return:
    mixer.mute(8);
    mixer.setPreVol(8, -9);
    mixer.setSendVol(8, 0, -6);
    mixer.setSendVol(8, 3, -5);

    // noise:
    mixer.mute(5);
    mixer.setVol(5, -40);
    mixer.setPreVol(5, -12);
    mixer.setSendVol(5, 0, -15);
    mixer.setSendVol(5, 3, -3);
    noiseFilter.setCutoff(250);

    // trumpet:
    trumpet.mute;
    mixer.setPreVol(6, -12);
    mixer.setSendVol(6, 0, 0);
    mixer.setSendVol(6, 2, 0);
    mixer.setSendVol(6, 3, -5);

    // chorale:
    mixer.setSendPre(7);
    mixer.setPreVol(7, -9);
    mixer.setVol(7, -36);
    mixer.setSendVol(7, 0, -24);
    mixer.setSendVol(7, 1, -28);
    mixer.setSendVol(7, 3, -21);

    // revPlayer:
    mixer.setSendPre(9);
    mixer.setVol(9, -70);
    mixer.setSendVol(9, 2, 0);


  }

  free {
    mixer.masterChan.mute;
    chorale.free;
    main.free;
    arps.free;
    bass.free; saturBass.free;
    noiseTrumpet.free; noiseTrumpetInput.free;
    noiseFilter.free; noise.free; trumpet.free;
    this.freeModule;
    isLoaded = false;
  }

  toggleMIDIFuncs { if(midiEnabled == true, { this.freeMIDIFuncs; }, { this.makeMIDIFuncs }); }

  makeMIDIFuncs {
    // main:

    midiDict[\loop1On] = MIDIFunc.noteOn({ main.playLoop1 },
      60, 2, sequencer);
    midiDict[\loop1Off] = MIDIFunc.noteOff({ main.releaseLoop1 },
      60, 2, sequencer);
    midiDict[\loop2On] = MIDIFunc.noteOn({ main.playLoop2 },
      62, 2, sequencer);
    midiDict[\loop2Off] = MIDIFunc.noteOff({ main.releaseLoop2 },
      62, 2, sequencer);
    midiDict[\loop3On] = MIDIFunc.noteOn({ main.playLoop3 },
      64, 2, sequencer);
    midiDict[\loop3Off] = MIDIFunc.noteOff({ main.releaseLoop3 },
      64, 2, sequencer);
    midiDict[\loop4On] = MIDIFunc.noteOn({ main.playLoop4 },
      65, 2, sequencer);
    midiDict[\loop4Off] = MIDIFunc.noteOff({ main.releaseLoop4 },
      65, 2, sequencer);

    midiDict[\loop1RevOn] = MIDIFunc.noteOn({ main.playLoop1Rev },
      72, 2, sequencer);
    midiDict[\loop1RevOff] = MIDIFunc.noteOff({ main.releaseLoop1Rev },
      72, 2, sequencer);
    midiDict[\loop2RevOn] = MIDIFunc.noteOn({ main.playLoop2Rev },
      74, 2, sequencer);
    midiDict[\loop2RevOff] = MIDIFunc.noteOff({ main.releaseLoop2Rev },
      74, 2, sequencer);
    midiDict[\loop3RevOn] = MIDIFunc.noteOn({ main.playLoop3Rev },
      76, 2, sequencer);
    midiDict[\loop3RevOff] = MIDIFunc.noteOff({ main.releaseLoop3Rev },
      76, 2, sequencer);
    midiDict[\loop4RevOn] = MIDIFunc.noteOn({ main.playLoop4Rev },
      77, 2, sequencer);
    midiDict[\loop4RevOff] = MIDIFunc.noteOff({ main.releaseLoop4Rev },
      77, 2, sequencer);

    midiDict[\loop1Half] = MIDIFunc.noteOn({ main.playLoop1Half },
      48, 2, sequencer);
    midiDict[\loop1HalfOff] = MIDIFunc.noteOff({ main.releaseLoop1Half },
      48, 2, sequencer);

    // arps:
    midiDict[\chord1On] = MIDIFunc.noteOn({ arps.playChord1; }, 60, 3, sequencer);
    midiDict[\chord1Off] = MIDIFunc.noteOff({ arps.stopChord1 }, 60, 3, sequencer);
    midiDict[\chord2On] = MIDIFunc.noteOn({ arps.playChord2; }, 62, 3, sequencer);
    midiDict[\chord2Off] = MIDIFunc.noteOff({ arps.stopChord2 }, 62, 3, sequencer);
    midiDict[\chord3On] = MIDIFunc.noteOn({ arps.playChord3; }, 64, 3, sequencer);
    midiDict[\chord3Off] = MIDIFunc.noteOff({ arps.stopChord3 }, 64, 3, sequencer);
    midiDict[\chord4On] = MIDIFunc.noteOn({ arps.playChord4; }, 65, 3, sequencer);
    midiDict[\chord4Off] = MIDIFunc.noteOff({ arps.stopChord4 }, 65, 3, sequencer);
    midiDict[\chord5On] = MIDIFunc.noteOn({ arps.playChord5; }, 67, 3, sequencer);
    midiDict[\chord5Off] = MIDIFunc.noteOff({ arps.stopChord5 }, 67, 3, sequencer);

    // trumpet:
    midiDict[\tptChord1] = MIDIFunc.noteOn({ noiseTrumpet.setChord1 }, 60, 4, sequencer);
    midiDict[\tptChord2] = MIDIFunc.noteOn({ noiseTrumpet.setChord2 }, 62, 4, sequencer);
    midiDict[\tptChord3] = MIDIFunc.noteOn({ noiseTrumpet.setChord3 }, 64, 4, sequencer);
    midiDict[\tptChord4] = MIDIFunc.noteOn({ noiseTrumpet.setChord4 }, 65, 4, sequencer);
    midiDict[\tptChord5] = MIDIFunc.noteOn({ noiseTrumpet.setChord5 }, 67, 4, sequencer);
    midiDict[\tptChordReset] = MIDIFunc.noteOn({ noiseTrumpet.reset }, 48, 4, sequencer);

    midiDict[\bassOnArray] = Array.fill(128, { | note | MIDIFunc.noteOn({ |vel|
      saturBass.playNote(note.midicps, vel.ccdbfs); }, note, 5, sequencer); });
    midiDict[\bassOffArray] = Array.fill(128, { | note | MIDIFunc.noteOff({
      saturBass.releaseNote(note.midicps) }, note, 5, sequencer); });

    midiEnabled = true;

  }

  freeMIDIFuncs { midiDict.do({ | func | func.free; }); midiEnabled = false; }

  playEndSong { revPlayer.playSampleOneShot(0, 1); }

}