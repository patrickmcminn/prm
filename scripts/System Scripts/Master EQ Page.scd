/*
Monday, January 19th 2015
Master EQ Page.scd
prm
Hobart, Australia
*/

var page = 'masterEQ';
var sendsBank = 0;
var submixerAPeak1Bank = 1;
var submixerAPeak2Bank = 2;
var submixerAPeak3Bank = 3;
var submixerBPeak1Bank = 4;
var submixerBPeak2Bank = 5;
var submixerBPeak3Bank = 6;
var submixerCPeak1Bank = 7;
var submixerCPeak2Bank = 8;
var submixerCPeak3Bank = 9;

var submixerBankResetFunc;

~ohm.makePage(page);
~ohm.addLeftKnobsBanks(8);

~ohm.setPageLoadFunction({
  ~ohm.globalStorage[\audioSystemMixer].value;
}, page);

//////////// Grid Functions:

//////// EQ Reset Funcs:

//// initial colors:
// submixer A:
~ohm.turnGridBlue(0, 0, page: page);
~ohm.turnGridCyan(1, 0, page: page);
~ohm.turnGridPurple(2, 0, page: page);
~ohm.turnGridYellow(3, 0, page: page);
~ohm.turnGridWhite(4, 0, page: page);
// submixer B:
~ohm.turnGridBlue(0, 1, page: page);
~ohm.turnGridCyan(1, 1, page: page);
~ohm.turnGridPurple(2, 1, page: page);
~ohm.turnGridYellow(3, 1, page: page);
~ohm.turnGridWhite(4, 1, page: page);
// submixer C:
~ohm.turnGridBlue(0, 2, page: page);
~ohm.turnGridCyan(1, 2, page: page);
~ohm.turnGridPurple(2, 2, page: page);
~ohm.turnGridYellow(3, 2, page: page);
~ohm.turnGridWhite(4, 2, page: page);

//// submixer A:

// Low:
~ohm.setGridFunc(0, 0, {
  ~prm.submixerA.eq.setLowFreq(250);
  ~prm.submixerA.eq.setLowGain(0);
  ~prm.submixerA.eq.setLowRQ(1);
  ~ohm.blinkGridPlay(0, 0, 'green', mul: 2);
  { ~ohm.blinkGridStop(0, 0, 'blue'); }.defer(1);
}, page: page);

// Peak 1:
~ohm.setGridFunc(1, 0, {
  ~prm.submixerA.eq.setPeak1Freq(600);
  ~prm.submixerA.eq.setPeak1Gain(0);
  ~prm.submixerA.eq.setPeak1RQ(1);
   ~ohm.blinkGridPlay(1, 0, 'green', mul: 2);
  { ~ohm.blinkGridStop(1, 0, 'cyan'); }.defer(1);
}, page: page);

// Peak 2:
~ohm.setGridFunc(2, 0, {
  ~prm.submixerA.eq.setPeak2Freq(1000);
  ~prm.submixerA.setPeak2Gain(0);
  ~prm.submixerA.setPeak2Freq(1000);
   ~ohm.blinkGridPlay(2, 0, 'green', mul: 2);
  { ~ohm.blinkGridStop(2, 0, 'purple'); }.defer(1);
}, page: page);

// Peak 3:
~ohm.setGridFunc(3, 0, {
  ~prm.submixerA.eq.setPeak3Freq(1500);
  ~prm.submixerA.eq.setPeak3Gain(0);
  ~prm.submixerA.eq.setPeak3RQ(1);
   ~ohm.blinkGridPlay(3, 0, 'green', mul: 2);
  { ~ohm.blinkGridStop(3, 0, 'yellow'); }.defer(1);
}, page: page);


// High:
~ohm.setGridFunc(4, 0, {
  ~prm.submixerA.eq.setHighFreq(2500);
  ~prm.submixerA.eq.setHighGain(0);
  ~prm.submixerA.eq.setHighRQ(1);
  ~ohm.blinkGridPlay(4, 0, 'green', mul: 2);
  { ~ohm.blinkGridStop(4, 0, 'white'); }.defer(1);
}, page: page);

