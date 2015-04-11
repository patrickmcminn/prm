~ohm = OhmRGB.new;

~reset =  { 8.do({ | i |
  if( x.isActive(i), { ~ohm.turnGridWhite(i, 0) }, { ~ohm.turnGridCyan(i, 0); });
}); };

// why does this consistently return a value that is one step behind the current sequencer value?
y = r {
  ~reset.value;
  if( x.activeStep.notNil, {
    if( x.isActive(x.activeStep),
      {~ohm.turnGridGreen(x.activeStep, 0);},
      { ~ohm.turnGridRed(x.activeStep, 0); });
    });
  0.05.wait;
}.loop.play;

y.stop;

x = FunctionSequencer.new(8);
x.playSequence;


y.stop;

8.do({ | i | ~ohm.setGridFunc(i, 0, { x.toggleActivateStep(i); }); });

x.setDirection('forward');
x.playSequence

x.addFunction(0, { { Out.ar(0, Saw.ar(60.midicps) * EnvGen.kr(Env.perc(0.01, 1, 1), 1, doneAction: 2)); }.play; });


// this works. how and why is it different?
8.do ({ | i |
  x.addFunction(i, { i.postln;
  ~reset.value;
  if( x.activeStep.notNil, {
    if( x.isActive(x.activeStep),
      {~ohm.turnGridGreen(x.activeStep, 0);},
      { ~ohm.turnGridRed(x.activeStep, 0); });

    });
  });
});
s.meter;

z = r {
  x.activeStep.postln;
  0.05.wait;
}.loop.play;

z.stop;

