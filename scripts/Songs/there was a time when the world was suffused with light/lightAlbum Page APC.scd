/*
Thursday, April 26th 2018
LightAlbum Page.scd
prm

page to navigate all of
'there was a time when the world was suffused with light'
*/

var page = 'light album';

var lightIsLoaded = false;
var meaningIsLoaded = false;
var wasntKnownIsLoaded = false;
var docileIsLoaded = false;
var sorrowIsLoaded = false;
var darknessIsLoaded = false;
var dissolutionIsLoaded = false;

~apc.makePage(page);
~apc.addPageLoadFunction(\masterFunc, { ~apc.storageDict[\loadMasterFunc].value; }, page);


/*
'there was a time when the world was suffused with light';
// Light:
~ohm.turnGridYellow(0, 0, page: page);
~ohm.setGridFunc(0, 0, {
if(lightIsLoaded == false, {
r {
~lightSend = Light_Sends.new(~prm.submixA, ~prm.submixB, ~prm.submixC,
~prm.irLibrary.irDict['3.4Cathedral'], ~prm.reverb.inBus,
relGroup: ~prm.procGroup, addAction: \addToHead);
while({ try { ~lightSend.isLoaded } != true }, { 0.001.wait; });
s.sync;
~light = Light.new(~prm.submixA, ~prm.submixB, ~prm.submixC,
~lightSend.reverb.inBus, ~lightSend.delays.inBus, ~prm.modularSend.inBus, ~lightSend.multiShift.inBus,
~prm.procGroup, \addToHead);
while({ try { ~light.isLoaded } != true }, { 0.001.wait; });
~ohm.setGridFunc(1, 0, { ~ohm.setPage('light'); }, page: page);
~ohm.turnGridGreen(1, 0);
lightIsLoaded = true;
}.play;
},
{
~light.free;
~lightSend.free;
~ohm.setGridFunc(1, 0, {  });
~ohm.turnGridOff(1, 0);
});
}, page: page);
*/


'everything was pregnant with meaning';
~apc.turnGridMagenta(0, 3, page: page);
~apc.setGridFunc(0, 3, {
	if( meaningIsLoaded == false, {
		r{
			~meaning = Meaning.new(~prm.audioIn, ~prm.modularSend.inBus, ~prm.irLibrary.irDict['2.0MediumHall'],
				~prm.procGroup, \addToHead);
			while({ try { ~meaning.isLoaded } != true }, { 0.001.wait; });
			~apc.turnGridGreen(1, 3);
			~apc.setGridFunc(1, 3, { ~apc.setPage('meaning'); }, page: page);
			meaningIsLoaded = true;
		}.play;
	},
	{
		~meaning.free;
		~apc.setGridFunc(1, 3, {});
		~apc.turnGridOff(1, 3);
		meaningIsLoaded = false;
	});
}, page: page);


/*
'and there was a soft glow';
~ohm.turnGridRed(0, 2, page: page);

'behind every shape just out of sight';
~ohm.turnGridRed(0, 3, page: page);
*/

'that which wasn’t known';
~apc.turnGridBlue(0, 0, page: page);
~apc.setGridFunc(0, 0, {
	if( wasntKnownIsLoaded == false, {
		r {
			~wasntKnown = WasntKnown.new(~prm.audioIn, ~prm.reverb.inBus, ~prm.granulator.inBus,
				~prm.modularSend.inBus, ~prm.delay.inBus, ~prm.procGroup, 'addToHead');
			while({ try { ~wasntKnown.isLoaded } != true }, { 0.001.wait; });
			~apc.turnGridGreen(1, 0);
			~apc.setGridFunc(1, 0, { ~wasntKnown.playSong }, page: page);
			~apc.setFaderFunc(1, { | val |
				~wasntKnown.mixer.setVol(val.ccdbfs(-70, 0));
			}, page: page);
			wasntKnownIsLoaded = true;
		}.play
	},
	{
		~wasntKnown.free;
		~apc.setGridFunc(1, 0, { });
		~apc.turnGridOff(1, 0);
		~apc.setFaderFunc(1, { });
		wasntKnownIsLoaded = false;
	});
}, page: page);