//// submixer B:

// Low:
~ohm.setGridFunc(0, 1, {
  ~prm.submixerB.eq.setLowFreq(250);
  ~prm.submixerB.eq.setLowGain(0);
  ~prm.submixerB.eq.setLowRQ(1);
  ~ohm.blinkGridPlay(0, 1, 'green', mul: 2);
  { ~ohm.blinkGridStop(0, 1, 'blue'); }.defer(1);
}, page: page);

// Peak 1:
~ohm.setGridFunc(1, 1, {
  ~prm.submixerB.eq.setPeak1Freq(600);
  ~prm.submixerB.eq.setPeak1Gain(0);
  ~prm.submixerB.eq.setPeak1RQ(1);
   ~ohm.blinkGridPlay(1, 1, 'green', mul: 2);
  { ~ohm.blinkGridStop(1, 1, 'cyan'); }.defer(1);
}, page: page);

// Peak 2:
~ohm.setGridFunc(2, 1, {
  ~prm.submixerB.eq.setPeak2Freq(1000);
  ~prm.submixerB.setPeak2Gain(0);
  ~prm.submixerB.setPeak2Freq(1000);
   ~ohm.blinkGridPlay(2, 1, 'green', mul: 2);
  { ~ohm.blinkGridStop(2, 1, 'purple'); }.defer(1);
}, page: page);

// Peak 3:
~ohm.setGridFunc(3, 1, {
  ~prm.submixerB.eq.setPeak3Freq(1500);
  ~prm.submixerB.eq.setPeak3Gain(0);
  ~prm.submixerB.eq.setPeak3RQ(1);
   ~ohm.blinkGridPlay(3, 1, 'green', mul: 2);
  { ~ohm.blinkGridStop(3, 1, 'yellow'); }.defer(1);
}, page: page);


// High:
~ohm.setGridFunc(4, 1, {
  ~prm.submixerB.eq.setHighFreq(2500);
  ~prm.submixerB.eq.setHighGain(0);
  ~prm.submixerB.eq.setHighRQ(1);
  ~ohm.blinkGridPlay(4, 1, 'green', mul: 2);
  { ~ohm.blinkGridStop(4, 1, 'white'); }.defer(1);
}, page: page);


//// submixer C:

// Low:
~ohm.setGridFunc(0, 2, {
  ~prm.submixerC.eq.setLowFreq(250);
  ~prm.submixerC.eq.setLowGain(0);
  ~prm.submixerC.eq.setLowRQ(1);
  ~ohm.blinkGridPlay(0, 2, 'green', mul: 2);
  { ~ohm.blinkGridStop(0, 2, 'blue'); }.defer(1);
}, page: page);

// Peak 1:
~ohm.setGridFunc(1, 2, {
  ~prm.submixerC.eq.setPeak1Freq(600);
  ~prm.submixerC.eq.setPeak1Gain(0);
  ~prm.submixerC.eq.setPeak1RQ(1);
   ~ohm.blinkGridPlay(1, 2, 'green', mul: 2);
  { ~ohm.blinkGridStop(1, 2, 'cyan'); }.defer(1);
}, page: page);

// Peak 2:
~ohm.setGridFunc(2, 2, {
  ~prm.submixerC.eq.setPeak2Freq(1000);
  ~prm.submixerC.setPeak2Gain(0);
  ~prm.submixerC.setPeak2Freq(1000);
   ~ohm.blinkGridPlay(2, 2, 'green', mul: 2);
  { ~ohm.blinkGridStop(2, 2, 'purple'); }.defer(1);
}, page: page);

// Peak 3:
~ohm.setGridFunc(3, 2, {
  ~prm.submixerC.eq.setPeak3Freq(1500);
  ~prm.submixerC.eq.setPeak3Gain(0);
  ~prm.submixerC.eq.setPeak3RQ(1);
   ~ohm.blinkGridPlay(3, 2, 'green', mul: 2);
  { ~ohm.blinkGridStop(3, 2, 'yellow'); }.defer(1);
}, page: page);


