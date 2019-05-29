/*
Thursday, January 16th 2014
Beram and McMinn

updating:
Sunday, June 8th 2014 prm
trying to get rid of buffer artifacts
prm

Thursday, June 19th 2014
converted to new system
prm

Monday, September 1st 2014
grain envelopes added
prm

*/

IM_Granulator : IM_Processor {

  var server, <synth, grainBufDict;
  var <isLoaded;
  var <crossfade;

  *new {

    |
    outBus = 0, grainDurLow = 0.01, grainDurHigh = 0.19, trigRate = 32, rateLow = 1, rateHigh = 1,
    posLow = 0.2, posHigh = 0.6, panLow = -1, panHigh = 1, sync = 0, bufLength = 3,
    send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, feedback = false,
    relGroup = nil, addAction = \addToHead
    |

    ^super.new(2, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prInit(
      grainDurLow, grainDurHigh, trigRate, rateLow, rateHigh, posLow, posHigh, panLow, panHigh, sync, bufLength);

  }

  prInit {
    |
    grainDurLow = 0.01, grainDurHigh = 0.19, trigRate = 32, rateLow = 1, rateHigh = 1,
    posLow = -0.9, posHigh = 0.9, panLow = -1, panHigh = 1, sync = 0, bufLength = 3
    |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDef;
      server.sync;
      this.prMakeGrainBuffers;
      server.sync;
      //while( { try { mixer.chanMono(0) } == nil }, { 0.01.wait } );
      while( { mixer.isLoaded.not; }, { 0.001.wait; });
      synth = Synth(\im_granulator_stereo, [\inBus, inBus, \outBus, mixer.chanStereo(0), \amp, 1,
        \grainDurLow, grainDurLow, \grainDurHigh, grainDurHigh, \trigRate, trigRate, \rateLow, rateLow, \rateHigh, rateHigh,
        \posLow, posLow, \posHigh, posHigh, \panLow, panLow, \panHigh, panHigh, \sync, sync, \bufLength, bufLength],
        group, \addToHead);
      while( { synth == nil }, { 0.001.wait; });

      crossfade = 1;

      isLoaded = true;
      //"loaded".postln;
    };
  }

  prAddSynthDef {

    SynthDef(\im_granulator_stereo, { // granulation of incoming audio
      |
      inBus = 0, outBus = 0, bufLength = 3, panLow = -1, panHigh = 1,
      grainDurLow = 0.01, grainDurHigh = 0.19, rateLow = 1, rateHigh = 1,
      posLow = 0.2, posHigh = 0.6, env = -1, sync = 0, trigRate = 32,
      amp = 1, mix = 1, cutoff = 20000
      |

      var input, dry, granStereoSum;
      var playhead, buffer, record, trigger, duration, position;
      var pan, rate, granulation, filter, sig;

      input = In.ar(inBus, 2);
      granStereoSum = Mix.ar([input[0], input[1]]) * 1;

      buffer = LocalBuf(server.sampleRate * bufLength, 1);
      buffer.clear;

      playhead = Phasor.ar(0, BufRateScale.kr(buffer), 0, BufFrames.kr(buffer));
      record = BufWr.ar(granStereoSum, buffer, playhead, 1);

      trigger = SelectX.ar(sync, [Dust.ar(trigRate), Impulse.ar(trigRate)]);
      duration = TRand.ar(grainDurLow, grainDurHigh, trigger);
      position = TRand.ar(posLow, posHigh, trigger);
      //position.poll;
      //if(position == 0, { position = position - 0.05 });
      position = Wrap.ar(playhead + position, 0, 1);
      rate = TRand.ar(rateLow, rateHigh, trigger);
      pan = TRand.ar(panLow, panHigh, trigger);

      granulation = GrainBuf.ar(2, trigger: trigger, dur: duration, sndbuf: buffer,
        rate: rate, pos: position, pan: pan, envbufnum: env);

      filter = LPF.ar(granulation, cutoff);
      sig = XFade2.ar(input, filter, mix);

      sig = sig * amp;
      sig = sig.softclip;
      Out.ar(outBus, sig);
    }).add;

  }

  prMakeGrainBuffers {
    {
      var grainEnvs;
      grainBufDict = IdentityDictionary.new;
      grainEnvs = (
        gabor:	Env.sine(1, 1),
        gabWide:	Env([0, 1, 1, 0], [1, 1, 1]),
        perc:	Env.perc(0.01, 0.99),
        revPerc:	Env.perc(0.99, 0.01),
        expodec:	Env.perc(0.01, 0.99, 1, 4),
        rexpodec:	Env.perc(0.99, 0.01, 1, 4)
      );
      server.sync;
      grainBufDict[\gabor] = Buffer.sendCollection(server, grainEnvs.at(\gabor).discretize, 1);
      grainBufDict[\gabWide] = Buffer.sendCollection(server, grainEnvs.at(\gabWide).discretize, 1);
      grainBufDict[\perc] = Buffer.sendCollection(server, grainEnvs.at(\perc).discretize, 1);
      grainBufDict[\revPerc] = Buffer.sendCollection(server, grainEnvs.at(\revPerc).discretize, 1);
      grainBufDict[\expodec] = Buffer.sendCollection(server, grainEnvs.at(\expodec).discretize, 1);
      grainBufDict[\rexpodec] = Buffer.sendCollection(server, grainEnvs.at(\rexpodec).discretize, 1);
      grainEnvs.do({ | env | env = nil; });
      grainEnvs = nil;
    }.fork;
  }

  //////// public:

  free {
    {
      grainBufDict.do({ | env | env.free; });
      server.sync;
      grainBufDict.do({ | env | env = nil; });
      grainBufDict = nil;
      synth.free;
      synth = nil;
    }.fork;
    this.freeProcessor;
  }


  setGrainDur { | grainDurLow = 0.01, grainDurHigh = 0.19 |
    this.setGrainDurLow(grainDurLow);
    this.setGrainDurHigh(grainDurHigh);
  }
  setGrainDurLow { | grainDurLow = 0.01 | synth.set(\grainDurLow, grainDurLow); }
  setGrainDurHigh { | grainDurHigh = 0.19 | synth.set(\grainDurHigh, grainDurHigh); }

  setTrigRate { | trigRate = 32 | synth.set(\trigRate, trigRate) }

  setPan { | panLow = -1, panHigh = 1 |
    this.setPanLow(panLow);
    this.setPanHigh(panHigh);
  }
  setPanLow { | panLow = -1 | synth.set(\panLow, panLow); }
  setPanHigh { | panHigh = 1 | synth.set(\panHigh, panHigh); }

  setRate { | rateLow = 1, rateHigh = 1 |
    this.setRateLow(rateLow);
    this.setRateHigh(rateHigh);
  }
  setRateLow { | rateLow = 1 | synth.set(\rateLow, rateLow); }
  setRateHigh { | rateHigh = 1 | synth.set(\rateHigh, rateHigh); }

  setPos { | posLow = 0.2, posHigh = 0.6 |
    this.setPosLow(posLow);
    this.setPosHigh(posHigh);
  }
  setPosLow { | posLow = 0.2 | synth.set(\posLow, posLow); }
  setPosHigh { | posHigh = 0.6 | synth.set(\posHigh, posHigh); }

  setSync { | sync = 0 |
    synth.set(\sync, sync);
  }

  setGrainEnvelope { | env = 'gabor' |
    switch(env,
      { 'hann' }, { synth.set(\env, -1) },
      { 'gabor' }, { synth.set(\env, grainBufDict[\gabor]); },
      { 'gabWide' }, { synth.set(\env, grainBufDict[\gabWide]); },
      { 'perc' }, { synth.set(\env, grainBufDict[\perc]); },
      { 'revPerc' }, { synth.set(\env, grainBufDict[\revPerc]); },
      { 'expodec' }, { synth.set(\env, grainBufDict[\expodec]); },
      { 'rexpodec' }, { synth.set(\env, grainBufDict[\rexpodec]); },
    );
  }

  setFilterCutoff { | cutoff = 20000 |
    synth.set(\cutoff, cutoff);
  }

  setCrossfade { | fade = 1 |
    crossfade = fade;
    synth.set(\mix, crossfade);
  }
}