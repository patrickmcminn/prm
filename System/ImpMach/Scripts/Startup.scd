fork {
  ~im = IM_AudioSystem(2);
  while({ try { ~im.isLoaded } != true }, { 0.001.wait; });

  //~directIns = IM_BandDirect([0, 1, 2, 3], ~im.systemMixer.inBus(0), ~im.delay.inBus, ~im.reverb.inBus, ~im.granulator.inBus, nil, relGroup: ~im.procGroup, addAction: \addToHead);
  ~im.makeSong('BandDirect');
  while({ try { ~im.getSong('BandDirect').isLoaded } != true }, { 0.001.wait; });

  try { "Ohm/LoadOhm.scd".loadRelative; };
};

s.meter;