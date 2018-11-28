/*
Tuesday, November 27th 2018
BeatstepPro_Page Banks.sc
prm
*/

/*
Sequencer + Drum Bank Arrays:
// slot 0 - note on func
// slot 1 - note off func


Control Bank Arrays:
// slot 0 - control button func
// slot 1 - control encoder func

// other plane:
// note on func
// note off func
*/

+BeatstepPro_Page {

	prMakeBanks { | numBanks = 1 |
		sequencer1BankArray = Array.new;
		this.addSequencer1Banks(numBanks);
		sequencer2BankArray = Array.new;
		this.addSequencer2Banks(numBanks);
		drumBankArray = Array.new;
		this.addDrumBanks(numBanks);
		controlBankArray = Array.new;
		this.addControlBanks(numBanks);
	}

	numBanks { | type |
		switch(type,
			\sequencer1, { ^sequencer1BankArray.size },
			\sequencer2, { ^sequencer2BankArray.size },
			\drum, { ^drumBankArray.size },
			\control, { ^controlBankArray.size }
		);
	}

	numSequencer1Banks { this.numBanks(\sequencer1); }
	numSequencer2Banks { this.numBanks(\sequencer2) }
	numDrumBanks { this.numBanks(\drum); }
	numControlBanks { this.numBanks(\control); }

	addSequencer1Banks { | numBanks = 1 |
		numBanks.do({
			sequencer1BankArray = sequencer1BankArray.add(Array.fill2D(84, 2, nil));
			sequencer1BankArray[sequencer1BankArray.size-1].do({ | item |
				item[0] = { };
				item[1] = { };
			});
		});
	}

	addSequencer2Banks { | numBanks = 1 |
		numBanks.do({
			sequencer2BankArray = sequencer2BankArray.add(Array.fill2D(84, 2, nil));
			sequencer2BankArray[sequencer2BankArray.size-1].do({ | item |
				item[0] = { };
				item[1] = { };
			});
		});
	}

	addDrumBanks { | numBanks = 1 |
		numBanks.do({
			drumBankArray = drumBankArray.add(Array.fill2D(84, 2, nil));
			drumBankArray[drumBankArray.size-1].do({ | item |
				item[0] = { };
				item[1] = { };
			});
		});
	}

	addControlBanks { | numBanks = 1 |
		numBanks.do({
			controlBankArray = controlBankArray.add(Array.fill3D(2, 16, 2, nil));
			controlBankArray[controlBankArray.size-1].do({ | plane |
				plane.do({ | item |
					item[0] = { };
					item[1] = { };
				});
			});
		});
	}

	//////// Setting Banks:

	setActiveSequencer1Bank { | bank = 0 |
		activeSequencer1Bnk = bank;
		84.do({ | index |
			this.prSetSequencer1Func(index, 'noteOn',
				sequencer1BankArray[activeSequencer1Bnk][index][0]);
			this.prSetSequencer1Func(index, 'noteOff',
				sequencer1BankArray[activeSequencer1Bnk][index][1]);
		});
	}

	setActiveSequencer2Bank { | bank = 0 |
		activeSequencer2Bnk = bank;
		84.do({ | index |
			this.prSetSequencer2Func(index, 'noteOn',
				sequencer2BankArray[activeSequencer2Bnk][index][0]);
			this.prSetSequencer2Func(index, 'noteOff',
				sequencer2BankArray[activeSequencer2Bnk][index][1]);
		});
	}

	setActiveSequencer2Bank { | bank = 0 |
		activeSequencer2Bnk = bank;
		84.do({ | index |
			this.prSetSequencer2Func(index, 'noteOn',
				sequencer2BankArray[activeSequencer2Bnk][index][0]);
			this.prSetSequencer2Func(index, 'noteOff',
				sequencer2BankArray[activeSequencer2Bnk][index][1]);
		});
	}

	setActiveDrumBank { | bank = 0 |
		activeDrumBnk = bank;
		16.do({ | index |
			this.prSetDrumButtonFunc(index, 'noteOn',
				drumBankArray[activeDrumBnk][index][0]);
			this.prSetDrumButtonFunc(index, 'noteOff',
				drumBankArray[activeDrumBnk][index][1]);
		});
	}

	setActiveControlBank { | bank = 0 |
		activeControlBnk = bank;
		16.do({ | index |
			this.prSetControlButtonFunc(index, 'noteOn',
				controlBankArray[activeControlBnk][0][index][0]);
			this.prSetControlButtonFunc(index, 'noteOff',
				controlBankArray[activeControlBnk][0][index][1]);
			this.prSetControlEncoderFunc(index,
				controlBankArray[activeControlBnk][1][index][0]);
		});
	}


}