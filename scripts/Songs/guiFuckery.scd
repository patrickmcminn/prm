x =  IM_ChannelStrip.new(0);

~window.layout = HLayout(VLayout(Knob().mode_(\vert), StaticText().string_("buts")));
~window.front;

~window = Window.new;

x.setVol(0, 0);

x = IM_Mixer.new(12);
x.isLoaded
x.makeGUI
~muteState = Button("Beirut", 6);
~muteState.states = [["chanName", Color.black, Color.green], ["chanName", Color.black, Color.red]];
~sendArray = Array.fill(4, { | i | HLayout(Knob().mode_(\vert), StaticText().string_("s"++(i+1)));});

x.vol(0);
~pan = Knob();

x.setName(0, 'dan');
x.setName(1, 'alex');
x.setName(2, 'penisTim');
x.setName(3, "harmReduction");

~pan.mode = \vert;

y = IM_Mixer.new(12);

(
var chanName = "test";
var font = Font("Beirut", 12);

~guis = IdentityDictionary.new;
~guis[\sendArray] = Array.fill(4, { | i | HLayout(Knob().mode_(\vert).maxWidth_(32), StaticText().string_("s"++(i+1)).font_(font));});
~guis[\sends] = Button().maxWidth_(50);
~guis[\vol] = Slider();
~guis[\volFB] = TextField();
~guis[\pan] = Knob().mode_(\vert).centered_("true");
~guis[\preVol] = TextField();
~guis[\preVolLabel] = StaticText().string_("pre vol").align_(\center);
~guis[\active] = Button().maxWidth_(50).font_(font);


~guis[\sends].states =[["pre", Color.black, Color.hsv(0.15, 0.5, 1, 1)], ["post", Color.black, Color.hsv(0.6, 0.5, 1, 1)]];
~guis[\sends].font = font;
//~guis[\sends].resize = 5;
~guis[\sends].bounds = Point(10, 10);
~guis[\vol].action = {
	this.setVol();
	~guis[\volFB].value = ~guis[\vol].value.linlin(0, 1, 0, 127).ccdbfs.trunc(0.01) };
~guis[\volFB].action = { ~guis[\vol].valueAction_(~guis[\volFB].value.asFloat.dbfsCC.linlin(0, 127, 0, 1)) };
~guis[\active].states = [[chanName, Color.black, Color.green], [chanName, Color.black, Color.red]];

~window = Window.new("channelStrip", Rect(128, 64, 60, 700));
~window.layout = VLayout(
	~guis[\sendArray][0], ~guis[\sendArray][1], ~guis[\sendArray][2], ~guis[\sendArray][3],
	~guis[\sends],
	~guis[\vol], ~guis[\volFB],
	~guis[\pan],
	HLayout(~guis[\preVol], ~guis[\preVolLabel]),
	~guis[\active]

);

~window.front
)

~guis[\sends].menu
Server.killAll;
x = IM_ChannelStrip.new(0);

x.isLoaded
x.isMuted

x.preOrPost
x.makeStripGUI;

w = Window.new("channelStrip", Rect(128, 64, 60, 700));
w = Window.new;
w.layout = x.gui
w.front

x.guiDict[\preVol].value.asFloat;

x.vol;

x.name = 'butts';


x.panBal
x.preVol;
x.setPreVol(-12);

w = Window.new;
w.layout =VLayout(
	HLayout(x.guiDict[\send0], x.guiDict[\send0Label]),
	HLayout(x.guiDict[\send1], x.guiDict[\send1Label]),
	HLayout(x.guiDict[\send2], x.guiDict[\send2Label]),
	HLayout(x.guiDict[\send3], x.guiDict[\send3Label])
			//guiDict[\sends]
);
w.front

w = Window.new;
w.layout = VLayout(x.guiDict[\layout[
w.front;

w = Window.new;
w.layout = VLayout(HLayout(x.guiDict[\send0], x.guiDict[\send0Label]));
w.front

x.guiDict[\send0];
w = Window.new;
w.layout = VLayout(x.guiDict[\send0], x.guiDict[\send1], x.guiDict[\send2], x.guiDict[\send3]);

w.layout = x.guiDict[\sendLayout];
w.front

x.send0Vol

x.preOrPost

x = Button();

x.states

View

x.minWidth
x.resize

	a = { Button(w) } ! 10;
	a;

w = Window.new;
s = Slider(w).action_({ | val, v | v.postln;});
x = Button(w).action_({ | val | val.value.postln; });
x.states = [["yes"], ["no"], ["maybe"]];
x.value;
x.value = 1

	x = Array.fill(12, { Button() });
	z = HLayout(*x);
	w = Window.new
	w.layout = z;
	w.front

EZSlider

w.front

AppClock


Slider

-9.dbamp
~window.size
~window.bounds


~guis[\sendArray][0]

HLayout

0.5.linlin(0, 1, 0, 127).ccdbfs

y.vol(0);



GUI

ControlSpec

Spec



/*
Send volumes
sends pre/post
fader
(fader vol)
pan
preVol
track active
*/




x = IM_Mixer.new(12, ~out, ~reverb, ~granulator, ~delay);

x.setVol(2, -12);

Font.availableFonts



	Caspases