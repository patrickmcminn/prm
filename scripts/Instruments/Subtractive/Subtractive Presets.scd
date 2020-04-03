/*
Wednesday, April 1st 2020
Subtractive Presets.scd
prm
*/


var page = 'subtractive';

var mainGridBank = 0;
var keyboardBank = 6;
var keyboardHoldBank = 7;

var mainMixerBank = 0;
var mainDeviceEncodersBank = 0;
var mainMixerEncodersBank = 0;
var mainSceneLaunchBank = 0;

var mainBank = 0;
var osc1Bank = 1;
var osc2Bank = 2;
var filterBank = 3;
var envBank = 4;
var lfoBank = 5;
var keyboard = 6;
var presetLoad = 7;
var presetSave = 8;

var colorReset, loader, path;
var populateWriteFuncs, makeGUI, addPreset;


path = "~/Library/Application Support/SuperCollider/Extensions/prm/scripts/Instruments/Subtractive/SubtractivePresetLoader.scd".standardizePath;

/*
line that is written:
- column
- row
- key
- inactive color
*/

colorReset = {
	var array = SemiColonFileReader.read(path);
	array.size.do({ | line |
		var column = array[line][0];
		var row = array[line][1];
		var color = (array[line][3]).asSymbol;
		("~apc.turnGrid" ++ color ++ "(" ++ column ++ "," ++ row ++ ", bank: 7, page: 'subtractive');").interpret;
		("~apc.turnGrid" ++ color ++ "(" ++ column ++ "," ++ row ++ ", bank: 8, page: 'subtractive');").interpret;
	});
};

loader = {
	var array = SemiColonFileReader.read(path);
	array.size.do({ | line |
		var column = array[line][0].asInteger;
		var row = array[line][1].asInteger;
		var key = array[line][2].asSymbol;
		var color = (array[line][3]).asSymbol;
		~apc.setGridFunc(column, row, {
			~prm.subtractive.readPreset(key);
			colorReset.value;
			~apc.turnGridGreen(column, row, bank: presetLoad, page: page);
		}, bank: presetLoad,  page: page);
	});
};

populateWriteFuncs = {
	5.do({ | row |
		8.do({ | column |
			~apc.setGridFunc(column, row, {
				//row.postln;  column.postln;
				// GUI bullshit goes here:
				{ makeGUI.value(column, row); }.defer;
			}, bank: presetSave, page: page);
		});
	});
};

makeGUI = { | column, row |
	var window, colorPicker, text, colorText, textField, goButton;
	column.postln;
	row.postln;
	window = Window.new("add preset key at ("++column++","++row++")", Rect((100.rand+400),(100.rand+400),225,100));
	text = StaticText.new(window).string_("preset name:");
	textField = TextField.new(window);
	colorText = StaticText.new(window).string_("controller color:");
	colorPicker = PopUpMenu.new(window);
	colorPicker.items = ["Red", "Yellow", "YellowGreen", "Cyan", "LightBlue", "Blue", "Purple", "Magenta", "Pink", "White"];
	goButton = Button.new(window).action_({
		[column, row, textField.string, colorPicker.item].postln;
		addPreset.value(column, row, textField.string, colorPicker.item);
		window.close;
	});
	goButton.string = "Write Preset";
	window.layout = VLayout(HLayout(VLayout(text, textField), VLayout(colorText, colorPicker)), goButton);
	window.front;
};

addPreset = { | column, row, key, color |
	var file = File(path, "a");
	// note that color needs to be capitalized to work!
	if( ~prm.subtractive.masterPresetDict.includesKey(key.asSymbol),
		{ "preset already exists".postln; },
		{
			~prm.subtractive.writePreset(key.asSymbol);
			file.write("\n"++column++";"++row++";"++key++";"++color++";");
			file.close;
			s.sync;
			loader.value;
			s.sync;
			colorReset.value;
			~apc.turnGridGreen(column, row, 2, presetLoad, page);
			("~apc.turnGrid" ++ color ++ "(" ++ column ++ "," ++ row ++ ", bank: 8, page: 'subtractive');").interpret;
		}.fork;
	);


};


colorReset.value;
loader.value;
populateWriteFuncs.value;