x = Docile_Moog(3, 0, "UMC404HD 192k", "UMC404HD 192k");


x.isLoaded;

Server.killAll;

x.playArp;

x.playArpRev;

s.meter

~prm = AudioSystem.new(2);


x.granulator.mixer.setPreVol(-18);

x.granulator.setGranulatorCrossfade(0);

x.granulator.setGrainEnvelope('rexpodec');


x = Docile_Main.new(0);

x.playLoop;


~prm = AudioSystem.new;
(
~docile = Docile.new(~prm.submixA, "UMC404HD 192k", "UMC404HD 192k", ~prm.reverb.inBus, ~prm.granulator.inBus,
  ~prm.modular.inBus, relGroup: ~prm.procGroup, addAction: \addToHead);
)

~ohm = OhmRGB.new;
~base = Base.new;

~ohm.setPage('docile');
~base.setPage('docile');
s.meter
Server.killAll;


s.meter