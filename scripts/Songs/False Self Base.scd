/*
Sunday, April 16th 2017
FalseSelfBase.scd
prm
*/

var page = 'falseSelf';

var mainGridBank = 0;

~base.makePage(page);

//////// main grid bank:

3.do({ | i | ~base.turnGridGreen(i, 2, mainGridBank, page); });
3.do({ | i | ~base.turnGridGreen(i, 3, mainGridBank, page); });

// G#:
~base.setGridFunc(0, 2, { | val |
  var vol = val.linlin(0, 127, 0, 1).ampdb;
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
  ~falseSelf.drums.playDrum2;
  ~falseSelf.drums.playDrum3;
}, bank: mainGridBank, page: page);

