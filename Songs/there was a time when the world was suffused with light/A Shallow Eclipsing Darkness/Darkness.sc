/*
Thursday, April 12th 2018
Darkness.sc
A Shallow Eclipsing Darkness

eschewing the Song class for now, to get away from using
submixes
*/

Darkness : IM_Module {

  var <isLoaded;
  var server;

  var <masterEQ, <songMixer;

  var <introSample, <bass, <trumpet, <drones;

  *new { | outBus, send0Bus, send1Bus, send2Bus, send3Bus, moogDeviceName, moogPortName, relGroup, addAction = 'addToHead' |
    ^super.new(1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup: relGroup, addAction: addAction).prInit(send0Bus, send1Bus, send2Bus, send3Bus, moogDeviceName, moogPortName);
  }

  prInit { | send0Bus, send1Bus, send2Bus, send3Bus, moogDeviceName, moogPortName |
    server = Server.default;
    server.waitForBoot {
      var path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/there was a time when the world was suffused with light/A Shallow Eclipsing Darkness/samples/introLoop.wav";

      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      masterEQ = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { masterEQ.isLoaded } != true }, { 0.001.wait; });

      songMixer = IM_Mixer.new(4, masterEQ.inBus, send0Bus, send1Bus, send2Bus, send3Bus,
        relGroup: group, addAction: \addToTail);
      while({ try { songMixer.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      introSample = SamplePlayer.newStereo(songMixer.chanStereo(0), path, relGroup: group, addAction: \addToHead);
      while({ try { introSample.isLoaded } != true }, { 0.001.wait; });

      bass = Darkness_Bass.new(3, songMixer.chanStereo(1), moogDeviceName, moogPortName, group, \addToHead);
      while({ try { bass.isLoaded } != true }, { 0.001.wait; });

      trumpet = Darkness_Trumpet.new(songMixer.chanStereo(2), 2, 2, group, \addToHead);
      while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });

      drones = Darkness_Drone.new(songMixer.chanStereo(3), group, \addToHead);
      while({ try { drones.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prSetInitialMixerLevels;
      this.prSetInitialParameters;

      isLoaded = true;
    }
  }

  prSetInitialMixerLevels {


    // Intro Sample:
    introSample.mixer.setVol(-3);
    songMixer.setSendVol(0, 0, -6);
    songMixer.setSendVol(0, 2, -24);
    songMixer.setSendVol(0, 3, 0);


    // Bass:
    songMixer.setSendVol(1, 0, -9);
    songMixer.setSendVol(1, 2, -24);
    songMixer.setSendVol(1, 3, 0);


    // Trumpet:
    trumpet.mixer.setVol(0, -9);
    trumpet.mixer.setVol(1, -12);
    songMixer.setSendVol(2, 0, -6);
    songMixer.setSendVol(2, 2, -8);
    songMixer.setSendVol(2, 3, 0);

    // Drones:
    drones.mixer.setVol(-3);
    songMixer.setSendVol(3, 0, 0);
    songMixer.setSendVol(3, 2, -30);
    songMixer.setSendVol(3, 3, 0);

  }

  prSetInitialParameters {

    masterEQ.setHighFreq(5500);
    masterEQ.setHighGain(6);

    introSample.setAttackTime(10);
    introSample.setReleaseTime(10);

  }

  //////// public functions:
  free {
    drones.free;
    trumpet.free;
    bass.free;
    introSample.free;
    songMixer.free;
    masterEQ.free;
    this.freeModule;
    isLoaded = false;
  }
}
