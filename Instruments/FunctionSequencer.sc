/*
Tuesday, February 17th 2015
FunctionSequencer.sc
prm
London, England
*/

FunctionSequencer {

  var functionArray;

  *new { | initSize = 16, clock = 'internal', initDirection = 'forward' |
    ^super.new.prInit(initSize, clock);
  }

  prInit { | initSize = 16, clock = 'internal' |
    functionArray = Array.newClear(initSize);
  }

}