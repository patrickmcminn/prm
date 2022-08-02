MIDIClient.init;    // if not already done

~mOut = MIDIOut.newByName("Pyramid MIDI USB", "Pyramid MIDI USB").latency_(Server.default.latency);
~clock1 = TempoClock.new(136/60);
~clock2 = TempoClock.new((136/60)*(16/15));
~clock2.free;

(

p = Pbind(
	\type, \midi,
	\midiout, ~mOut,
	\chan, 0,
	\note, 0,
	\dur, Pseq([0.5, 0.25, 0.25, 0.25, 0.25, 0.75], inf),
	\legato, 0.2,
).play(~clock1);

q = Pbind(
	\type, \midi,
	\midiout, ~mOut,
	\chan, 1,
	\note, 1,
	\dur, Pseq([0.5, 0.25, 0.25, 0.25, 0.25, 0.75], inf),
	\legato, 0.2,
).play(~clock2);

)

(
p.stop;
q.stop;
)


