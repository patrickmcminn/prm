/*
Thursday, October 13th 2016
SubtractiveKeyboard.scd
prm

keyboard for playing subtractive synth on the Ohm
*/

var page = 'subtractive';

var mainKnobBank = 0;
var osc1KnobBank = 1;
var osc2KnobBank = 2;
var filterKnobBank = 3;
var vcaKnobBank = 4;
var lfo1KnobBank = 5;
var lfo2KnobBank = 6;
var presetsKnobBank = 7;

var mainGridBank = 0;
var osc1GridBank = 1;
var osc2GridBank = 2;
var filterGridBank = 3;
var vcaGridBank = 4;
var lfo1GridBank = 5;
var lfo2GridBank = 6;
var presetsGridBank = 7;
var keyboardGridBank = 8;
var keyboardSustainGridBank = 9;

var mainSliderBank = 0;
var osc1SliderBank = 1;
var osc2SliderBank = 2;
var filterSliderBank = 3;
var vcaSliderBank = 4;
var lfo1SliderBank = 5;
var lfo2SliderBank = 6;
var presetsSliderBank = 7;

var mainCrossfaderBank = 0;
var osc1CrossfaderBank = 1;
var osc2CrossfaderBank = 2;
var filterCrossfaderBank = 3;
var vcaCrossfaderBank = 4;
var lfo1CrossfaderBank = 5;
var lfo2CrossfaderBank = 6;
var presetsCrossfaderBank = 7;

var mainButtonBank = 0;
var osc1ButtonBank = 1;
var osc2ButtonBank = 2;
var filterButtonBank = 3;
var vcaButtonBank = 4;
var lfo1ButtonBank = 5;
var lfo2ButtonBank = 6;
var presetsButtonBank = 7;
var keyboardButtonBank = 8;

var lowestOctave = 2;
var whiteKeyArray = [0, 2, 4, 5, 7, 9, 11, 12];
var blackKeyArray = [1, 3, 6, 8, 10];
var blackKeyGrid = [1, 2, 4, 5, 6];
var note = 12;
var noteVol = -12;

var makeKeyboard;

var makeOctave;

//////// Left Buttons:

// sustain mode:

/// octaves:
~ohm.setLeftButtonMonitorFunc(2, {
  switch(lowestOctave,
    0, { ~ohm.turnLeftButtonRed(2); ~ohm.turnLeftButtonGreen(3); },
    1, { ~ohm.turnLeftButtonWhite(2); ~ohm.turnLeftButtonGreen(3); },
    2, { ~ohm.turnLeftButtonGreen(2); ~ohm.turnLeftButtonGreen(3); },
    3, { ~ohm.turnLeftButtonGreen(2); ~ohm.turnLeftButtonWhite(3); },
    4, { ~ohm.turnLeftButtonGreen(2); ~ohm.turnLeftButtonCyan(3); },
    5, { ~ohm.turnLeftButtonGreen(2); ~ohm.turnLeftButtonRed(3); },
  );
}, keyboardButtonBank, page);

// octave down:
~ohm.setLeftButtonFunc(2, {
  lowestOctave = lowestOctave - 1;
  makeKeyboard.value;
}, bank: keyboardButtonBank, page: page);

// octave up:
~ohm.setLeftButtonFunc(3, {
  lowestOctave = lowestOctave + 1;
  makeKeyboard.value;
}, bank: keyboardButtonBank, page: page);

//////// GRID:


//// creation functions:
makeKeyboard = {
  makeOctave.value(0, 'red');
  makeOctave.value(1, 'blue');
  makeOctave.value(2, 'cyan');
  makeOctave.value(3, 'magenta');
  makeOctave.value(4, 'green');
  makeOctave.value(5, 'blue');
  makeOctave.value(6, 'yellow');
  makeOctave.value(7, 'white');
  makeOctave.value(8, 'red');
};

makeOctave = { | octave = 0, color = 'red' |
  var currentOctave = octave - lowestOctave;
  var whiteKeyRow = currentOctave * 2;
  var blackKeyRow = whiteKeyRow + 1;
  //currentOctave.postln;
  if( (0 <= currentOctave) && (3 >= currentOctave), {
    // turn initial color:
    8.do({ | i | ~ohm.turnGridColor(i, (7-whiteKeyRow), color); });
    blackKeyGrid.do({ | key | ~ohm.turnGridColor(key, (7-blackKeyRow), color); });
    // white keys:
    whiteKeyArray.do({ | key, i |
      ~ohm.setGridFunc(i, (7-whiteKeyRow), {
        ~prm.subtractive.playNote(((note * (octave + 1)) + key).midicps, noteVol);
        ~ohm.turnGridGreen(i, (7-whiteKeyRow));
      }, 'noteOn', keyboardGridBank, page);
      ~ohm.setGridFunc(i, (7-whiteKeyRow), {
        ~prm.subtractive.releaseNote(((note * (octave + 1)) + key).midicps, noteVol);
        ~ohm.turnGridColor(i, (7-whiteKeyRow), color);
      }, 'noteOff', keyboardGridBank, page);
    });
    // black keys:
    blackKeyArray.do({ | key, i |
      ~ohm.setGridFunc(blackKeyGrid[i], (7-blackKeyRow), {
        ~prm.subtractive.playNote(((note * (octave + 1)) + key).midicps, noteVol);
        ~ohm.turnGridGreen(blackKeyGrid[i], (7-blackKeyRow));
      }, 'noteOn', keyboardGridBank, page);
      ~ohm.setGridFunc(blackKeyGrid[i], (7-blackKeyRow), {
        ~prm.subtractive.releaseNote(((note * (octave + 1)) + key).midicps, noteVol);
        ~ohm.turnGridColor(blackKeyGrid[i], (7-blackKeyRow), color);
      }, 'noteOff', keyboardGridBank, page);
      });
  });
};



