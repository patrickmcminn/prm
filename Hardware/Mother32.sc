/*
Monday, January 25th 2016
Mother32.sc
prm
class to interface with the Mother32 hardware

4/23/2020
assignable out seems to cause the interpreter to freeze if mutliple instances are running
that's my working theory at least
especially as it ain't doing shit right now, I'm taking it out
*/

Mother32 : IM_Module {

  // assumes assignable out is in a CC transmit mode

  var <isLoaded, server;
  var <assignableOut;
  var <hardwareIn;
  var midiOutPort;
  var <midiChannel;
  var <sequencerDict, <sequencerClock, <tempo, <beats;

  *new { | hardwareInBus = 0, outBus = 0, deviceName = nil, portName = nil, midiChan = 1, assignableCC = 1,
    send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(
      hardwareInBus, deviceName, portName, midiChan, assignableCC);
  }

  prInit { | hardwareInBus = 0, deviceName = nil, portName = nil, midiChan = 1, assignableCC = 1 |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      midiChannel = (midiChan-1);
      this.prInitMIDI(deviceName, portName);
      server.sync;

			/*
      assignableOut = MoogMotherAssignableOut.new(deviceName, portName, nil, assignableCC, midiChannel, group, \addToTail);
      while({ try { assignableOut.isLoaded } != true }, { 0.001.wait; });
			*/

      hardwareIn = IM_HardwareIn.new(hardwareInBus, mixer.chanMono(0), group, \addToHead);
      while({ try { hardwareIn.isLoaded } != true }, { 0.001.wait; });

      sequencerDict = IdentityDictionary.new;
      sequencerClock = TempoClock.new;

      server.sync;

      isLoaded = true;
      //midiOutPort.postln;
    }
  }

  prInitMIDI { | deviceName = nil, portName = nil |
    MIDIIn.connectAll;
    server.sync;
    midiOutPort = MIDIOut.newByName(deviceName, portName);
    midiOutPort.latency = 0;
  }

  //////// public functions:
  free {
    hardwareIn.free;
    //assignableOut.free;
    this.freeModule;
  }

  playNote { | freq = 220 |
	//	this.releaseNote(freq);
    midiOutPort.noteOn(midiChannel, freq.cpsmidi);
  }

  releaseNote { | freq = 220 |
    midiOutPort.noteOff(midiChannel, freq.cpsmidi);
  }

	allNotesOff { midiOutPort.allNotesOff(midiChannel); }

  makeSequence { | name |
    {
      sequencerDict[name] = IM_PatternSeq.new(name, group, \addToHead);
      sequencerDict[name].stop;
      server.sync;
      sequencerDict[name].addKey(\type, \midi);
      sequencerDict[name].addKey(\midicmd, \noteOn);
      sequencerDict[name].addKey(\midiout, midiOutPort);
      sequencerDict[name].addKey(\chan, midiChannel);
    }.fork;
  }

  addKey {  | name, key, action |
    sequencerDict[name].addKey(key, action);
  }

  playSequence { | name, clock = 'internal', quant = 'nil' |
    var playClock;
    if( clock == 'internal', { playClock = sequencerClock }, { playClock = clock });
    sequencerDict[name].play(playClock);
  }

  resetSequence { | name | sequencerDict[name].reset; }
  stopSequence { | name | sequencerDict[name].stop; }
  pauseSequence { | name | sequencerDict[name].pause }
  resumeSequence { | name | sequencerDict[name].resume; }
  isSequencePlaying { | name | ^sequencerDict[name].isPlaying }
  setSequenceQuant { | name, quant = 0 | sequencerDict[name].setQuant(quant) }

  setSequencerClockTempo { | bpm = 60 |
    var bps = bpm/60;
    tempo = bps;
    sequencerClock.tempo = tempo;
  }


}