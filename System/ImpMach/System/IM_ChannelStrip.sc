// 5/9/2014 JAB
// 9/11/2015 - prm - vol, panBal, sends 0-3 vol vars added

IM_ChannelStrip {
	var <inBus;
	var <synth, <isMuted, <isFeedback;
	var <send0IsMuted, <send1IsMuted, <send2IsMuted, <send3IsMuted;
	var <isLoaded;
	var <vol, <panBal, <send0Vol, <send1Vol, <send2Vol, <send3Vol, <preOrPost;
	var <sends;

	*new { |outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil, nilBus = nil,
		feedback = false, relGroup = nil, addAction = \addToHead|

		^super.new.prInit(outBus, send0Bus, send1Bus, send2Bus, send3Bus, nilBus, feedback,
			relGroup, addAction);
	}

	*newNoSends { | outBus = 0, nilBus = nil, feedback = false, relGroup = nil, addAction = \addToHead |
		^super.new.prInitNoSends(outBus, nilBus, feedback, relGroup, addAction);
	}

	prInit { |outBus, send0Bus, send1Bus, send2Bus, send3Bus, nilBus, feedback, relGroup, addAction|
		var server = Server.default;

		sends = true;
		isLoaded = false;
		isMuted = false;
		isFeedback = feedback;

		vol = 0;
		panBal = 0;
		preOrPost = 'post';
		send0Vol = -70;
		send1Vol = -70;
		send2Vol = -70;
		send3Vol = -70;

		send0IsMuted = false;
		send1IsMuted = false;
		send2IsMuted = false;
		send3IsMuted = false;

		server.waitForBoot {
			this.prAddSynthDefs;
			inBus = Bus.audio(server, 2);
			server.sync;

			[send0Bus, send1Bus, send2Bus, send3Bus].do { |bus|
				if(bus == nil, { bus == nilBus })
			};
			server.sync;

			if(feedback.not,
				{
					synth = Synth(\IM_ChannelStrip, [\inBus, inBus, \outBus, outBus,
						\send0Bus, send0Bus, \send1Bus, send1Bus, \send2Bus, send2Bus, \send3Bus, send3Bus],
					relGroup, addAction
					);
				},
				{
					synth = Synth(\IM_ChannelStripFeedback, [\inBus, inBus, \outBus, outBus,
						\send0Bus, send0Bus, \send1Bus, send1Bus, \send2Bus, send2Bus, \send3Bus, send3Bus],
					relGroup, addAction
					);
				}
			);

			while( { synth == nil }, { 0.001.wait; });
			isLoaded = true;
		};
	}

	prInitNoSends { | outBus = 0, nilBus = nil, feedback = false, relGroup = nil, addAction = \addToHead |
		var server = Server.default;

		sends = false;
		isLoaded = false;
		isMuted = false;
		isFeedback = feedback;

		vol = 0;
		panBal = 0;
		preOrPost = 'post';

		server.waitForBoot {
			this.prAddSynthDefs;
			inBus = Bus.audio(server, 2);
			server.sync;

			if(feedback.not,
				{
					synth = Synth(\IM_ChannelStrip, [\inBus, inBus, \outBus, outBus],
						relGroup, addAction
					);
				},
				{
					synth = Synth(\IM_ChannelStripFeedback, [\inBus, inBus, \outBus, outBus],
						relGroup, addAction
					);
				}
			);

			while( { synth == nil }, { 0.001.wait; });
			isLoaded = true;
		};

	}

	prAddSynthDefs {
		SynthDef(\IM_ChannelStrip, { |preAmp = 1, inBus = 1, outBus = 0, amp = 1, mute = 0,
			preOrPost = 1, panBal = 0, monoOrStereo = 1,
			send0Bus, send0Amp = 0, send1Bus, send1Amp = 0,
			send2Bus, send2Amp = 0, send3Bus, send3Amp = 0,
			send0Mute = 1, send1Mute = 1, send2Mute = 1, send3Mute = 1,
			ampLagTime = 0.05|

			var sig, input, monoOrStereoSig, monoInput, stereoInput, sendSig, lagTime = 0.05;

			amp = amp.lag(ampLagTime);

			input = In.ar(inBus, 2);
			input = input * preAmp;

			monoInput = Pan2.ar(input[0], panBal, 3.dbamp);
			stereoInput = Balance2.ar(input[0], input[1], panBal, 3.dbamp);

			monoOrStereoSig = (monoInput * (1-monoOrStereo)) + (stereoInput * monoOrStereo);
			// adjusts for weird drop in volume:
			monoOrStereoSig = monoOrStereoSig * 3.dbamp;
			sig = monoOrStereoSig * amp * (1 - mute);

			// Choose pre or post fx sends
			sendSig = (monoOrStereoSig * (1 - preOrPost)) + (sig * preOrPost);

			// Fx sends
			Out.ar(send0Bus, sendSig * send0Amp.lag(lagTime) * send0Mute);
			Out.ar(send1Bus, sendSig * send1Amp.lag(lagTime) * send1Mute);
			Out.ar(send2Bus, sendSig * send2Amp.lag(lagTime) * send2Mute);
			Out.ar(send3Bus, sendSig * send3Amp.lag(lagTime) * send3Mute);

			Out.ar(outBus, sig);
		}).add;

		SynthDef(\IM_ChannelStripFeedback, { |preAmp = 1, inBus = 1, outBus = 0, amp = 1, mute = 0,
			preOrPost = 1, panBal = 0, monoOrStereo = 1,
			send0Bus, send0Amp = 0, send1Bus, send1Amp = 0,
			send2Bus, send2Amp = 0, send3Bus, send3Amp = 0,
			send0Mute = 1, send1Mute = 1, send2Mute = 1, send3Mute = 1,
			ampLagTime = 0.05|

			var sig, input, monoOrStereoSig, monoInput, stereoInput, sendSig, lagTime = 0.05;

			amp = amp.lag(ampLagTime);

			input = InFeedback.ar(inBus, 2);
			input = input * preAmp;

			monoInput = Pan2.ar(input[0], panBal, 3.dbamp);
			stereoInput = Balance2.ar(input[0], input[1], panBal, 3.dbamp);

			monoOrStereoSig = (monoInput * (1-monoOrStereo)) + (stereoInput * monoOrStereo);
			sig = monoOrStereoSig * amp * (1 - mute);

			// Choose pre or post fx sends
			sendSig = (monoOrStereoSig * (1 - preOrPost)) + (sig * preOrPost);

			// Fx sends
			Out.ar(send0Bus, sendSig * send0Amp.lag(lagTime) * send0Mute);
			Out.ar(send1Bus, sendSig * send1Amp.lag(lagTime) * send1Mute);
			Out.ar(send2Bus, sendSig * send2Amp.lag(lagTime) * send2Mute);
			Out.ar(send3Bus, sendSig * send3Amp.lag(lagTime) * send3Mute);

			Out.ar(outBus, sig);
		}).add;

		SynthDef(\IM_ChannelStrip_noSends, { |preAmp = 1, inBus = 1, outBus = 0, amp = 1, mute = 0,
			preOrPost = 1, panBal = 0, monoOrStereo = 1,
			ampLagTime = 0.05|

			var sig, input, monoOrStereoSig, monoInput, stereoInput, sendSig, lagTime = 0.05;

			amp = amp.lag(ampLagTime);

			input = In.ar(inBus, 2);
			input = input * preAmp;

			monoInput = Pan2.ar(input[0], panBal, 3.dbamp);
			stereoInput = Balance2.ar(input[0], input[1], panBal, 3.dbamp);

			monoOrStereoSig = (monoInput * (1-monoOrStereo)) + (stereoInput * monoOrStereo);
			// adjusts for weird drop in volume:
			monoOrStereoSig = monoOrStereoSig * 3.dbamp;
			sig = monoOrStereoSig * amp * (1 - mute);

			Out.ar(outBus, sig);
		}).add;

		SynthDef(\IM_ChannelStripFeedback_noSends, { |preAmp = 1, inBus = 1, outBus = 0, amp = 1, mute = 0,
			preOrPost = 1, panBal = 0, monoOrStereo = 1,
			ampLagTime = 0.05|

			var sig, input, monoOrStereoSig, monoInput, stereoInput, sendSig, lagTime = 0.05;

			amp = amp.lag(ampLagTime);

			input = InFeedback.ar(inBus, 2);
			input = input * preAmp;

			monoInput = Pan2.ar(input[0], panBal, 3.dbamp);
			stereoInput = Balance2.ar(input[0], input[1], panBal, 3.dbamp);

			monoOrStereoSig = (monoInput * (1-monoOrStereo)) + (stereoInput * monoOrStereo);
			sig = monoOrStereoSig * amp * (1 - mute);

			Out.ar(outBus, sig);
		}).add;
	}

	setMono { synth.set(\monoOrStereo, 0) }
	setStereo { synth.set(\monoOrStereo, 1) }

	inBusMono {
		this.setMono;
		^inBus.subBus(0);
	}

	chanMono { ^this.inBusMono }  // Synonym

	inBusStereo {
		this.setStereo;
		^inBus;
	}

	chanStereo { ^this.inBusStereo }  // Synonym

	setPreVol { |db = 0| synth.set(\preAmp, db.dbamp) }
	setVol { |db = 0, lagTime = 0|
		synth.set(\amp, db.dbamp, \ampLagTime, lagTime);
		vol = db;
	}

	fadeOut { |lagTime = 2| synth.set(\amp, -999.dbamp, \ampLagTime, lagTime) }
	fade{ | targetdb = 0, time = 2 | synth.set(\amp, targetdb.dbamp, \ampLagTime, time); vol = targetdb; }

	mute { synth.set(\mute, 1); isMuted = true; }
	unMute { synth.set(\mute, 0); isMuted = false; }
	tglMute { if(isMuted, { this.unMute }, { this.mute }) }

	setSendVol { |sendNum = 0, db = 0|
		if( sends == true, {
			var sendSymbol = ("send" ++ sendNum ++ "Amp").asSymbol;
			synth.set(sendSymbol, db.dbamp);
			switch(sendNum,
				0, { send0Vol = db },
				1, { send1Vol = db },
				2, { send2Vol = db },
				3, { send3Vol = db });
		});
	}

	muteSend { | sendNum = 0 |
		switch(sendNum,
			0, { synth.set(\send0Mute, 0) },
			1, { synth.set(\send1Mute, 0) },
			2, { synth.set(\send2Mute, 0) },
			3, { synth.set(\send3Mute, 0) });
	}

	unMuteSend { | sendNum = 0 |
		if( sends == true, {
			switch(sendNum,
				0, { synth.set(\send0Mute, 1) },
				1, { synth.set(\send1Mute, 1) },
				2, { synth.set(\send2Mute, 1) },
				3, { synth.set(\send3Mute, 1) });
		});
	}

	mapSend0Amp { | bus | if(sends == true, { synth.set(\send0Amp, bus.asMap);}); }
	mapSend1Amp { | bus | if(sends == true, { synth.set(\send1Amp, bus.asMap); }); }
	mapSend2Amp { | bus | if(sends == true, { synth.set(\send2Amp, bus.asMap);}); }
	mapSend3Amp { | bus | if(sends == true, { synth.set(\send3Amp, bus.asMap);}); }

	setSendPre {
		if(sends == true, {
			preOrPost = 'pre';
			synth.set(\preOrPost, 0)
		});
	}
	setSendPost {
		if(sends == true, {
			preOrPost = 'post';
			synth.set(\preOrPost, 1);
		});
	}

	setPanBal { |pan = 0|
		if(sends == true, {
			panBal = pan;
			synth.set(\panBal, panBal)
		});
	}

	setOutBus { |out = 0| synth.set(\outBus, out) }

	mapAmp { | bus | synth.set(\amp, bus.asMap); }
	mapPan { | bus | synth.set(\panBal, bus.asMap); }

	// set {}    // set all parameters of a channel strip (convenience that calls other functions)

	free {
		this.mute;

		synth.free;
		inBus.free;

		synth = nil;
		inBus = nil;
		isMuted = nil;
		isFeedback = nil;
	}
}