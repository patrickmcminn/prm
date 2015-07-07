x = Connections_NoteRecord.new;
x.isLoaded;
y = IM_HardwareIn.new(0, x.inBus);
y.free;
x.resetBasslineRecordRoutine;
x.resetCascadeRecordRoutine;
x.basslineRecordRoutine.next;
x.cascadeRecordRoutine.next;

x.noteBufferArray[0].play;

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

z = Connections_Inlet.new(~prm.submixB, x.noteBufferArray, x.cascadeBufferArray, ~prm.irLibrary.irDict['3.4Cathedral'],
  ~prm.procGroup, \addToHead);

z.isLoaded;
z.attackRandomizer.isLoaded;
z.cascade.playSequences(a);

z.attackRandomizer.playSequences(a);
Server.killAll;

z.granulator.setGranulatorCrossfade(-1);


x = { SoundIn.ar(0) }.play;

Server.killAll;