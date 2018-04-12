IM_AaltoSequencer {

  var clockTask, valArray, trigArray, <length, <offset, currentStep, <lagTime, <jitter;
  var quantizeArray; // set of values that are locked to or interpolated
  var <val, valDelay, <delayVal, <trig, trigDelay, <delayTrig;
  const maxLength = 64;

  *new {
    ^super.new.prInit;
  }

  prInit {
    clockTask = nil;  // how about using a pattern for the clock, so that all sorts of weird walks become possible with different algorithms; the pattern just outputs the current step
    valArray = Array.newClear(maxLength);
    trigArray = Array.newClear(maxLength);  // maybe make a step class for instances?
    length = 16;
    offset = 0;
  }

  next {
  }

  stepByClockOrManually {
  }

  writeTrig { |trigNum = 0, trig = 1|
  }

  writeAllTrigs { |trigArray|
  }

  writeVal { |stepNum = 0, val = 0|
  }

  writeAllVals { |valArray|
  }

  start { }
  stop { }
  reset { }
}