x = CV_Gate.new(32);

x.makeTrig(1, 0.1);

x.makeGate(-0.3);
x.releaseGate;


x.gateIsHigh;
x.free;

VSTPlugin

VSTPlugin.search
VSTPlugin.stopSearch;


VSTPlugin.plugins['ValhallaRoom_x64'].print

VSTPlugin.pluginList;

VSTPlugin.print;

Server.killAll;

SynthDef(\insert, { arg bus;
    ReplaceOut.ar(bus, VSTPlugin.ar(SoundIn.ar(0, 2), 2));
}).add;

~fx = VSTPluginController(Synth(\insert, [\bus, 0])).open("ValhallaRoom_x64");
~fx.free;
~fx.gui

~fx.set(2, 2);

~fx.info.printPresets;

s.meter

SynthDef(\plugin, { | inBus = 0, outBus = 0 |
	var input, vst, sig;
	input = SoundIn.ar(0);
	vst = VSTPlugin.ar(input, 2);
	sig = Out.ar(outBus, vst);
}).add;

~fx = VSTPluginController(Synth(\plugin, [\outBus, 0])).open("Decapitator");
~fx.free;

~fx.loaded;