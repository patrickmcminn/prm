/*
Monday, April 23rd 2018
Docile.sc
prm

to compress the world to a docile state
*/

Docile : IM_Module {

  var server;
  var <isLoaded;

  var <main, <kick, <noisies, <moog;
  var <clock;

  var <mainLoopIsPlaying;

  var <kickSection1IsPlaying, <kickSection2IsPlaying, <kickSection3IsPlaying;
  var <kickSection4IsPlaying, <kickSection5IsPlaying, <kickSection6IsPlaying;
  var <kickSection7IsPlaying, <kickSection8IsPlaying;

  var <noisiesFullLoopIsPlaying;



  *new {
    | outBus = 0, moogDeviceName, moogPortName, send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead' |
    ^super.new(4, outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup: relGroup, addAction: addAction).prInit(moogDeviceName, moogPortName);
  }

  prInit { |moogDeviceName, moogPortName |
    server = Server.default;
    server.waitForBoot {
      var kicksPath, kicksArray;
      isLoaded = false;

      // location of samples
      kicksPath = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/there was a time when the world was suffused with light/to compress the world to a docile state/samples/Kick/";
      kicksArray = (kicksPath ++ "*").pathMatch;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      main = Docile_Main.new(mixer.chanStereo(0), group, \addToHead);
      while({ try { main.isLoaded } != true }, { 0.001.wait; });

      kick = Sampler.newStereo(mixer.chanStereo(1), kicksArray, relGroup: group, addAction: \addToHead);
      while({ try { kick.isLoaded } != true }, { 0.001.wait; });

      noisies = Docile_Noisies.new(mixer.chanStereo(2), group, \addToHead);
      while({ try { noisies.isLoaded } != true }, { 0.001.wait; });

      moog = Docile_Moog.new(3, mixer.chanStereo(3), moogDeviceName, moogPortName, group, \addToHead);
      while({ try { moog.isLoaded } != true }, { 0.001.wait; });

      clock = TempoClock.new(142/60);

      server.sync;

      this.prInitializeParameters;

      server.sync;

      this.prMakeKickSequences;
      this.prMakeNoisiesSequences;

      isLoaded = true;
    }
  }

  prInitializeParameters {

    clock.beatsPerBar = 2;

    // intitial playing parameters:

    mainLoopIsPlaying = false;

    kickSection1IsPlaying = false;
    kickSection2IsPlaying = false;
    kickSection3IsPlaying = false;
    kickSection4IsPlaying = false;
    kickSection5IsPlaying = false;
    kickSection6IsPlaying = false;
    kickSection7IsPlaying = false;
    kickSection8IsPlaying = false;

    noisiesFullLoopIsPlaying = false;

    //////// mixer:

    // main:
    mixer.setVol(0, -3);
    // reverb starts at zero but please remember to ramp it up

    // kick:
    mixer.setVol(1, -12);
    mixer.setSendVol(1, 0, -18);

    // noisies:
    mixer.setVol(2, -9);
    mixer.setSendVol(2, 0, -15.2);

    // moog:
    mixer.setVol(3, -4.2);
    mixer.setSendVol(3, 0, -6);


    //noisies:
    noisies.sampler.makeSequence(\full, 'oneShot');

    // kick sequences:
    kick.makeSequence(\section1, 'oneShot');
    kick.makeSequence(\section2, 'oneShot');
    kick.makeSequence(\section3, 'oneShot');
    kick.makeSequence(\section4, 'oneShot');
    kick.makeSequence(\section5, 'oneShot');
    kick.makeSequence(\section6, 'oneShot');
    kick.makeSequence(\section7, 'oneShot');
    kick.makeSequence(\section8, 'oneShot');

  }

  prMakeNoisiesSequences {

    var blip, blipLong, white, purple, pink, gray, brown, blue;

    blip = noisies.sampler.bufferArray[0];
    blipLong = noisies.sampler.bufferArray[1];
    white = noisies.sampler.bufferArray[2];
    purple = noisies.sampler.bufferArray[3];
    pink = noisies.sampler.bufferArray[4];
    gray = noisies.sampler.bufferArray[5];
    brown = noisies.sampler.bufferArray[6];
    blue = noisies.sampler.bufferArray[7];

    noisies.sampler.addKey(\full, \dur, 1/6);
    noisies.sampler.addKey(\full, \buffer, Pseq(
      [
        [brown, blip], Rest, brown, Rest, [brown, purple], Rest,
        [brown, white, gray], Rest, [brown, purple], gray, brown, Rest,

        [brown, blipLong, gray], Rest, brown, gray, brown, Rest,
        [brown, gray, pink], Rest, [brown, purple], gray, brown, Rest,

        [brown, gray, blip], Rest, brown, gray, [brown, purple], Rest,
        [brown, gray, pink], Rest, [brown, blue], gray, brown, Rest,

        [brown, gray, blip], Rest, [brown, blue], Rest, [brown, gray, pink], Rest,
        [brown, blue, pink, purple], Rest, brown, gray, pink, brown
    ], inf));

  }

  prMakeKickSequences {
    var six06, peso, heartbeat, blip, eight08, dmx;

    six06 = kick.bufferArray[0];
    eight08 = kick.bufferArray[1];
    dmx = kick.bufferArray[2];
    peso = kick.bufferArray[3];
    heartbeat = kick.bufferArray[4];
    blip = kick.bufferArray[5];

    kick.addKey(\test, \dur, 0.25);
    kick.addKey(\test, \buffer, kick.bufferArray[0]);

    kick.addKey(\section1, \dur, 2);
    kick.addKey(\section1, \buffer, Pseq([[six06, eight08, dmx], [dmx, eight08]], inf));
    kick.addKey(\section1, \amp, 0.65);

    kick.addKey(\section2, \dur, 2);
    kick.addKey(\section2, \buffer, Pseq([[six06, eight08, dmx]], inf));
    kick.addKey(\section2, \amp, 0.7);

    kick.addKey(\section3, \dur, 1);
    kick.addKey(\section3, \buffer, Pseq([[six06, eight08, dmx], [dmx, eight08]], inf));
    kick.addKey(\section3, \amp, 0.7);

    kick.addKey(\section4, \dur, 1);
    kick.addKey(\section4, \buffer, Pseq([[six06, peso, eight08, dmx], [six06, dmx, eight08]], inf));
    kick.addKey(\section4, \amp, 0.7);

    kick.addKey(\section5, \dur, 2);
    kick.addKey(\section5, \buffer, six06);
    kick.addKey(\section5, \amp, 0.6);

    kick.addKey(\section6, \dur, Pseq([0.75, 0.25, 0.25, 0.75], inf));
    kick.addKey(\section6, \buffer, Pseq([six06, blip, heartbeat, blip], inf));
    kick.addKey(\section6, \amp, 0.6);

    kick.addKey(\section7, \dur, Pseq([0.75, 0.25, 0.25, 0.75], inf));
    kick.addKey(\section7, \buffer, Pseq([six06, blip, [six06, peso, heartbeat], blip], inf));
    kick.addKey(\section7, \amp, 0.6);

    kick.addKey(\section8, \dur, Pseq([0.75, 0.25, 0.25, 0.75], inf));
    kick.addKey(\section8, \buffer, Pseq([Rest, blip, heartbeat, blip], inf));
    kick.addKey(\section8, \amp, 0.4);
  }

  //////// public functions:

  free {
    main.free;
    kick.free;
    noisies.free;
    moog.free;
    this.freeModule;
    isLoaded = false;
  }

  //////// Main:
  playMainLoop {
    clock.playNextBar({
      main.playLoop;
      mainLoopIsPlaying = true;
    });
  }

  stopMainLoop {
    clock.playNextBar({
      main.stopLoop;
      mainLoopIsPlaying = false;
    });
  }

  toggleMainLoop {
    if( mainLoopIsPlaying == false,
      { this.playMainLoop },
      { this.stopMainLoop }
    );
  }


  //////// KICK:


  playKickSection1 {
    clock.playNextBar({
      kick.playSequence(\section1, clock);
      kickSection1IsPlaying = true;
    });
  }
  stopKickSection1 {
    clock.playNextBar({
      kick.stopSequence(\section1);
      kickSection1IsPlaying = false;
    });
  }
  tglKickSection1 {
    if( kickSection1IsPlaying == false,
      { this.playKickSection1 },
      { this.stopKickSection1 }
    );
  }

  playKickSection2 {
    clock.playNextBar({
      kick.playSequence(\section2, clock);
      kickSection2IsPlaying = true;
    });
  }
  stopKickSection2 {
    clock.playNextBar({
      kick.stopSequence(\section2);
      kickSection2IsPlaying = false;
    });
  }
  tglKickSection2 {
    if( kickSection2IsPlaying == false,
      { this.playKickSection2 },
      { this.stopKickSection2 }
    );
  }

  playKickSection3 {
    clock.playNextBar({
      kick.playSequence(\section3, clock);
      kickSection3IsPlaying = true;
    });
  }
  stopKickSection3 {
    clock.playNextBar({
      kick.stopSequence(\section3);
      kickSection3IsPlaying = false;
    });
  }
  tglKickSection3 {
    if( kickSection3IsPlaying == false,
      { this.playKickSection3 },
      { this.stopKickSection3 }
    );
  }

  playKickSection4 {
    clock.playNextBar({
      kick.playSequence(\section4, clock);
      kickSection4IsPlaying = true;
    });
  }
  stopKickSection4 {
    clock.playNextBar({
      kick.stopSequence(\section4);
      kickSection4IsPlaying = false;
    });
  }
  tglKickSection4 {
    if( kickSection4IsPlaying == false,
      { this.playKickSection4 },
      { this.stopKickSection4 }
    );
  }

  playKickSection5 {
    clock.playNextBar({
      kick.playSequence(\section5, clock);
      kickSection5IsPlaying = true;
    });
  }
  stopKickSection5 {
    clock.playNextBar({
      kick.stopSequence(\section5);
      kickSection5IsPlaying = false;
    });
  }
  tglKickSection5 {
    if( kickSection5IsPlaying == false,
      { this.playKickSection5 },
      { this.stopKickSection5 }
    );
  }

  playKickSection6 {
    clock.playNextBar({
      kick.playSequence(\section6, clock);
      kickSection6IsPlaying = true;
    });
  }
  stopKickSection6 {
    clock.playNextBar({
      kick.stopSequence(\section6);
      kickSection6IsPlaying = false;
    });
  }
  tglKickSection6 {
    if( kickSection6IsPlaying == false,
      { this.playKickSection6 },
      { this.stopKickSection6 }
    );
  }

  playKickSection7 {
    clock.playNextBar({
      kick.playSequence(\section7, clock);
      kickSection1IsPlaying = true;
    });
  }
  stopKickSection7 {
    clock.playNextBar({
      kick.stopSequence(\section7);
      kickSection7IsPlaying = false;
    });
  }
  tglKickSection7 {
    if( kickSection7IsPlaying == false,
      { this.playKickSection7 },
      { this.stopKickSection7 }
    );
  }

  playKickSection8 {
    clock.playNextBar({
      kick.playSequence(\section8, clock);
      kickSection8IsPlaying = true;
    });
  }
  stopKickSection8 {
    clock.playNextBar({
      kick.stopSequence(\section8);
      kickSection8IsPlaying = false;
    });
  }
  tglKickSection8 {
    if( kickSection8IsPlaying == false,
      { this.playKickSection8 },
      { this.stopKickSection8 }
    );
  }

  //////// NOISIES:
  playNoisiesLoop {
    clock.playNextBar({
      noisies.sampler.playSequence(\loop, clock);
      noisiesFullLoopIsPlaying = true;
    });
  }
  stopNoisiesLoop {
    clock.playNextBar({
      noisies.sampler.stopSequence(\loop);
      noisiesFullLoopIsPlaying = false;
    });
  }
  tglNoisiesLoop {
    if( noisiesFullLoopIsPlaying == false,
      { this.playNoisiesLoop },
      { this.stopNoisiesLoop }
    );
  }



}