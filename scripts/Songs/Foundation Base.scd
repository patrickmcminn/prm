/*
Tuesday, June 20th 2017
Foundation Base.scd
prm
*/

var page = 'foundation';

var mixerBank1 = 0;
var mixerBank2 = 1;
var activeFadersBank = mixerBank1;

var mainGridBank = 0;

var dorian = [2, 4, 5, 7, 9, 11, 12]+36;

~base.makePage(page);

~base.addTouchButtonsBanks(3, page: page);
~base.addFadersBanks(3, page: page);
~base.addGridBanks(2, page: page);

////////////////////////////
///// Main Grid Bank: /////
//////////////////////////

///// Keyboard!:
~base.turnAllGridMagenta(mainGridBank, page);
4.do({ | i | ~base.turnGridOff(7, i, mainGridBank, page); });

7.do({ | i |
  ~base.setGridFunc(i, 0, {
    ~foundation.moog.moog.playNote(dorian[i].midicps);
    ~base.turnGridWhite(i, 0);
    },'noteOn', bank: mainGridBank, page: page);
   ~base.setGridFunc(i, 0, {
    ~foundation.moog.moog.releaseNote(dorian[i].midicps);
    ~base.turnGridMagenta(i, 0);
    }, 'noteOff', bank: mainGridBank, page: page);
});
7.do({ | i |
  ~base.setGridFunc(i, 1, { ~foundation.moog.moog.playNote((dorian[i]+12).midicps);
    ~base.turnGridWhite(i, 1);
    },'noteOn', bank: mainGridBank, page: page);
  ~base.setGridFunc(i, 1, { ~foundation.moog.moog.releaseNote((dorian[i]+12).midicps);
    ~base.turnGridMagenta(i, 1);
  }, 'noteOff', bank: mainGridBank, page: page);
});
7.do({ | i |
  ~base.setGridFunc(i, 2, { ~foundation.moog.moog.playNote((dorian[i]+24).midicps);
      ~base.turnGridWhite(i, 2);
      }, 'noteOn', bank: mainGridBank, page: page);
  ~base.setGridFunc(i, 2, {
      ~foundation.moog.moog.releaseNote((dorian[i]+24).midicps);
      ~base.turnGridMagenta(i, 2);
    }, 'noteOff', bank: mainGridBank, page: page);
  });
7.do({ | i |
  ~base.setGridFunc(i, 3, {
      ~foundation.moog.moog.playNote((dorian[i]+36).midicps);
      ~base.turnGridWhite(i, 3);
      }, 'noteOn', bank: mainGridBank, page: page);
  ~base.setGridFunc(i, 3, {
      ~foundation.moog.moog.releaseNote((dorian[i]+36).midicps);
      ~base.turnGridMagenta(i, 3);
    }, 'noteOff', bank: mainGridBank, page: page);
});

//// Buffer Record:
~base.turnGridBlue(7, 3, mainGridBank, page);
~base.turnGridCyan(7, 2, mainGridBank, page);
~base.turnGridYellow(7, 1, mainGridBank, page);

// low D:
~base.setGridFunc(7, 3, {
  ~foundation.songBuffers.recordLowDBuffer;
  ~base.turnGridGreen(7, 3);
}, bank: mainGridBank, page: page);
// a:
~base.setGridFunc(7, 2, {
  ~foundation.songBuffers.recordABuffer;
  ~base.turnGridGreen(7, 2);
}, bank: mainGridBank, page: page);
// high D:
~base.setGridFunc(7, 1, {
  ~foundation.songBuffers.recordHighDBuffer;
  ~base.turnGridGreen(7, 1);
}, bank: mainGridBank, page: page);

////////////////////////////
///// Upper Bank: /////////
//////////////////////////

//// Moog Volume:
// Slider:
~base.setFaderMode(1, 'whiteFill', mixerBank1, page);
~base.setFaderMonitorFunc(1, {
  ~base.setFaderValue(1, ~foundation.mixerA.vol(0).dbfsCC);
}, mixerBank1, page);
~base.setFaderFunc(1, { | val |
  ~foundation.mixerA.setVol(0, val.ccdbfs(-70, 0));
}, mixerBank1, page);
// Button:
~base.setTouchButtonMonitorFunc(1, { if( ~foundation.mixerA.isMuted(0),
  { ~base.turnTouchButtonRed(1) }, { ~base.turnTouchButtonGreen(1) });
}, mixerBank1, page);
~base.setTouchButtonFunc(1, { ~foundation.mixerA.tglMute(0); }, bank: mixerBank1, page: page);

//// HighPass :
// Slider:
~base.setFaderMode(2, 'yellowWalk', mixerBank1, page);
~base.setFaderMonitorFunc(2, {
  var eq = ~foundation.moog.eq.highPassCutoff.explin(20, 1310, 0, 127);
  ~base.setFaderValue(2, eq, mixerBank1, page);
}, mixerBank1, page);
~base.setFaderFunc(2, { | val |
  var eq = val.linexp(0, 127, 20, 1310);
  ~foundation.moog.eq.setHighPassCutoff(eq);
}, mixerBank1, page);

//// LowPass:
// Slider:
~base.setFaderMode(3, 'yellowWalk', mixerBank1, page);
~base.setFaderMonitorFunc(3, {
  var eq = ~foundation.moog.eq.lowPassCutoff.explin(3010, 20000, 0, 127);
  ~base.setFaderValue(3, eq, mixerBank1, page);
}, mixerBank1, page);
~base.setFaderFunc(3, { | val |
  var eq = val.linexp(0, 127, 3010, 20000);
  ~foundation.moog.eq.setLowPassCutoff(eq);
}, mixerBank1, page);

//// Delay Send:
// Slider:
~base.setFaderMode(4, 'blueFill', mixerBank1, page);
~base.setFaderMonitorFunc(4, {
  var vol = ~foundation.mixerA.sendVol(0, 3).dbfsCC;
  ~base.setFaderValue(4, vol);
}, mixerBank1, page);
~base.setFaderFunc(4, { | val |
  ~foundation.mixerA.setSendVol(0, 3, val.ccdbfs(-70, 0));
}, mixerBank1, page);

