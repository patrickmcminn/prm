/*
Monday, October 3rd 2022
Chesapeake.sc
prm
*/

Chesapeake : IM_Module {

	var <isLoaded, server;
	var <sequencer;
	var <tide, <bass, <wtrCrisis, <algae;
	var <midiDict, <midiEnabled;
	var <tidalCSV, <waterCSV;
	var <data, <normData;
	var <mapRout;
	var <dataBuf, <dataBus, <dataSynth;
	var <tideRate, <qualRate, <chordTime;
	var <ch3Tide;

	var <netOut;
	var <ip = "10.0.0.1";

	///// global variables:

	var <date, <time;

	var <dieOffEvent = false;
	var <oxygenCounter = 0;
	var <oxygenCrisis = false;
	var <oxygenStable = true;
	var <dieOffThreshold = 10;

	var <algalBloomEvent = false;
	var <severeAlgalBloomEvent = false;
	var <severeBloomTrigger = false;
	var <bloomCounter = 0;
	var <bloomThreshold = 10;

	*new {
		|
		outBus, seq,
		send0Bus, send1Bus, send2Bus, send3Bus, relGroup, addAction = 'addToHead'
		|
		^super.new(4, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(seq);
	}

	prInit { | seq |
		server = Server.default;
		server.waitForBoot {
			isLoaded = false;
			while({ try { mixer.isLoaded } != true }, { 0.001.wait; });

			chordTime = 30;

			data = IdentityDictionary.new;
			normData = IdentityDictionary.new;
			mapRout = IdentityDictionary.new;
			dataBuf = IdentityDictionary.new;
			dataBus = IdentityDictionary.new;
			dataSynth = IdentityDictionary.new;

			netOut = NetAddr.new(ip, 53000);


			sequencer = try { seq.uid };
			midiDict = IdentityDictionary.new;
			midiEnabled = false;

			tide = Chesa_Tide.new(mixer.chanStereo(0), group, \addToHead);
			while({ try { tide.isLoaded } != true }, { 0.001.wait; });

			bass = SubJuno.new(mixer.chanStereo(1), relGroup: group, addAction: \addToHead);
			while({ try { bass.isLoaded } != true }, { 0.001.wait; });

			wtrCrisis = Chesa_Crisis.new(mixer.chanStereo(2), relGroup: group, addAction: \addToHead);
			while({ try { wtrCrisis.isLoaded } != true }, { 0.001.wait; });

			algae = Chesa_Algae.new(mixer.chanStereo(3), relGroup: group, addAction: \addToHead);
			while({ try { algae.isLoaded } != true }, { 0.001.wait; });

			ch3Tide = Chesa_Tide.new(2, group, \addToHead);
			while({ try { ch3Tide.isLoaded } != true }, { 0.001.wait; });

			server.sync;

			this.prCalculateRates;
			this.prAddSynthDefs;
			this.prLoadDataSets;

			server.sync;

			this.prMakeBuffers;
			this.prMakeBusses;
			this.prCreateMappingRoutines;
			this.prMakeNetworkRoutines;

			server.sync;

			this.prSetInitialParameters;
			this.prMakeSequences;

			server.sync;

			this.prPopulateSequences;

			isLoaded = true;
		}

	}

	prCalculateRates {

		// tidal rate:
		// increments of 6 minutes
		// 10 entries an hour
		// 240 entries per day
		// 47 days
		// 11280 entries

		// I would like each day (240 samples) to take 4 minutes (240 seconds)
		// 1 sample per second


		// qual rate
		// increments of 15 minutes
		// 4 entries per hour
		// 96 entries per day
		// 47 days
		// 4512 entries total
		// I would like each days (96 samples) to take 4 minutes (240 seconds)
		// 0.4 samples per second


		var controlRate = server.sampleRate / server.options.blockSize;
		tideRate = 1/controlRate;
		qualRate = 0.4/controlRate;

	}

	prSetInitialParameters {
		//// tide:
		mixer.setPreVol(0, -12);
		mixer.setVol(0, -3);
		mixer.setSendVol(0, 0, -24);

		//// bass:
		bass.readPreset(\chesapeakeBass);
		mixer.setPreVol(1, -12);
		mixer.setVol(1, -6);
		mixer.setSendVol(1, 0, -15);

		//// water crisis:
		mixer.setPreVol(2, -12);
		mixer.setVol(2, -9);
		mixer.setSendVol(2, 0, -24);

		//// algae:
		mixer.setPreVol(3, -12);
		mixer.setVol(3, -6);
		mixer.setSendVol(3, 0, -18);

		ch3Tide.mixer.setVol(-6);
	}

	prMakeSequences {
		bass.makeSequence(\basslinePt1);
	}

	prPopulateSequences {
		bass.addKey(\basslinePt1, \note, Prand([-5, 2, 3, 9, 15, [-5, 7], [2, 9]], inf));
		bass.addKey(\basslinePt1, \legato, 1.2);
		bass.addKey(\basslinePt1, \dur, Prand([4, 8, 12, 16, 18, 8, 12, 14], inf));
		bass.addKey(\basslinePt1, \octave, Prand([3, 4], inf));
		bass.addKey(\basslinePt1, \amp, 0.5);
	}


	prMakeBusses  {
		//dataBus[\date] = Bus.control;
		//dataBus[\time] = Bus.control;

		dataBus[\tide] = Bus.control;
		dataBus[\salinity] = Bus.control;
		dataBus[\ph] = Bus.control;
		dataBus[\oxygen] = Bus.control;
		dataBus[\turbidity] = Bus.control;
		dataBus[\chlorophyll] = Bus.control;
		dataBus[\temp] = Bus.control;

		dataBus[\tideNorm] = Bus.control;
		dataBus[\salinityNorm] = Bus.control;
		dataBus[\phNorm] = Bus.control;
		dataBus[\oxygenNorm] = Bus.control;
		dataBus[\turbidityNorm] = Bus.control;
		dataBus[\chlorophyllNorm] = Bus.control;
		dataBus[\tempNorm] = Bus.control;
	}

	prAddSynthDefs {
		SynthDef(\prm_chesa_dataPlayer, {
			| outBus, buffer, rate, startPos = 0 |
			var sig = PlayBuf.kr(1, buffer, rate, 1, startPos, doneAction: 2);
			Out.kr(outBus, sig);
		}).add;
	}

	prLoadDataSets {

		var tidalPath = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/Chesapeake_SC/dataSets/tidesAugustSeptember2022.csv";
		var waterPath = "/Users/patrickmcminn/Library/Application Support/SuperCollider/Extensions/prm/Songs/Chesapeake_SC/dataSets/waterQualityAugustSeptember2022.csv";

		tidalCSV = CSVFileReader.read(tidalPath, true);
		waterCSV = CSVFileReader.read(waterPath, true);

		server.sync;

		tidalCSV = tidalCSV.flop;
		waterCSV = waterCSV.flop;

		data[\tide] = Array.fill((tidalCSV[2].size)-1, { | i | tidalCSV[2][i+1].interpret.asFloat });
		normData[\tide] = data[\tide].abs.normalize;

		data[\date] = Array.fill((tidalCSV[0].size)-1, { | i | tidalCSV[0][i+1] });
		data[\time] = Array.fill((tidalCSV[1].size)-1, { | i | tidalCSV[1][i+1] });

		data[\salinity] = Array.fill((waterCSV[2].size)-1, { | i | waterCSV[2][i+1].interpret.asFloat; });
		data[\ph] = Array.fill((waterCSV[3].size)-1, { | i | waterCSV[3][i+1].interpret.asFloat; });
		data[\oxygen] = Array.fill((waterCSV[4].size)-1, { | i | waterCSV[4][i+1].interpret.asFloat; });
		data[\turbidity] = Array.fill((waterCSV[5].size)-1, { | i | waterCSV[5][i+1].interpret.asFloat; });
		data[\chlorophyll] = Array.fill((waterCSV[6].size)-1, { | i | waterCSV[6][i+1].interpret.asFloat; });
		data[\temp] = Array.fill((waterCSV[7].size)-1, { | i | waterCSV[7][i+1].interpret.asFloat; });

		normData[\salinity] = data[\salinity].normalize;
		normData[\ph] = data[\ph].normalize;
		normData[\oxygen] = data[\oxygen].normalize;
		normData[\turbidity] = data[\turbidity].normalize;
		normData[\chlorophyll] = data[\chlorophyll].normalize;
		normData[\temp] = data[\temp].normalize

	}

	prMakeBuffers {
		//dataBuf[\date] = Buffer.loadCollection(server, data[\date], 1);
		//dataBuf[\time] = Buffer.loadCollection(server, data[\time], 1);

		dataBuf[\tideNorm] = Buffer.loadCollection(server, normData[\tide], 1);

		dataBuf[\salinityNorm] = Buffer.loadCollection(server, normData[\salinity], 1);
		dataBuf[\phNorm] = Buffer.loadCollection(server, normData[\ph], 1);
		dataBuf[\oxygenNorm] = Buffer.loadCollection(server, normData[\oxygen], 1);
		dataBuf[\turbidityNorm] = Buffer.loadCollection(server, normData[\turbidity], 1);
		dataBuf[\chlorophyllNorm] = Buffer.loadCollection(server, normData[\chlorophyll], 1);
		dataBuf[\tempNorm] = Buffer.loadCollection(server, normData[\temp], 1);

		dataBuf[\tide] = Buffer.loadCollection(server, data[\tide], 1);
		dataBuf[\salinity] = Buffer.loadCollection(server, data[\salinity], 1);
		dataBuf[\ph] = Buffer.loadCollection(server, data[\ph], 1);
		dataBuf[\oxygen] = Buffer.loadCollection(server, data[\oxygen], 1);
		dataBuf[\turbidity] = Buffer.loadCollection(server, data[\turbidity], 1);
		dataBuf[\chlorophyll] = Buffer.loadCollection(server, data[\chlorophyll], 1);
		dataBuf[\temp] = Buffer.loadCollection(server, data[\temp], 1);
	}

	prMakeNetworkRoutines {

		mapRout[\dateNetOut] = r{
			var dateCounter = 0;
			{
				date = data[\date][dateCounter].asString;
				// here is where you send the date to QLab
				netOut.sendMsg('/cue/date/text', ("date:"+date).asString);
				dateCounter = dateCounter + 1;
				1.wait;
			}.loop;
		};

		mapRout[\timeNetOut] = r {
			var timeCounter = 0;
			{
				time = data[\time][timeCounter].asString;
				// here is where you send the time to QLab
				netOut.sendMsg('/cue/time/text', ("time:"+time).asString);
				timeCounter = timeCounter + 1;
				1.wait;
			}.loop;
		};

		mapRout[\tempNetOut] = r {
      var temp;
			{
				dataBus[\temp].get({ | val | temp = val });
        server.sync;
        temp = temp.trunc(0.01);
        temp.postln;
				netOut.sendMsg('/cue/temp/text', ("waterTemp ="+temp+"F"));
				2.5.wait;
			}.loop;
		};

		mapRout[\tideNetOut] = r {
      var height;
			{
				dataBus[\tide].get({ | val | height = val; });
        server.sync;
        height = height.trunc(0.01);
				netOut.sendMsg('/cue/tide/text', (height+"ft").asString);
				1.wait;
			}.loop;
		};

		mapRout[\oxygenNetOut] = r {
			var oxygen;
      {
				dataBus[\oxygen].get({ | val | oxygen = val });
        server.sync;
        oxygen = oxygen.trunc(0.01);
				netOut.sendMsg('/cue/oxygen/text', ("dissolvedOxygen:"+oxygen+"mg/L").asString);
				netOut.sendMsg('/cue/oxyCounter/text', ("oxygenCounter ="+oxygenCounter).asString);
				netOut.sendMsg('/cue/oxyStable/text', ("oxygenStable ="+oxygenStable).asString);
				//netOut.sendMsg('/cue/oxyCrisis/text', ("oxygenCrisis ="+oxygenCrisis).asString);
				netOut.sendMsg('/cue/dieOff/text', ("dieOffEvent ="+dieOffEvent).asString);
				2.5.wait;
			}.loop;
		};

		mapRout[\algaeNetOut] = r{
			var chloro;
      {
				dataBus[\chlorophyll].get({ | val | chloro = val });
        server.sync;
        chloro = chloro.trunc(0.01);
				netOut.sendMsg('/cue/chlorophyll/text', ("chlorophyll:"+chloro+"ug/L").asString);
				netOut.sendMsg('/cue/bloomCounter/text', ("bloomCounter ="+bloomCounter).asString);
				netOut.sendMsg('/cue/bloomEvent/text', ("algalBloomEvent ="+algalBloomEvent).asString);
				netOut.sendMsg('/cue/severeBloomEvent/text', ("severeAlgalBloomEvent ="+severeBloomTrigger).asString);
				2.5.wait;
			}.loop;
		};

    mapRout[\crisisNetOut] = r {
      loop {
        if( dieOffEvent == true, { netOut.sendMsg('/cue/crisisIn/go'); }, {  netOut.sendMsg('/cue/crisisOut/go');  });
        if( severeBloomTrigger == true,
          { netOut.sendMsg('/cue/algaeIn/go'); }, { netOut.sendMsg('/cue/algaeOut/go'); });
        1.wait;
      };
      /*
      var previous = false;
      loop {
        if( dieOffEvent != previous,
          {
            if( dieOffEvent == true,
              { netOut.sendMsg('/cue/crisisOut/go'); },
              { netOut.sendMsg('/cue/crisisIn/go'); });
            previous = dieOffEvent
          },
          { previous = dieOffEvent });
        1.wait;
      };
      */
    };



	}

	prCreateMappingRoutines {
		mapRout[\tideMap] = r {
			loop {
				dataBus[\tideNorm].get({ | val |
					var durLow = if( val > 0.5,
						{ val.lincurve(0, 1, 1, 0.01, -1.5) },
						{ val.lincurve(0, 1, 1, 0.01, 1.5); });
					var durHigh = if( val > 0.5,
						{ val.lincurve(0, 1, 3, 0.3, -1.5) },
						{ val.lincurve(0, 1, 3, 0.3, 1.5); });
					var trigLow = if( val > 0.5,
						{ val.lincurve(0, 1, 0.5, 45, -1.5); },
						{ val.lincurve(0, 1, 0.5, 45, 1.5); });
					var trigHigh = if( val > 0.5,
						{ val.lincurve(0, 1, 3, 60, -1.5); },
						{ val.lincurve(0, 1, 3, 60, 1.5); });
					var cutoffLow = val.linexp(0, 1, 400, 4500);
					var cutoffHigh = val.linexp(0, 1, 900, 9000);
					var tremSpeed = val.linlin(0, 1, 0.1, 8);
					var verb = val.linlin(0, 1, -24, -15);
					tide.cloud.setSustain(durLow, durHigh);
					tide.cloud.setTrigRate(trigLow, trigHigh);
					tide.cloud.setCutoff(cutoffLow, cutoffHigh);
					tide.tremolo.setVolLFOFreq(tremSpeed);
					mixer.setSendVol(0, 0, verb);

					ch3Tide.cloud.setSustain(durLow, durHigh);
					ch3Tide.cloud.setTrigRate(trigLow, trigHigh);
					ch3Tide.cloud.setCutoff(cutoffLow, cutoffHigh);
					ch3Tide.tremolo.setVolLFOFreq(tremSpeed);
				});
				0.01.wait;
			}
		};

		mapRout[\chordChange] = r {
			loop {
				tide.playMarkovChord;
				ch3Tide.playMarkovChord;
				chordTime.wait;
			};
		};

		mapRout[\chordTime] = r {
			loop {
				dataBus[\tideNorm].get({ | val |
					chordTime = val.linlin(0, 1, 60, 5);
				});
				0.1.wait;
			}
		};

		mapRout[\oxygenInst] = r {
			var time = 2.5;
			var param = 3;
			loop {
				dataBus[\oxygen].get({ | val | param = val });
        server.sync;
				//param.postln;
				if( (param < 1) && (oxygenCrisis) == false, {
					oxygenCrisis = true;
					tide.cloud.setInstArray([\gaborGendy, \gaborGendy, \rexpodecRect, \expodecSaw, \rexpodecSaw]);
					ch3Tide.cloud.setInstArray([\gaborGendy, \gaborGendy, \rexpodecRect, \expodecSaw, \rexpodecSaw]);
					algae.granulator.setRate(0.5, 0.5);
				});
				if( (param > 2) && (oxygenCrisis == true), {
					dieOffEvent = false;
					oxygenCrisis = false;
					tide.cloud.setInstArray([\gaborSine, \gaborSine, \gaborSine, \percRevSine, \percRect, \gaborSaw]);
					ch3Tide.cloud.setInstArray([\gaborSine, \gaborSine, \gaborSine, \percRevSine, \percRect, \gaborSaw]);
					algae.granulator.setRate(1, 1);
				});
				if( param < 1.5, {
					tide.cloud.addToInstArray(\expodecRect);
					tide.cloud.removeFromInstArray(\gaborSine);
					tide.cloud.addToInstArray(\gaborGendy);

					ch3Tide.cloud.addToInstArray(\expodecRect);
					ch3Tide.cloud.removeFromInstArray(\gaborSine);
					ch3Tide.cloud.addToInstArray(\gaborGendy);
				});
				if( (param < 3) && (param > 1.5), {
					tide.cloud.addToInstArray(\gaborRect);
					tide.cloud.removeFromInstArray(\gaborSine);
					tide.cloud.removeFromInstArray(\rexpodecSine);
					tide.cloud.removeFromInstArray(\percRevSine);
					tide.cloud.removeFromInstArray(\gaborGendy);

					ch3Tide.cloud.addToInstArray(\gaborRect);
					ch3Tide.cloud.removeFromInstArray(\gaborSine);
					ch3Tide.cloud.removeFromInstArray(\rexpodecSine);
					ch3Tide.cloud.removeFromInstArray(\percRevSine);
					ch3Tide.cloud.removeFromInstArray(\gaborGendy);
				});
				//if( (param < 3) && (oxygenStable == true), { oxygenStable = false; });
				if( (param > 3) && (param < 5), {
					tide.cloud.removeFromInstArray(\gaborGendy);
					tide.cloud.removeFromInstArray(\gaborRect);
					tide.cloud.removeFromInstArray(\gaborSaw);
					tide.cloud.addToInstArray(\gaborSine);
					tide.cloud.addToInstArray(\percSine);
					tide.cloud.addToInstArray(\rexpodecSine);
					tide.cloud.addToInstArray(\percRevSine);

					ch3Tide.cloud.removeFromInstArray(\gaborGendy);
					ch3Tide.cloud.removeFromInstArray(\gaborRect);
					ch3Tide.cloud.removeFromInstArray(\gaborSaw);
					ch3Tide.cloud.addToInstArray(\gaborSine);
					ch3Tide.cloud.addToInstArray(\percSine);
					ch3Tide.cloud.addToInstArray(\rexpodecSine);
					ch3Tide.cloud.addToInstArray(\percRevSine);
				});
				if( (param > 4.5) && ( oxygenStable == true),
					{
						tide.cloud.setInstArray([\percRevSine, \gaborWideSine, \gaborWideSaw, \percRevSaw]);
						ch3Tide.cloud.setInstArray([\percRevSine, \gaborWideSine, \gaborWideSaw, \percRevSaw]);
						algae.granulator.setRate(2, 2);
					},
					{ oxygenStable = true });

				if( (param < 3) && (oxygenStable == true), { oxygenStable = false; });

				time.wait;
			};
		};

		mapRout[\oxygenCounter] = r {
			var time = 2.5;
			loop {
				if( oxygenCrisis == true,
					{ if( oxygenCounter < dieOffThreshold, {
						oxygenCounter = oxygenCounter + 1; "oxygen crisis = true".postln; }); },
					{ if( oxygenCounter > 0, { oxygenCounter = oxygenCounter -1 }); });
				if( oxygenCounter >= dieOffThreshold,
					{ dieOffEvent = true; "Die Off Event = true".postln; },
					{ dieOffEvent = false;  });
				time.wait;
			}
		};

		mapRout[\crisisBassVol] = r {
			{
				var volArray = [-70, -24, -21, -18, -15, -12, -9, -6, -3, 0, 0];
				var cutoffArray = [350, 700, 900, 1100, 1300, 1500, 1800, 2200, 2500, 5500];
				var granSendArray = [-70, -28, -24, -21, -18, -15, -12, -9, -6, -3, 0];

				wtrCrisis.mixer.setVol(volArray[oxygenCounter], 0.1);
				mixer.setSendVol(2, 1, granSendArray[oxygenCounter]);

				/*
				if( oxygenCrisis == true,
				{ wtrCrisis.mixer.setVol(volArray[oxygenCounter], 0.1) },
				{ wtrCrisis.mixer.setVol(-70) });
				*/
				if( dieOffEvent == true, { wtrCrisis.mixer.setVol(0, 3); });

				if( oxygenCrisis == true, { wtrCrisis.filter.setCutoff(cutoffArray[oxygenCounter]) });
				if( dieOffEvent == true, { wtrCrisis.filter.setCutoff(8000); });

				0.1.wait;
			}.loop;
		};

		mapRout[\crisisBass] = r {
			{
				if( dieOffEvent == true,
					{
						if( wtrCrisis.synth.isSequencePlaying(\bassline) == true,
							{ wtrCrisis.synth.stopSequence(\bassline); });
						if( wtrCrisis.synth.isSequencePlaying(\dieOff) != true,
							{ wtrCrisis.synth.playSequence(\dieOff); });
						wtrCrisis.filter.setCutoffLFOBottomRatio(0.25);
						wtrCrisis.filter.setCutoffLFOTopRatio(3);
						wtrCrisis.filter.setRQ(0.35);
					},
					{
						if( wtrCrisis.synth.isSequencePlaying(\bassline) != true,
							{ wtrCrisis.synth.playSequence(\bassline); });
						if( wtrCrisis.synth.isSequencePlaying(\dieOff) == true,
							{ wtrCrisis.synth.stopSequence(\dieOff); });
						wtrCrisis.filter.setCutoffLFOBottomRatio(1);
						wtrCrisis.filter.setCutoffLFOTopRatio(1);
						wtrCrisis.filter.setRQ(1);
				});

				3.wait;
			}.loop;
		};

		mapRout[\bloomCounter] = r {
			var time = 2.5;
			var param = 15;
			{
				dataBus[\chlorophyll].get({ | val | param = val; });
				param.postln;
				if( param < 50, { algalBloomEvent = false; });
				if( param < 100, { severeAlgalBloomEvent = false; });
				if( param >= 50, { algalBloomEvent = true; });
				if( param >= 100, { severeAlgalBloomEvent = true; });

				//if( severeAlgalBloomEvent == true, { severeBloomCounter = severeBloomCounter + 1; });
				if( algalBloomEvent == true,
					{ if((bloomCounter < 10) && (0.6.coin == true) && (severeAlgalBloomEvent != true),
						{ bloomCounter = bloomCounter + 1; })},
					{ if( bloomCounter > 0,
						{ bloomCounter = bloomCounter - 2; }); if( bloomCounter < 0, { bloomCounter = 0 }); }
				);
				bloomCounter.postln;
				if( (severeAlgalBloomEvent == true) && (bloomCounter < 10) && (0.5.coin == true),
					{ bloomCounter = bloomCounter + 1; });
				if( bloomCounter == 10, { severeBloomTrigger = true });
				if( bloomCounter == 0, { severeBloomTrigger = false; });
				if( (bloomCounter < 10) && (0.2.coin == true), { severeBloomTrigger = false; });
				if( (severeBloomTrigger == true) && (severeAlgalBloomEvent == false) && (0.5.coin),
					{ if( bloomCounter >0, { bloomCounter = bloomCounter -1; }); });
				time.wait;
			}.loop;
		};

		mapRout[\bloomEvent] = r {
			{
				var time = 2.5;

				if( severeBloomTrigger == true,
					{
						if( algae.synth.isSequencePlaying(\algae) == true, { algae.synth.stopSequence(\algae); });
						if( algae.synth.isSequencePlaying(\bloom) != true, { algae.synth.playSequence(\bloom); });
						mixer.setSendVol(3, 0, -70);
					},
					{
						if( algae.synth.isSequencePlaying(\algae) != true, { algae.synth.playSequence(\algae); });
						if( algae.synth.isSequencePlaying(\bloom) == true, { algae.synth.stopSequence(\bloom); });
						mixer.setSendVol(3, 0, -18);
				});
				if( algalBloomEvent == true,
					{ algae.granulator.setGrainEnvelope(\expodec); }, { algae.granulator.setGrainEnvelope(\revPerc); });


				time.wait;
			}.loop;
		};

		mapRout[\chloroAlgae] = r {
			var bloomArray = [1, 1.5, 1, 0.5, 0.25, 0.08, 0.07, 0.05, 0.04, 0.03, 0.03];
			var bloomDur = [10, 12, 15, 17, 20, 25, 30, 35, 40, 45, 50];
			{
				dataBus[\chlorophyllNorm].get({ | val |
					var durLow;
					var durHigh;
					var trigRate = val.linlin(0, 0.8, 0.25, 20);
					var cutoff = val.linexp(0, 1, 600, 5500);
					if( severeBloomTrigger != true,
						{
							algae.granulator.setTrigRate(trigRate);
							algae.eq.setLowPassCutoff(cutoff);
							algae.granulator.setSync(0);
							algae.granulator.setGrainDur(3, 5);
						},
						{
							algae.granulator.setTrigRate(bloomDur[bloomCounter]);
							algae.granulator.setSync(1);
							algae.granulator.setGrainDur(bloomArray[bloomCounter], bloomArray[bloomCounter]);
							algae.eq.setLowPassCutoff(7000);
						}
					);
				});
				0.1.wait;
			}.loop;
		};

		mapRout[\crisisVolume] = r {
			var fadeTime = 10;
			var time = 2.5;
			{
				if( severeBloomTrigger || dieOffEvent, { tide.mixer.setVol(-70, fadeTime); });
				if( (severeBloomTrigger == false) && (dieOffEvent == false), { tide.mixer.setVol(0, fadeTime); });
				time.wait;
			}.loop;
		};

		mapRout[\bassTide] = r {
			{
				dataBus[\tideNorm].get({ | val |
					var vol = if( val < 0.4, { -inf }, { val.linlin(0.4, 1, 0, 0.4).ampdb });
					bass.mixer.setVol(vol);
					//vol.postln;
				});
				0.01.wait;
			}.loop;
		};

		mapRout[\turbTrem] = r {
			{
				dataBus[\turbidityNorm].get({ | val |
					var trem = val.lincurve(0, 0.8, 0, 1, -1.2);
					tide.tremolo.setVolLFODepth(trem);
					ch3Tide.tremolo.setVolLFODepth(trem);
				});
				0.1.wait;
			}.loop;
		};

		mapRout[\tempAlgae] = r {
			var time = 2.5;
			{
				dataBus[\temp].get({ | val |
					var changed = false;
					//val.postln;
					if( val < 75, { algae.changePitchSet; changed = true; });
					if( (val < 77) &&  (0.6.coin) && (changed == false), { algae.changePitchSet; changed = true; });
					if( (val < 80) && (0.3.coin) && (changed == false), { algae.changePitchSet; changed = true; });
					if( (changed == false) && (0.1.coin), { algae.changePitchSet; changed = true; });
					if( changed == true, { "set changed".postln; });
				});
				time.wait;
			}.loop;
		}

	}

	//////// public functions:

	free { }

	playTidalData { | startPos = 0 |
		dataSynth[\tideNorm] = Synth(\prm_chesa_dataPlayer,
			[\outBus, dataBus[\tideNorm], \buffer, dataBuf[\tideNorm], \rate, tideRate, \startPos, startPos],
			group, \addToHead);
		dataSynth[\tide] = Synth(\prm_chesa_dataPlayer,
			[\outBus, dataBus[\tide], \buffer, dataBuf[\tide], \rate, tideRate, \startPos, startPos],
			group, \addToHead);
		/*
		dataSynth[\date] = Synth(\prm_chesa_dataPlayer,
		[\outBus, dataBus[\date], \buffer, dataBuf[\date], \rate, tideRate, \startPos, startPos],
		group, \addToHead);
		dataSynth[\time] = Synth(\prm_chesa_dataPlayer,
		[\outBus, dataBus[\time], \buffer, dataBuf[\time], \rate, tideRate, \startPos, startPos],
		group, \addToHead);
		*/
	}

	freeTidalData { dataSynth[\tide].free; }

	playWaterQualityData { | startPos = 0 |
		dataSynth[\oxygen] = Synth(\prm_chesa_dataPlayer,
			[\outBus, dataBus[\oxygen], \buffer, dataBuf[\oxygen], \rate, qualRate, \startPos, startPos],
			group, \addToHead);
		dataSynth[\turbidityNorm] = Synth(\prm_chesa_dataPlayer,
			[\outBus, dataBus[\turbidityNorm], \buffer, dataBuf[\turbidityNorm], \rate, qualRate, \startPos, startPos],
			group, \addToHead);
		dataSynth[\chlorophyllNorm] = Synth(\prm_chesa_dataPlayer,
			[\outBus, dataBus[\chlorophyllNorm], \buffer, dataBuf[\chlorophyllNorm], \rate, qualRate, \startPos, startPos],
			group, \addToHead);
		dataSynth[\chlorophyll] = Synth(\prm_chesa_dataPlayer,
			[\outBus, dataBus[\chlorophyll], \buffer, dataBuf[\chlorophyll], \rate, qualRate, \startPos, startPos],
			group, \addToHead);
		dataSynth[\temp] = Synth(\prm_chesa_dataPlayer,
			[\outBus, dataBus[\temp], \buffer, dataBuf[\temp], \rate, qualRate, \startPos, startPos],
			group, \addToHead);
	}

	playMappingRoutines {
		mapRout[\bassTide].play;
		mapRout[\chordTime].play;
		mapRout[\chordChange].play;
		mapRout[\tideMap].play;
		mapRout[\oxygenInst].play;
		mapRout[\turbTrem].play;
		mapRout[\oxygenCounter].play;
		mapRout[\crisisBassVol].play;
		mapRout[\crisisBass].play;
		mapRout[\bloomCounter].play;
		mapRout[\bloomEvent].play;
		mapRout[\chloroAlgae].play;
		mapRout[\crisisVolume].play;
		mapRout[\tempAlgae].play;

		mapRout[\time].play;
		mapRout[\date].play;
	}


} 