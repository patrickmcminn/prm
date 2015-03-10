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
  var div = pow(2, i);
  var row;
  if( i < 8, { row = 0 }, { row = 1 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerA.setLoopDivison(div);
    divResetFunc.value(submixerABank);
    ~ohm.turnGridGreen(column, row);
    }, bank: submixerABank, page: page);
});
// Submixer B:
16.do({ | i |
  var column = i%8;
  var div = pow(2, i);
  var row;
  if( i < 8, { row = 0 }, { row = 1 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerB.setLoopDivison(div);
    divResetFunc.value(submixerBBank);
    ~ohm.turnGridGreen(column, row);
    }, bank: submixerBBank, page: page);
});
// Submixer C:
16.do({ | i |
  var column = i%8;
  var div = pow(2, i);
  var row;
  if( i < 8, { row = 0 }, { row = 1 });
  ~ohm.setGridFunc(column, row, {
    ~prm.submixerC.setLoopDivison(div);
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
rateResetFunc = {
  8.do({ | i | ~ohm.turnGridYellow(i, 6, page: page); });
  8.do({ | i | ~ohm.turnGridYellow(i, 7, page: page); });
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
    ~prm.submixerB.setLoopRate(rate);
    rateResetFunc.value(submixerCBank);
    ~ohm.turnGridGreen(column, row);
  }, bank: submixerBBank, page: page);
});