x = Connections_NoteRecord.new;
x.isLoaded;
y = IM_HardwareIn.new(0, x.inBus);
y.free;
x.resetBasslineRecordRoutine;
x.resetCascadeRecordRoutine;
x.basslineRecordRoutine.next;
x.cascadeRecordRoutine.next;

x.noteBufferArray[5].play;
x.cascadeBufferArray[2].play;

z = Connections_AttackRandomizer.new(0, x.noteBufferArray);
z.isLoaded;

z = Connections_Cascade.new(0, x.noteBufferArray, x.cascadeBufferArray);


z.playSequences(a);
z.stopSequences(a);
z.stopSequences;

  a = TempoClock.new;
  a.tempo = 75/60;

Sampler

~prm = AudioSystem.new(2);
~prm.irLibrary.irDict['3.4Cathedral'];

~irLibrary = IM_IRLibrary.new("~/Library/Application Support/SuperCollider/Extensions/prm/Effects/Reverb/ImpulseResponses")
~irLibrary.irDict;

z = Connections_Inlet.new(0, x.noteBufferArray, x.cascadeBufferArray, ~irLibrary.irDict['3.4Cathedral']);
z.isLoaded;
z.attackRandomizer.isLoaded;
z.cascade.playSequences(a);

z.attackRandomizer.playSequences(a);
Server.killAll;

z.granulator.setGranulatorCrossfade(-1);

z.reverb.isLoaded;
z.splitter.isLoaded;
z.cascade.isLoaded;
z.attackRandomizer.isLoaded

z.attackRandomizer.stopSequences;
z.cascade.stopSequences;
x = { SoundIn.ar(0) }.play;

Server.killAll;

z.mixer.setVol(-12);
s.meter;


b = Droner.newMono(~prm.submixA, send0Bus: ~prm.reverb.inBus, relGroup: ~prm.procGroup, addAction: \addToHead);
b.isLoaded;


t = IM_HardwareIn.new(0, c.inBus)
c = Connections_AirSputters.new(~prm.submixA, a, ~prm.procGroup, \addToHead);
c.isLoaded;

c.granulator.setGranulatorCrossfade(1);

c.mixer.setPreVol(28);

s.meter;

~prm.isLoaded


~prm = AudioSystem.new(2);

x = Connections.new(~prm.submixA, ~prm.submixB, ~prm.submixC, ~prm.irLibrary('3.4Cathedral'),
  ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, nil, ~prm.procGroup, \addToHead);