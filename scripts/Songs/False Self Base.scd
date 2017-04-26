/*
Sunday, April 16th 2017
FalseSelfBase.scd
prm
*/

var page = 'falseSelf';

var mainGridBank = 0;
var phrase1Bank = 1;
var phrase2Bank = 2;
var activeGridBank = mainGridBank;

var mixerBank1 = 0;
var mixerBank2 = 1;
var activeFadersBank = mixerBank1;

var phraseResetFunc;

~base.makePage(page);

~base.addTouchButtonsBanks(3, page: page);
~base.addFadersBanks(3, page: page);
~base.addGridBanks(2, page: page);

//////// Control Bank Change:

~base.setControlButtonMonitorFunc(1, {
  ~base.turnControlButtonMagenta(1);
  ~base.turnControlButtonMagenta(1, 'right');
  ~base.turnControlButtonBlue(2);
  ~base.turnControlButtonBlue(2, 'right');
  ~base.turnControlButtonGreen(activeFadersBank + 1, 'right');
}, page: page);

~base.setControlButtonFunc(1, {
	activeFadersBank = mixerBank1;
	~base.setActiveFadersBank(activeFadersBank);
  ~base.setActiveTouchButtonsBank(activeFadersBank);
}, page: page);
~base.setControlButtonFunc(2, {
	activeFadersBank = mixerBank2;
	~base.setActiveFadersBank(activeFadersBank);
  ~base.setActiveTouchButtonsBank(activeFadersBank);
}, page: page);

//// pitch shifter banks:
~base.setControlButtonMonitorFunc(3, {
  ~base.turnControlButtonYellow(3);
  ~base.turnControlButtonYellow(3, 'right');
  ~base.turnControlButtonCyan(4);
  ~base.turnControlButtonCyan(4, 'right');
  ~base.turnControlButtonWhite(5);
  ~base.turnControlButtonWhite(5, 'right');
  ~base.turnControlButtonGreen(activeGridBank + 3, 'right');
}, page: page);

~base.setControlButtonFunc(3, {
  activeGridBank = mainGridBank;
  ~base.setActiveGridBank(activeGridBank);
}, page: page);

~base.setControlButtonFunc(4, {
  activeGridBank = phrase1Bank;
  ~base.setActiveGridBank(activeGridBank);
}, page: page);

~base.setControlButtonFunc(5, {
  activeGridBank = phrase2Bank;
  ~base.setActiveGridBank(activeGridBank);
}, page: page);


//////// Mixer:

////// Mixer Bank 1:

//// FakeGuitar:
~base.setFaderMode(1, 'whiteFill', bank: mixerBank1, page: page);
// Mute:
~base.setTouchButtonMonitorFunc(1, {
	if( ~falseSelf.mixerA.isMuted(0), { ~base.turnTouchButtonRed(1) }, { ~base.turnTouchButtonGreen(1) });
}, bank: mixerBank1, page: page);
~base.setTouchButtonFunc(1, { ~falseSelf.mixerA.tglMute(0) }, bank: mixerBank1, page: page);
// volume:
~base.setFaderMonitorFunc(1, {
	~base.setFaderValue(1, ~falseSelf.mixerA.vol(0).dbfsCC);
}, bank: mixerBank1, page: page);
~base.setFaderFunc(1, { | val | ~falseSelf.mixerA.setVol(0, val.ccdbfs);
}, bank: mixerBank1, page: page);

//// Drums:
~base.setFaderMode(2, 'yellowFill', bank: mixerBank1, page: page);
// Mute:
~base.setTouchButtonMonitorFunc(2, {
	if( ~falseSelf.mixerB.isMuted(2), { ~base.turnTouchButtonRed(2) }, { ~base.turnTouchButtonGreen(2) });
}, bank: mixerBank1, page: page);
~base.setTouchButtonFunc(2, { ~falseSelf.mixerB.tglMute(2) }, bank: mixerBank1, page: page);
// Volume:
~base.setFaderMonitorFunc(2, {
	~base.setFaderValue(2, ~falseSelf.mixerB.vol(2).dbfsCC);
}, bank: mixerBank1, page: page);
~base.setFaderFunc(2, { | val | ~falseSelf.mixerB.setVol(2, val.ccdbfs);
}, bank: mixerBank1, page: page);

