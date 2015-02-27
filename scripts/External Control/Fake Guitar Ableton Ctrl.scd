/*
Script for piping Ableton into the FakeGuitar class
*/

x = FakeGuitar.new(0);

(
MIDIIn.connectAll;
~inPort = MIDIIn.findPort("IAC Driver", "Bus 1");

// play notes:
127.do({ | note |
  MIDIFunc.noteOn({ | vel |
    var amp = vel.linlin(0, 127, 0, 1);
    x.playNote(note.midicps, amp);
  }, note);
  MIDIFunc.noteOff({
    x.releaseNote(note.midicps);
  }, note);
});
);

(
//////// Envelope:
// attack:
OSCdef(\fakeGuitarAttack, { | msg |
  var attack = msg.at(1);
  attack = attack/1000;
  x.setAttack(attack);
}, '/fakeGuitarAttack');

// release:
OSCdef(\fakeGuitarRelease, { | msg |
  var release = msg.at(1);
  release = release/1000;
  x.setRelease(release);
}, '/fakeGuitarRelease');

//////// Vibrato:

// Vibrato Speed:
OSCdef(\fakeGuitarVibratoSpeed, { | msg |
  var speed = msg.at(1);
  x.setVibratoSpeed(speed);
}, '/fakeGuitarVibratoSpeed');

// Vibrato Amp:
OSCdef(\fakeGuitarVibratoDepth, { | msg |
  var depth = msg.at(1);
  x.setVibratoDepth(depth);
}, '/fakeGuitarVibratoDepth');

// vol:
OSCdef(\fakeGuitarVolume, { | msg |
  var volume = msg.at(1);
  x.setVol(volume);
}, '/fakeGuitarVolume');

//////// Flanger:

// flanger speed:
OSCdef(\fakeGuitarFlangerSpeed, { | msg |
  var speed = msg.at(1);
  x.setFlangerSpeed(speed);
}, '/fakeGuitarFlangerSpeed');

// flanger feedback:
OSCdef(\fakeGuitarFlangerFeedback, { | msg |
  var feedback = msg.at(1);
  x.setFlangerFeedback(feedback);
}, '/fakeGuitarFlangerFeedback');

// flanger Waveform:
OSCdef(\fakeGuitarFlangerWaveform, { | msg |
  var waveform = msg.at(1);
  switch(waveform.asSymbol,
    { 'sine' }, { x.setFlangerWaveform('sine') },
    { 'half sine' }, { x.setFlangerWaveform('hSine'); },
    { 'saw' }, { x.setFlangerWaveform('saw') },
    { 'rev saw' }, { x.setFlangerWaveform('revSaw'); },
    { 'rect' }, { x.setFlangerWaveform('rect'); },
    { 'sample and hold' }, { x.setFlangerWaveform('sampleAndHold') },
    { 'noise' }, { x.setFlangerWaveform('noise') }
  );
}, '/fakeGuitarFlangerWaveform');


//////// Filters:

// Pre-Distortion Cutoff:
OSCdef(\fakeGuitarPreDistortionCutoff, { | msg |
  var cutoff = msg.at(1);
  x.setPreDistortionCutoff(cutoff);
}, '/fakeGuitarPreDistortionCutoff');

// Pre-Distortion RQ:
OSCdef(\fakeGuitarPreDistortionRQ, { | msg |
  var rq = msg.at(1);
  x.setPreDistortionRQ(rq);
}, '/fakeGuitarPreDistortionRQ');

// Set Distortion:
OSCdef(\fakeGuitarDistortion, { | msg |
  var distortion = msg.at(1);
  var distAmp = distortion.linexp(1, 1000, 1, 0.1);
  x.setDistortionGain(distortion);
  x.setDistortionAmp(distAmp);
}, '/fakeGuitarDistortion');

// Filter Type:
OSCdef(\fakeGuitarFilterType, { | msg |
  var type = msg.at(1);
  switch(type.asSymbol,
    { 'LowPass' }, { x.setFilterType(0) },
    { 'HighPass' }, { x.setFilterType(1) },
    { 'BandPass' }, { x.setFilterType(2) }
  );
}, '/fakeGuitarFilterType');

// Set Cutoff:
OSCdef(\fakeGuitarCutoff, { | msg |
  var cutoff = msg.at(1);
  x.setCutoff(cutoff);
}, '/fakeGuitarCutoff');

// Set RQ:
OSCdef(\fakeGuitarRQ, { | msg |
  var rq = msg.at(1);
  x.setRQ(rq);
}, '/fakeGuitarRQ');

//////// Reverb:

// reverb amp:
OSCdef(\fakeGuitarReverbAmp, { | msg |
  var amp = msg.at(1);
  x.setReverbAmp(amp);
}, '/fakeGuitarReverbAmp');

// reverb room size:
OSCdef(\fakeGuitarReverbRoomSize, { | msg |
  var size = msg.at(1);
  x.setReverbRoomSize(size);
}, '/fakeGuitarReverbRoomSize');

// reverb damp:
OSCdef(\fakeGuitarReverbDamp, { | msg |
  var damp = msg.at(1);
  x.setReverbDamp(damp);
}, '/fakeGuitarReverbDamp');

//////// EQ:

// low gain:
OSCdef(\fakeGuitarLowGain, { | msg |
  var gain = msg.at(1);
  x.setLowGain(gain);
}, '/fakeGuitarLowGain');

// high gain:
OSCdef(\fakeGuitarHighGain, { | msg |
  var gain = msg.at(1);
  x.setHighGain(gain);
}, '/fakeGuitarHighGain');

///////// Waveloss: (!!!)
OSCdef(\fakeGuitarWaveloss, { | msg |
  var waveloss = msg.at(1);
  x.setWaveLossAmount(waveloss);
}, '/fakeGuitarWaveLoss');
)

s.meter;