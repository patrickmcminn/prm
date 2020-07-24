/*
Monday, April 30th 2018
Darkness Base.scd
prm

a shallow eclipsing darkness

updated 4/22/2020
Pyramid

Pandemic times
*/


var page = 'darkness';
var tempo = 50;

var scale = [1, 3, 4, 6, 8, 9, 11, 13];

~base.makePage(page);

//////////////////
//// Faders:  //
////////////////

/////////////////////////
///////// GRID /////////
///////////////////////

~base.turnAllGridWhite(page: page);

//////// TRUMPET LFO ////////
~base.turnGridBlue(1, 3, page: page);
~base.setGridFunc(1, 3, {
  ~darkness.trumpet.lowPassFilter.setCutoffLFOBottomRatio(0.1);
}, 'noteOn', page: page);
~base.setGridFunc(1, 3, {
  ~darkness.trumpet.lowPassFilter.setCutoffLFOBottomRatio(1);
}, 'noteOff', page: page);


//////////// keyboard:

8.do({ | i | ~base.turnGridMagenta(i, 0, page: page); ~base.turnGridMagenta(i, 1, page: page); });
8.do({ | note |
  var octaveOffset = 24;
  ~base.setGridFunc(note, 0, {
    ~darkness.bass.moog.playNote((scale[note] + octaveOffset).midicps);
    ~base.turnGridGreen(note, 0);
  }, 'noteOn', page: page);
  ~base.setGridFunc(note, 0, {
    ~darkness.bass.moog.releaseNote((scale[note] + octaveOffset).midicps);
    ~base.turnGridMagenta(note, 0);
  }, 'noteOff', page: page);
});
7.do({ | note |
  var octaveOffset = 36;
  ~base.setGridFunc(note+1, 1, {
    ~darkness.bass.moog.playNote((scale[note+1] + octaveOffset).midicps);
    ~base.turnGridGreen(note+1, 1);
  }, 'noteOn', page: page);
  ~base.setGridFunc(note+1, 1, {
    ~darkness.bass.moog.releaseNote((scale[note+1] + octaveOffset).midicps);
    ~base.turnGridMagenta(note+1, 1);
  }, 'noteOff', page: page);
});

