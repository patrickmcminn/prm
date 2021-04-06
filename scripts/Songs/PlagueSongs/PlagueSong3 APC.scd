/*
Tuesday, March 9th 2021
PlagueSong3 APC.scd
prm
*/

var page = 'plague3';

var verb = 0;
var gran = 1;
var mod = 2;
var delay = 3;

var main = 0;

var layerReset, delayReset, guitarReset;

~apc.makePage(page);
~apc.addMixerEncodersBanks(3, page);

~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);

//////// mixer:

~apc.mapMixer(~p3.mixer, 6, 0, 0, verb, gran, mod, delay, main, page);
~apc.mapSendBanks(\main, verb, gran, mod, delay, main, page);

/////// record enables:
~apc.setDeviceEncodersMonitorFunc(\record, {
	var tpt, horn;
	tpt = ~p3.trumpet.isMuted;
	horn = ~p3.hornInput.isMuted;
	if( tpt == true, { ~apc.turnRecordEnableButtonOff(1) }, { ~apc.turnRecordEnableButtonOn(1) });
	if( horn == true, { ~apc.turnRecordEnableButtonOff(2) }, { ~apc.turnRecordEnableButtonOn(2) });
}, bank: main, page: page);

// trumpet:
~apc.setRecordEnableButtonFunc(1, { ~p3.trumpet.tglMute }, bank: main, page: page);
// horn:
~apc.setRecordEnableButtonFunc(2, { ~p3.hornInput.tglMute }, bank: main, page: page);

/////////////////////////
///// KNOBS (knobs) ////
///////////////////////

~apc.setDeviceEncodersMonitorFunc(\cv, {
	var layer, mimMix, guitarMix;
	layer = ~p3.arbLayer.value.linlin(0, 0.25, 0, 127);
	mimMix = ~p3.mimeophonMix.value.linlin(0, 1, 0, 127);
	guitarMix = ~p3.guitarMix.value.linlin(0, 1, 0, 127);
	~apc.setDeviceEncoderValue(1, layer);
	~apc.setDeviceEncoderValue(2, mimMix);
	~apc.setDeviceEncoderValue(3, guitarMix);
}, main, page);

~apc.setDeviceEncoderFunc(1, { | val |
	var layer = val.linlin(0, 127, 0, 0.25);
	~p3.arbLayer.setValue(layer);
}, main, page);

~apc.setDeviceEncoderFunc(2, { | val |
	var mix = val.linlin(0, 127, 0, 1);
	~p3.mimeophonMix.setValue(mix);
}, main, page);

~apc.setDeviceEncoderFunc(3, { | val |
	var mix = val.linlin(0, 127, 0, 1);
	~p3.guitarMix.setValue(mix);
}, main, page);


///////////////////////
////// GRID: /////////
/////////////////////

~apc.turnAllGridCyan(1, main, page);

///// arbhar record signal:
~apc.setGridMonitorFunc(\arbRecord, {
	if( ~p3.arbRecord.gateIsHigh, { ~apc.turnGridGreen(0, 0) }, { ~apc.turnGridRed(0, 0); });
}, bank: main, page: page);
~apc.setGridFunc(0, 0, { ~p3.arbRecord.toggleGate; }, bank: main, page: page);

layerReset = { 6.do({ | i | ~apc.turnGridPurple(i+1, 0, 1, main, page); }); };
layerReset.value;


///// arbhar layer:

~apc.setGridMonitorFunc(\arbLayer, {
	var layer = ~p3.arbLayer.value;
	switch(layer,
		0, { layerReset.value; ~apc.turnGridGreen(1, 0); },
		0.05, { layerReset.value; ~apc.turnGridGreen(2, 0); },
		0.075, { layerReset.value; ~apc.turnGridGreen(3, 0); },
		0.11, { layerReset.value; ~apc.turnGridGreen(4, 0); },
		0.15, { layerReset.value; ~apc.turnGridGreen(5, 0); },
		0.19, { layerReset.value; ~apc.turnGridGreen(6, 0); });
}, main, page);

~apc.setGridFunc(1, 0, { ~p3.arbLayer.setValue(0); }, bank: main, page: page);
~apc.setGridFunc(2, 0, { ~p3.arbLayer.setValue(0.05); }, bank: main, page: page);
~apc.setGridFunc(3, 0, { ~p3.arbLayer.setValue(0.075); }, bank: main, page: page);
~apc.setGridFunc(4, 0, { ~p3.arbLayer.setValue(0.11); }, bank: main, page: page);
~apc.setGridFunc(5, 0, { ~p3.arbLayer.setValue(0.15); }, bank: main, page: page);
~apc.setGridFunc(6, 0, { ~p3.arbLayer.setValue(0.19); }, bank: main, page: page);


delayReset = { 5.do({ | i | ~apc.turnGridWhite(i, 4, 1, main, page); }); };
delayReset.value;

///// delay mix slews:
~apc.setGridMonitorFunc(\delSlew, {
	var mix = ~p3.mimeophonMix.value;
	switch(mix,
		0, { delayReset.value; ~apc.turnGridGreen(0, 4); },
		0.1, { delayReset.value; ~apc.turnGridGreen(1, 4); },
		0.25, { delayReset.value; ~apc.turnGridGreen(2, 4); },
		0.5, { delayReset.value; ~apc.turnGridGreen(3, 4); },
		1, { delayReset.value; ~apc.turnGridGreen(4, 4); });
}, main, page);

~apc.setGridFunc(0, 4, { ~p3.mimeophonMix.setValue(0, 15); }, bank: main, page: page);
~apc.setGridFunc(1, 4, { ~p3.mimeophonMix.setValue(0.1, 10); }, bank: main, page: page);
~apc.setGridFunc(2, 4, { ~p3.mimeophonMix.setValue(0.25, 5); }, bank: main, page: page);
~apc.setGridFunc(3, 4, { ~p3.mimeophonMix.setValue(0.5, 5); }, bank: main, page: page);
~apc.setGridFunc(4, 4, { ~p3.mimeophonMix.setValue(1, 5); }, bank: main, page: page);

///// guitar mix slews:

guitarReset = { 5.do({ | i | ~apc.turnGridPink(i, 3, 1, main, page); }); };
guitarReset.value;
~apc.setGridMonitorFunc(\guitSlew, {
	var mix = ~p3.guitarMix.value;
	switch(mix,
		0, { guitarReset.value; ~apc.turnGridGreen(0, 3); },
		0.1, { guitarReset.value; ~apc.turnGridGreen(1, 3); },
		0.25, { guitarReset.value; ~apc.turnGridGreen(2, 3); },
		0.5, { guitarReset.value; ~apc.turnGridGreen(3, 3); },
		1,{ guitarReset.value; ~apc.turnGridGreen(4, 3); });
}, main, page);
~apc.setGridFunc(0, 3, { ~p3.guitarMix.setValue(0, 15) }, bank: main, page: page);
~apc.setGridFunc(1, 3, { ~p3.guitarMix.setValue(0.1, 10) }, bank: main, page: page);
~apc.setGridFunc(2, 3, { ~p3.guitarMix.setValue(0.25, 5) }, bank: main, page: page);
~apc.setGridFunc(3, 3, { ~p3.guitarMix.setValue(0.5, 5) }, bank: main, page: page);
~apc.setGridFunc(4, 3, { ~p3.guitarMix.setValue(1, 5) }, bank: main, page: page);



/*
Arbhar Values:
0 - 1
0.05 -2
0.075 - 3
0.11 - 4
0.15 - 5
0.19 - 6

0.23 - 1

seems to wrap without hitting omega buffr
*/