// High:
~ohm.setGridFunc(4, 2, {
  ~prm.submixerC.eq.setHighFreq(2500);
  ~prm.submixerC.eq.setHighGain(0);
  ~prm.submixerC.eq.setHighRQ(1);
  ~ohm.blinkGridPlay(4, 2, 'green', mul: 2);
  { ~ohm.blinkGridStop(4, 2, 'white'); }.defer(1);
}, page: page);

//////// Bank Change Functions:

submixerBankResetFunc = { | rowOffset = 4, thisPage = 'current' |
  ~ohm.turnCyan(0, 4, page: thisPage);
  ~ohm.turnPurple(1, 4, page: thisPage);
  ~ohm.turnYellow(2, 4, page: thisPage);
};

//// initial colors:
~ohm.turnGridGreen(0, 3, page: page);
submixerBankResetFunc.value(4, page);
submixerBankResetFunc.value(5, page);
submixerBankResetFunc.value(6, page);

// sends bank:
~ohm.setGridFunc(0, 3, {
  ~ohm.setActiveLeftKnobsBank(sendsBank);
  ~ohm.turnGridGreen(0, 3);
  submixerBankResetFunc.value(4);
  submixerBankResetFunc.value(5);
  submixerBankResetFunc.value(6);
}, page: page);

//// Submixer A:

// Peak 1:
~ohm.setGridFunc(0, 4, {
  ~ohm.setActiveLeftKnobsBank(submixerAPeak1Bank);
  submixerBankResetFunc.value(4);
  submixerBankResetFunc.value(5);
  submixerBankResetFunc.value(6);
  ~ohm.turnGridBlue(0, 3);
  ~ohm.turnGridGreen(0, 4);
}, page: page);

// Peak 2:
~ohm.setGridFunc(1, 4, {
  ~ohm.setActiveLeftKnobsBank(submixerAPeak2Bank);
  submixerBankResetFunc.value(4);
  submixerBankResetFunc.value(5);
  submixerBankResetFunc.value(6);
  ~ohm.turnGridBlue(0, 3);
  ~ohm.turnGridGreen(1, 4);
}, page: page);

// Peak 3:
~ohm.setGridFunc(2, 4, {
  ~ohm.setActiveLeftKnobsBank(submixerAPeak3Bank);
  submixerBankResetFunc.value(4);
  submixerBankResetFunc.value(5);
  submixerBankResetFunc.value(6);
  ~ohm.turnGridBlue(0, 3);
  ~ohm.turnGridGreen(2, 4);
}, page: page);

//// Submixer B:

// Peak 1:
~ohm.setGridFunc(0, 5, {
  ~ohm.setActiveLeftKnobsBank(submixerBPeak1Bank);
  submixerBankResetFunc.value(4);
  submixerBankResetFunc.value(5);
  submixerBankResetFunc.value(6);
  ~ohm.turnGridBlue(0, 3);
  ~ohm.turnGridGreen(0, 5);
}, page: page);

// Peak 2:
~ohm.setGridFunc(1, 5, {
  ~ohm.setActiveLeftKnobsBank(submixerBPeak2Bank);
  submixerBankResetFunc.value(4);
  submixerBankResetFunc.value(5);
  submixerBankResetFunc.value(6);
  ~ohm.turnGridBlue(0, 3);
  ~ohm.turnGridGreen(1, 5);
}, page: page);

// Peak 3:
~ohm.setGridFunc(2, 5, {
  ~ohm.setActiveLeftKnobsBank(submixerBPeak3Bank);
  submixerBankResetFunc.value(4);
  submixerBankResetFunc.value(5);
  submixerBankResetFunc.value(6);
  ~ohm.turnGridBlue(0, 3);
  ~ohm.turnGridGreen(2, 5);
}, page: page);

//// Submixer C:

// Peak 1:
~ohm.setGridFunc(0, 6, {
  ~ohm.setActiveLeftKnobsBank(submixerCPeak1Bank);
  submixerBankResetFunc.value(4);
  submixerBankResetFunc.value(5);
  submixerBankResetFunc.value(6);
  ~ohm.turnGridBlue(0, 3);
  ~ohm.turnGridGreen(0, 6);
}, page: page);