//// Bass:
~base.setFaderMode(3, 'cyanFill', bank: mixerBank1, page: page);
// Mute:
~base.setTouchButtonMonitorFunc(3, {
	if( ~falseSelf.mixerB.isMuted(1), { ~base.turnTouchButtonRed(3) }, { ~base.turnTouchButtonGreen(3) });
}, bank: mixerBank1, page: page);
~base.setTouchButtonFunc(3, { ~falseSelf.mixerB.tglMute(1) });
// Volume:
~base.setFaderMonitorFunc(3, {
	~base.setFaderValue(3, ~falseSelf.mixerB.vol(1).dbfsCC);
}, bank: mixerBank1, page: page);
~base.setFaderFunc(3, { | val | ~falseSelf.mixerB.setVol(1, val.ccdbfs);
}, bank: mixerBank1, page: page);

//// Trumpet:
~base.setFaderMode(4, 'magentaFill', bank: mixerBank1, page: page);
// Mute:
~base.setTouchButtonMonitorFunc(4, {
	if( ~falseSelf.mixerA.isMuted(2), { ~base.turnTouchButtonRed(4) }, { ~base.turnTouchButtonGreen(4) });
}, bank: mixerBank1, page: page);
~base.setTouchButtonFunc(4, { ~falseSelf.mixerA.tglMute(2) }, bank: mixerBank1, page: page);
//// Volume:
~base.setFaderMonitorFunc(4, {
	~base.setFaderValue(4, ~falseSelf.mixerA.vol(2).dbfsCC);
}, bank: mixerBank1, page: page);
~base.setFaderFunc(4, { | val | ~falseSelf.mixerA.setVol(2, val.ccdbfs);
}, bank: mixerBank1, page: page);

//// Bells:
~base.setFaderMode(5, 'blueFill', bank: mixerBank1, page: page);
// Mute:
~base.setTouchButtonMonitorFunc(5, {
	if( ~falseSelf.mixerA.isMuted(1), { ~base.turnTouchButtonRed(5) }, { ~base.turnTouchButtonGreen(5) });
}, bank: mixerBank1, page: page);
~base.setTouchButtonFunc(5, { ~falseSelf.mixerA.tglMute(1) }, bank: mixerBank1, page: page);
// Volume:
~base.setFaderMonitorFunc(5, {
	~base.setFaderValue(5, ~falseSelf.mixerA.vol(1).dbfsCC );
}, bank: mixerBank1, page: page);
~base.setFaderFunc(5, { | val | ~falseSelf.mixerA.setVol(1, val.ccdbfs);
}, bank: mixerBank1, page: page);

//// Melody:
~base.setFaderMode(6, 'cyanFill', bank: mixerBank1, page: page);
// Mute:
~base.setTouchButtonMonitorFunc(6, {
	if( ~falseSelf.mixerB.isMuted(0), { ~base.turnTouchButtonRed(6) }, { ~base.turnTouchButtonGreen(6) });
}, bank: mixerBank1, page: page);
~base.setTouchButtonFunc(6, { ~falseSelf.mixerB.tglMute(0) }, bank: mixerBank1, page: page);
// Volume:
~base.setFaderMonitorFunc(6, {
	~base.setFaderValue(6, ~falseSelf.mixerB.vol(0).dbfsCC);
}, bank: mixerBank1, page: page);
~base.setFaderFunc(6, { | val | ~falseSelf.mixerB.setVol(0, val.ccdbfs);
}, bank: mixerBank1, page: page);

//// Modular:
~base.setFaderMode(7, 'whiteFill', bank: mixerBank1, page: page);
// Mute:
~base.setTouchButtonMonitorFunc(7, {
	if( ~falseSelf.mixerC.isMuted(0), { ~base.turnTouchButtonRed(7) }, { ~base.turnTouchButtonGreen(7) });
}, bank: mixerBank1, page: page);
~base.setTouchButtonFunc(7, { ~falseSelf.mixerC.tglMute(0) }, bank: mixerBank1, page: page);
// Volume:
~base.setFaderMonitorFunc(7, {
	~base.setFaderValue(7, ~falseSelf.mixerC.vol(0).dbfsCC );
}, bank: mixerBank1, page: page);
~base.setFaderFunc(7, { | val | ~falseSelf.mixerC.setVol(0, val.ccdbfs);
}, bank: mixerBank1, page: page);

