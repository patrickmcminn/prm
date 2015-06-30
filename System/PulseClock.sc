/*
Monday, June 29th 2015
PulseClock.sc
prm

Tempo Clock that outputs a Pulse to a given Hardware Output
*/

PulseClock {

  var server, <isLoaded;
  var <tempoClock;

  *new { | bpm = 120 |
    super.new.prInit(bpm);
  }

}

/*

SynthDef(\singleImpulse, {
  | outBus = 0 |
  var impulse, sig;
  OffsetOut.ar(outBus, Impulse.ar(0));
  FreeSelf.kr(Impulse.kr(0));
}).add;

x = Synth(\singleImpulse);


y = TempoClock.new(2);

y.schedAbs(y.beats.ceil, { Synth(\singleImpulse); 1 });
y.clear
y.

*/