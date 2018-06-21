/*
Monday, May 28th 2018
APC40Mk2_Feedback.sc
prm
*/

+ APC40Mk2 {

  /////// Colors:

  // basic grid function:
  prTurnGridColor { | num, colorVal |
    midiOutPort.noteOn(1, num, colorVal);
  }

  prTurnLaunchButtonColor { | num, colorVal |
    var note = 86 - num;
    midiOutPort.noteOn(1, note, colorVal);
  }

  prTurnStopClipColor { }

}