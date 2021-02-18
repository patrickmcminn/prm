/*
Friday, February 12th 2021
PlagueSong2 Base.scd
prm
*/

var page = 'plague2';

var main = 0;

~base.makePage(page);

6.do({ | i | ~base.setFaderMode(i+1, 'cyanFill', main, page); });

//// volumes:

// volume monitoring:
6.do({ | chan |
	~base.setFaderMonitorFunc(chan+1, {
		var volume = ~p2.mixer.vol(chan).dbfsCC;
		~base.setFaderValue(chan+1, volume, main, page);
	}, main, page);
});

// volume:
6.do({ | chan |
	~base.setFaderFunc(chan+1, { | val |
		~p2.mixer.setVol(chan, val.ccdbfs);
	}, main, page);
});

/////// GRID:
~base.turnAllGridCyan(main, page); 