//// Trumpet Canon:
~base.setFaderMode(8, 'magentaFill', bank: mixerBank1, page: page);
// Mute:
~base.setTouchButtonMonitorFunc(8, {
	if( ~falseSelf.mixerA.isMuted(3), { ~base.turnTouchButtonRed(8) }, { ~base.turnTouchButtonGreen(8) });
}, bank: mixerBank1, page: page);
~base.setTouchButtonFunc(8, { ~falseSelf.mixerA.tglMute(3) }, bank: mixerBank1, page: page);
// Volume:
~base.setFaderMonitorFunc(8, {
	~base.setFaderValue(8, ~falseSelf.mixerA.vol(3).dbfsCC );
}, bank: mixerBank1, page: page);
~base.setFaderFunc(8, { | val | ~falseSelf.mixerA.setVol(3, val.ccdbfs);
}, bank: mixerBank1, page: page);

//// Plane Noise:
~base.setFaderMode(1, 'cyanFill', bank: mixerBank2, page: page);
// Mute:
~base.setTouchButtonMonitorFunc(1, {
	if( ~falseSelf.mixerC.isMuted(3), { ~base.turnTouchButtonRed(1) }, { ~base.turnTouchButtonGreen(1) });
}, bank: mixerBank2, page: page);
~base.setTouchButtonFunc(1, { ~falseSelf.mixerC.tglMute(3) }, bank: mixerBank2, page: page);
// Volume:
~base.setFaderMonitorFunc(1, {
	~base.setFaderValue(1, ~falseSelf.mixerC.vol(3).dbfsCC; );
}, bank: mixerBank2, page: page);
~base.setFaderFunc(1, { | val | ~falseSelf.mixerC.setVol(3, val.ccdbfs);
}, bank: mixerBank2, page: page);

//// Mid- Buzz:
~base.setFaderMode(2, 'cyanFill', bank: mixerBank2, page: page);
// Mute:
~base.setTouchButtonMonitorFunc(2, {
	if( ~falseSelf.mixerC.isMuted(4), { ~base.turnTouchButtonRed(2) }, { ~base.turnTouchButtonGreen(2) });
}, bank: mixerBank2, page: page);
~base.setTouchButtonFunc(2, { ~falseSelf.mixerC.tglMute(4) }, bank: mixerBank2, page: page);
// Volume:
~base.setFaderMonitorFunc(2, {
	~base.setFaderValue(2, ~falseSelf.mixerC.vol(4).dbfsCC; );
}, bank: mixerBank2, page: page);
~base.setFaderFunc(2, { | val | ~falseSelf.mixerC.setVol(4, val.ccdbfs);
}, bank: mixerBank2, page: page);

//// Crude Drones:
~base.setFaderMode(3, 'yellowFill', bank: mixerBank2, page: page);
// Mute:
~base.setTouchButtonMonitorFunc(3, {
	if( ~falseSelf.mixerC.isMuted(1), { ~base.turnTouchButtonRed(3) }, { ~base.turnTouchButtonGreen(3) });
}, bank: mixerBank2, page: page);
~base.setTouchButtonFunc(3, { ~falseSelf.mixerC.tglMute(1) }, bank: mixerBank2, page: page);
// Volume:
~base.setFaderMonitorFunc(3, {
	~base.setFaderValue(3, ~falseSelf.mixerC.vol(1).dbfsCC );
}, bank: mixerBank2, page: page);
~base.setFaderFunc(3, { | val | ~falseSelf.mixerC.setVol(1, val.ccdbfs);
}, bank: mixerBank2, page: page);

//// Orchestra:
~base.setFaderMode(4, 'cyanFill', bank: mixerBank2, page: page);
// Mute:
~base.setTouchButtonMonitorFunc(4, {
	if( ~falseSelf.mixerB.isMuted(3), { ~base.turnTouchButtonRed(4) }, { ~base.turnTouchButtonGreen(4) });
}, bank: mixerBank2, page: page);
~base.setTouchButtonFunc(4, { ~falseSelf.mixerB.tglMute(3); }, bank: mixerBank2, page: page);
// Volume:
~base.setFaderMonitorFunc(4, {
	~base.setFaderValue(4,  ~falseSelf.mixerB.vol(3).dbfsCC );
}, bank: mixerBank2, page: page);
~base.setFaderFunc(4, { | val | ~falseSelf.mixerB.setVol(3, val.ccdbfs);
}, bank: mixerBank2, page: page);

