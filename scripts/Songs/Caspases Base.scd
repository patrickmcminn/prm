/*
Thursday, August 25th 2022
Caspases Base.scd
prm
*/

var page = 'caspases';

var main = 0;

~base.makePage(page);

~base.turnAllGridBlue(main, page);
~base.turnGridWhite(0, 0, page: page);

~base.setFaderMode(1, 'whiteFill', main, page);
3.do({ | i | ~base.setFaderMode(i+2, 'redFill', main, page); });

~base.setFaderMonitorFunc(1, {
	var kick = ~caspases.kick.isMuted;
	if( kick, { ~base.turnTouchButtonRed(1) }, { ~base.turnTouchButtonGreen(1) });

	~base.setFaderValue(1, ~caspases.mixer.vol(8).dbfsCC, main, page);
	~base.setFaderValue(2, ~caspases.scTpt.mixer.vol.dbfsCC, main, page);
	~base.setFaderValue(3, ~caspases.distTpt.mixer.vol.dbfsCC, main, page);
	~base.setFaderValue(4, ~caspases.shiftTpt.mixer.vol.dbfsCC, main, page);
}, main, page);
~base.setFaderFunc(1, { | val | ~caspases.mixer.setVol(8, val.ccdbfs) }, main, page);
~base.setFaderFunc(2, { | val | ~caspases.scTpt.mixer.setVol(val.ccdbfs) }, main, page);
~base.setFaderFunc(3, { | val | ~caspases.distTpt.mixer.setVol(val.ccdbfs) }, main, page);
~base.setFaderFunc(4, { | val | ~caspases.shiftTpt.mixer.setVol(val.ccdbfs) }, main, page);

~base.setTouchButtonFunc(1, { ~caspases.kick.tglMute; }, bank: main, page: page);