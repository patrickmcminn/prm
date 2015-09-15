/*
Tuesday, August 5th 2014
Sequencer.sc
prm
*/

PatternSequencer {

  var name, internalClock;

  *new { | uniqueName, target = nil, addAction = 'addToHead' |
    ^super.new.prInit(uniqueName, target, addAction);
  }

  prInit { | uniqueName, target, addAction |
    var server = Server.default;
    server.waitForBoot {
      name = uniqueName;
      internalClock = TempoClock.new(1);
      Pbindef(name,
        \group, target,
        \addAction, addAction
      );
    }
  }

  //////// Public Function:

  free {
    this.stop;
    Pbindef(\name).clear;
  }

  play { | clock = 'internal' |
    var playClock;
    if( clock == 'internal', { playClock = internalClock }, { playClock = clock });
    Pbindef(name).play(playClock);
  }

  reset { Pbindef(name).reset; }
  stop { Pbindef(name).stop; }
  pause { Pbindef(name).pause }
  resume { Pbindef(name).resume; }
  isPlaying { ^Pbindef(name).isPlaying }

  addKey { | key, action |
    Pbindef(name, key, action);
  }

  setQuant { | quant = 0 |
    Pbindef(name).quant = quant;
  }

}