//// 16th Drones:
~base.setFaderMode(5, 'yellowFill', bank: mixerBank2, page: page);
// Mute:
~base.setTouchButtonMonitorFunc(5, {
	if( ~falseSelf.mixerC.isMuted(2), { ~base.turnTouchButtonRed(5) }, { ~base.turnTouchButtonGreen(5) });
}, bank: mixerBank2, page: page);
~base.setTouchButtonFunc(5, { ~falseSelf.mixerC.tglMute(2); }, bank: mixerBank2, page: page);
// Volume:
~base.setFaderMonitorFunc(5, {
	~base.setFaderValue(5, ~falseSelf.mixerC.vol(2).dbfsCC );
}, bank: mixerBank2, page: page);
~base.setFaderFunc(5, { | val | ~falseSelf.mixerC.setVol(2, val.ccdbfs);
}, bank: mixerBank2, page: page);

//// Trumpet Melody:
~base.setFaderMode(6, 'magentaFill', bank: mixerBank2, page: page);
// Mute:
~base.setTouchButtonMonitorFunc(6, {
	if( ~falseSelf.mixerA.isMuted(4), { ~base.turnTouchButtonRed(6) }, { ~base.turnTouchButtonGreen(6) });
}, bank: mixerBank2, page: page);
~base.setTouchButtonFunc(6, { ~falseSelf.mixerA.tglMute(4); }, bank: mixerBank2, page: page);
// Volume:
~base.setFaderMonitorFunc(6, {
	~base.setFaderValue(6, ~falseSelf.mixerA.vol(4).dbfsCC );
}, bank: mixerBank2, page: page);
~base.setFaderFunc(6, { | val | ~falseSelf.mixerA.setVol(4, val.ccdbfs);
}, bank: mixerBank2, page: page);

//// Guitars:
~base.setFaderMode(7, 'cyanFill', bank: mixerBank2, page: page);
// Mute:
~base.setTouchButtonMonitorFunc(7, {
	if( ~falseSelf.mixerB.isMuted(4), { ~base.turnTouchButtonRed(7) }, { ~base.turnTouchButtonGreen(7) });
}, bank: mixerBank2, page: page);
~base.setTouchButtonFunc(7, { ~falseSelf.mixerB.tglMute(4); }, bank: mixerBank2, page: page);
// Volume:
~base.setFaderMonitorFunc(7, {
	~base.setFaderValue(7,  ~falseSelf.mixerB.vol(4).dbfsCC );
}, bank: mixerBank2, page: page);
~base.setFaderFunc(7, { | val | ~falseSelf.mixerB.setVol(4, val.ccdbfs);
}, bank: mixerBank2, page: page);

//// Trumpet End:
~base.setFaderMode(8, 'magentaFill', bank: mixerBank2, page: page);
// Mute:
~base.setTouchButtonMonitorFunc(8, {
	if( ~falseSelf.mixerA.isMuted(5), { ~base.turnTouchButtonRed(8) }, { ~base.turnTouchButtonGreen(8) });
}, bank: mixerBank2, page: page);
~base.setTouchButtonFunc(8, { ~falseSelf.mixerA.tglMute(5); }, bank: mixerBank2, page: page);
// Volume:
~base.setFaderMonitorFunc(8, {
	~base.setFaderValue(8, ~falseSelf.mixerA.vol(5).dbfsCC );
}, bank: mixerBank2, page: page);
~base.setFaderFunc(8, { | val | ~falseSelf.mixerA.setVol(5, val.ccdbfs);
}, bank: mixerBank2, page: page);

//////// main grid bank:

3.do({ | i | ~base.turnGridGreen(i, 2, mainGridBank, page); });
3.do({ | i | ~base.turnGridGreen(i, 3, mainGridBank, page); });

