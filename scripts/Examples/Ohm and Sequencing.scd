x = OhmRGB.new;


x.setGridMonitorFunc(0, 7, {
  8.do({ | i |
    if( i == ~num, { x.turnGridColumnColor(i, 'green') }, { x.turnGridColumnColor(i, 'off'); });
    x.turnGridMagenta(i, b[i]);
    //x.turnGridWhite(b[i], b[i]);
  });
  });
~num = 0;



~rout = r {
  loop {
    ~num = (~num + 1)%8;
    0.25.wait;
    b = Array.fill(8, { 8.rand });
  }
}.play;

b = Array.fill(8, { 8.rand });

~rout.stop;






y = FunctionSequencer.new;
y.addFunction(0, { "dicks".postln; });

SynthDef(\saw, { | freq = 220, amp = 0.5, releaseTime = 0.5 |
  var saw, env, sig;
  saw = LPF.ar(Saw.ar(freq), 1500);
  env = EnvGen.kr(Env.perc(0.01, releaseTime), 1, doneAction: 2);
  sig = saw * env;
  sig = Pan2.ar(sig, 0);
  Out.ar(0, sig);
}).add;

Synth(\saw);

y.setFunctionArraySize(8);

8.do({ | column |
  8.do({ | row |
    x.setGridFunc(column, row, {
      y.addFunction(row, { Synth(\saw, [\freq, 110 * (row + 1)]) });
    });
  });
});

y.playSequence
y.stopSequence;

8.do({ | i | y.addFunction(i, { Synth(\saw, [\freq, 55  * (i +1)]); }); });
y.addFunction(0, { "ok".postln });
y.addFunction(0, { Synth(\saw, [\freq, 220]) });
y.addFunction(4, { Synth(\saw, [\freq, 110]); });
y.addFunction(5, { Synth(\saw, [\freq, 55]); Synth(\saw, [\freq, 1660, \releaseTime, 0.05]); });


y.addFunction(1, { Synth(\saw, [\freq, 440, \releaseTime, 0.05]) });
y.addFunction(3, { Synth(\saw, [\freq, 880, \releaseTime, 0.05]) });
y.addFunction(6, { Synth(\saw, [\freq, 440, \releaseTime, 0.05]) });
y.addFunction(7, { Synth(\saw, [\freq, 660, \releaseTime, 0.05]) });

y.clock.tempo = 1.5;

y.setNumberSteps(8);

y.setDirection('reverse');