// Peak 2:
~ohm.setGridFunc(1, 6, {
  ~ohm.setActiveLeftKnobsBank(submixerCPeak2Bank);
  submixerBankResetFunc.value(4);
  submixerBankResetFunc.value(5);
  submixerBankResetFunc.value(6);
  ~ohm.turnGridBlue(0, 3);
  ~ohm.turnGridGreen(1, 6);
}, page: page);

// Peak 3:
~ohm.setGridFunc(2, 6, {
  ~ohm.setActiveLeftKnobsBank(submixerCPeak3Bank);
  submixerBankResetFunc.value(4);
  submixerBankResetFunc.value(5);
  submixerBankResetFunc.value(6);
  ~ohm.turnGridBlue(0, 3);
  ~ohm.turnGridGreen(2, 6);
}, page: page);



//////// knob functions:

// sends bank:

//// submixer A:
// Send 0:
~ohm.setLeftKnobFunc(0, 2, { | val |
  var vol = val.linlin(0, 127, -70, 0);
  ~prm.submixerA.mixer.setSendVol(0, vol);
}, page: page, bank: sendsBank);
// Send 1:
~ohm.setLeftKnobFunc(0, 1, { | val |
  var vol = val.linlin(0, 127, -70, 0);
  ~prm.submixerA.mixer.setSendVol(1, vol);
}, page: page, bank: sendsBank);
// Send 2:
~ohm.setLeftKnobFunc(0, 0, { | val |
  var vol = val.linlin(0, 127, -70, 0);
  ~prm.submixerA.setSendVol(2, vol)
}, page: page, bank: sendsBank);

////Submix B:
// Send 0:
~ohm.setLeftKnobFunc(1, 2, { | val |
  var vol = val.linlin(0, 127, -70, 0);
  ~prm.submixerB.mixer.setSendVol(0, vol);
}, page: page, bank: sendsBank);
// Send 1:
~ohm.setLeftKnobFunc(1, 1, { | val |
  var vol = val.linlin(0, 127, -70, 0);
  ~prm.submixerB.mixer.setSendVol(1, vol);
}, page: page, bank: sendsBank);
// Send 2:
~ohm.setLeftKnobFunc(1, 0, { | val |
  var vol = val.linlin(0, 127, -70, 0);
  ~prm.submixerB.setSendVol(2, vol)
}, page: page, bank: sendsBank);

//// Submix C:
// Send 0:
~ohm.setLeftKnobFunc(2, 2, { | val |
  var vol = val.linlin(0, 127, -70, 0);
  ~prm.submixerC.mixer.setSendVol(0, vol);
}, page: page, bank: sendsBank);
// Send 1:
~ohm.setLeftKnobFunc(2, 1, { | val |
  var vol = val.linlin(0, 127, -70, 0);
  ~prm.submixerC.mixer.setSendVol(1, vol);
}, page: page, bank: sendsBank);
// Send 2:
~ohm.setLeftKnobFunc(2, 0, { | val |
  var vol = val.linlin(0, 127, -70, 0);
  ~prm.submixerC.setSendVol(2, vol)
}, page: page, bank: sendsBank);


////// Submix A EQ:

//// Low:
// Low Freq:
3.do({ | bank |
  ~ohm.setGridFunc(0, 2, { | val |
    var freq = val.linexp(0, 127, 20, 2500);
    ~prm.submixerA.eq.setLowFreq(freq);
  }, bank: bank+1, page: page);
});
// Low RQ:
3.do({ | bank |
  ~ohm.setGridFunc(1, 2, { | val |
    var rq = val.linlin(0, 127, 1, 0.1);
    ~prm.submixerA.eq.setLowRQ(rq);
  }, bank: bank+1, page: page);
});

// Low Gain:
3.do({ | bank |
  ~ohm.setGridFunc(2, 2, { | val |
    var gain = val.linlin(0, 127, -12, 12);
    ~prm.submixerA.eq.setLowGain(gain);
  }, bank: bank+1, page: page);
});

