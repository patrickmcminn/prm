/*
Thursday, January 28th 2015
Submixer Page
prm
LAX to BWI
*/

var page = 'submixer';
var divResetFunc, posResetFunc, rateResetFunc;
var submixerABank = 0;
var submixerBBank = 1;
var submixerCBank = 2;

~ohm.makePage(page);

~ohm.addGridBanks(2, page: page);

~ohm.setPageLoadFunction({
  ~ohm.storageDict[\loadGlobalSubmixerFunc].value;
}, page);

///////////// Grid Functions:

//////// Bank Change:
~ohm.turnCrossfaderButtonBlue(0, page: page);

~ohm.setCrossfaderButtonFunc(0, r {
  ~ohm.setActiveGridBank(submixerBBank);
  ~ohm.turnCrossfaderButtonCyan(0).yield;

  ~ohm.setActiveGridBank(submixerCBank);
  ~ohm.turnCrossfaderButtonPurple(0).yield;

  ~ohm.setActiveGridBank(submixerABank);
  ~ohm.turnCrossfaderButtonBlue(0).yield;
}.loop, page: page);


//////// division:
divResetFunc = { | bank |
  8.do({ | i | ~ohm.turnGridCyan(i, 0, bank: bank, page: page); });
  8.do({ | i | ~ohm.turnGridCyan(i, 1, bank: bank, page: page); });
};
// Inital color:
3.do({ | i | divResetFunc.value(i); });
3.do({ | i | ~ohm.turnGridGreen(0, 0, bank: i, page: page); });
//// functions:
// Submixer A:
16.do({ | i |
  var column = i%8;
  //var div = pow(2, i);
  var row;
  if( i < 8, { row = 0 }, { row = 1 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerA.setLoopDivison(i + 1);
    divResetFunc.value(submixerABank);
    ~ohm.turnGridGreen(column, row);
    }, bank: submixerABank, page: page);
});
// Submixer B:
16.do({ | i |
  var column = i%8;
  //var div = pow(2, i);
  var row;
  if( i < 8, { row = 0 }, { row = 1 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerB.setLoopDivison(i + 1);
    divResetFunc.value(submixerBBank);
    ~ohm.turnGridGreen(column, row);
    }, bank: submixerBBank, page: page);
});
// Submixer C:
16.do({ | i |
  var column = i%8;
  //var div = pow(2, i);
  var row;
  if( i < 8, { row = 0 }, { row = 1 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerC.setLoopDivison(i + 1);
    divResetFunc.value(submixerCBank);
    ~ohm.turnGridGreen(column, row);
    }, bank: submixerCBank, page: page);
});

//////// position:
posResetFunc =  { | bank |
  8.do({ | i | ~ohm.turnGridBlue(i, 3, page: page, bank: bank); });
  8.do({ | i | ~ohm.turnGridBlue(i, 4, page: page, bank: bank); });
};
// intial color:
3.do({ | i | posResetFunc.value(i); });
3.do({ | i |  ~ohm.turnGridGreen(0, 3, page: page, bank: i); });
//// functions:
// Submixer A:
16.do({ | i |
  var column = i%8;
  var posSeries = Array.series(16, 0, 0.0625);
  var pos = posSeries[i];
  var row;
  if( i < 8, { row = 3 }, { row = 4 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerA.setLoopPosition(pos);
    posResetFunc.value(submixerABank);
    ~ohm.turnGridGreen(column, row);
  }, bank: submixerABank, page: page);
});
// Submixer B:
16.do({ | i |
  var column = i%8;
  var posSeries = Array.series(16, 0, 0.0625);
  var pos = posSeries[i];
  var row;
  if( i < 8, { row = 3 }, { row = 4 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerB.setLoopPosition(pos);
    posResetFunc.value(submixerBBank);
    ~ohm.turnGridGreen(column, row);
  }, bank: submixerBBank, page: page);
});
// SubmixerC:
16.do({ | i |
  var column = i%8;
  var posSeries = Array.series(16, 0, 0.0625);
  var pos = posSeries[i];
  var row;
  if( i < 8, { row = 3 }, { row = 4 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerC.setLoopPosition(pos);
    posResetFunc.value(submixerCBank);
    ~ohm.turnGridGreen(column, row);
  }, bank: submixerCBank, page: page);
});


//////// rate:
rateResetFunc = { | bank |
  8.do({ | i | ~ohm.turnGridYellow(i, 6, bank: bank, page: page); });
  8.do({ | i | ~ohm.turnGridYellow(i, 7, bank: bank, page: page); });
};
// initial colors:
3.do({ | i | rateResetFunc.value(i); });
3.do({ | i | ~ohm.turnGridGreen(7, 6, bank: i, page: page); });

