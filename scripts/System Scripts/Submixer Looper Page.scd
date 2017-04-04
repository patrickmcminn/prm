/*
Thursday, January 28th 2015
Submixer Page
prm
LAX to BWI
updated 2017.2.3 -- Baltimore, MD
*/

var page = 'submixer';
var divResetFunc, posResetFunc, rateResetFunc;
var submixerABank = 0;
var submixerBBank = 1;
var submixerCBank = 2;

var currentSubmixerBank;

~ohm.makePage(page);

~ohm.addGridBanks(2, page: page);

~ohm.addPageLoadFunction(\looperGlobalMixer, {
  ~ohm.storageDict[\loadGlobalSubmixerFunc].value;
}, page);

currentSubmixerBank = submixerABank;

///////////// Grid Functions:

//////// Bank Change:

~ohm.setCrossfaderButtonMonitorFunc(0, {
  switch(currentSubmixerBank,
    submixerABank, { ~ohm.turnCrossfaderButtonBlue(0); },
    submixerBBank, { ~ohm.turnCrossfaderButtonCyan(0); },
    submixerCBank, { ~ohm.turnCrossfaderButtonMagenta(0); }
  );
}, page: page);


~ohm.setCrossfaderButtonFunc(0, r {
  ~ohm.setActiveGridBank(submixerBBank);
  (currentSubmixerBank = submixerBBank).yield;

  ~ohm.setActiveGridBank(submixerCBank);
  (currentSubmixerBank = submixerCBank).yield;

  ~ohm.setActiveGridBank(submixerABank);
  (currentSubmixerBank = submixerABank).yield;
}.loop, page: page);


//////// division:

////// functions:
//// Submixer A:
// monitor:
~ohm.setGridMonitorFunc(0, 0, {
  var div = ~prm.submixerA.div - 1;
  var row;
  if(div < 8, { row = 0 }, { row = 1 });
  8.do({ | i | ~ohm.turnGridCyan(i, 0); });
  8.do({ | i | ~ohm.turnGridCyan(i, 1); });
  ~ohm.turnGridGreen(div%8, row);
}, bank: submixerABank, page: page);

// func:
16.do({ | i |
  var column = i%8;
  //var div = pow(2, i);
  var row;
  if( i < 8, { row = 0 }, { row = 1 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerA.setLoopDivison(i + 1);
  }, bank: submixerABank, page: page);
});

//// Submixer B:
// monitor:
~ohm.setGridMonitorFunc(0, 0, {
  var div = ~prm.submixerB.div - 1;
  var row;
  if(div < 8, { row = 0 }, { row = 1 });
  8.do({ | i | ~ohm.turnGridCyan(i, 0); });
  8.do({ | i | ~ohm.turnGridCyan(i, 1); });
  ~ohm.turnGridGreen(div%8, row);
}, bank: submixerBBank, page: page);

// func:
16.do({ | i |
  var column = i%8;
  //var div = pow(2, i);
  var row;
  if( i < 8, { row = 0 }, { row = 1 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerB.setLoopDivison(i + 1);
  }, bank: submixerBBank, page: page);
});

//// Submixer C:
// monitor:
~ohm.setGridMonitorFunc(0, 0, {
  var div = ~prm.submixerC.div - 1;
  var row;
  if(div < 8, { row = 0 }, { row = 1 });
  8.do({ | i | ~ohm.turnGridCyan(i, 0); });
  8.do({ | i | ~ohm.turnGridCyan(i, 1); });
  ~ohm.turnGridGreen(div%8, row);
}, bank: submixerCBank, page: page);

// func:
16.do({ | i |
  var column = i%8;
  //var div = pow(2, i);
  var row;
  if( i < 8, { row = 0 }, { row = 1 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerC.setLoopDivison(i + 1);
  }, bank: submixerCBank, page: page);
});



//////// position:
////// functions:
//// Submixer A:
// monitor:
~ohm.setGridMonitorFunc(0, 3, {
  var pos = ~prm.submixerA.pos.linlin(0, 1, 0, 16);
  var row;
  if(pos < 8, { row = 3 }, { row = 4 });
  8.do({ | i | ~ohm.turnGridBlue(i, 3); });
  8.do({ | i | ~ohm.turnGridBlue(i, 4); });
  ~ohm.turnGridGreen(pos%8, row);
}, bank: submixerABank, page: page);
// func:
16.do({ | i |
  var column = i%8;
  var posSeries = Array.series(16, 0, 0.0625);
  var pos = posSeries[i];
  var row;
  if( i < 8, { row = 3 }, { row = 4 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerA.setLoopPosition(pos);
  }, bank: submixerABank, page: page);
});
//// Submixer B:
// monitor:
~ohm.setGridMonitorFunc(0, 3, {
  var pos = ~prm.submixerB.pos.linlin(0, 1, 0, 16);
  var row;
  if(pos < 8, { row = 3 }, { row = 4 });
  8.do({ | i | ~ohm.turnGridBlue(i, 3); });
  8.do({ | i | ~ohm.turnGridBlue(i, 4); });
  ~ohm.turnGridGreen(pos%8, row);
}, bank: submixerBBank, page: page);
// func:
16.do({ | i |
  var column = i%8;
  var posSeries = Array.series(16, 0, 0.0625);
  var pos = posSeries[i];
  var row;
  if( i < 8, { row = 3 }, { row = 4 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerB.setLoopPosition(pos);
  }, bank: submixerBBank, page: page);
});

