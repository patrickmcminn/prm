/*
Saturday, April 22nd 2017
Light Base.scd
prm
*/

var page = 'light';

var main = 0;

var mixo = [0, 2, 4, 5, 7, 9, 10] + 2 + 36;

~base.makePage(page);

//////////////////
//// sliders: ///
////////////////

~base.setFaderMode(1, 'whiteFill', main, page);
~base.setFaderMode(2, 'blueFill', main, page);
~base.setFaderMode(3, 'blueFill', main, page);
~base.setFaderMode(4, 'blueFill', main, page);
~base.setFaderMode(5, 'blueFill', main, page);


~base.setFaderMonitorFunc(1, {
  var modEnable = ~light.mixer.isMuted(8);
  var mod = ~light.mixer.vol(8).dbfsCC;
  var modVerb = ~light.mixer.sendVol(8, 0).dbfsCC;
  var modGran = ~light.mixer.sendVol(8, 1).dbfsCC;
  var modMod = ~light.mixer.sendVol(8, 2).dbfsCC;
  var modDel = ~light.mixer.sendVol(8, 3).dbfsCC;

  if( modEnable == true, { ~base.turnTouchButtonRed(1) }, { ~base.turnTouchButtonGreen(1) });
  ~base.setFaderValue(1, mod);
  ~base.setFaderValue(2, modVerb);
  ~base.setFaderValue(3, modGran);
  ~base.setFaderValue(4, modMod);
  ~base.setFaderValue(5, modDel);
}, main, page);

~base.setTouchButtonFunc(1, { ~light.mixer.tglMute(8); }, bank: main, page: page);

~base.setFaderFunc(1, { | val | ~light.mixer.setVol(8, val.ccdbfs) }, main, page);
~base.setFaderFunc(2, { | val | ~light.mixer.setSendVol(8, 0, val.ccdbfs) }, main, page);
~base.setFaderFunc(3, { | val | ~light.mixer.setSendVol(8, 1, val.ccdbfs) }, main, page);
~base.setFaderFunc(4, { | val | ~light.mixer.setSendVol(8, 2, val.ccdbfs) }, main, page);
~base.setFaderFunc(5, { | val | ~light.mixer.setSendVol(8, 3, val.ccdbfs) }, main, page);



////////////////
//// Grid: ////
//////////////

~base.turnAllGridYellow(main, page);

7.do({ | i |
  ~base.setGridFunc(i, 0, {
    ~light.bass.playNote(mixo[i].midicps);
    ~base.turnGridWhite(i, 0);
    },'noteOn', bank: main, page: page);
   ~base.setGridFunc(i, 0, {
    ~light.bass.releaseNote(mixo[i].midicps);
    ~base.turnGridYellow(i, 0);
    }, 'noteOff', bank: main, page: page);
});
7.do({ | i |
  ~base.setGridFunc(i, 1, {
    ~light.bass.playNote((mixo[i]+12).midicps);
    ~base.turnGridWhite(i, 1);
    },'noteOn', bank: main, page: page);
   ~base.setGridFunc(i, 1, {
    ~light.bass.releaseNote((mixo[i]+12).midicps);
    ~base.turnGridYellow(i, 1);
    }, 'noteOff', bank: main, page: page);
});
7.do({ | i |
  ~base.setGridFunc(i, 2, {
    ~light.bass.playNote((mixo[i]+24).midicps);
    ~base.turnGridWhite(i, 2);
    },'noteOn', bank: main, page: page);
   ~base.setGridFunc(i, 2, {
    ~light.bass.releaseNote((mixo[i]+24).midicps);
    ~base.turnGridYellow(i, 2);
    }, 'noteOff', bank: main, page: page);
});
7.do({ | i |
  ~base.setGridFunc(i, 3, {
    ~light.bass.playNote((mixo[i]+36).midicps);
    ~base.turnGridWhite(i, 3);
    },'noteOn', bank: main, page: page);
   ~base.setGridFunc(i, 3, {
    ~light.bass.releaseNote((mixo[i]+36).midicps);
    ~base.turnGridYellow(i, 3);
    }, 'noteOff', bank: main, page: page);
});

