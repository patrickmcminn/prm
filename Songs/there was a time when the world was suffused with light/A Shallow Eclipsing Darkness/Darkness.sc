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

  var <introSample, <bass, <trumpet, <trumpetInput, <drones;

  var <basslineIsPlaying, <introIsPlaying;

  var <drone1IsPlaying, <drone2IsPlaying, <drone3IsPlaying, <drone4IsPlaying;
  var <drone5IsPlaying, <drone6IsPlaying, <drone7IsPlaying, <drone8IsPlaying;
  var <drone9IsPlaying, <drone10IsPlaying;

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
        relGroup: group, addAction: \addToHead);
      while({ try { songMixer.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      songMixer.tglMute(2);

      introSample = SamplePlayer.newStereo(songMixer.chanStereo(0), path, relGroup: group, addAction: \addToHead);
      while({ try { introSample.isLoaded } != true }, { 0.001.wait; });

      bass = Darkness_Bass.new(3, songMixer.chanStereo(1), moogDeviceName, moogPortName, group, \addToHead);
      while({ try { bass.isLoaded } != true }, { 0.001.wait; });

      trumpet = Darkness_Trumpet.new(songMixer.chanStereo(2), 2, 2, group, \addToHead);
      while({ try { trumpet.isLoaded } != true }, { 0.001.wait; });
      trumpetInput = IM_HardwareIn.new(0, trumpet.inBus, group, \addToHead);
      while({ try { trumpetInput.isLoaded } != true }, { 0.001.wait; });

      drones = Darkness_Drone.new(songMixer.chanStereo(3), group, \addToHead);
      while({ try { drones.isLoaded } != true }, { 0.001.wait; });

      server.sync;

      this.prSetInitialMixerLevels;
      this.prSetInitialParameters;

      isLoaded = true;
    }
  }

  prSetInitialMixerLevels {


    ////// send 0 is reverb
    ////// send 1 is granulator
    ////// send 2 is delay
    ////// send 3 is crazy granulator

    // Intro Sample:
    introSample.mixer.setPreVol(12);
    introSample.mixer.setVol(-3);
    songMixer.setSendVol(0, 0, -6);
    songMixer.setSendVol(0, 2, -24);
    songMixer.setSendVol(0, 3, 0);


    // Bass:
    songMixer.setPreVol(1, 3);
    songMixer.setSendVol(1, 0, -9);
    songMixer.setSendVol(1, 2, -24);
    songMixer.setSendVol(1, 3, 0);


    // Trumpet:
    songMixer.setPreVol(2, 3);
    trumpet.mixer.setVol(0, -9);
    trumpet.mixer.setVol(1, -18);
    songMixer.setSendVol(2, 0, -12);
    songMixer.setSendVol(2, 2, -8);
    songMixer.setSendVol(2, 3, 0);

    // Drones:
    drones.mixer.setVol(-3);
    songMixer.setSendVol(3, 0, 0);
    songMixer.setSendVol(3, 2, -30);
    songMixer.setSendVol(3, 3, 0);

  }

  prSetInitialParameters {

    introIsPlaying = false;
    basslineIsPlaying = false;

    this.setBasslineTempo(100);

    drone1IsPlaying = false; drone2IsPlaying = false; drone3IsPlaying = false;
    drone4IsPlaying = false; drone5IsPlaying = false; drone6IsPlaying = false;
    drone7IsPlaying = false; drone8IsPlaying = false;
    drone9IsPlaying = false; drone10IsPlaying = false;

    masterEQ.setHighFreq(5500);
    masterEQ.setHighGain(6);

    introSample.setAttackTime(10);
    introSample.setReleaseTime(10);

  }

  //////// public functions:
  free {
    introIsPlaying = false;
    basslineIsPlaying = false;
    drones.free;
    trumpet.free;
    trumpetInput.free;
    bass.free;
    introSample.free;
    songMixer.free;
    masterEQ.free;
    this.freeModule;
    isLoaded = false;
  }

  playBassline {
    bass.moog.playSequence(\darkness);
    basslineIsPlaying = true;
  }
  stopBassline {
    bass.moog.stopSequence(\darkness);
    basslineIsPlaying = false;
  }
  tglBassline {
    if( basslineIsPlaying == false, { this.playBassline }, { this.stopBassline });
  }

  setBasslineTempo { | tempo = 100 |
    bass.moog.setSequencerClockTempo(tempo);
  }

  playIntro {
    introSample.playSampleSustaining('intro');
    introIsPlaying = true;
  }
  stopIntro {
    introSample.releaseSampleSustaining('intro');
    introIsPlaying = false;
  }
  tglIntro {
    if(introIsPlaying == false, { this.playIntro }, { this.stopIntro });
  }

  playDrone1 {
    drones.synth.playSequence(\cSharp);
    drone1IsPlaying = true;
  }
  stopDrone1 {
    drones.synth.stopSequence(\cSharp);
    drone1IsPlaying = false;
  }
  tglDrone1 {
    if(drone1IsPlaying == false, { this.playDrone1 }, { this.stopDrone1 });
  }

  playDrone2 {
    drones.synth.playSequence(\dSharp);
    drone2IsPlaying = true;
  }
  stopDrone2 {
    drones.synth.stopSequence(\dSharp);
    drone2IsPlaying = false;
  }
  tglDrone2 {
    if(drone2IsPlaying == false, { this.playDrone2 }, { this.stopDrone2 });
  }

  playDrone3 {
    drones.synth.playSequence(\E);
    drone3IsPlaying = true;
  }
  stopDrone3 {
    drones.synth.stopSequence(\E);
    drone3IsPlaying = false;
  }
  tglDrone3 {
    if(drone3IsPlaying == false, { this.playDrone3 }, { this.stopDrone3 });
  }

  playDrone4 {
    drones.synth.playSequence(\fSharp);
    drone4IsPlaying = true;
  }
  stopDrone4 {
    drones.synth.stopSequence(\fSharp);
    drone4IsPlaying = false;
  }
  tglDrone4 {
    if(drone4IsPlaying == false, { this.playDrone4 }, { this.stopDrone4 });
  }

  playDrone5 {
    drones.synth.playSequence(\gSharp);
    drone5IsPlaying = true;
  }
  stopDrone5 {
    drones.synth.stopSequence(\gSharp);
    drone5IsPlaying = false;
  }
  tglDrone5 {
    if(drone5IsPlaying == false, { this.playDrone5 }, { this.stopDrone5 });
  }

  playDrone6 {
    drones.synth.playSequence(\A);
    drone6IsPlaying = true;
  }
  stopDrone6 {
    drones.synth.stopSequence(\A);
    drone6IsPlaying = false;
  }
  tglDrone6 {
    if(drone6IsPlaying == false, { this.playDrone6 }, { this.stopDrone6 });
  }

  playDrone7 {
    drones.synth.playSequence(\B);
    drone7IsPlaying = true;
  }
  stopDrone7 {
    drones.synth.stopSequence(\B);
    drone7IsPlaying = false;
  }
  tglDrone7 {
    if(drone7IsPlaying == false, { this.playDrone7 }, { this.stopDrone7 });
  }

  playDrone8 {
    drones.synth.playSequence(\cSharp2);
    drone8IsPlaying = true;
  }
  stopDrone8 {
    drones.synth.stopSequence(\cSharp2);
    drone8IsPlaying = false;
  }
  tglDrone8 {
    if(drone8IsPlaying == false, { this.playDrone8 }, { this.stopDrone8 });
  }

  playDrone9 {
    drones.synth.playSequence(\cSharp);
    drone9IsPlaying = true;
  }
  stopDrone9 {
    drones.synth.stopSequence(\cSharp);
    drone9IsPlaying = false;
  }
  tglDrone9 {
    if(drone9IsPlaying == false, { this.playDrone9 }, { this.stopDrone9 });
  }

  playDrone10 {
    drones.synth.playSequence(\cSharp0);
    drone10IsPlaying = true;
  }
  stopDrone10 {
    drones.synth.stopSequence(\cSharp0);
    drone10IsPlaying = false;
  }
  tglDrone10 {
    if(drone10IsPlaying == false, { this.playDrone10 }, { this.stopDrone10 });
  }

}