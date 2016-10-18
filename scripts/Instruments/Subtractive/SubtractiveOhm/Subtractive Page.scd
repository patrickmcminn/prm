/*
Monday, September 12th 2016
Subtractive Page - Main.scd
next attempt at making a control surface page for the Subtractive synth
prm
*/

// 'main' page -- for page creation and bank changing:
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

~ohm.makePage(page);

~ohm.addGridBanks(9, page);
~ohm.addLeftButtonsBanks(8, page);
~ohm.addLeftKnobsBanks(7, page);
~ohm.addLeftSlidersBanks(7, page);
~ohm.addCrossfaderButtonsBanks(7, page);
~ohm.addCrossfaderBanks(7, page);


(
//////// bank files load:
"/banks/SubtractiveMain.scd".loadRelative;
"/banks/SubtractiveOsc1.scd".loadRelative;
"/banks/SubtractiveOsc2.scd".loadRelative;
"/banks/SubtractiveFilter.scd".loadRelative;
"/banks/SubtractiveVca.scd".loadRelative;
"/banks/SubtractiveLFO1.scd".loadRelative;
"/banks/SubtractiveLFO2.scd".loadRelative;
"/banks/SubtractivePresets.scd".loadRelative;
);
