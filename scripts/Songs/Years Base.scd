/*
Monday, April 11th 2022
Years Base.scd
prm
*/

var page = 'years';

var main = 0;

~base.makePage(page);

////// block chords:
~base.setFaderMode(1, 'blueFill', main, page);
~base.setFaderFunc(1, { | val |
	~years.mixer.setVol(8, val.ccdbfs);
}, bank: main, page: page);
///// modIn:
~base.setFaderMode(2, 'cyanFill', main, page);
~base.setFaderFunc(2, { | val |
	~years.mixer.setVol(9, val.ccdbfs);
}, bank: main, page: page);
///// noise In:
~base.setFaderMode(3, 'yellowFill', main, page);
~base.setFaderFunc(3, { | val |
	~years.mixer.setVol(10, val.ccdbfs);
}, bank: main, page: page);

///// delay send:
~base.setFaderMode(8, 'whiteFill', main, page);
~base.setFaderFunc(8, { | val |
	~yearsDelay.mixer.setVol(val.ccdbfs);
}, bank: main, page: page);


//// monitor funcs:
~base.setFaderMonitorFunc(1, {
	var blockChords = ~years.mixer.vol(8).dbfsCC;
	var modIn = ~years.mixer.vol(9).dbfsCC;
	var noiseIn = ~years.mixer.vol(10).dbfsCC;
	var delay = ~yearsDelay.mixer.vol.dbfsCC;

	var blockChordsMute = ~years.mixer.isMuted(8);
	var modMute = ~years.mixer.isMuted(9);
	var noiseMute = ~years.mixer.isMuted(10);


	~base.setFaderValue(1, blockChords);
	~base.setFaderValue(2, modIn);
	~base.setFaderValue(3, noiseIn);
	~base.setFaderValue(8, delay);

	if( blockChordsMute != true, { ~base.turnTouchButtonGreen(1) }, { ~base.turnTouchButtonRed(1) });
	if( modMute !=true, { ~base.turnTouchButtonGreen(2) }, { ~base.turnTouchButtonRed(2) });
	if( noiseMute !=true, { ~base.turnTouchButtonGreen(3) }, { ~base.turnTouchButtonRed(3) });
}, bank: main, page: page);

~base.setTouchButtonFunc(1, { ~years.mixer.tglMute(8); }, 'noteOn', main, page);
~base.setTouchButtonFunc(2, { ~years.mixer.tglMute(9); }, 'noteOn', main, page);
~base.setTouchButtonFunc(3, { ~years.mixer.tglMute(10); }, 'noteOn', main, page);

//////////////////
//// GRID: //////
////////////////

4.do({ | i | ~base.turnGridMagenta(0, i, main, page); });
4.do({ | i | ~base.turnGridMagenta(1, i, main, page); });
4.do({ | i | ~base.turnGridMagenta(2, i, main, page); });
4.do({ | i | ~base.turnGridMagenta(3, i, main, page); });

4.do({ | i | ~base.setGridFunc(0, i, { | vel |
	~years.drums.drums.playSample(i, vel.ccdbfs);
	~base.turnGridWhite(0, i);
}, 'noteOn', main, page);
});
4.do({ | i | ~base.setGridFunc(1, i, { | vel |
	~years.drums.drums.playSample(i+4, vel.ccdbfs);
	~base.turnGridWhite(1, i);
}, 'noteOn', main, page);
});
4.do({ | i | ~base.setGridFunc(2, i, { | vel |
	~years.drums.drums.playSample(i+8, vel.ccdbfs);
	~base.turnGridWhite(2, i);
}, 'noteOn', main, page);
});
4.do({ | i | ~base.setGridFunc(3, i, { | vel |
	~years.drums.drums.playSample(i+12, vel.ccdbfs);
	~base.turnGridWhite(3, i);
}, 'noteOn', main, page);
});

4.do({ | i | ~base.setGridFunc(0, i, { ~base.turnGridMagenta(0, i, main, page); }, 'noteOff', main, page); });
4.do({ | i | ~base.setGridFunc(1, i, { ~base.turnGridMagenta(1, i, main, page); }, 'noteOff', main, page); });
4.do({ | i | ~base.setGridFunc(2, i, { ~base.turnGridMagenta(2, i, main, page); }, 'noteOff', main, page); });
4.do({ | i | ~base.setGridFunc(3, i, { ~base.turnGridMagenta(3, i, main, page); }, 'noteOff', main, page); });