// G#:
~base.setGridFunc(0, 2, { | val |
  var vol = val.linlin(0, 127, 0, 0.5).ampdb;
  ~falseSelf.bellSection.playNote(68.midicps, vol-3, vol);
}, 'noteOn', mainGridBank, page);
// B#:
~base.setGridFunc(1, 2, { | val |
  var vol = val.linlin(0, 127, 0, 1).ampdb;
  ~falseSelf.bellSection.playNote(72.midicps, vol-3, vol);
}, bank: mainGridBank, page: page);
// C#:
~base.setGridFunc(2, 2, { | val |
  var vol = val.linlin(0, 127, 0, 1).ampdb;
  ~falseSelf.bellSection.playNote(73.midicps, vol-3, vol);
}, bank: mainGridBank, page: page);
// F#:
~base.setGridFunc(0, 3, { | val |
  var vol = val.linlin(0, 127, 0, 1).ampdb;
  ~falseSelf.bellSection.playNote(78.midicps, vol-3, vol);
}, bank: mainGridBank, page: page);
// D#
~base.setGridFunc(1, 3, { | val |
  var vol = val.linlin(0, 127, 0, 1).ampdb;
  ~falseSelf.bellSection.playNote(75.midicps, vol-3, vol);
}, bank: mainGridBank, page: page);
// E:
~base.setGridFunc(2, 3, { | val |
  var vol = val.linlin(0, 127, 0, 1).ampdb;
  ~falseSelf.bellSection.playNote(76.midicps, vol-3, vol);
}, bank: mainGridBank, page: page);


//// kick:
~base.turnGridWhite(3, 3, bank: mainGridBank, page: page);
~base.setGridFunc(3, 3, {
  ~falseSelf.drums.playDrum0;
  ~falseSelf.drums.playDrum1;
  ~falseSelf.drums.playDrum2(-12);
  ~falseSelf.drums.playDrum3(-12);
}, bank: mainGridBank, page: page);

//// shift 1 is -12:
~base.setGridMonitorFunc(4, 3, {
  if( ~falseSelf.trumpetMelody.shift1.shiftAmount == -12,
    { ~base.turnGridWhite(4, 3) }, { ~base.turnGridMagenta(4, 3) });
}, bank: mainGridBank, page: page);
~base.setGridFunc(4, 3, { ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12)
}, bank: mainGridBank, page: page);

////////////////////////////////////
///// Pitch Shifting Sequence: ////
//////////////////////////////////

phraseResetFunc = {
  ~base.turnAllGridMagenta;
  3.do({ | i | ~base.turnGridOff(i+5, 1); });
  4.do({ | i | ~base.turnGridOff(i + 4, 0); });
};

~base.turnAllGridMagenta(phrase1Bank, page: page);
~base.turnAllGridMagenta(phrase2Bank, page: page);
3.do({ | i | ~base.turnGridOff(i+5, 1, phrase1Bank, page); });
3.do({ | i | ~base.turnGridOff(i+5, 1, phrase2Bank, page); });
4.do({ | i | ~base.turnGridOff(i + 4, 0, phrase1Bank, page); });
4.do({ | i | ~base.turnGridOff(i + 4, 0, phrase2Bank, page); });

//////// Phrase 1:

// note 1:
~base.setGridFunc(0, 3, {
  phraseResetFunc.value;
  ~base.turnGridWhite(0, 3);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-15);
}, bank: phrase1Bank, page: page);

// note 2:
~base.setGridFunc(1, 3, {
  phraseResetFunc.value;
  ~base.turnGridWhite(1, 3);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-14);
}, bank: phrase1Bank, page: page);

// note 3:
~base.setGridFunc(2, 3, {
  phraseResetFunc.value;
  ~base.turnGridWhite(2, 3);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-17);
}, bank: phrase1Bank, page: page);

// note 4:
~base.setGridFunc(3, 3, {
  phraseResetFunc.value;
  ~base.turnGridWhite(3, 3);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-16);
}, bank: phrase1Bank, page: page);

// note 5:
~base.setGridFunc(0, 2, {
  phraseResetFunc.value;
  ~base.turnGridWhite(0, 2);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-15);
}, bank: phrase1Bank, page: page);

// note 6:
~base.setGridFunc(1, 2, {
  phraseResetFunc.value;
  ~base.turnGridWhite(1, 2);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-14);
}, bank: phrase1Bank, page: page);

// note 7:
~base.setGridFunc(2, 2, {
  phraseResetFunc.value;
  ~base.turnGridWhite(2, 2);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-17);
}, bank: phrase1Bank, page: page);

// note 8:
~base.setGridFunc(3, 2, {
  phraseResetFunc.value;
  ~base.turnGridWhite(3, 2);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-16);
}, bank: phrase1Bank, page: page);

