/*
Sunday, March 19th 2017
Habit.sc
prm
Baltimore, MD
1:23 am on a Saturday night, listening to Oren Ambarchi


updated Saturday, May 8th 2021
*/

Habit : IM_Module {

  var <isLoaded;
  var server;
  var <moog, <modular, <modularInput, <trumpetInput, <trumpet, <trumpetLoopers, <trumpetLoopersInput, <terr;
  var <clock;
  var <songMixer;

  *new {
		|
		outBus, micIn, inArray, outArray, moogDeviceName, moogPortName, sequencer, delay,
		send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead'
		|
    ^super.new(5, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false,
			relGroup, addAction).prInit(micIn, inArray, outArray, moogDeviceName, moogPortName, sequencer, delay);
  }

  prInit {
    |
		micIn, inArray, outArray, moogDeviceName, moogPortName, sequencer, delay
    |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      clock = TempoClock.new(1);
      server.sync;

			/*
      // delay:
      delay = SimpleDelay.newStereo(outBus, 2, 0.6, 5, send0Bus, send1Bus, send2Bus, nil,
        relGroup: group, addAction: \addToHead);
      while({ try { delay.isLoaded } != true }, { 0.001.wait; });


      songMixer = IM_Mixer.new(4, outBus, send0Bus, send1Bus, send2Bus, delay.inBus,
        false, group, \addToHead);
      while({ try { songMixer.isLoaded } != true }, { 0.001.wait; });
			*/

      // trumpet:
      trumpet = Habit_LiveTrumpet.new(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });
      trumpetInput = IM_HardwareIn.new(micIn, trumpet.inBus, group, \addToHead);
      while({ try { trumpetInput.isLoaded } != true }, { 0.001.wait; });

      // moog:
			moog = Habit_Moog.new(inArray[0], mixer.chanStereo(1), relGroup: group, addAction: \addToHead,
        deviceName: moogDeviceName, portName: moogPortName);
      while({ try { moog.isLoaded } != true }, { 0.001.wait; });

			// terr:
			terr = IM_HardwareIn.newStereo(inArray[2], mixer.chanStereo(2), group, \addToHead);
			while({ try { terr.isLoaded } != true }, { 0.001.wait; });

      // modular:
			modular = IM_HardwareIn.new(inArray[1], mixer.chanMono(3), group, \addToHead);
      while({ try { modular.isLoaded} != true }, { 0.001.wait; });

      // trumpet loopers:
      trumpetLoopers = Habit_TrumpetLoopers.new(mixer.chanStereo(4), relGroup: group, addAction: \addToHead);
      while({ try { trumpetLoopers.isLoaded } != true}, { 0.001.wait; });
      trumpetLoopersInput = IM_HardwareIn.new(micIn, trumpetLoopers.inBus, group, \addToHead);
      while({ try { trumpetLoopersInput.isLoaded } != true }, { 0.001.wait; });

      server.sync;

			this.prSetInitialParameters(delay);

      server.sync;

      isLoaded = true;
    }
  }

	prSetInitialParameters { | delay |
		5.do({ | chan | mixer.setPreVol(chan, -9); });
		5.do({ | chan | mixer.setVol(chan, -70); });
		mixer.setVol(4, -6);

		delay.setDelayTime(2);
		delay.setFeedback(0.6);
		delay.mixer.setSendVol(0, -12);

		// trumpet
		mixer.setVol(0, -6);
		mixer.mute(0);
		mixer.setSendVol(0, 0, -3);
		mixer.setSendVol(0, 3, 0);
		mixer.setSendVol(0, 2, 0);

		// moog:
		mixer.setVol(1, -6);
		mixer.setSendVol(1, 0, -14.6);
		mixer.setSendVol(1, 3, -25.3);
		//mixer.setSendVol(1, 2, 0);

		// terr:
		mixer.setVol(2, -6);
		mixer.setSendVol(2, 0, -13);
		mixer.setSendVol(2, 3, -9);

		// modular:
		mixer.setVol(3, -9);
		mixer.setSendVol(3, 0, -14.6);
		mixer.setSendVol(3, 3, -18);
		//mixer.setSendVol(3, 2, 0);

		// trumpet loopers:
		mixer.setVol(4, -9);
		mixer.setSendVol(4, 0, -3);
		mixer.setSendVol(4, 3, 0);
		mixer.setSendVol(4, 2, 0);

	}

  //////// public functions:

  free {
    trumpetInput.free;
    trumpetLoopersInput.free;
    trumpet.free;
    trumpetInput.free;
    modular.free;
    modularInput.free;
    moog.free;
    clock.free;
    this.freeSong;
  }

  setClockTempo { | tempo = 60 |
    var bps = tempo/60;
    clock.tempo = bps;
  }


}