//// Peak 1:
// Peak 1 Freq:
~ohm.setGridFunc(0, 1, { | val |
  var freq = val.linexp(0, 127, 60, 15000);
  ~prm.submixerA.eq.setPeak1Freq(freq);
}, bank: submixerAPeak1Bank, page: page);

// Peak 1 RQ:
~ohm.setGridFunc(1, 1, { | val |
  var rq = val.linlin(0, 127, 1, 0.01);
  ~prm.submixerA.eq.setPeak1RQ(rq);
}, bank: submixerAPeak1Bank, page: page);

// Peak 1 Gain:
~ohm.setGridFunc(2, 1, { | val |
  var gain = val.linlin(0, 127, -16, 16);
  ~prm.submixerA.eq.setPeak1Gain(gain);
}, bank: submixerAPeak1Bank, page: page);

//// Peak 2:
// Peak 12Freq:
~ohm.setGridFunc(0, 1, { | val |
  var freq = val.linexp(0, 127, 60, 15000);
  ~prm.submixerA.eq.setPeak2Freq(freq);
}, bank: submixerAPeak2Bank, page: page);

// Peak 2 RQ:
~ohm.setGridFunc(1, 1, { | val |
  var rq = val.linlin(0, 127, 1, 0.01);
  ~prm.submixerA.eq.setPeak2RQ(rq);
}, bank: submixerAPeak2Bank, page: page);

// Peak 2 Gain:
~ohm.setGridFunc(2, 1, { | val |
  var gain = val.linlin(0, 127, -16, 16);
  ~prm.submixerA.eq.setPeak2Gain(gain);
}, bank: submixerAPeak2Bank, page: page);

//// Peak 3:
// Peak 3 Freq:
~ohm.setGridFunc(0, 1, { | val |
  var freq = val.linexp(0, 127, 60, 15000);
  ~prm.submixerA.eq.setPeak3Freq(freq);
}, bank: submixerAPeak3Bank, page: page);

// Peak 3 RQ:
~ohm.setGridFunc(1, 1, { | val |
  var rq = val.linlin(0, 127, 1, 0.01);
  ~prm.submixerA.eq.setPeak3RQ(rq);
}, bank: submixerAPeak3Bank, page: page);

// Peak 3 Gain:
~ohm.setGridFunc(2, 1, { | val |
  var gain = val.linlin(0, 127, -16, 16);
  ~prm.submixerA.eq.setPeak3Gain(gain);
}, bank: submixerAPeak3Bank, page: page);

//// High:
// High Freq:
3.do({ | bank |
  ~ohm.setGridFunc(0, 0, { | val |
    var freq = val.linexp(0, 127, 250, 17000);
    ~prm.submixerA.eq.setHighFreq(freq);
  }, bank: bank+1, page: page);
});

// High RQ:
3.do({ | bank |
  ~ohm.setGridFunc(1, 0, { | val |
    var rq = val.linlin(0, 127, 1, 0.1);
    ~prm.submixerA.setHighRQ(rq);
  }, bank: bank+1, page: page);
});

// High Gain:
3.do({ | bank |
  ~ohm.setGridFunc(2, 0, { | val |
    var gain = val.linlin(0, 127, -12, 12);
    ~prm.submixerA.eq.setHighGain(gain);
  }, bank: bank+1, page: page);
});


////// Submix B EQ:

//// Low:
// Low Freq:
3.do({ | bank |
  ~ohm.setGridFunc(0, 2, { | val |
    var freq = val.linexp(0, 127, 20, 2500);
    ~prm.submixerB.eq.setLowFreq(freq);
  }, bank: bank+4, page: page);
});
// Low RQ:
3.do({ | bank |
  ~ohm.setGridFunc(1, 2, { | val |
    var rq = val.linlin(0, 127, 1, 0.1);
    ~prm.submixerB.eq.setLowRQ(rq);
  }, bank: bank+4, page: page);
});

// Low Gain:
3.do({ | bank |
  ~ohm.setGridFunc(2, 2, { | val |
    var gain = val.linlin(0, 127, -12, 12);
    ~prm.submixerB.eq.setLowGain(gain);
  }, bank: bank+4, page: page);
});

