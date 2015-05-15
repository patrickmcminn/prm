var page = 'subtractive';
var controlButtonsResetFunc;
var basicBank = 0;
var returnsBank = 7;
var octaveAdd = [36, 48, 60, 72];
var majorGridBank = 0;
var majorArray = [0, 2, 4, 5, 7, 9, 11, 12];
var majorArrayColor = ['white', 'cyan', 'magenta', 'cyan', 'yellow', 'cyan', 'blue', 'white'];


/* // if things need to be made:
~prm = AudioSystem.new(2);
~prm.systemMixer.setVol(0, 24);
~base = Base.new;
~sub = Subtractive.new(~prm.submixA, ~prm.reverb.inBus, ~prm.granulator.inBus);
*/


~base.makePage(page);
~base.setPage(page);
~base.addFadersBanks(7, page: page);
~base.addTouchButtonsBanks(7, page: page);
~base.addGridBanks(7, page: page);

//////// Control Buttons:

controlButtonsResetFunc = {
  7.do({ | i | ~base.turnControlButtonMagenta(i + 1, 'left', page: page); });
  ~base.turnControlButtonWhite(8, 'left', page: page);
  ~base.turnControlButtonWhite(8, 'right', page: page);
};
controlButtonsResetFunc.value;


////////////////////
//// Bank Change:///
////////////////////

// basic synth controls bank:
~base.setControlButtonFunc(1, {
  ~base.setActiveFadersBank(basicBank);
  controlButtonsResetFunc.value;
  ~base.turnControlButtonGreen(1, 'right', page: page);
}, page: page);

// returns bank:
~base.setControlButtonFunc(8, {
  ~base.setActiveFadersBank(returnsBank);
  //~base.setActiveTouchButtonsBank(returnsBank);
  controlButtonsResetFunc.value;
  ~base.turnControlButtonGreen(8, 'right', page: page);
}, page: page);



/////////////
// Grids: //
////////////

//////// Major Grid Bank:

// first row:
8.do({ | i | ~base.setGridFunc(i, 0, { | vel |
  var vol = vel.ccdbfs(-24, 3);
  ~sub.playNote((majorArray[i] + octaveAdd[0]).midicps, vol);
  ~base.turnGridGreen(i, 0);
  }, 'noteOn', bank: majorGridBank, page: page);
});
8.do({ | i | ~base.setGridFunc(i, 0, {
  ~sub.releaseNote((majorArray[i] + octaveAdd[0]).midicps);
  ~base.turnGridColor(i, 0, majorArrayColor[i], page: page);
  }, 'noteOff', bank: majorGridBank, page: page);
});

// second row:
8.do({ | i | ~base.setGridFunc(i, 1, { | vel |
  var vol = vel.ccdbfs(-24, 3);
  ~sub.playNote((majorArray[i] + octaveAdd[1]).midicps, vol);
  ~base.turnGridGreen(i, 1);
  }, 'noteOn', bank: majorGridBank, page: page);
});
8.do({ | i | ~base.setGridFunc(i, 1, {
  ~sub.releaseNote((majorArray[i] + octaveAdd[1]).midicps);
  ~base.turnGridColor(i, 1, majorArrayColor[i], page: page);
  }, 'noteOff', bank: majorGridBank, page: page);
});

// third row:
8.do({ | i | ~base.setGridFunc(i, 2, { | vel |
  var vol = vel.ccdbfs(-24, 3);
  ~sub.playNote((majorArray[i] + octaveAdd[2]).midicps, vol);
  ~base.turnGridGreen(i, 2);
  }, 'noteOn', bank: majorGridBank, page: page);
});
8.do({ | i | ~base.setGridFunc(i, 2, {
  ~sub.releaseNote((majorArray[i] + octaveAdd[2]).midicps);
  ~base.turnGridColor(i, 2, majorArrayColor[i], page: page);
  }, 'noteOff', bank: majorGridBank, page: page);
});

// fourth row:
8.do({ | i | ~base.setGridFunc(i, 3, { | vel |
  var vol = vel.ccdbfs(-24, 3);
  ~sub.playNote((majorArray[i] + octaveAdd[3]).midicps, vol);
  ~base.turnGridGreen(i, 3);
  }, 'noteOn', bank: majorGridBank, page: page);
});
8.do({ | i | ~base.setGridFunc(i, 3, {
  ~sub.releaseNote((majorArray[i] + octaveAdd[3]).midicps);
  ~base.turnGridColor(i, 3, majorArrayColor[i], page: page);
  }, 'noteOff', bank: majorGridBank, page: page);
});




////////////////////
//synth subbanks: //
////////////////////

//// basis synth bank:

~base.setAllFaderModes('greenFill', bank: basicBank, page: page);

// cutoff:
~base.setFaderFunc(1, { | val |
  var cutoff = val.linexp(0, 127, 100, 17000);
  ~sub.setFilterCutoff(cutoff);
  ~base.setFaderValue(1, val, page: page);
}, bank: basicBank, page: page);
// res:
~base.setFaderFunc(2, { | val |
  var res = val.linlin(0, 127, 0.0, 1.2);
  ~sub.setFilterRes(res);
  ~base.setFaderValue(2, val, page: page);
}, bank: basicBank, page: page);
// attack time:
~base.setFaderFunc(3, { | val |
  var attack = val.linlin(0, 127, 0.05, 5);
  ~sub.setAttackTime(attack);
  ~base.setFaderValue(3, val, page: page);
}, bank: basicBank, page: page);
// decay time:
~base.setFaderFunc(4, { | val |
  var decay = val.linlin(0, 127, 0.05, 3);
  ~sub.setDecayTime(decay);
  ~base.setFaderValue(4, val, page: page);
}, bank: basicBank, page: page);
// sustain level:
~base.setFaderFunc(5, { | val |
  var sustain = val.linlin(0, 127, 0.0, 1.0);
  ~sub.setSustainLevel(sustain);
  ~base.setFaderValue(5, val, page: page);
}, bank: basicBank, page: page);
// release level:
~base.setFaderFunc(6, { | val |
  var release = val.linlin(0, 127, 0.05, 7);
  ~sub.setReleaseTime(release);
  ~base.setFaderValue(6, val, page: page);
}, bank: basicBank, page: page);



//// returns bank:
~base.setAllFaderModes('cyanFill', bank: returnsBank, page: page);

// reverb send:
~base.setFaderFunc(1, { | val |
  ~sub.mixer.setSendVol(0, val.ccdbfs(-70, 0));
  ~base.setFaderValue(1, val, page: page);
}, bank: returnsBank, page: page);

// granulator send:
~base.setFaderFunc(2, { | val |
  ~sub.mixer.setSendVol(1, val.ccdbfs(-70, 6));
  ~base.setFaderValue(2, val, page: page);
}, bank: returnsBank, page: page);





