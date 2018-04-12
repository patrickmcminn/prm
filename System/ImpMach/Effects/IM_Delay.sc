/*
ADAPTED INTO SYNTHDEF BY JAB FROM:

Here's a little patch I put together, it's a bucket brigade device emulation.
sampleRate controls delay time, msLength sets maximum delay time in ms.
Low pass frequencies in ~bbdBlock vary aliasing effect.
Several shorter modulated ~bbdBlocks can be combined to make a chorus.
Any suggestions or improvements welcome.

Cheers,
Graeme
*/

IM_Delay : IM_Processor {
  var synth;
  var <isLoaded;

  *new { |outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    feedback = false, relGroup = nil, addAction = \addToHead|

    ^super.new(2, 1, outBus, send0Bus, send1Bus, send2Bus, send3Bus, feedback,
      relGroup, addAction).prInit;
  }

  prInit {
    var server = Server.default;

    server.waitForBoot {
      isLoaded = false;
      this.prAddSynthDefs;
      server.sync;
      //while( { try { mixer.chanMono(0) } == nil }, { 0.01.wait } );
      while( { mixer.isLoaded.not }, { 0.001.wait; });
      synth = Synth(\IM_bbdDelay, [\inBus, inBus, \outBus, mixer.chanMono(0)], group);
      while({ synth == nil }, { 0.001.wait; });
      isLoaded = true;
    };
  }

  prAddSynthDefs {
    // Should any other arguments be lagged?
    // Add compander and sampleRate lfo. Lowering the sample rate kills the feedback
    SynthDef(\IM_bbdDelay, { |inBus = 1, preAmp = 1, msLength = 50, sampleRate = 16000, lpFreqScale = 2,
      feedback = 0.7, mix = 1, outBus = 0|
      var input, local, signal;
      var delayInput, output;
      var numSamples, buf, clk, count, readpos, writepos, write, read;
      var lpFreq, inputFilt, outputFilt;

      input = In.ar(inBus, 2) * preAmp;
      local = LocalIn.ar(input.numChannels);
      signal = local * feedback;
      delayInput = (input + signal).softclip;

      // Allocate buffer
      numSamples = ((SampleRate.ir / 1000) * msLength).floor;
      buf = LocalBuf(numSamples * 2, delayInput.numChannels).clear;

      // Clock / read and write head calculation
      sampleRate = sampleRate.lag;
      clk = Impulse.ar(sampleRate);
      count = PulseCount.ar(clk);

      readpos = count % (numSamples * 2);
      writepos = (numSamples + count) % (numSamples * 2);

      // Antialias input filter
      lpFreq = (sampleRate / 8).max(400).min(18000);
      inputFilt = LPF.ar(delayInput, lpFreq * lpFreqScale);

      // Buffer read/write
      write = BufWr.ar(inputFilt, buf, writepos);
      read = BufRd.ar(inputFilt.numChannels, buf, readpos);

      // Antialias output filter / output
      outputFilt = DFM1.ar(read, lpFreq, 0, 0.47, 0, 0.0002);  // adjust to taste

      // Output
      LocalOut.ar(outputFilt);

      output = XFade2.ar(input, signal, mix);
      Out.ar(outBus, output);
    }).add;
  }

  setMSLength { |msLength = 700| synth.set(\msLength, msLength) }
  setSampleRate { |sampleRate = 16000| synth.set(\sampleRate, sampleRate) }
  setFeedback { |feedback = 0.7| synth.set(\feedback, feedback) }
  setMix { |mix = -0.3| synth.set(\mix, mix) }  // -1 is completely dry, 1 is all wet
  setPreAmp { | amp = 1 | synth.set(\preAmp, amp); }
  setLPFreqScale { | scale = 1 | synth.set(\lpFreqScale, scale); }

  free {
    synth.free;
    synth = nil;

    this.freeProcessor;
  }
}