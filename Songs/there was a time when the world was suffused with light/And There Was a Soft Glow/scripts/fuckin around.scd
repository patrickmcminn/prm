x = Subtractive.new(0);
MIDIIn.connectAll;

x.playNote(220);
x.releaseNote(220);

y = Array.fill(127, { | i | MIDIFunc.noteOn({ x.playNote(i.midicps) }, i, 8); });
z = Array.fill(127, { | i | MIDIFunc.noteOff({ x.releaseNote(i.midicps) }, i, 8); });
y.free;

x.setReleaseTime(0.3);

x.setSustainLevel(0.3);
x.setDecayTime(0.4);
x.setAttackTime(0.45);



(
var path = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/there was a time when the world was suffused with light/And There Was a Soft Glow/Samples/gurgler/";
~sampleArray = (path ++ "*").pathMatch;
);
y = Sampler.newStereo(0, ~sampleArray);

y.playSampleSustaining(\loop1, 0, -6);
y.releaseSampleSustaining(\loop1);

y.playSampleSustaining(\loop2, 1, -6);
y.releaseSampleSustaining(\loop2);

y.playSampleSustaining(\loop3, 2, -6);
y.releaseSampleSustaining(\loop3);




MIDIFunc.noteOn({ y.playSampleSustaining(\loop1, 0, -6); }, 60, 11);
MIDIFunc.noteOff({ y.releaseSampleSustaining(\loop1); }, 60, 11);
MIDIFunc.noteOn({ y.playSampleSustaining(\loop2, 1, -6); }, 61, 11);
MIDIFunc.noteOff({ y.releaseSampleSustaining(\loop2); }, 61, 11);
MIDIFunc.noteOn({ y.playSampleSustaining(\loop3, 2, -6); }, 62, 11);
MIDIFunc.noteOff({ y.releaseSampleSustaining(\loop3); }, 62, 11);






	