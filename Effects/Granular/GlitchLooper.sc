/*
Wednesday, December 2nd 2015
driving from Burlington, VT to Portland, ME
GlitchLooper.sc
prm
*/

GlitchLooper : IM_Processor {

  var server, <isLoaded;
  var recorder, trigger, osc;
  var <buffer;
  var <startPosLow, <startPosHigh, <posOffsetLow, <posOffsetHigh;
  var startPos, endPos, <repeats;
  var pattern, <trigFreq;

  *newStereo { | outBus = 0, bufLength = 1, send0Bus, send1Bus, send2Bus, send3Bus,
    relGroup = nil, addAction = 'addToHead' |
    ^super.new(1, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInitStereo(bufLength);
  }

  prInitStereo { | bufLength |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDefs;

      server.sync;

      startPosLow = 0;
      startPosHigh = 0.9;
      posOffsetLow = 0.05;
      posOffsetHigh = 0.1;
      startPos = 0;
      endPos = 0.05;
      repeats = 26;
      trigFreq = 0.7;

      buffer = Buffer.alloc(server, server.sampleRate * bufLength, 2);

      while({ try { mixer.isLoaded } != true }, { 0.001.wait; });
      server.sync;
      this.prMakePattern;
      server.sync;
      trigger = Synth(\prm_GlitchLooper_OSCTrigger, [\trigRate, trigFreq], group, \addToHead);
      recorder = Synth(\prm_GlitchLooper_Recorder, [\inBus, inBus, \buffer, buffer], group, \addToHead);
      server.sync;


      isLoaded = true;
    }
  }

  prAddSynthDefs {

    SynthDef(\prm_GlitchLooper_Recorder, {
      | inBus = 0, buffer |
      var input, recorder;
      input = In.ar(inBus, 2);
      recorder = RecordBuf.ar(input, buffer, 0, 1, 0, 1, 1, 1);
    }).add;

    SynthDef(\prm_GlitchLooper_Player, {
      | outBus = 0, buffer, startPos = 0, endPos = 1,
      attackTime = 0.01, releaseTime = 0.01 |
      var playhead, player, sustainTime, envelope, sig;
      playhead = Phasor.ar(0, 1, startPos * BufFrames.ir(buffer), endPos * BufFrames.ir(buffer));
      player = BufRd.ar(2, buffer, playhead, 1, 2);
      sustainTime = (BufFrames.ir(buffer)/SampleRate.ir) * (endPos - startPos);
      sustainTime = sustainTime - (attackTime + releaseTime);
      //sustainTime.poll;
      envelope = EnvGen.ar(
        Env.linen(attackTime, sustainTime, releaseTime), 1, doneAction: 2);
      sig = player * envelope;
      Out.ar(outBus, sig);
    }).add;

    SynthDef(\prm_GlitchLooper_OSCTrigger, {
			| name, id = 1, trigger = 0, trigRate = 3, trigVal = 1 |
			var dust, impulse, trig, send;
			dust = Dust.kr(trigRate);
			impulse = Impulse.kr(trigRate);
			trig = Select.kr(trigger, [dust, impulse]);
			SendReply.kr(trig, '/glitchLooper', trigVal, id);
		}).add;

  }

  prMakePattern {
    pattern = Pbind(
      \instrument, \prm_GlitchLooper_Player,
      \buffer, buffer,
      \outBus, mixer.chanStereo(0),
      \group, group,
      \addAction, \addToHead,
      \startPos, Pfunc({ startPos }),
      \endPos, Pfunc({ endPos }),
      \attackTime, 0.001,
      \releaseTime, 0.001,
      //\repeater, Pfunc({ repeats }),
      \durCalc, Pfunc({(buffer.numFrames/server.sampleRate) * (endPos - startPos)}),
      \dur, Pseq([Pkey(\durCalc)],  29 ),
      \legato, 1
    );

    osc = OSCFunc({ | msg |
        var id, val, val2;
        id = msg.at(1);
        val = msg.at(2);
        val2 = msg.at(3);
        case
        { val == 1 } { this.playGlitch;  }
      }, '/glitchLooper');
  }

  playGlitch {
    startPos = rrand(startPosLow, startPosHigh);
    endPos = startPos + rrand(posOffsetLow, posOffsetHigh);
    //startPos.postln;
    //endPos.postln;
    //pattern.play(quant:0);
    Pbind(
      \instrument, \prm_GlitchLooper_Player,
      \buffer, buffer,
      \outBus, mixer.chanStereo(0),
      \group, group,
      \addAction, \addToHead,
      \startPos, startPos ,
      \endPos, endPos ,
      \attackTime, 0.001,
      \releaseTime, 0.001,
      //\repeater, Pfunc({ repeats }),
      \dur, Pseq([(buffer.numFrames/server.sampleRate) * (endPos - startPos)],  repeats),
      \legato, 1
    ).play(quant: 0);
  }

  setRepeats { | num = 16 | repeats = num }
  setTriggerFrequency { | freq = 0.7 |
    trigFreq = freq;
    trigger.set(\trigRate, trigFreq);
  }
  setStartPosLow { | posLow = 0 | startPosLow = posLow }
  setStartPosHigh { | posHigh = 0.8 | startPosHigh = posHigh }
  setPosOffsetLow { | offset = 0.05 | posOffsetLow = offset; }
  setPosOffsetHigh { | offset = 0.2 | posOffsetHigh = offset; }
}

