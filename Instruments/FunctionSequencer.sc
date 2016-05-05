/*
Tuesday, February 17th 2015
FunctionSequencer.sc
prm
London, England
*/

FunctionSequencer {

  var <isLoaded;
  var <functionArray, <activeArray;
  var <clock;
  var sequenceRoutine;
  var forwardFunc, reverseFunc, backAndForthFunc, randomFunc, driftFunc;
  var count, beatDivision;
  var <activeStep, <numberSteps;
  var sequenceDirection;

  *new { | initSize = 16, clock = 'internal' |
    ^super.new.prInit(initSize, clock);
  }

  prInit { | initSize = 16, tempoClock = 'internal' |
    isLoaded = false;
    functionArray = Array.newClear(initSize);
    activeArray = Array.fill(initSize, { true; });
    numberSteps = initSize;
    if( tempoClock == 'internal', { clock = TempoClock.new(1); }, { clock = tempoClock });
    isLoaded = true;
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
    sequenceDirection = direction;
    try { sequenceRoutine.stop; };
    this.prMakeRoutines;
    sequenceRoutine.play(clock, quant);
  }

  prMakeRoutines {
    sequenceRoutine = r {
      loop {
        switch(sequenceDirection,
          'forward', { forwardFunc.value; },
          'reverse', { reverseFunc.value; },
          'backAndForth', { backAndForthFunc.value; },
          'random', { randomFunc.value; },
          'drift', { driftFunc.value; }
        );
        beatDivision.wait;
      };
    };

    forwardFunc = {
        activeStep = count % numberSteps;
        if( activeArray[activeStep] == true, { functionArray[activeStep].value });
        count = count + 1;
        //beatDivision.wait;
    };

    reverseFunc =  {
        activeStep = count % numberSteps;
        if( activeArray[activeStep] == true, { functionArray[activeStep].value });
        count = count - 1;
        //beatDivision.wait;
    };
    // not working yet!
    backAndForthFunc =  {
        activeStep = count % numberSteps;
        if( activeArray[activeStep] == true, { functionArray[activeStep].value });
        count = count + 1;
        count = count - 1;
        //beatDivision.wait;
    };
    randomFunc = {
        var step = numberSteps.rand;
        activeStep = step;
        if( activeArray[activeStep] == true, { functionArray[activeStep].value });
        //beatDivision.wait;
    };
    driftFunc = {
        activeStep = count % numberSteps;
        if( activeArray[activeStep] == true, { functionArray[activeStep].value });
        count = choose([count + 1, count - 1]);
        //beatDivision.wait;
    };
  }

  //////////////// Public Functions:


  setDirection { | direction = 'forward', quant = 0 |
    sequenceDirection = direction;


    /*
    clock.play({
      sequenceRoutine.stop.reset;
      this.playSequence(beatDivision, quant, direction);
    }, beatDivision);
    */
    /*
    switch(direction,
      'forward', { sequenceRoutine = forwardRoutine; },
      'reverse', { sequenceRoutine = reverseRoutine; },
      'backAndForth', { sequenceRoutine = backAndForthRoutine; },
      'random', { sequenceRoutine = randomRoutine; },
      'drift', { sequenceRoutine = driftRoutine; }
    );
    sequenceRoutine.play(clock, quant);
    */
  }


  stopSequence { sequenceRoutine.stop; }
  //pauseSequence { sequenceRoutine.pause; }
  resumeSequence { sequenceRoutine.resume; }

  addFunction { | step = 0, func | functionArray[step] = func; }
  removeFunction { | step = 0 | functionArray[step] = nil; }
  setNumberSteps { | steps = 16 | numberSteps = steps; }

  setMaxFunctionArraySize { // note: clears array of previously-existing functions!
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