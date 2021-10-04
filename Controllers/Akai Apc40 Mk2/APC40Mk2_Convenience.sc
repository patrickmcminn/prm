/*
Thursday, July 23 2020
APC40Mk2_Convenience.sc
prm


*/

+ APC40Mk2 {

	mapSendBanks { | name, send0Bank, send1Bank, send2Bank, send3Bank, deviceButtonsBank, page = 'active' |

		this.setDeviceButtonsMonitorFunc(name.asSymbol, {
			if( this.activeMixerEncodersBank == (send0Bank),
				{ this.turnDeviceButtonOn(1, deviceButtonsBank, page) }, { ~apc.turnDeviceButtonOff(1, deviceButtonsBank, page) });
			if( this.activeMixerEncodersBank  == (send1Bank),
				{ this.turnDeviceButtonOn(2, deviceButtonsBank, page) }, { ~apc.turnDeviceButtonOff(2, deviceButtonsBank, page) });
			if( this.activeMixerEncodersBank  == (send2Bank),
				{ this.turnDeviceButtonOn(3, deviceButtonsBank, page) }, { ~apc.turnDeviceButtonOff(3, deviceButtonsBank, page) });
			if( this.activeMixerEncodersBank == (send3Bank),
				{ this.turnDeviceButtonOn(4, deviceButtonsBank, page) }, { ~apc.turnDeviceButtonOff(4, deviceButtonsBank, page) });
		}, bank: deviceButtonsBank, page: page);

		this.setDeviceButtonFunc(1, { this.setActiveMixerEncodersBank(send0Bank); }, bank: deviceButtonsBank, page: page);
		this.setDeviceButtonFunc(2, { this.setActiveMixerEncodersBank(send1Bank);  }, bank: deviceButtonsBank, page: page);
		this.setDeviceButtonFunc(3, { this.setActiveMixerEncodersBank(send2Bank); }, bank: deviceButtonsBank, page: page);
		this.setDeviceButtonFunc(4, { this.setActiveMixerEncodersBank(send3Bank); }, bank: deviceButtonsBank, page: page);
	}

	mapMixer {
		| mixer, numChans = 8, chanOffset = 0, surfaceOffset = 0,
		send0Bank, send1Bank, send2Bank, send3Bank, mixerBank, page = 'active' |

		numChans.do({ | i |
			var surfaceMod = i + surfaceOffset + 1;
			var chanMod = i + chanOffset;

			// volumes:
			this.setFaderFunc(surfaceMod, { | val | (mixer.interpret).setVol(chanMod, val.ccdbfs); }, bank: mixerBank, page: page);

			// toggle mutes:
			this.setTrackSelectFunc(surfaceMod, { (mixer.interpret).tglMute(chanMod) }, bank: mixerBank, page: page);
			this.setMixerMonitorFunc("mute"++(mixer.asSymbol++surfaceMod++chanMod).asSymbol, {
				if( (mixer.interpret).isMuted(chanMod),
					{ this.turnTrackSelectButtonOff(surfaceMod) }, { this.turnTrackSelectButtonOn(surfaceMod); });
			}, bank: mixerBank, page: page);
			// sends pre/post:
			this.setCrossfaderSelectFunc(surfaceMod, {
				if( (mixer.interpret).preOrPost(chanMod) == 'post',
					{ (mixer.interpret).setSendPre(chanMod) }, { (mixer.interpret).setSendPost(chanMod) });
			}, bank: mixerBank, page: page);
			this.setMixerMonitorFunc("preOrPost"++(mixer.asSymbol++surfaceMod++chanMod).asSymbol, {
				if((mixer.interpret).preOrPost(chanMod) == 'post',
					{ this.turnCrossfaderSelectButtonOrange(surfaceMod) },
					{ this.turnCrossfaderSelectButtonYellow(surfaceMod) });
			}, bank: mixerBank, page: page);
			//// sends:
			this.setMixerEncoderFunc(surfaceMod, { | val |
				(mixer.interpret).setSendVol(chanMod, 0, val.ccdbfs); }, bank: send0Bank, page: page);
			this.setMixerEncoderFunc(surfaceMod, { | val |
				(mixer.interpret).setSendVol(chanMod, 1, val.ccdbfs); }, bank: send1Bank, page: page);
			this.setMixerEncoderFunc(surfaceMod, { | val |
				(mixer.interpret).setSendVol(chanMod, 2, val.ccdbfs); }, bank: send2Bank, page: page);
			this.setMixerEncoderFunc(surfaceMod, { | val |
				(mixer.interpret).setSendVol(chanMod, 3, val.ccdbfs); }, bank: send3Bank, page: page);

			this.setMixerEncodersMonitorFunc("send"++(mixer.asSymbol++surfaceMod++chanMod).asSymbol, {
				this.setMixerEncoderValue(surfaceMod, (mixer.interpret).sendVol(chanMod, 0).dbfsCC); }, bank: send0Bank, page: page);
			this.setMixerEncodersMonitorFunc("send"++(mixer.asSymbol++surfaceMod++chanMod).asSymbol, {
				this.setMixerEncoderValue(surfaceMod, (mixer.interpret).sendVol(chanMod, 1).dbfsCC); }, bank: send1Bank, page: page);
			this.setMixerEncodersMonitorFunc("send"++(mixer.asSymbol++surfaceMod++chanMod).asSymbol, {
				this.setMixerEncoderValue(surfaceMod, (mixer.interpret).sendVol(chanMod, 2).dbfsCC); }, bank: send2Bank, page: page);
			this.setMixerEncodersMonitorFunc("send"++(mixer.asSymbol++surfaceMod++chanMod).asSymbol, {
				this.setMixerEncoderValue(surfaceMod, (mixer.interpret).sendVol(chanMod, 3).dbfsCC); }, bank: send3Bank, page: page);
		});

	}

}