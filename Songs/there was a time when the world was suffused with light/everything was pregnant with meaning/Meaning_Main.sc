/*
Wednesday, April 18th 2018
Meaning_Main.sc
prm

everything was pregnant with meaning
*/

Meaning_Main : IM_Module {

  var <isLoaded;
  var server;

  var <lowEQ, <highEQ, <antenna, splitter, <sampler;

  var antennaBus;

  var <note1IsPlaying, <note2IsPlaying, <note3IsPlaying, <note4IsPlaying;

  *new { | outBus = 0, relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, relGroup: relGroup, addAction: addAction).prInit;
  }

  prInit {
    server = Server.default;
    server.waitForBoot {

      var path, sampleArray;

      path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/there was a time when the world was suffused with light/everything was pregnant with meaning/samples/melody/";
      sampleArray = (path ++ "*").pathMatch;

      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

      this.prAddSynthDef;

      antennaBus = Bus.audio(server, 2);

      server.sync;

      lowEQ = Equalizer.newStereo(mixer.chanStereo(0), group, \addToHead);
      while({ try { lowEQ.isLoaded } != true }, { 0.001.wait; });

      antenna = Synth(\prm_Meaning_brokenAntenna, [\inBus, antennaBus, \outBus, mixer.chanStereo(0)],
        group, \addToHead);
      while({ antenna == nil }, { 0.001.wait; });
      server.sync;

      highEQ = Equalizer.newStereo(antennaBus, group, \addToHead);
      while({ try { highEQ.isLoaded } != true }, { 0.001.wait; });

      splitter = Splitter.newStereo(2, [lowEQ.inBus, highEQ.inBus], relGroup: group, addAction: \addToHead);
      while({ try { splitter.isLoaded } != true }, { 0.001.wait; });

      sampler = Sampler.newStereo(splitter.inBus, sampleArray, relGroup: group, addAction: \addToHead);
      while({ try { sampler.isLoaded } != true }, { 0.001.wait; });

      this.prInitializeParameters;

      server.sync;

      isLoaded = true;
    }
  }

  prInitializeParameters {
    note1IsPlaying = false;
    note2IsPlaying = false;
    note3IsPlaying = false;
    note4IsPlaying = false;


    // mixer:
    lowEQ.mixer.setPreVol(-9);
    highEQ.mixer.setPreVol(-9);

    // low EQ:
    lowEQ.setLowPassCutoff(219);
    lowEQ.setLowFreq(116);
    lowEQ.setLowGain(6.07);
    lowEQ.setLowRQ(1.40845070422535);

    // high EQ:
    highEQ.setHighPassCutoff(400);
    highEQ.setLowPassCutoff(10050);
    highEQ.setPeak1Freq(1450);
    highEQ.setPeak1Gain(6.07);
    highEQ.setPeak1RQ(1.40845070422535);
    highEQ.setPeak2Freq(1110);
    highEQ.setPeak2Gain(14.3);
    highEQ.setPeak2RQ(0.05555555555556);


    // Sampler:
    sampler.setReleaseTime(0.5);

  }

  prAddSynthDef {
    SynthDef(\prm_Meaning_brokenAntenna, {
      |
      inBus, outBus, mix = 1,
      lowFreq = 120, lowAtk = 0.00137, lowRel = 0.008
      lowInputGain = 0.7079, lowOutputGain = 1.07152,
      lowThresh = 0.14125, lowBelowRatio = 4, lowAboveRatio = 0.23,
      midAtk = 0.00228, midRel = 0.0096,
      midInputGain = 0.6839, midOutputGain = 0.71614,
      midThresh = 0.14125, midBelowRatio = 4, midAboveRatio = 0.465 ,
      hiFreq = 3250, hiAtk = 0.00042, hiRel = 0.00449,
      hiInputGain = 0.6839, hiOutputGain = 1.92752,
      hiThresh = 0.23041, hiBelowRatio = 4,  hiAboveRatio = 1,
      outputGain = 1,
      amp = 1
      |

      var input, dry, low, mid, hi, sum, sig;
      input = In.ar(inBus, 2);
      dry = input * (1-mix);
      low = LPF.ar(input, lowFreq);
      low = low * lowInputGain;
      low = Compander.ar(low, low, lowThresh, lowBelowRatio, lowAboveRatio, lowAtk, lowRel);
      low = low * lowOutputGain;
      mid = BBandPass.ar(input, 1685, 4);
      mid = mid * midInputGain;
      mid = Compander.ar(mid, mid, midThresh, midBelowRatio, midAboveRatio, midAtk, midRel);
      mid = mid * midOutputGain;
      hi = HPF.ar(input, hiFreq);
      hi = hi * hiInputGain;
      hi = Compander.ar(hi, hi, hiThresh, hiBelowRatio, hiAboveRatio, hiAtk, hiRel);
      hi = hi*hiOutputGain;
      sum = low + mid + hi;
      sig = sum * outputGain;
      sig = sig * mix;
      sig = dry + sig;
      sig = sig * amp;
      sig = sig.softclip;
      sig = Out.ar(outBus, sig);
    }).add;
  }

  //////// public functions:
  free {
    sampler.free;
    splitter.free;
    lowEQ.free;
    highEQ.free;
    antenna.free;
    antennaBus.free;
    this.freeModule;
    isLoaded = false;
  }

  playNote1 {
    sampler.playSampleSustaining('note1', 0, 0, 1);
    note1IsPlaying = true;
  }
  releaseNote1 {
    sampler.releaseSampleSustaining('note1');
    note1IsPlaying = false;
  }

  playNote2 {
    sampler.playSampleSustaining('note2', 0, 0, 2.midiratio);
    note2IsPlaying = true;
  }
  releaseNote2 {
    sampler.releaseSampleSustaining('note2');
    note2IsPlaying = false;
  }

  playNote3 {
    sampler.playSampleSustaining('note3', 0, 0, -3.midiratio);
    note3IsPlaying = true;
  }
  releaseNote3 {
    sampler.releaseSampleSustaining('note3');
    note3IsPlaying = false;
  }

  playNote4 {
    sampler.playSampleSustaining('note4', 1, 0,  1);
    note4IsPlaying = true;
  }
  releaseNote4 {
    sampler.releaseSampleSustaining('note4');
    note4IsPlaying = false;
  }
}