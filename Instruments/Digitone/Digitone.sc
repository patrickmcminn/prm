/*
Monday, August 1st 2022
Digitone.sc
prm

for use in controlling the Digitone from SC
*/

Digitone : IM_Module {

	var server, <isLoaded;
	var midiInPort, midiOutPort;
	var <controlArray, dictArray;

	var <track1IsMuted = false;
	var <track2IsMuted = false;
	var <track3IsMuted = false;
	var <track4IsMuted = false;

	*new { | deviceName = "", portName = "", track1Chan, track2Chan, track3Chan, track4Chan |
		^super.new.prInit(deviceName, portName, track1Chan, track2Chan, track3Chan, track4Chan);
	}

	prInit { | deviceName = "APC40 mkII", portName = "APC40 mkII", track1Chan, track2Chan, track3Chan, track4Chan |
		isLoaded = false;
		this.prInitMIDI(deviceName, portName);
		this.prMakeControlFuncs(track1Chan, track2Chan, track3Chan, track4Chan);
		isLoaded = true;
	}

	prInitMIDI { | deviceName = "APC40 mkII", portName = "APC40 mkII" |
		MIDIIn.connectAll;
		//MIDIClient.init;
		//MIDIIn.connect(portName, deviceName);
		midiInPort = MIDIIn.findPort(deviceName, portName);
		midiOutPort = MIDIOut.newByName(deviceName, portName);
		midiOutPort.latency = 0;
	}

	free {

	}

	prMakeControlFuncs { | track1Chan, track2Chan, track3Chan, track4Chan |
		controlArray = Array.newClear(4);
		this.prMakeTrackFunc(1, track1Chan);
		this.prMakeTrackFunc(2, track2Chan);
		this.prMakeTrackFunc(3, track3Chan);
		this.prMakeTrackFunc(4, track4Chan);
	}

	prMakeTrackFunc { | track, channel |
		var dict = IdentityDictionary.new;
		controlArray[track-1] = dict;
		dict[\mute] = { midiOutPort.control(channel, 94, 127); };
		dict[\unMute] = { midiOutPort.control(channel, 94, 0); };
		dict[\volume] = { | val | midiOutPort.control(channel, 95, val); };
	}

	muteTrack { | track | controlArray[track-1][\mute].value; }
	unMuteTrack { | track | controlArray[track-1][\unMute].value; }

	tglMuteTrack1 { if(track1IsMuted, { this.unMuteTrack1 }, { this.muteTrack1 }); }
	muteTrack1 {
		this.muteTrack(1);
		track1IsMuted = true;
	}
	unMuteTrack1 {
		this.unMuteTrack(1);
		track1IsMuted = false;
	}

	tglMuteTrack2 { if(track2IsMuted, { this.unMuteTrack2 }, { this.muteTrack2 }); }
	muteTrack2 {
		this.muteTrack(2);
		track2IsMuted = true;
	}
	unMuteTrack2 {
		this.unMuteTrack(2);
		track2IsMuted = false;
	}

	tglMuteTrack3 { if(track3IsMuted, { this.unMuteTrack3 }, { this.muteTrack3 }); }
	muteTrack3 {
		this.muteTrack(3);
		track3IsMuted = true;
	}
	unMuteTrack3 {
		this.unMuteTrack(3);
		track3IsMuted = false;
	}

	tglMuteTrack4 { if(track4IsMuted, { this.unMuteTrack4 }, { this.muteTrack4 }); }
	muteTrack4 {
		this.muteTrack(4);
		track4IsMuted = true;
	}
	unMuteTrack4 {
		this.unMuteTrack(4);
		track4IsMuted = false;
	}

	setTrackVol { | track, val | controlArray[track-1][\volume].value(val); }
	setTrack1Vol { | val | this.setTrackVol(1, val); }
	setTrack2Vol { | val | this.setTrackVol(2, val); }
	setTrack3Vol { | val | this.setTrackVol(3, val); }
	setTrack4Vol { | val | this.setTrackVol(4, val); }

} 