// Submixer A:
16.do({ | i |
  var column = i%8;
  var rateArray = [0.1, 0.25, 0.5, 0.75, 0.8, 5/6, 8/9, 1, 9/8, 6/5, 5/4, 4/3, 3/2, 8/5, 5/3, 2];
  var rate = rateArray[i];
  var row;
  if( i < 8, { row = 6 }, { row = 7 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerA.setLoopRate(rate);
    rateResetFunc.value(submixerABank);
    ~ohm.turnGridGreen(column, row);
  }, bank: submixerABank, page: page);
});
// Submixer B:
16.do({ | i |
  var column = i%8;
  var rateArray = [0.1, 0.25, 0.5, 0.75, 0.8, 5/6, 8/9, 1, 9/8, 6/5, 5/4, 4/3, 3/2, 8/5, 5/3, 2];
  var rate = rateArray[i];
  var row;
  if( i < 8, { row = 6 }, { row = 7 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerB.setLoopRate(rate);
    rateResetFunc.value(submixerBBank);
    ~ohm.turnGridGreen(column, row);
  }, bank: submixerBBank, page: page);
});
// Submixer C:
16.do({ | i |
  var column = i%8;
  var rateArray = [0.1, 0.25, 0.5, 0.75, 0.8, 5/6, 8/9, 1, 9/8, 6/5, 5/4, 4/3, 3/2, 8/5, 5/3, 2];
  var rate = rateArray[i];
  var row;
  if( i < 8, { row = 6 }, { row = 7 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerC.setLoopRate(rate);
    rateResetFunc.value(submixerCBank);
    ~ohm.turnGridGreen(column, row);
  }, bank: submixerCBank, page: page);
});

//////// Knobs:

//// Submixer A Sends:
// Reverb:
~ohm.setLeftKnobFunc(0, 2, { | val |
  ~prm.submixerA.mixer.setSendVol(0, val.ccdbfs(-70, 0));
}, page: page);
// Granulator:
~ohm.setLeftKnobFunc(0, 1, { | val |
  ~prm.submixerA.mixer.setSendVol(1, val.ccdbfs(-70, 6));
}, page: page);
// Modular:
~ohm.setLeftKnobFunc(0, 0, { | val |
  ~prm.submixerA.mixer.setSendVol(2, val.ccdbfs(-70, 0));
}, page: page);

//// Submixer B Sends:
// Reverb:
~ohm.setLeftKnobFunc(1, 2, { | val |
  ~prm.submixerB.mixer.setSendVol(0, val.ccdbfs(-70, 0));
}, page: page);
// Granulator:
~ohm.setLeftKnobFunc(1, 1, { | val |
  ~prm.submixerB.mixer.setSendVol(1, val.ccdbfs(-70, 6));
}, page: page);
// Modular:
~ohm.setLeftKnobFunc(1, 0, { | val |
  ~prm.submixerB.mixer.setSendVol(2, val.ccdbfs(-70, 0));
}, page: page);

//// Submixer C Sends:
// Reverb:
~ohm.setLeftKnobFunc(2, 2, { | val |
  ~prm.submixerC.mixer.setSendVol(0, val.ccdbfs(-70, 0));
}, page: page);
// Granulator:
~ohm.setLeftKnobFunc(2, 1, { | val |
  ~prm.submixerC.mixer.setSendVol(1, val.ccdbfs(-70, 6));
}, page: page);
~ohm.setLeftKnobFunc(2, 0, { | val |
  ~prm.submixerC.mixer.setSendVol(2, val.ccdbfs(-70, 0));
}, page: page);

//////// Left Buttons:

// setting pre or post:

// initial colors:
~ohm.turnLeftButtonCyan(0, page: page);
~ohm.turnLeftButtonCyan(1, page: page);
~ohm.turnLeftButtonCyan(2, page: page);

~ohm.setLeftButtonFunc(0, r {
  ~prm.submixerA.mixer.setSendPre;
  ~ohm.turnLeftButtonWhite(0).yield;

  ~prm.submixerA.mixer.setSendPost;
  ~ohm.turnLeftButtonCyan(0).yield;
}.loop, page: page);

~ohm.setLeftButtonFunc(1, r {
  ~prm.submixerB.mixer.setSendPre;
  ~ohm.turnLeftButtonWhite(1).yield;

  ~prm.submixerB.mixer.setSendPost;
  ~ohm.turnLeftButtonCyan(1).yield;
}.loop, page: page);

~ohm.setLeftButtonFunc(2, r {
  ~prm.submixerC.mixer.setSendPre;
  ~ohm.turnLeftButtonWhite(2).yield;

  ~prm.submixerC.mixer.setSendPost;
  ~ohm.turnLeftButtonCyan(2).yield;
}.loop, page: page);