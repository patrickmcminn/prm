/*
Tuesday, February 17th 2015
FunctionSequencer.sc
prm
London, England
*/

FunctionSequencer {

  var isLoaded;
  var <functionArray, <activeArray;
  var <clock;
  var sequenceRoutine;
  var forwardRoutine, reverseRoutine, backAndForthRoutine, randomRoutine, driftRoutine;
  var count, beatDivision;
  var <activeStep, <numberSteps;

  *new { | initSize = 16, clock = 'internal' |
    ^super.new.prInit(initSize, clock);
  }

  prInit { | initSize = 16, tempoClock = 'internal' |
    isLoaded = false;
    functionArray = Array.newClear(initSize);
    activeArray = Array.fill(initSize, { true; });
    numberSteps = initSize;
    if( tempoClock == 'internal', { clock = TempoClock.new(1); });
  }

  //////// Public Functions:

  free {
    functionArray = nil;
    try { clock.free; };
    clock = nil;
    sequenceRoutine = nil;
  }

  playSequence { | beatDiv = 0.25, quant = 0, direction = 'forward' |
    count = 0;
    beatDivision = beatDiv;
    try { sequenceRoutine.stop; };
    this.prMakeRoutines;
    switch(direction,
      'forward', { sequenceRoutine = forwardRoutine; },
      'reverse', { sequenceRoutine = reverseRoutine; },
      'backAndForth', { sequenceRoutine = backAndForthRoutine; },
      'random', { sequenceRoutine = randomRoutine; },
      'drift', { sequenceRoutine = driftRoutine; }
    );
    sequenceRoutine.play(clock, quant);
  }

  prMakeRoutines {
    forwardRoutine = r {
      loop {
        activeStep = count % numberSteps;
        if( activeArray.wrapAt(count) == true, { functionArray.wrapAt(count).value });
        count = count + 1;
        beatDivision.wait;
      };
    };
    reverseRoutine = r { | beatDiv = 0.25 |
      loop {
        activeStep = count % numberSteps;
        if( activeArray.wrapAt(count) == true, { functionArray.wrapAt(count).value });
        count = count - 1;
        beatDivision.wait;
      };
    };
    backAndForthRoutine = r { | beatDiv = 0.25 |
      loop {
        activeStep = count % numberSteps;
        if( activeArray.wrapAt(count) == true, { functionArray.wrapAt(count).value });
        count = count + 1;
        count = count - 1;
        beatDivision.wait;
      };
    };
    randomRoutine = r { | beatDiv = 0.25 |
      loop {
        var step = functionArray.size.rand;
        activeStep = step;
        if( activeArray[step] == true, { functionArray.wrapAt(count).value });
        beatDivision.wait;
      };
    };
    driftRoutine = r { | beatDiv = 0.25 |
      loop {
        activeStep = count % numberSteps;
        if( activeArray.wrapAt(count) == true, { functionArray.wrapAt(count).value });
        count = choose([count + 1, count - 1]);
        beatDivision.wait;
      };
    };
  }

  //////////////// Public Functions:


  setDirection { | direction = 'forward', quant = 0 |
    sequenceRoutine.stop;
    switch(direction,
      'forward', { sequenceRoutine = forwardRoutine; },
      'reverse', { sequenceRoutine = reverseRoutine; },
      'backAndForth', { sequenceRoutine = backAndForthRoutine; },
      'random', { sequenceRoutine = randomRoutine; },
      'drift', { sequenceRoutine = driftRoutine; }
    );
    sequenceRoutine.play(clock, quant);
  }


  stopSequence { sequenceRoutine.stop; }
  //pauseSequence { sequenceRoutine.pause; }
  resumeSequence { sequenceRoutine.resume; }

  addFunction { | step = 0, func | functionArray[step] = func; }
  removeFunction { | step = 0 | functionArray[step] = nil; }

  setFunctionArraySize { // note: clears array of previously-existing functions!
    | size = 16 |
    functionArray = nil;
    functionArray = Array.newClear(size);
    activeArray = Array.fill(size, { true });
  }

  isActive { | step = 0 |
    ^activeArray[step];
  }

  activateStep { | step = 0 | activeArray[step] = true; }
  deactivateStep { | step = 0 | activeArray[step] = false; }
  toggleActivateStep { | step = 0 |
    if(activeArray[step] == true, { this.deactivateStep(step) }, { this.activateStep(step) });
  }
}