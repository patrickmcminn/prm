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

var octave = 0;
var blackKeyArray = [1, 2, 4, 5, 6];

//////// Left Buttons:

// sustain mode:

/// octaves:
~ohm.setLeftButtonMonitorFunc(2, {
  switch(octave
    { -5 } { ~ohm.turnLeftButtonRed(2); ~ohm.turnLeftButtonGreen(3) }
    { -4 } { ~ohm.turnLeftButtonWhite(2); ~ohm.turnLeftButtonGreen(3); }
    { -3 } { ~ohm.turnLeftButtonYellow(2); ~ohm.turnLeftButtonGreen(3); }
    { -2 } { ~ohm.turnLeftButtonCyan(2); ~ohm.turnLeftButtonGreen(3); }
    { -1 } { ~ohm.turnLeftButtonBlue(2); ~ohm.turnLeftButtonGreen(3); }
    { 0 } { ~ohm.turnLeftButtonGreen(2); ~ohm.turnLeftButtonGreen(3); }
    { 1 } { ~ohm.turnLeftButtonGreen(2); ~ohm.turnLeftButtonBlue(3); }
    { 2 } { ~ohm.turnLeftButtonGreen(2); ~ohm.turnLeftButtonCyan(3); }
    { 3 } { ~ohm.turnLeftButtonGreen(2); ~ohm.turnLeftButtonYellow(3); }
    { 4 } { ~ohm.turnLeftButtonGreen(2); ~ohm.turnLeftButtonYellow(3); }
    { 5 } { ~ohm.turnLeftButtonGreen(2); ~ohm.turnLeftButtonRed(3); }
  );
}, keyboardButtonBank, page);

// octave down:
~ohm.setLeftButtonFunc(2, { octave = octave - 1; }, bank: keyboardButtonBank, page: page);

// octave up:
~ohm.setLeftButtonFunc(3, { octave = octave + 1; }, bank: keyboardButtonBank, page: page);

//////// GRID:





