/*
Thursday, July 9th 2015
Cortez Ohm.scd
prm
*/

var page = 'cortez';

~ohm.makePage(page);

//~ohm.turnAllGridCyan(page);

~ohm.setControlButtonFunc(2, 0, { ~base.setPage(page) }, page: page);

7.do({ | i | ~ohm.turnGridPurple(i, 0, page: page); });

// chord 1:
~ohm.setGridFunc(0, 0, {
  ~cortez.chordsSynth.releaseNote(52.midicps);
  ~cortez.chordsSynth.releaseNote(52.midicps);
  ~cortez.chordsSynth.playNote(52.midicps, -6);
  ~cortez.chordsSynth.playNote(59.midicps, -6);
  ~ohm.turnGridGreen(0, 0);
}, page: page);
~ohm.setGridFunc(0, 0, {
  ~cortez.chordsSynth.releaseNote(52.midicps);
  ~cortez.chordsSynth.releaseNote(52.midicps);
  ~ohm.turnGridPurple(0, 0);
}, 'noteOff', page: page);


// chord 2:
~ohm.setGridFunc(1, 0, {
  ~cortez.chordsSynth.releaseNote(52.midicps);
  ~cortez.chordsSynth.releaseNote(55.midicps);
  ~cortez.chordsSynth.playNote(52.midicps, -6);
  ~cortez.chordsSynth.playNote(55.midicps, -6);
  ~ohm.turnGridGreen(1, 0);
}, page: page);
~ohm.setGridFunc(1, 0, {
  ~cortez.chordsSynth.releaseNote(52.midicps);
  ~cortez.chordsSynth.releaseNote(55.midicps);
  ~ohm.turnGridPurple(1, 0);
}, 'noteOff', page: page);

// chord 3:
~ohm.setGridFunc(2, 0, {
  ~cortez.chordsSynth.releaseNote(50.midicps);
  ~cortez.chordsSynth.releaseNote(57.midicps);
  ~cortez.chordsSynth.playNote(50.midicps, -6);
  ~cortez.chordSynth.playNote(57.midicps, -6);
  ~ohm.turnGridGreen(2, 0);
}, page: page);
~ohm.setGridFunc(2, 0, {
  ~cortez.chordsSynth.releaseNote(50.midicps);
  ~cortez.chordsSynth.releaseNote(57.midicps);
  ~ohm.turnGridPurple(2, 0);
}, 'noteOff', page: page);

// chord 4:
~ohm.setGridFunc(3, 0, {
  ~cortez.chordsSynth.releaseNote(50.midicps);
  ~cortez.chordsSynth.releaseNote(54.midicps);
  ~cortez.chordsSynth.playNote(50.midicps, -6);
  ~cortez.chordsSynth.playNote(54.midicps, -6);
  ~ohm.turnGridGreen(3, 0);
}, page: page);
~ohm.setGridFunc(3, 0, {
  ~cortez.chordsSynth.releaseNote(50.midicps);
  ~cortez.chordsSynth.releaseNote(54.midicps);
  ~ohm.turnGridPurple(3, 0);
}, 'noteOff', page: page);

// chord 5:
~ohm.setGridFunc(4, 0, {
  ~cortez.chordsSynth.releaseNote(52.midicps);
  ~cortez.chordsSynth.releaseNote(57.midicps);
  ~cortez.chordsSynth.playNote(52.midicps, -6);
  ~cortez.chordsSynth.playNote(57.midicps, -6);
  ~ohm.turnGridGreen(4, 0);
}, page: page);
~ohm.setGridFunc(4, 0, {
  ~cortez.chordsSynth.releaseNote(52.midicps);
  ~cortez.chordsSynth.releaseNote(57.midicps);
  ~ohm.turnGridPurple(4, 0);
}, 'noteOff', page: page);

// chord 6:
~ohm.setGridFunc(5, 0, {
  ~cortez.chordsSynth.releaseNote(52.midicps);
  ~cortez.chordsSynth.releaseNote(45.midicps);
  ~cortez.chordsSynth.playNote(52.midicps, -6);
  ~cortez.chordsSynth.playNote(45.midicps, -6);
  ~ohm.turnGridGreen(5, 0);
}, page: page);
~ohm.setGridFunc(5, 0, {
  ~cortez.chordsSynth.releaseNote(52.midicps);
  ~cortez.chordsSynth.releaseNote(45.midicps);
  ~ohm.turnGridPurple(5, 0);
}, 'noteOff', page: page);

// chord 7:
~ohm.setGridFunc(6, 0, {
  ~cortez.chordsSynth.releaseNote(54.midicps);
  ~cortez.chordsSynth.releaseNote(47.midicps);
  ~cortez.chordsSynth.playNote(54.midicips, -6);
  ~cortez.chordsSynth.playNote(47.midicps, -6);
  ~ohm.turnGridGreen(6, 0);
}, page: page);
~ohm.setGridFunc(6, 0, {
  ~cortez.chordsSynth.releaseNote(54.midicps);
  ~cortez.chordsSynth.releaseNote(47.midicps);
  ~ohm.turnGridPurple(6, 0);
}, 'noteOff', page: page);

