/*
Wednesday, September 3rd 2014
PoppyNoSynth.sc
prm
*/

PoppyNoSynth : IM_Processor {

  var <isLoaded;
  var playTrigger, recorder, oscTrigger;
  var <buffer, segmentGroup;
  var <isTriggering, <trigRate;
  var <inputIsMuted;

  var <attackTime, <releaseTime, <sustainTimeLow, <sustainTimeHigh, <playRate;
  var <playVol, <filterCutoff;

  *new { | outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    feedback = false, relGroup = nil, addAction = \addToHead |
    ^super.new(2, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback, relGroup, addAction).prInit;
  }

  prInit {
    var server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      while({ try { mixer.isLoaded } != true }, { 0.001.wait });

      this.prInitParameters;
      this.prAddSynthDefs;
      buffer = Buffer.alloc(server, server.sampleRate * 20, 2);
      segmentGroup = Group.new(group, \addToHead);
      server.sync;
      recorder = Synth(\prm_poppy_recorder, [\inBus, inBus, \buffer, buffer], group, \addToHead);
      oscTrigger = OSCFunc({ | msg |
        var id, val, val2;
        id = msg.at(1);
        val = msg.at(2);
        val2 = msg.at(3);
        case
        { val == 1 } { this.playSegment;  }
      }, '/poppyNoSynth');
      server.sync;

      isLoaded = true;
    }
  }

  prInitParameters {
    isTriggering = false;
    attackTime = 0.05;
    releaseTime = 0.05;
    sustainTimeLow = 0.5;
    sustainTimeHigh = 1.5;
    playVol = 0;
    trigRate = 0.3;
    playRate = 1;
    filterCutoff = 20000;
  }

  prAddSynthDefs {

    SynthDef(\prm_poppy_playTrig, {
			| name, id = 1, trigger = 0, trigRate = 3, trigVal = 1 |
			var dust, impulse, trig, send;
			dust = Dust.kr(trigRate);
			impulse = Impulse.kr(trigRate);
			trig = Select.kr(trigger, [dust, impulse]);
			SendReply.kr(trig, '/poppyNoSynth', trigVal, id);
		}).add;

    SynthDef(\prm_poppy_recorder, {
      | inBus = 0, inputMute = 1, buffer, recordLevel = 1, preLevel = 0.6, loop = 1, recordTrigger = 1 |
      var input, record;
      input = In.ar(inBus, 2) * inputMute;
      record = RecordBuf.ar(input, buffer, 0, recordLevel, preLevel, recordTrigger, loop, 1);
    }).add;

    SynthDef(\prm_poppy_playBuf, {
      |
      outBus, amp = 1, buffer, loop = 1, rate = 1,
      attackTime = 0.05, releaseTime = 0.05, sustainTime = 1, startPos = 0, cutoff = 20000
      |
      var playBuf, env, filter, sig;
      playBuf = PlayBuf.ar(2, buffer, rate, 1, startPos, loop);
      env = EnvGen.kr(Env.linen(attackTime, sustainTime, releaseTime), 1, doneAction: 2);
      filter = LPF.ar(playBuf, cutoff);
      sig = filter * env;
      sig = sig * amp;
      sig = Out.ar(outBus, sig);
    }).add;

  }

  //////// Public Functions:

  free {
    if( isTriggering == true, { this.freeTrigger });
    playTrigger = nil;
    segmentGroup.free;
    segmentGroup = nil;
    recorder.free;
    recorder = nil;
    oscTrigger.free;
    oscTrigger = nil;
    this.freeProcessor;
  }

  toggleMuteInput {
    if( inputIsMuted == true, { this.unMuteInput; }, { this.muteInput; });
  }
  muteInput {
    recorder.set(\inputMute, 0);
    inputIsMuted = true;
  }
  unMuteInput {
    recorder.set(\inputMute, 1);
    inputIsMuted = false;
  }

  clearBuffer {
    buffer.zero;
  }

  toggleTrigger {
    if( isTriggering == true, { this.freeTrigger; }, { this.makeTrigger; });
  }

  makeTrigger {
    playTrigger = Synth(\prm_poppy_playTrig, [\trigRate, trigRate], group, \addToHead);
    isTriggering = true;
  }

  freeTrigger {
    playTrigger.free;
    isTriggering = false;
  }

  playSegment {
    Synth(\prm_poppy_playBuf, [\outBus, mixer.chanStereo, \buffer, buffer, \amp, playVol.dbamp,
      \attackTime, attackTime, \releaseTime, releaseTime, \sustainTime, rrand(sustainTimeLow, sustainTimeHigh),
      \startPos, buffer.numFrames * 1.0.rand, \rate, playRate, \cutoff, filterCutoff],
      segmentGroup, \addToHead);
  }

  setAttackTime { | attack = 0.05 | attackTime = attack }
  setReleaseTime { | release = 0.05 | releaseTime = release; segmentGroup.set(\releaseTime, releaseTime) }
  setSustainTimeLow { | time = 0.5 | sustainTimeLow = time }
  setSustainTimeHigh { | time = 1.5 | sustainTimeHigh = time }
  setSustainTime { | timeLow = 0.5, timeHigh = 1.5 |
    this.setSustainTimeLow(timeLow);
    this.setSustainTimeHigh(timeHigh);
  }
  setPlayVol { | vol = 0 | playVol = vol; }
  setTrigRate { | rate = 0.3 |
    trigRate = rate;
    if( isTriggering = true, { playTrigger.set(\trigRate, trigRate) });
  }
  setPlayRate { | rate = 1 | playRate = rate }
  setCutoff { | cutoff = 20000 | filterCutoff = cutoff; }
  setPreLevel { | level = 0.6 | recorder.set(\preLevel, level); }
}