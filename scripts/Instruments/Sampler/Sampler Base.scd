/*
Sampler Base.scd

prm
*/

var page = 'sampler';

var activeBank;

var main = 0;
var record = 1;

activeBank = main;


~base.makePage(page);

~base.addGridBanks(1, page);

/////// Control Buttons:
2.do({ | i | ~base.turnControlButtonWhite(i+1, 'left', page: page); });
~base.setControlButtonMonitorFunc(1, {
	if( activeBank == main,
		{ ~base.turnControlButtonGreen(1, 'right', main, page); },
		{ ~base.turnControlButtonWhite(1, 'right', main, page) });
	if( activeBank == record,
		{ ~base.turnControlButtonGreen(2, 'right', main, page); },
		{ ~base.turnControlButtonWhite(2, 'right', main, page); });
}, main, page);
~base.setControlButtonFunc(1, {
	activeBank = main;
	~base.setActiveGridBank(activeBank);
}, bank: main, page: page);
~base.setControlButtonFunc(2, {
	activeBank = record;
	~base.setActiveGridBank(activeBank);
}, bank: main, page: page);

////////////////////////////
///// Main Grid Bank: /////
//////////////////////////

16.do({ | i | ~base.turnGridMagenta((i%4), 3-((i/4).asInt), main, page); });

16.do({ | slot |
	~base.setGridFunc((slot%4), 3-((slot/4).asInt), { | vel |
		~prm.sampler.playSample(slot, vel.ccdbfs(-33, 3));
		~base.turnGridGreen((slot%4), 3-((slot/4).asInt));
	}, 'noteOn', main, page);
	~base.setGridFunc((slot%4), 3-((slot/4).asInt), { | vel |
		~prm.sampler.releaseSample(slot);
		~base.turnGridMagenta((slot%4), 3-((slot/4).asInt));
	}, 'noteOff', main, page);
});

//////// record bank:

16.do({ | i | ~base.turnGridMagenta((i%4), 3-((i/4).asInt), record, page); });
16.do({ | slot |
	~base.setGridFunc((slot%4), 3-((slot/4).asInt), { | vel |
		~prm.sampler.playSample(slot, vel.ccdbfs(-33, 3));
		~base.turnGridGreen((slot%4), 3-((slot/4).asInt));
	}, 'noteOn', record, page);
	~base.setGridFunc((slot%4), 3-((slot/4).asInt), { | vel |
		~prm.sampler.releaseSample(slot);
		~base.turnGridMagenta((slot%4), 3-((slot/4).asInt));
	}, 'noteOff', record, page);
});

16.do({ | i | ~base.turnGridYellow((i%4)+4, 3-((i/4).asInt), record, page); });
16.do({ | slot |
	~base.setGridFunc((slot%4)+4, 3-((slot/4).asInt), { | vel |
		if( ~prm.sampler.isRecording == false,
			{
				~prm.sampler.recordSampleStereo;
				~base.turnGridRed((slot%4)+4, 3-((slot/4).asInt));
			},
			{
				~prm.sampler.stopRecordingLoadSlot(slot);
				~base.turnGridYellow((slot%4)+4, 3-((slot/4).asInt));
		});
	}, 'noteOn', record, page);
});