/*
'that which wasn’t known';
~ohm.turnGridBlue(0, 4, page: page);
~ohm.setGridFunc(0, 4, {

if( wasntKnownIsLoaded == false, {
r{
~wasntKnown = WasntKnown.new(~prm.submixA, ~prm.submixB, ~prm.submixC, ~prm.reverb.inBus, ~prm.granulator.inBus,
~prm.modularSend.inBus, nil, ~prm.procGroup, \addToHead);
while({ try { ~wasntKnown.isLoaded } != true }, { 0.001.wait; });
~ohm.turnGridGreen(1, 4);
~ohm.setGridFunc(1, 4, { ~ohm.setPage('wasntKnown'); }, page: page);
wasntKnownIsLoaded = true;
}.play;
},
{
~wasntKnown.free;
~ohm.setGridFunc(1, 4, {});
~ohm.turnGridOff(1, 4);
wasntKnownIsLoaded = false;
});
}, page: page);
*/

/*
'could be known';
~ohm.turnGridRed(0, 5, page: page);

'or discarded without consequence';
~ohm.turnGridRed(0, 6, page: page);
*/

'to compress the world to a docile state';
~apc.turnGridWhite(2, 3, page: page);
~apc.setGridFunc(2, 3, {
	if( docileIsLoaded == false, {
		r {
			~docile = Docile.new(~prm.audioIn, ~modOutput[0], ~modInput[0], ~modInput[1], ~moogDevice, ~moogPort,
				~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
				~prm.procGroup, \addToHead);
			while({ try { ~docile.isLoaded } != true }, { 0.001.wait; });
			~apc.turnGridGreen(3, 3);
			~apc.setGridFunc(3, 3, { ~apc.setPage('docile'); }, page: page);
			docileIsLoaded = true;
		}.play;
	},
	{
		~docile.free;
		~apc.setGridFunc(3, 3, { });
		~apc.turnGridOff(3, 3);
		docileIsLoaded = false;
	});
}, page: page);

/*
'to compress the world to a docile state';
~ohm.turnGridWhite(0, 7);
~ohm.setGridFunc(0, 7, {
if( docileIsLoaded == false, {
r{
~docile = Docile.new(~prm.submixA, "UMC404HD 192k", "UMC404HD 192k", ~prm.reverb.inBus, ~prm.granulator.inBus,
~prm.modularSend.inBus, relGroup: ~prm.procGroup, addAction: \addToHead);
while({ try { ~docile.isLoaded } != true }, { 0.001.wait; });
~ohm.turnGridGreen(1, 7);
~ohm.setGridFunc(1, 7, { ~ohm.setPage('docile'); }, page: page);
docileIsLoaded = true;
}.play;
},
{
~docile.free;
~ohm.setGridFunc(1, 7, {});
~ohm.turnGridOff(1, 7);
docileIsLoaded = false;
});
}, page: page);
*/


'there is a sorrow in beginning to understand';
~apc.turnGridBlue(2, 1, page: page);
~apc.setGridFunc(2, 1, {
	if( sorrowIsLoaded == false, {
		r{
			/*
			~sorrow = Sorrow.new(~prm.audioIn, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus,
			~prm.delay.inBus, relGroup: ~prm.procGroup, addAction: \addToHead);
			*/
			~sorrow = Sorrow.new(~prm.audioIn, ~micIn, ~pickupIn, ~modInput[0], ~modOutput[7],
				~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
				~moogDevice, ~moogPort, ~prm.procGroup, \addToHead);
			while({ try { ~sorrow.isLoaded } != true }, { 0.001.wait; });
			~apc.turnGridGreen(3, 1);
			~apc.setGridFunc(3, 1, { ~apc.setPage('sorrow'); }, page: page);
			sorrowIsLoaded = true;
		}.play;
	},
	{
		~sorrow.free;
		~apc.setGridFunc(3, 1, {});
		~apc.turnGridOff(3, 1);
		sorrowIsLoaded = false;
	});
}, page: page);


