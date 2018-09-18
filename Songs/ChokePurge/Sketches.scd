~prm = AudioSystem.new(2);

~prm.reverb.setHighPassFreq(150);
~prm.reverb.setLowPassFreq(20000);

(
~choke = IM_Mixer.new(16, ~prm.audioIn,
  ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
  relGroup: ~prm.procGroup, addAction: \addToHead);
);


// ~path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/ChokePurge/samples/feedback 4.aif";

~path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/ChokePurge/samples/Feedback4.wav"

~fb = SamplePlayer.newStereo(~choke.chanStereo(0), ~path);

~fb.playSampleSustaining('fb4', -3, 0.25);
~fb.playSampleSustaining('fb48va', -9, 0.5);
~fb.playSampleSustaining('fb3', -12, 1)

~fb.releaseSampleSustaining('fb48va');
~fb.releaseSampleSustaining('fb48va');
~fb.releaseSampleSustaining('fb3');

~fb.mixer.setVol(-3);
~choke.setVol(0, -3);
~choke.setSendVol(0, 0, -18);

~fb.mixer.unMute
~fb.mixer.mute
~fb.mixer.tglMute

s.meter

z = GrainCloud2.new(0);
z.setInstArray([\gaborGendy]);
z.addNote((63-12).midicps);


~choke.setVol(1, -9);
~choke.setSendVol(1, 0, -21);
~choke.setSendVol(1, 1, -15);
~beaut = Beauty.newMono(~choke.chanStereo(1));

~beaut.mixer.setVol(0);
~beaut.setLowPassCutoffs(1500, 1600);
~beaut.setLowPassCutoffs(3500, 4500);
~beaut.setLowPassCutoffLeft(3500);
~beaut.setLowPassCutoffRight(4500);

~beautyChange = r {
  loop {
    ~beaut.setDelayTimes(rrand(0.001, 0.1), rrand(0.001, 0.1), rrand(0.001, 0.1), rrand(0.001, 0.1));
    ~beaut.setPans(rrand(-1, 1), rrand(-1, 1), rrand(-1, 1), rrand(-1, 1));
    1.wait;
  }
};
~beautyChange.play

~choke.setVol(2, -12);
~choke.setSendVol(2, 0, -21);
~fbSynth = FeedbackSynth.new(~choke.chanStereo(2), addAction: 'addToHead');
~fbSynth.mixer.setVol(-12);

~fbSynth.playNote(34.midicps);
~fbSynth.playNote(39.midicps);
~fbSynth.setFilterCutoff(1600);
~fbSynth.mixer.setPanBal(-0.5);

~choke.setVol(3, -9);
~choke.setSendVol(3, 0, -6);
~triad = Subtractive.new(~choke.chanStereo(3));
~triad.isLoaded;


~triad.setPanLFO2Bottom(-1);
~triad.setPanLFO2Top(1);
~triad.setPanLFO1Bottom(-1);
~triad.setPanLFO1Top(1);
~triad.setLFO1Waveform('noise');
~triad.setLFO2Waveform('noise');
~triad.setFilterCutoffLFO1BottomRatio(0.3);
~triad.setFilterCutoffLFO1TopRatio(2);
~triad.setLFO1Freq(0.3);
~triad.setLFO2Freq(0.3);
~triad.setFilterCutoff(1700);
~triad.setOsc1Waveform('rect');
~triad.setOsc2Waveform('rect');
~triad.setOsc1PulseWidth(0.2);
~triad.setOsc2PulseWidth(0.8);
~triad.setOsc2DetuneCents(-20);


~triad.playNote(65.midicps);
~triad.playNote(61.midicps);
~triad.playNote(58.midicps);
//~triad.mixer.setVol(-9);

~choke.setVol(4, -6);
~choke.setSendVol(4, 0, -12);
~glitchy = GlitchySynth.new(~choke.chanStereo(4));
~glitchy.setWaveLossAmount(15);
~glitchy.setBitDepth(4)
~glitchy.isLoaded;
~glitchy.playNote(85.midicps, 0.6);
~glitchy.releaseNote(85.midicps);
~glitchy.setFilterCutoff(2300)
~glitchy.playNote(55.midicps);
~glitchy.releaseNote(55.midicps);

~glitchy.playNote(65.midicps);

~choke.setVol(5, -9);
~bass = SaturSynth.new(~choke.chanStereo(5));
~bass.isLoaded;
//~bass.mixer.setVol(-9);
~bass.playNote(34.midicps);
~bass.releaseNote(34.midicps);



s.makeWindow;


~path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/ChokePurge/samples/Feedback4.wav"
~buf = Buffer.read(s, ~path);
(
{
var in, chain;
  //in = Saw.ar(55).dup;
  in = PlayBuf.ar(2, ~buf, loop: 1);
  chain = FFT({LocalBuf(2048, 1)}.dup, in);
  chain = PV_BrickWall(chain, LFNoise2.kr([4, 4.1]).range(-1, 0.8));
  IFFT(chain) // inverse FFT
}.play;
)

(
{
var in, chain;
  //in = Saw.ar(55).dup;
  in = PlayBuf.ar(2, ~buf, loop: 1);
  chain = FFT({LocalBuf(2048, 1)}.dup, in);
  chain = PV_BinScramble(chain, 0.5, 0.3, Impulse.kr(1));
  IFFT(chain) // inverse FFT
}.play;
)

(
{
  var in, chain;
  //in = Saw.ar(55).dup;
  in = PlayBuf.ar(2, ~buf, loop: 1);
  chain = FFT({LocalBuf(2048, 1)}.dup, in);
  chain = PV_BinShift(chain, LFNoise2.kr(1).range(0.25, 4), 0, 1);
  //chain = PV_BinScramble(chain, 0.5, 0.3, Impulse.kr(1));
  IFFT(chain) // inverse FFT
}.play;
)

(
{
  var in1, in2, chain1, chain2, chain;
  in1 = PlayBuf.ar(2, ~buf, loop: 1);
  in2 = PlayBuf.ar(2, ~buf, 0.5, loop: 1);
  chain1 = FFT(LocalBuf(2048), in1);
  chain2 = FFT(LocalBuf(2048), in2);
  chain = PV_RandWipe(chain1, chain2, LFNoise1.kr(0.25).range(0.2, 0.8), Impulse.kr(0.5));
  IFFT(chain).dup;
}.play;
)


s.makeWindow;





