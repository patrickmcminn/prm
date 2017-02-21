x = OhmRGB.new;
y = FunctionSequencer.new;

SynthDef(\saw, { | freq = 220, amp = 0.5, releaseTime = 7 |
  var saw, env, sig;
  saw = LPF.ar(Saw.ar(freq), 900);
  env = EnvGen.kr(Env.perc(0.01, releaseTime), 1, doneAction: 2);
  sig = saw * env;
  sig = Pan2.ar(sig, 0);
  Out.ar(0, sig);
}).add;


y.clock.tempo = 0.5;
y.setNumberSteps(8);

~step = Array.newClear(8);

8.do({ | column |
  8.do({ | row |
    x.setGridFunc(column, row, {
      y.addFunction(column, { Synth(\saw, [\freq, 110 * (row + 1)]);
      });
      ~step[column] = row;
    });
  });
});

8.do({ | i | x.setGridMonitorFunc(i, 0, {
  if( y.activeStep == i, { x.turnGridColumnColor(i, 'green'); }, { x.turnGridColumnColor(i, 'blue'); } );
  //if( y.activeStep == i, { x.turnGridGreen(i, 7); }, { x.turnGridOff(i, 7); });
  try { x.turnGridMagenta(i, ~step[i]); }
  });
});

//8.do({ | i | x.setGridFunc(0, i, { y.addFunction(0, { Synth(\saw, [\freq, 110 * (i + 1)]); }); }); });

y.playSequence
y.stopSequence;
y.activeStep;
8.do({ | i | y.addFunction(i, { Synth(\saw, [\freq, 55  * (i +1)]); }); });


y.setDirection('reverse');

y.setDirection('drift');
y.setDirection('forward');
y.setDirection('random');

