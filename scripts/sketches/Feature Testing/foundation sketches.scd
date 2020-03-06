MIDIIn.connectAll;


~pyr = MIDIIn.findPort("Pyramid MIDI USB", "Pyramid MIDI USB");
~pyrOut = MIDIOut.newByName("Pyramid MIDI USB", "Pyramid MIDI USB");

APC40Mk2

~chord = Foundation_ChordSynth.new(~prm.audioIn, ~pyr, 9 , ~prm.procGroup, \addToHead);
~chord.isLoaded;
~chord.sequencer;

~pyr.uid

~chord.playNote(220);
~chord.releaseNote(220);

s.meter
~bass = Foundation_BassSection.new(~prm.audioIn, ~pyr, 1, 2, ~prm.procGroup, \addToHead);
~bass.free;

~bass.eq.setHighPassCutoff(0);
~cello = Foundation_Cellos.new(~prm.audioIn, ~pyr, 10, ~prm.procGroup, \addToHead);

~bass.satur.playNote(110);

127.ccdbfs


~cello.subtractive.playNote(220);
~cello.subtractive.releaseNote(220);
APC40Mk2

MIDIClient.list;

MIDIClient.sources

w = Array.fill(128, { | i | MIDIFunc.noteOn({  ~chord.playNote(i.midicps) }, i, 8, ~pyr.uid); });
z = Array.fill(128, { | i | MIDIFunc.noteOff({ ~chord.releaseNote(i.midicps) }, i, 8, ~pyr.uid); });

w.free;
z.free;


~found = Foundation_Moog.new(~modInput[0], ~prm.audioIn, ~moogDevice, ~moogPort, ~pyr, 0, ~prm.procGroup, \addToHead);

~found.moog.playNote(220);
~found.moog.releaseNote(220);


~found.eq.setLowPassCutoff(20000);
~found.eq.setHighPassCutoff(20);

s.meter

~found.free;

127.ccdbfs.dbamp

~cello.subtractive.setReleaseTime(2.5);


MIDIFunc

~found

x = Mother32.new(~modInput[0], ~prm.audioIn, "Pyramid MIDI USB", "Pyramid MIDI USB", relGroup: ~prm.procGroup, addAction: \addToHead);

x.playNote(440);
x.releaseNote(440);


x.makeSequence(\test);

x.addKey(\test, \dur, 0.25);
x.addKey(\test, \note, Pseq([0, 2, 4, 5, 12], inf));

x.playSequence(\test);

x.stopSequence(\test)


Foundation_BassSection

x = SaturSynth.new(0);

x.playNote(110);

x.releaseNote(110);