// note 9:
~base.setGridFunc(0, 1, {
  phraseResetFunc.value;
  ~base.turnGridWhite(0, 1);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-16);
}, bank: phrase1Bank, page: page);


// note 10:
~base.setGridFunc(1, 1, {
  phraseResetFunc.value;
  ~base.turnGridWhite(1, 1);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-17);
}, bank: phrase1Bank, page: page);


// note 11:
~base.setGridFunc(2, 1, {
  phraseResetFunc.value;
  ~base.turnGridWhite(2, 1);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-15);
}, bank: phrase1Bank, page: page);

// note 12:
~base.setGridFunc(3, 1, {
  phraseResetFunc.value;
  ~base.turnGridWhite(3, 1);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-13);
}, bank: phrase1Bank, page: page);

// note 13:
~base.setGridFunc(0, 0, {
  phraseResetFunc.value;
  ~base.turnGridWhite(0, 0);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-16);
}, bank: phrase1Bank, page: page);

// note 14:
~base.setGridFunc(1, 0, {
  phraseResetFunc.value;
  ~base.turnGridWhite(1, 0);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-17);
}, bank: phrase1Bank, page: page);

// note 15:
~base.setGridFunc(2, 0, {
  phraseResetFunc.value;
  ~base.turnGridWhite(2, 0);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-15);
}, bank: phrase1Bank, page: page);

// note 16:
~base.setGridFunc(3, 0, {
  phraseResetFunc.value;
  ~base.turnGridWhite(3, 0);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-13);
}, bank: phrase1Bank, page: page);

// note 17:
~base.setGridFunc(4, 3, {
  phraseResetFunc.value;
  ~base.turnGridWhite(4, 3);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-16);
}, bank: phrase1Bank, page: page);

// note 18:
~base.setGridFunc(5, 3, {
  phraseResetFunc.value;
  ~base.turnGridWhite(5, 3);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-14);
}, bank: phrase1Bank, page: page);

// note 19:
~base.setGridFunc(6, 3, {
  phraseResetFunc.value;
  ~base.turnGridWhite(6, 3);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-15);
}, bank: phrase1Bank, page: page);

// note 20:
~base.setGridFunc(7, 3, {
  phraseResetFunc.value;
  ~base.turnGridWhite(7, 3);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-14);
}, bank: phrase1Bank, page: page);

// note 21:
~base.setGridFunc(4, 2, {
  phraseResetFunc.value;
  ~base.turnGridWhite(4, 2);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-4);
}, bank: phrase1Bank, page: page);

// note 22:
~base.setGridFunc(5, 2, {
  phraseResetFunc.value;
  ~base.turnGridWhite(5, 2);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-14);
}, bank: phrase1Bank, page: page);

// note 23:
~base.setGridFunc(6, 2, {
  phraseResetFunc.value;
  ~base.turnGridWhite(6, 2);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-15);
}, bank: phrase1Bank, page: page);

// note 24:
~base.setGridFunc(7, 2, {
  phraseResetFunc.value;
  ~base.turnGridWhite(7, 2);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-14);
}, bank: phrase1Bank, page: page);

// note 25:
~base.setGridFunc(4, 1, {
  phraseResetFunc.value;
  ~base.turnGridWhite(4, 1);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-12);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-20);
}, bank: phrase1Bank, page: page);

//////// Phrase 2:

// note 1:
~base.setGridFunc(0, 3, {
  phraseResetFunc.value;
  ~base.turnGridWhite(0, 3);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-8);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-15);
}, bank: phrase2Bank, page: page);

// note 2:
~base.setGridFunc(1, 3, {
  phraseResetFunc.value;
  ~base.turnGridWhite(1, 3);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-7);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-14);
}, bank: phrase2Bank, page: page);

// note 3:
~base.setGridFunc(2, 3, {
  phraseResetFunc.value;
  ~base.turnGridWhite(2, 3);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-9);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-17);
}, bank: phrase2Bank, page: page);

// note 4:
~base.setGridFunc(3, 3, {
  phraseResetFunc.value;
  ~base.turnGridWhite(3, 3);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-8);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-17);
}, bank: phrase2Bank, page: page);

// note 5:
~base.setGridFunc(0, 2, {
  phraseResetFunc.value;
  ~base.turnGridWhite(0, 2);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-8);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-15);
}, bank: phrase2Bank, page: page);