/*
'an objective truth';
~ohm.turnGridRed(4, 1, page: page);
*/

'a shallow eclipsing darkness';
~apc.turnGridWhite(4, 4, page: page);
~apc.setGridFunc(4, 4, {
	if( darknessIsLoaded == false, {
		r {
			~darkness = Darkness.new(~prm.audioIn, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
				~pickupIn, ~modOutput[0], ~modInput[0], ~modInput[1], ~modInput[3], ~moogDevice, ~moogPort,
				~prm.procGroup, \addToHead);
			while({ try { ~darkness.isLoaded } != true }, { 0.001.wait; });
			~apc.turnGridGreen(5, 4);
			~apc.setGridFunc(5, 4, { ~apc.setPage('darkness'); }, page: page);
			darknessIsLoaded = true;
		}.play;
	},
	{
		~darkness.free;
		~apc.setGridFunc(5, 4, { });
		~apc.turnGridOff(5, 4);
		darknessIsLoaded = false;
	});
}, page: page);

			/*
			'a shallow eclipsing darkness';
			~ohm.turnGridWhite(4, 2, page: page);
			~ohm.setGridFunc(4, 2, {
			if( darknessIsLoaded == false, {
			r{
			~darknessReturns = Darkness_Returns.new(~prm.submixA, ~prm.reverb.inBus,
			relGroup: ~prm.procGroup, addAction: \addToHead);
			while({ try { ~darknessReturns.isLoaded } != true }, { 0.001.wait; });
			~darkness = Darkness.new(~prm.submixA, ~prm.reverb.inBus, ~prm.granulator.inBus, ~darknessReturns.delay.inBus,
			~darknessReturns.granulator.inBus, "UMC404HD 192k", "UMC404HD 192k", ~prm.procGroup, \addToHead);
			while({ try { ~darkness.isLoaded } != true }, { 0.001.wait; });
			~ohm.turnGridGreen(5, 2);
			~ohm.setGridFunc(5, 2, { ~ohm.setPage('darkness'); });
			darknessIsLoaded = true;
			}.play;
			},
			{
			~darkness.free;
			~darknessReturns.free;
			~ohm.setGridFunc(5, 2, {});
			~ohm.turnGridOff(5, 2);
			darknessIsLoaded = false;
			});
			}, page: page);
			*/

			'dissolution of color onto an infinite plane';
			~apc.turnGridPink(4, 3, page: page);
			~apc.setGridFunc(4, 3, {
				if( dissolutionIsLoaded == false, {
					r{
						~dissolution = Dissolution.new(~prm.audioIn, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus,
							~prm.delay.inBus, relGroup: ~prm.procGroup, addAction: \addToHead);
						while({ try { ~dissolution.isLoaded } != true }, { 0.001.wait; });
						~apc.turnGridGreen(5, 3);
						~apc.setGridFunc(5, 3, { ~apc.setPage('dissolution'); });
						dissolutionIsLoaded = true;
					}.play;
				},
				{
					~dissolution.free;
					~apc.setGridFunc(5, 3, {});
					~apc.turnGridOff(5, 3);
					dissolutionIsLoaded = false;
				});
			}, page: page);
			/*
			'dissolution of color onto an infinite plane';
			~ohm.turnGridBlue(4, 3, page: page);
			~ohm.setGridFunc(4, 3, {
			if( dissolutionIsLoaded == false, {
			r {
			~dissolution = Dissolution.new(~prm.submixA, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus,
			relGroup: ~prm.procGroup, addAction: \addToHead);
			while({ try { ~dissolution.isLoaded } != true }, { 0.001.wait; });
			~ohm.turnGridGreen(5, 3);
			~ohm.setGridFunc(5, 3, { ~ohm.setPage('dissolution'); });
			dissolutionIsLoaded = true;
			}.play;
			},
			{
			~dissolution.free;
			~ohm.setGridFunc(5, 3, {});
			~ohm.turnGridOff(5, 3);
			dissolutionIsLoaded = false;
			});
			}, page: page);
			*/
			