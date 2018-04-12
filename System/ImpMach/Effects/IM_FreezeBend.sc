/*
Thursday, May 22nd 2014
prm
*/

// Should this just extend IM_GrainFreeze?
/*
TO DO: 9/16/2014

have this class inherit from IM_GrainFreeze
*/

IM_FreezeBend : IM_GrainFreeze {
  //var <isLoaded;
  var controlBus, controlSynth;
  var <bendActive;

  *new { |outBus = 0, relGroup = nil, addAction = 'addToTail' |
    //^super.new(outBus, relGroup: relGroup, addAction: addAction).prInit.prFreezeBendInit;
    ^super.new(outBus, relGroup, addAction).prFreezeBendInit;
  }

  prFreezeBendInit {
    var server = Server.default;
    isLoaded = false;
    bendActive = false;
    {
      this.prAddControlSynthDef;
      controlBus = Bus.control;
      server.sync;

      while({ try { mixer.isLoaded } != true }, { 0.001.wait });
      isLoaded = true;
    }.fork;
  }

  prAddControlSynthDef {
    SynthDef(\im_freezeBend_sineLFO, { |outBus = 0,
      freq = 0.5, phase = 0, rangeLow = 0, rangeHigh = 1|

      var sig = SinOsc.kr(freq, phase).range(rangeLow, rangeHigh);
      Out.kr(outBus, sig);
    }).add;
  }

  //////// public functions:
  free {
    this.freeFreeze;
    this.freeRateLFO;
    synth.free;
    this.freeProcessor;
  }

  makeRateLFO { | freq = 0.2, rangeLow = 0.75, rangeHigh = 1, phase = (pi/2), type = 'sine' |
    if( freezeActive == true, {
      if( bendActive == true, { controlSynth.free; });
      switch(type,
        { 'sine' }, { this.makeSineRateLFO(freq, rangeLow, rangeHigh, phase) }
      )
      },
      { "freeze not active".postln; });
  }
  freeRateLFO { | newPlayRate = 1 |
    controlSynth.free;
    this.setPlayRate(newPlayRate);
    bendActive= false;
  }

  makeSineRateLFO { | freq = 0.2, rangeLow = 0.75, rangeHigh = 1, phase = (pi/2) |
    {
      server.sync;
      controlSynth = Synth(\im_freezeBend_sineLFO, [\outBus, controlBus, \freq, freq, \phase, phase,
        \rangeLow, rangeLow, \rangeHigh, rangeHigh], group, \addToHead);
      server.sync;
      this.mapPlayRate(controlBus);
      bendActive = true;
    }.fork;
  }

}