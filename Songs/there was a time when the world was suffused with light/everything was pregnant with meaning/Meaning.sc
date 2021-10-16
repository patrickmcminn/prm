/*
Meaing.sc
Saturday, April 21st 2018
prm

everything was pregnant with meaning
*/

Meaning : IM_Module {

  var <isLoaded;
  var server;

  var <hiss, <main, <synth, <synthEQ;
  var <reverb, <delay;

  var <trumpet, <modular;

  var <midiEnabled = false;
	var midiFuncs, midiDict;

  var seq;

  *new {
    |
    outBus = 0, micInBus, moogInBus, modInBus, sequencer,
		send0Bus, send1Bus, send2Bus, send3Bus,
		relGroup, addAction = 'addToHead'
    |
    ^super.new(5, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(micInBus, moogInBus, modInBus, sequencer);
  }

  prInit { | micInBus, moogInBus, modInBus, sequencer |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      midiEnabled = false;
      midiDict = IdentityDictionary.new;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      /*
      /////// Send Returns:
      returns = IM_Mixer.new(2, mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { returns.isLoaded } != true }, { 0.001.wait; });

      reverb = Meaning_Reverb.new(returns.chanStereo(0), reverbBuffer, group, \addToHead);
      while({ try { reverb.isLoaded } != true }, { 0.001.wait; });

      delay = Meaning_Delay.new(returns.chanStereo(1), group, \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });
      */

      //////// Main Mixer:

      hiss = Meaning_Hiss.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { hiss.isLoaded } != true }, { 0.001.wait; });

      main = Meaning_Main.new(mixer.chanStereo(1), group, \addToHead);
      while({ try { main.isLoaded } != true }, { 0.001.wait; });


      synthEQ = Equalizer.newMono(mixer.chanStereo(2), group, \addToHead);
      while({ try { synthEQ.isLoaded } != true }, { 0.001.wait; });
      synth = IM_HardwareIn.new(moogInBus, synthEQ.inBus, group, \addToHead);
      while({ try { synth.isLoaded } != true }, { 0.001.wait; });

      trumpet = IM_HardwareIn.new(micInBus, mixer.chanStereo(3), group, \addToHead);
      while({ try{ trumpet.isLoaded } != true }, { 0.001.wait; });

      modular = IM_HardwareIn.new(modInBus, mixer.chanStereo(4), group, \addToHead);
      while({ try { modular.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      seq = sequencer.uid;

      this.prInitializeParameters;

      server.sync;

      isLoaded = true;
    }
  }

  prInitializeParameters {
    //////// Mixer:

    // hiss:
    mixer.setPreVol(0, -3);
    mixer.setVol(0, 0);
    mixer.setSendVol(0, 0, -20);
    mixer.setSendVol(0, 3, 0);

    // main:
    mixer.setPreVol(1, -12);
    mixer.setVol(1, 3);
    mixer.setSendVol(1, 0, 0);
    mixer.setSendVol(1, 3, -12);

    // synth:
    synthEQ.setHighPassCutoff(12500);
    mixer.mute(2);
    mixer.setPreVol(2, -6);
    mixer.setSendVol(2, 0, -15);
    mixer.setSendVol(2, 3, -3);
    mixer.setVol(2, -17);

    // trumpet:
    mixer.mute(3);
    mixer.setPreVol(3, 0);
    mixer.setVol(3, -9);
    mixer.setSendVol(3, 0, -6);
    mixer.setSendVol(3, 2, 9);
    mixer.setSendVol(3, 3, 0);

    // modular:
    mixer.mute(4);
    mixer.setPreVol(4, 0);
    mixer.setVol(4, -12);
    mixer.setSendVol(4, 0, -9);

  }

  //////// public functions:

  free {
    synth.free;
    main.free;
    hiss.free;
    trumpet.free;
    modular.free;

    this.freeModule;

    isLoaded = false;
  }

  toggleMIDIFuncs { if( midiEnabled == false, { this.makeMIDIFuncs }, { this.freeMIDIFuncs }); }

  makeMIDIFuncs {
    midiDict[\note1On] = MIDIFunc.noteOn({ main.playNote1 }, 60, 0, seq);
    midiDict[\note1Off] = MIDIFunc.noteOff({ main.releaseNote1 }, 60, 0, seq);
    midiDict[\note2On] = MIDIFunc.noteOn({ main.playNote2 }, 62, 0, seq);
    midiDict[\note2Off] = MIDIFunc.noteOff({ main.releaseNote2 }, 62, 0, seq);
    midiDict[\note3On] = MIDIFunc.noteOn({ main.playNote3 }, 64, 0, seq);
    midiDict[\note3Off] = MIDIFunc.noteOff({ main.releaseNote3 }, 64, 0, seq);
    midiDict[\note4On] = MIDIFunc.noteOn({ main.playNote4 }, 65, 0, seq);
    midiDict[\note4Off] = MIDIFunc.noteOff({ main.releaseNote4 }, 65, 0, seq);

    midiEnabled = true;
  }

  freeMIDIFuncs {
    midiDict.do({ | func | func.free; });
    midiEnabled = false;
  }



}