//makeOctave.value(3, 'cyan');

/*
// OCTAVE 0 (COLOR IS RED):
makeOctave0 = {
  var octave = 0;
  var currentOctave = octave - lowestOctave;
  var whiteKeyRow = currentOctave * 2;
  var blackKeyRow = whiteKeyRow + 1;
  /*
  white keys:
  0 = 0
  1 = 2
  2 = 4
  3 = 6
  black keys:
  0 = 1
  1 = 3
  2 = 5
  3 = 7
  */
  if( 0 >= currentOctave && 3<= currentOctave, {
    // turn initial color:
    8.do({ | i | ~ohm.turnGridRed(i, (7-(whiteKeyRow))); });
    blackKeyGrid.do({ | key | ~ohm.turnGridRed(i, (7-blackKeyRow)); });
    // white keys:
    whiteKeyArray.do({ | key, i |
      ~ohm.setGridFunc(i, (7-whiteKeyRow), {
        ~prm.subtractive.playNote((note + key).midicps, noteVol);
        ~ohm.turnGridGreen(i, (7-whiteKeyRow));
      }, 'noteOn', keyboardGridBank, page);
      ~ohm.setGridFunc(i, (7-whiteKeyRow), {
        ~prm.subtractive.releaseNote((note + key).midicps, noteVol);
        ~ohm.turnGridRed(i, (7-whiteKeyRow));
      }, 'noteOff', keyboardGridBank, page);
    });
    // black keys:
    blackKeyArray.do({ | key, i |
      ~ohm.setGridFunc(blackKeyGrid[i], (7-blackKeyRow), {
        ~prm.subtractive.playNote((note + key).midicps, noteVol);
        ~ohm.turnGridGreen(blackKeyGrid[i], (7-blackKeyRow));
      }, 'noteOn', keyboardGridBank, page);
      ~ohm.setGridFunc(blackKeyGrid[i], (7-blackKeyRow), {
        ~prm.subtractive.releaseNote((note + key).midicps, noteVol);
        ~ohm.turnGridRed(blackKeyGrid[i], (7-blackKeyRow));
      }, 'noteOff', keyboardGridBank, page);
      });
  });
};


// OCTAVE 1 (BLUE):
makeOctave1 = {
  var octave = 1;
  var currentOctave = octave - lowestOctave;
  var whiteKeyRow = currentOctave * 2;
  var blackKeyRow = whiteKeyRow + 1;
  if( 0 >= currentOctave && 3<= currentOctave, {
    // turn initial color:
    8.do({ | i | ~ohm.turnGridBlue(i, (7-(whiteKeyRow))); });
    blackKeyGrid.do({ | key | ~ohm.turnGridBlue(i, (7-blackKeyRow)); });
    // white keys:
    whiteKeyArray.do({ | key, i |
      ~ohm.setGridFunc(i, (7-whiteKeyRow), {
        ~prm.subtractive.playNote((note + key).midicps, noteVol);
        ~ohm.turnGridGreen(i, (7-whiteKeyRow));
      }, 'noteOn', keyboardGridBank, page);
      ~ohm.setGridFunc(i, (7-whiteKeyRow), {
        ~prm.subtractive.releaseNote((note + key).midicps, noteVol);
        ~ohm.turnGridRed(i, (7-whiteKeyRow));
      }, 'noteOff', keyboardGridBank, page);
    });
    // black keys:
    blackKeyArray.do({ | key, i |
      ~ohm.setGridFunc(blackKeyGrid[i], (7-blackKeyRow), {
        ~prm.subtractive.playNote((note + key).midicps, noteVol);
        ~ohm.turnGridGreen(blackKeyGrid[i], (7-blackKeyRow));
      }, 'noteOn', keyboardGridBank, page);
      ~ohm.setGridFunc(blackKeyGrid[i], (7-blackKeyRow), {
        ~prm.subtractive.releaseNote((note + key).midicps, noteVol);
        ~ohm.turnGridRed(blackKeyGrid[i], (7-blackKeyRow));
      }, 'noteOff', keyboardGridBank, page);
      });
  });
};

// OCTAVE 2 (CYAN):
makeOctave2 = { };

// OCTAVE 3 (MAGENTA):
makeOctave3 = { };

// OCTAVE 4 (GREEN):
makeOctave4 = { };

// OCTAVE 5 (BLUE):
makeOctave5 = { };

// OCTAVE 6 (YELLOW):
makeOctave6 = { };

// OCTAVE 7 (WHITE):
makeOctave7 = { };

// OCTAVE 8 (RED):
makeOctave8 = { };

/*
9.do({ | i |
  // Octave 0:
  if( 0 >= (0 - lowestOctave), {
    8.do({ | i |
      ~ohm.setGridFunc(i, (7 - lowestOctave), {
        ~prm.subtractive.playNote(note.midicps, noteVol)
        ~ohm.turnGridGreen(i, (7-lowestOctave));
      }, 'noteOn', keyboardGridBank, page);
    });
*/
*/

        