//// Peak 1:
// Peak 1 Freq:
~ohm.setGridFunc(0, 1, { | val |
  var freq = val.linexp(0, 127, 60, 15000);
  ~prm.submixerB.eq.setPeak1Freq(freq);
}, bank: submixerBPeak1Bank, page: page);

// Peak 1 RQ:
~ohm.setGridFunc(1, 1, { | val |
  var rq = val.linlin(0, 127, 1, 0.01);
  ~prm.submixerB.eq.setPeak1RQ(rq);
}, bank: submixerBPeak1Bank, page: page);

// Peak 1 Gain:
~ohm.setGridFunc(2, 1, { | val |
  var gain = val.linlin(0, 127, -16, 16);
  ~prm.submixerB.eq.setPeak1Gain(gain);
}, bank: submixerBPeak1Bank, page: page);

//// Peak 2:
// Peak 12Freq:
~ohm.setGridFunc(0, 1, { | val |
  var freq = val.linexp(0, 127, 60, 15000);
  ~prm.submixerB.eq.setPeak2Freq(freq);
}, bank: submixerBPeak2Bank, page: page);

// Peak 2 RQ:
~ohm.setGridFunc(1, 1, { | val |
  var rq = val.linlin(0, 127, 1, 0.01);
  ~prm.submixerB.eq.setPeak2RQ(rq);
}, bank: submixerBPeak2Bank, page: page);

// Peak 2 Gain:
~ohm.setGridFunc(2, 1, { | val |
  var gain = val.linlin(0, 127, -16, 16);
  ~prm.submixerB.eq.setPeak2Gain(gain);
}, bank: submixerBPeak2Bank, page: page);

//// Peak 3:
// Peak 3 Freq:
~ohm.setGridFunc(0, 1, { | val |
  var freq = val.linexp(0, 127, 60, 15000);
  ~prm.submixerB.eq.setPeak3Freq(freq);
}, bank: submixerBPeak3Bank, page: page);

// Peak 3 RQ:
~ohm.setGridFunc(1, 1, { | val |
  var rq = val.linlin(0, 127, 1, 0.01);
  ~prm.submixerB.eq.setPeak3RQ(rq);
}, bank: submixerBPeak3Bank, page: page);

// Peak 3 Gain:
~ohm.setGridFunc(2, 1, { | val |
  var gain = val.linlin(0, 127, -16, 16);
  ~prm.submixerB.eq.setPeak3Gain(gain);
}, bank: submixerBPeak3Bank, page: page);

//// High:
// High Freq:
3.do({ | bank |
  ~ohm.setGridFunc(0, 0, { | val |
    var freq = val.linexp(0, 127, 250, 17000);
    ~prm.submixerB.eq.setHighFreq(freq);
  }, bank: bank+4, page: page);
});

// High RQ:
3.do({ | bank |
  ~ohm.setGridFunc(1, 0, { | val |
    var rq = val.linlin(0, 127, 1, 0.1);
    ~prm.submixerB.setHighRQ(rq);
  }, bank: bank+4, page: page);
});

// High Gain:
3.do({ | bank |
  ~ohm.setGridFunc(2, 0, { | val |
    var gain = val.linlin(0, 127, -12, 12);
    ~prm.submixerB.eq.setHighGain(gain);
  }, bank: bank+4, page: page);
});

////// Submix C EQ:
//// Low:
// Low Freq:
3.do({ | bank |
  ~ohm.setGridFunc(0, 2, { | val |
    var freq = val.linexp(0, 127, 20, 2500);
    ~prm.submixerC.eq.setLowFreq(freq);
  }, bank: bank+7, page: page);
});
// Low RQ:
3.do({ | bank |
  ~ohm.setGridFunc(1, 2, { | val |
    var rq = val.linlin(0, 127, 1, 0.1);
    ~prm.submixerC.eq.setLowRQ(rq);
  }, bank: bank+7, page: page);
});

// Low Gain:
3.do({ | bank |
  ~ohm.setGridFunc(2, 2, { | val |
    var gain = val.linlin(0, 127, -12, 12);
    ~prm.submixerC.eq.setLowGain(gain);
  }, bank: bank+7, page: page);
});

