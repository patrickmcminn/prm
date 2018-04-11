MIDIIn.connectAll;

Server.killAll;

~prm = AudioSystem.new(2);
x = IM_Mixer_1Ch.new(~prm.submixA, ~prm.reverb.inBus);
~bass = Darkness_Bass.new(3, x.chanStereo(0), "UMC1820", "UMC1820");

x.setSendVol(0, -12);

~bass.moog.playSequence(\darkness);

x.setVol(-9);


s.meter

~bass.moog.addKey(\darkness, \octave, 3);

~bass.moog.sequencerClock.tempo = 2;


~bass.moog.stopSequence(\darkness);
Mother32

s.meter