// note 6:
~base.setGridFunc(1, 2, {
  phraseResetFunc.value;
  ~base.turnGridWhite(1, 2);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-7);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-14);
}, bank: phrase2Bank, page: page);

// note 7:
~base.setGridFunc(2, 2, {
  phraseResetFunc.value;
  ~base.turnGridWhite(2, 2);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-9);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-17);
}, bank: phrase2Bank, page: page);

// note 8:
~base.setGridFunc(3, 2, {
  phraseResetFunc.value;
  ~base.turnGridWhite(3, 2);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-9);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-16);
}, bank: phrase2Bank, page: page);

// note 9:
~base.setGridFunc(0, 1, {
  phraseResetFunc.value;
  ~base.turnGridWhite(0, 1);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-5);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-8);
}, bank: phrase2Bank, page: page);


// note 10:
~base.setGridFunc(1, 1, {
  phraseResetFunc.value;
  ~base.turnGridWhite(1, 1);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-9);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-16);
}, bank: phrase2Bank, page: page);


// note 11:
~base.setGridFunc(2, 1, {
  phraseResetFunc.value;
  ~base.turnGridWhite(2, 1);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-7);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-14);
}, bank: phrase2Bank, page: page);

// note 12:
~base.setGridFunc(3, 1, {
  phraseResetFunc.value;
  ~base.turnGridWhite(3, 1);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-8);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-17);
}, bank: phrase2Bank, page: page);

// note 13:
~base.setGridFunc(0, 0, {
  phraseResetFunc.value;
  ~base.turnGridWhite(0, 0);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-7);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-16);
}, bank: phrase2Bank, page: page);

// note 14:
~base.setGridFunc(1, 0, {
  phraseResetFunc.value;
  ~base.turnGridWhite(1, 0);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-9);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-16);
}, bank: phrase2Bank, page: page);

// note 15:
~base.setGridFunc(2, 0, {
  phraseResetFunc.value;
  ~base.turnGridWhite(2, 0);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-7);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-14);
}, bank: phrase2Bank, page: page);

// note 16:
~base.setGridFunc(3, 0, {
  phraseResetFunc.value;
  ~base.turnGridWhite(3, 0);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-8);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-12);
}, bank: phrase2Bank, page: page);

// note 17:
~base.setGridFunc(4, 3, {
  phraseResetFunc.value;
  ~base.turnGridWhite(4, 3);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-8);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-12);
}, bank: phrase2Bank, page: page);

// note 18:
~base.setGridFunc(5, 3, {
  phraseResetFunc.value;
  ~base.turnGridWhite(5, 3);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-5);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-9);
}, bank: phrase2Bank, page: page);

// note 19:
~base.setGridFunc(6, 3, {
  phraseResetFunc.value;
  ~base.turnGridWhite(6, 3);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-3);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-7);
}, bank: phrase2Bank, page: page);

// note 20:
~base.setGridFunc(7, 3, {
  phraseResetFunc.value;
  ~base.turnGridWhite(7, 3);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-2);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-6);
}, bank: phrase2Bank, page: page);

// note 21:
~base.setGridFunc(4, 2, {
  phraseResetFunc.value;
  ~base.turnGridWhite(4, 2);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(3);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-4);
}, bank: phrase2Bank, page: page);

// note 22:
~base.setGridFunc(5, 2, {
  phraseResetFunc.value;
  ~base.turnGridWhite(5, 2);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-7);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-12);
}, bank: phrase2Bank, page: page);

// note 23:
~base.setGridFunc(6, 2, {
  phraseResetFunc.value;
  ~base.turnGridWhite(6, 2);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-5);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-10);
}, bank: phrase2Bank, page: page);

// note 24:
~base.setGridFunc(7, 2, {
  phraseResetFunc.value;
  ~base.turnGridWhite(7, 2);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-4);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-9);
}, bank: phrase2Bank, page: page);

// note 25:
~base.setGridFunc(4, 1, {
  phraseResetFunc.value;
  ~base.turnGridWhite(4, 1);
  ~falseSelf.trumpetMelody.shift1.setShiftAmount(-9);
  ~falseSelf.trumpetMelody.shift2.setShiftAmount(-17);
}, bank: phrase2Bank, page: page);