//// Peak 1:
// Peak 1 Freq:
~ohm.setGridFunc(0, 1, { | val |
  var freq = val.linexp(0, 127, 60, 15000);
  ~prm.submixerC.eq.setPeak1Freq(freq);
}, bank: submixerCPeak1Bank, page: page);

// Peak 1 RQ:
~ohm.setGridFunc(1, 1, { | val |
  var rq = val.linlin(0, 127, 1, 0.01);
  ~prm.submixerC.eq.setPeak1RQ(rq);
}, bank: submixerCPeak1Bank, page: page);

// Peak 1 Gain:
~ohm.setGridFunc(2, 1, { | val |
  var gain = val.linlin(0, 127, -16, 16);
  ~prm.submixerC.eq.setPeak1Gain(gain);
}, bank: submixerCPeak1Bank, page: page);

//// Peak 2:
// Peak 12Freq:
~ohm.setGridFunc(0, 1, { | val |
  var freq = val.linexp(0, 127, 60, 15000);
  ~prm.submixerC.eq.setPeak2Freq(freq);
}, bank: submixerCPeak2Bank, page: page);

// Peak 2 RQ:
~ohm.setGridFunc(1, 1, { | val |
  var rq = val.linlin(0, 127, 1, 0.01);
  ~prm.submixerC.eq.setPeak2RQ(rq);
}, bank: submixerCPeak2Bank, page: page);

// Peak 2 Gain:
~ohm.setGridFunc(2, 1, { | val |
  var gain = val.linlin(0, 127, -16, 16);
  ~prm.submixerC.eq.setPeak2Gain(gain);
}, bank: submixerCPeak2Bank, page: page);

//// Peak 3:
// Peak 3 Freq:
~ohm.setGridFunc(0, 1, { | val |
  var freq = val.linexp(0, 127, 60, 15000);
  ~prm.submixerC.eq.setPeak3Freq(freq);
}, bank: submixerCPeak3Bank, page: page);

// Peak 3 RQ:
~ohm.setGridFunc(1, 1, { | val |
  var rq = val.linlin(0, 127, 1, 0.01);
  ~prm.submixerC.eq.setPeak3RQ(rq);
}, bank: submixerCPeak3Bank, page: page);

// Peak 3 Gain:
~ohm.setGridFunc(2, 1, { | val |
  var gain = val.linlin(0, 127, -16, 16);
  ~prm.submixerC.eq.setPeak3Gain(gain);
}, bank: submixerCPeak3Bank, page: page);

//// High:
// High Freq:
3.do({ | bank |
  ~ohm.setGridFunc(0, 0, { | val |
    var freq = val.linexp(0, 127, 250, 17000);
    ~prm.submixerC.eq.setHighFreq(freq);
  }, bank: bank+7, page: page);
});

// High RQ:
3.do({ | bank |
  ~ohm.setGridFunc(1, 0, { | val |
    var rq = val.linlin(0, 127, 1, 0.1);
    ~prm.submixerC.setHighRQ(rq);
  }, bank: bank+7, page: page);
});

// High Gain:
3.do({ | bank |
  ~ohm.setGridFunc(2, 0, { | val |
    var gain = val.linlin(0, 127, -12, 12);
    ~prm.submixerC.eq.setHighGain(gain);
  }, bank: bank+7, page: page);
});

//////////// Left Sliders Funcs:

//// Waveloss:

// Submixer A:
~ohm.setLeftSliderFunc(0, { | val |
  var waveloss = val.linlin(0, 127, 0, 90);
  ~prm.submixerA.setWaveLossAmount(waveloss);
}, page: page);

// Submixer B:
~ohm.setLeftSliderFunc(1, { | val |
  var waveloss = val.linlin(0, 127, 0, 90);
  ~prm.submixerB.setWaveLossAmount(waveloss);
}, page: page);

// Submixer C:
~ohm.setLeftSliderFunc(2, { | val |
  var waveloss = val.linlin(0, 127, 0, 90);
  ~prm.submixerC.setWaveLossAmount(waveloss);
}, page: page);
