/*
Tuesday, April 21st 2015
Reverb Page.scd
New Orleans, LA
*/

var page = 'reverb';


~ohm.makePage(page);

// Left Knobs:

// delay Coefficient: CAREFUL!
~ohm.setLeftKnobFunc(0, 0, { | val |
  var coeff = val.linlin(0, 127, 0.01, 1);
  ~prm.reverb.setDelayCoefficient(coeff);
}, page: page);
// feedback Coefficient:
~ohm.setLeftKnobFunc(1, 0, { | val |
  var coeff = val.linlin(0, 127, 0, 1);
  ~prm.reverb.setFeedbackCoefficient(coeff);
}, page: page);

// lowpass:
~ohm.setLeftKnobFunc(2, 2, { | val |
  var cutoff = val.linexp(0, 127, 750, 16000);
  ~prm.reverb.setLowPassCutoff(cutoff);
}, page: page);
// highpass:
~ohm.setLeftKnobFunc(3, 2, { | val |
  var cutoff = val.linexp(0, 127, 750, 20);
  ~prm.reverb.setHighPassCutoff(cutoff);
}, page: page);

//////////////
//// GRID ///
/////////////