//// SubmixerC:
~ohm.setGridMonitorFunc(0, 3, {
  var pos = ~prm.submixerC.pos.linlin(0, 1, 0, 16);
  var row;
  if(pos < 8, { row = 3 }, { row = 4 });
  8.do({ | i | ~ohm.turnGridBlue(i, 3); });
  8.do({ | i | ~ohm.turnGridBlue(i, 4); });
  ~ohm.turnGridGreen(pos%8, row);
}, bank: submixerCBank, page: page);
// func:
16.do({ | i |
  var column = i%8;
  var posSeries = Array.series(16, 0, 0.0625);
  var pos = posSeries[i];
  var row;
  if( i < 8, { row = 3 }, { row = 4 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerC.setLoopPosition(pos);
  }, bank: submixerCBank, page: page);
});


//////// rate:
////// functions:

//// SubmixerA:
// monitor func:
~ohm.setGridMonitorFunc(0, 6, {
  var rateArray = [0.1, 0.25, 0.5, 0.75, 0.8, 5/6, 8/9, 1, 9/8, 6/5, 5/4, 4/3, 3/2, 8/5, 5/3, 2];
  var rate = rateArray.atIdentityHash(~prm.submixerA.rate);
  var row;
  if(rate < 8, { row = 6 }, { row = 7 });
  8.do({ | i | ~ohm.turnGridYellow(i, 6); });
  8.do({ | i | ~ohm.turnGridYellow(i, 7); });
  ~ohm.turnGridGreen(rate%8, row);
}, bank: submixerABank, page: page);
// func:
16.do({ | i |
  var column = i%8;
  var rateArray = [0.1, 0.25, 0.5, 0.75, 0.8, 5/6, 8/9, 1, 9/8, 6/5, 5/4, 4/3, 3/2, 8/5, 5/3, 2];
  var rate = rateArray[i];
  var row;
  if( i < 8, { row = 6 }, { row = 7 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerA.setLoopRate(rate);
  }, bank: submixerABank, page: page);
});

//// Submixer B:
// monitor func:
~ohm.setGridMonitorFunc(0, 6, {
  var rateArray = [0.1, 0.25, 0.5, 0.75, 0.8, 5/6, 8/9, 1, 9/8, 6/5, 5/4, 4/3, 3/2, 8/5, 5/3, 2];
  var rate = rateArray.atIdentityHash(~prm.submixerB.rate);
  var row;
  if(rate < 8, { row = 6 }, { row = 7 });
  8.do({ | i | ~ohm.turnGridYellow(i, 6); });
  8.do({ | i | ~ohm.turnGridYellow(i, 7); });
  ~ohm.turnGridGreen(rate%8, row);
}, bank: submixerBBank, page: page);
// func:
16.do({ | i |
  var column = i%8;
  var rateArray = [0.1, 0.25, 0.5, 0.75, 0.8, 5/6, 8/9, 1, 9/8, 6/5, 5/4, 4/3, 3/2, 8/5, 5/3, 2];
  var rate = rateArray[i];
  var row;
  if( i < 8, { row = 6 }, { row = 7 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerB.setLoopRate(rate);
  }, bank: submixerBBank, page: page);
});


//// Submixer C:
// monitor func:
~ohm.setGridMonitorFunc(0, 6, {
  var rateArray = [0.1, 0.25, 0.5, 0.75, 0.8, 5/6, 8/9, 1, 9/8, 6/5, 5/4, 4/3, 3/2, 8/5, 5/3, 2];
  var rate = rateArray.atIdentityHash(~prm.submixerC.rate);
  var row;
  if(rate < 8, { row = 6 }, { row = 7 });
  8.do({ | i | ~ohm.turnGridYellow(i, 6); });
  8.do({ | i | ~ohm.turnGridYellow(i, 7); });
  ~ohm.turnGridGreen(rate%8, row);
}, bank: submixerCBank, page: page);
// func:
16.do({ | i |
  var column = i%8;
  var rateArray = [0.1, 0.25, 0.5, 0.75, 0.8, 5/6, 8/9, 1, 9/8, 6/5, 5/4, 4/3, 3/2, 8/5, 5/3, 2];
  var rate = rateArray[i];
  var row;
  if( i < 8, { row = 6 }, { row = 7 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerC.setLoopRate(rate);
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

// monitor funcs:
~ohm.setLeftButtonMonitorFunc(0, {
  if(~prm.submixerA.mixer.preOrPost == 'post',
    { ~ohm.turnLeftButtonCyan(0); },
    { ~ohm.turnLeftButtonWhite(0); });
}, page: page);
~ohm.setLeftButtonMonitorFunc(1, {
  if(~prm.submixerB.mixer.preOrPost == 'post',
    { ~ohm.turnLeftButtonCyan(1); },
    { ~ohm.turnLeftButtonWhite(1); });
}, page: page);
~ohm.setLeftButtonMonitorFunc(2, {
  if(~prm.submixerC.mixer.preOrPost == 'post',
    { ~ohm.turnLeftButtonCyan(2); },
    { ~ohm.turnLeftButtonWhite(2); });
}, page: page);

// funcs:
~ohm.setLeftButtonFunc(0, r {
  ~prm.submixerA.mixer.setSendPre.yield;

  ~prm.submixerA.mixer.setSendPost.yield;
}.loop, page: page);

~ohm.setLeftButtonFunc(1, r {
  ~prm.submixerB.mixer.setSendPre.yield;

  ~prm.submixerB.mixer.setSendPost.yield;
}.loop, page: page);

~ohm.setLeftButtonFunc(2, r {
  ~prm.submixerC.mixer.setSendPre.yield;

  ~prm.submixerC.mixer.setSendPost.yield;
}.loop, page: page);