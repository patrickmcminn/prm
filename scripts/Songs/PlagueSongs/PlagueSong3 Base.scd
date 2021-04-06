/*
Tuesday, March 9th 2021
PlagueSong3 Base.scd
prm
*/

var page = 'plague3';

var main = 0;

~base.makePage(page);

6.do({ | i | ~base.setFaderMode(i+1, 'cyanFill', main, page); });

//// volumes:

// volume monitoring:
6.do({ | chan |
	~base.setFaderMonitorFunc(chan+1, {
		var volume = ~p3.mixer.vol(chan).dbfsCC;
		~base.setFaderValue(chan+1, volume, main, page);
	}, main, page);
});

// volume:
6.do({ | chan |
	~base.setFaderFunc(chan+1, { | val |
		~p3.mixer.setVol(chan, val.ccdbfs);
	}, main, page);
});

/////// GRID:
~base.turnAllGridCyan(main, page); 