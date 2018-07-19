/*
Sunday, July 8th 2018
APC40Mk2_Page_Banks.sc
prm
*/


/*

////// Grid Bank Array:
// slot 0 - note on func
// slot 1 - note off func
// slot 2 - color

*/
/*
////// Mixer Bank Array:
//// plane 1:
slot 0 - mixer func
slot 1 - track select
slot 2 - track activator
slot 3 - crossfade select
slot 4 - solo select
slot 5 - record enable

//// plane 3:
// note on func
// note off func
// color

*/

+ APC40Mk2_Page {

  prMakeBanks { | numBanks = 1 |
    activeGridBnk = 0; activeSceneLaunchBnk = 0; activeClipStopBnk = 0;
    activeMixerBnk = 0; activeMixerEncoderBnk = 0; activeDeviceEncoderBnk = 0;
    activeDeviceButtonsBnk = 0; activeControlButtonsBnk = 0;

    gridBankMonitorFuncArray = Array.new;
    sceneLaunchBankMonitorFuncArray = Array.new;
    clipStopBankMonitorFuncArray = Array.new;
    mixerBankMonitorFuncArray = Array.new;
    mixerEncoderBankMonitorFuncArray = Array.new;
    deviceEncoderBankMonitorFuncArray = Array.new;
    deviceButtonsBankMonitorFuncArray = Array.new;
    controlButtonsBankMonitorFuncArray = Array.new;

    gridBankArray = Array.new;
    this.addGridBanks(numBanks);
    sceneLaunchBankArray = Array.new;
    this.addSceneLaunchBanks(numBanks);
    clipStopBankArray = Array.new;
    this.addClipStopBanks(numBanks);
    mixerBankArray = Array.new;
    this.addMixerBanks(numBanks);

    mixerEncoderBankArray = Array.new;
    this.addMixerEncoderBanks(numBanks);
    deviceEncoderBankArray = Array.new;
    this.addDeviceEncoderBanks(numBanks);
    deviceButtonsBankArray = Array.new;
    this.addDeviceButtonsBanks(numBanks);
    controlButtonsBankArray = Array.new;
    this.addControlButtonsBanks(numBanks);

  }

  addGridBanks { | numBanks = 1 |
    numBanks.do({
      gridBankArray = gridBankArray.add(Array.fill2D(40, 3, nil));
      gridBankArray[gridBankArray.size-1].do({ | item, index |
        item[0] = { };
        item[1] = { };
        item[2] = 0;
      });
      gridBankMonitorFuncArray = gridBankMonitorFuncArray.add(IdentityDictionary.new);
    });
  }

  addSceneLaunchBanks { | numBanks = 1 |
    numBanks.do({
      sceneLaunchBankArray = sceneLaunchBankArray.add(Array.fill2D(5, 3, nil));
      sceneLaunchBankArray[sceneLaunchBankArray.size-1].do({ | item, index |
        item[0] = { };
        item[1] = { };
        item[2] = 0;
      });
      sceneLaunchBankMonitorFuncArray = sceneLaunchBankMonitorFuncArray.add(IdentityDictionary.new);
    });
  }

  addClipStopBanks { | numBanks = 1 |
    numBanks.do({
      clipStopBankArray = clipStopBankArray.add(Array.fill2D(9, 3, nil));
      clipStopBankArray[clipStopBankArray.size-1].do({ | item, index |
        item[0] = { };
        item[1] = { };
        item[2] = 0;
      });
      clipStopBankMonitorFuncArray = clipStopBankMonitorFuncArray.add(IdentityDictionary.new);
    });
  }

  addMixerBanks { | numBanks = 1 |
    /*
    ////// Mixer Bank Array:
    //// plane 1:
    slot 0 - mixer func
    slot 1 - track select
    slot 2 - track activator
    slot 3 - crossfade select
    slot 4 - solo select
    slot 5 - record enable

    //// plane 3:
    // note on func
    // note off func
    // color
    */
    numBanks.do({
      mixerBankArray = mixerBankArray.add(Array.fill3D(6, 9, 3, nil));
      mixerBankArray[mixerBankArray.size-1].do({ | plane |
        plane.do({ | item |
            item[0] = { };
            item[1] = { };
            item[2] = 0;
          });
        });
      mixerBankMonitorFuncArray = mixerBankMonitorFuncArray.add(IdentityDictionary.new);
    });
  }

  addMixerEncoderBanks { | numBanks = 1 |
    numBanks.do({
      mixerEncoderBankArray = mixerEncoderBankArray.add(Array.fill2D(9, 2, nil));
      mixerEncoderBankArray[mixerEncoderBankArray.size-1].do({ | item, index |
        item[0] = { };
        item[1] = 0;
      });
      mixerEncoderBankMonitorFuncArray = mixerEncoderBankMonitorFuncArray.add(IdentityDictionary.new);
    });
  }

  addDeviceEncoderBanks { | numBanks = 1 |
    numBanks.do({
      deviceEncoderBankArray = deviceEncoderBankArray.add(Array.fill2D(10, 2, nil));
      deviceEncoderBankArray[deviceEncoderBankArray.size-1].do({ | item, index |
        item[0] = { };
        item[1] = 0;
      });
      deviceEncoderBankMonitorFuncArray = deviceEncoderBankMonitorFuncArray.add(IdentityDictionary.new);
    });
  }

  addDeviceButtonsBanks { | numBanks = 1 |
    numBanks.do({
      deviceButtonsBankArray = deviceButtonsBankArray.add(Array.fill2D(14, 3, nil));
      deviceButtonsBankArray[deviceButtonsBankArray.size-1].do({ | item, index |
        item[0] = { };
        item[1] = { };
        item[2] = 0;
      });
      deviceButtonsBankMonitorFuncArray = deviceButtonsBankMonitorFuncArray.add(IdentityDictionary.new);
    });
  }

  addControlButtonsBanks { | numBanks = 1 |
    numBanks.do({
      controlButtonsBankArray = controlButtonsBankArray.add(Array.fill2D(10, 3, nil));
      controlButtonsBankArray[controlButtonsBankArray.size-1].do({ | item, index |
        item[0] = { };
        item[1] = { };
        item[2] = 0;
      });
      controlButtonsBankMonitorFuncArray = controlButtonsBankMonitorFuncArray.add(IdentityDictionary.new);
    });
  }

  //////// Setting banks:

  setActiveGridBank { | bank = 0 |
    gridBankMonitorFuncArray[activeGridBnk].do({ | r | r.stop; });
    activeGridBnk = bank;
    40.do({ | index |
      this.prSetGridFunc(index, 'noteOn', gridBankArray[activeGridBnk][index][0]);
      this.prSetGridFunc(index, 'noteOff', gridBankArray[activeGridBnk][index][1]);
      this.prTurnGridColor(index, gridBankArray[activeGridBnk][index][2]);
    });
    gridBankMonitorFuncArray[activeGridBnk].do({ | r | r.reset.play; });
  }

  setActiveSceneLaunchBank { | bank = 0 |
    sceneLaunchBankMonitorFuncArray[activeSceneLaunchBnk].do({ | r | r.stop; });
    activeSceneLaunchBnk = bank;
    5.do({ | index |
      this.prSetSceneLaunchFunc(index, 'noteOn', gridBankArray[activeSceneLaunchBnk][index][0]);
      this.prSetSceneLaunchFunc(index, 'noteOff', gridBankArray[activeSceneLaunchBnk][index][1]);
      this.prSetSceneLaunchColor(index, gridBankArray[activeSceneLaunchBnk][index][2]);
    });
    sceneLaunchBankMonitorFuncArray[activeSceneLaunchBnk].do({ | r | r.reset.play; });
  }

  setActiveClipStopBank { | bank = 0 |
    clipStopBankMonitorFuncArray[activeClipStopBnk].do({ | r | r.stop; });
    activeClipStopBnk = bank;
    9.do({ | index |
      this.prSetClipStopFunc(index, 'noteOn', clipStopBankArray[activeClipStopBnk][index][0]);
      this.prSetClipStopFunc(index, 'noteOff', clipStopBankArray[activeClipStopBnk][index][1]);
      this.prSetClipStopColor(index, clipStopBankArray[activeClipStopBnk][index][2]);
    });
    clipStopBankMonitorFuncArray[activeClipStopBnk].do({ | r | r.reset.play; });
  }

  setActiveMixerBank { | bank = 0 |
    mixerBankMonitorFuncArray[activeMixerBnk].do({ | r | r.stop; });
    activeMixerBnk = bank;
    8.do({ | index |
      this.prSetFaderFunc(index, mixerBankArray[activeMixerBnk][0][index][0]);

      this.prSetTrackSelectFunc(index, 'noteOn', mixerBankArray[activeMixerBnk][1][index][0]);
      this.prSetTrackSelectFunc(index, 'noteOff', mixerBankArray[activeMixerBnk][1][index][1]);
      this.prSetTrackSelectColor(index, mixerBankArray[activeMixerBnk][1][index][2]);

      this.prSetTrackActivatorFunc(index, 'noteOn', mixerBankArray[activeMixerBnk][2][index][0]);
      this.prSetTrackActivatorFunc(index, 'noteOff', mixerBankArray[activeMixerBnk][2][index][1]);
      this.prSetTrackActivatorColor(index, mixerBankArray[activeMixerBnk][2][index][2]);

      this.prSetCrossfaderSelectFunc(index, 'noteOn', mixerBankArray[activeMixerBnk][3][index][0]);
      this.prSetCrossfaderSelectFunc(index, 'noteOff', mixerBankArray[activeMixerBnk][3][index][1]);
      this.setCrossfaderSelectButtonColor(index, mixerBankArray[activeMixerBnk][3][index][2]);

      this.prSetSoloFunc(index, 'noteOn', mixerBankArray[activeMixerBnk][4][index][0]);
      this.prSetSoloFunc(index, 'noteOff', mixerBankArray[activeMixerBnk][4][index][1]);
      this.prSetSoloButtonColor(index, mixerBankArray[activeMixerBnk][4][index][2]);

      this.prSetRecordEnableFunc(index, 'noteOn', mixerBankArray[activeMixerBnk][5][index][0]);
      this.prSetRecordEnableFunc(index, 'noteOff', mixerBankArray[activeMixerBnk][5][index][1]);
      this.prSetRecordEnableButtonColor(index, mixerBankArray[activeMixerBnk][5][index][2]);
    });
    mixerBankMonitorFuncArray[activeMixerBnk].do({ | r | r.reset.play; });
  }

  setActiveMixerEncoderBank { | bank = 0 |
    mixerEncoderBankMonitorFuncArray[activeMixerEncoderBnk].do({ | r | r.stop; });
    activeMixerEncoderBnk = bank;
    9.do({ | index |
      this.prSetMixerEncoderFunc(index, mixerEncoderBankArray[activeMixerEncoderBnk][index][0]);
      this.prSetMixerEncoderValue(index, mixerEncoderBankArray[activeMixerEncoderBnk][index][1]);
    });
    mixerEncoderBankMonitorFuncArray[activeMixerEncoderBnk].do({ | r | r.reset.play; });
  }

  setActiveDeviceEncoderBank { | bank = 0 |
    deviceEncoderBankMonitorFuncArray[activeDeviceEncoderBnk].do({ | r | r.stop; });
    activeDeviceEncoderBnk = bank;
    9.do({ | index |
      this.prSetDeviceEncoderFunc(index, deviceEncoderBankArray[activeDeviceEncoderBnk][index][0]);
      this.prSetDeviceEncoderValue(index, deviceEncoderBankArray[activeDeviceEncoderBnk][index][0]);
    });
    deviceEncoderBankMonitorFuncArray[activeDeviceEncoderBnk].do({ | r | r.reset.play; });
  }

  setActiveDeviceButtonsBank { | bank = 0 |
    deviceButtonsBankMonitorFuncArray[activeDeviceButtonsBnk].do({ | r | r.stop; });
    activeDeviceButtonsBnk = bank;
    14.do({ | index |
      this.prSetDeviceButtonFunc(index, 'noteOn', deviceButtonsBankArray[activeDeviceButtonsBnk][index][0]);
      this.prSetDeviceButtonFunc(index, 'noteOff', deviceButtonsBankArray[activeDeviceButtonsBnk][index][1]);
      this.prSetDeviceButtonColor(index, deviceButtonsBankArray[activeDeviceButtonsBnk][index][2]);
    });
    deviceButtonsBankMonitorFuncArray[activeDeviceButtonsBnk].do({ | r | r.reset.play; });
  }

  setActiveControlButtonsBank { | bank = 0 |
    controlButtonsBankMonitorFuncArray[activeControlButtonsBnk].do({ | r | r.stop; });
    activeControlButtonsBnk = bank;
    10.do({ | index |
      this.prSetControlButtonFunc(index, 'noteOn', controlButtonsBankArray[activeControlButtonsBnk][index][0]);
      this.prSetControlButtonFunc(index, 'noteOff', controlButtonsBankArray[activeControlButtonsBnk][index][1]);
      this.prSetControlButtonColor(index, controlButtonsBankArray[activeControlButtonsBnk][index][2]);
    });
    controlButtonsBankMonitorFuncArray[activeControlButtonsBnk].do({ | r | r.reset.play; });
  }

  //////// bank monitor routines:
  stopActiveBankMonitorRoutines {
    gridBankMonitorFuncArray[activeGridBnk].do({ |r | r.stop; });
    sceneLaunchBankMonitorFuncArray[activeSceneLaunchBnk].do({ | r | r.stop; });
    clipStopBankMonitorFuncArray[activeClipStopBnk].do({ | r | r.stop; });
    mixerBankMonitorFuncArray[activeMixerBnk].do({ | r | r.stop; });
    mixerEncoderBankMonitorFuncArray[activeMixerEncoderBnk].do({ | r | r.stop; });
    deviceEncoderBankMonitorFuncArray[activeDeviceEncoderBnk].do({ | r | r.stop; });
    deviceButtonsBankMonitorFuncArray[activeDeviceButtonsBnk].do({ | r | r.stop; });
    controlButtonsBankMonitorFuncArray[activeControlButtonsBnk].do({ |r | r.stop; });
  }

  startActiveBankMonitorRoutines {
    gridBankMonitorFuncArray[activeGridBnk].do({ |r | r.reset.play; });
    sceneLaunchBankMonitorFuncArray[activeSceneLaunchBnk].do({ | r | r.reset.play; });
    clipStopBankMonitorFuncArray[activeClipStopBnk].do({ | r | r.reset.play; });
    mixerBankMonitorFuncArray[activeMixerBnk].do({ | r | r.reset.play; });
    mixerEncoderBankMonitorFuncArray[activeMixerEncoderBnk].do({ | r | r.reset.play; });
    deviceEncoderBankMonitorFuncArray[activeDeviceEncoderBnk].do({ | r |r.reset.play; });
    deviceButtonsBankMonitorFuncArray[activeDeviceButtonsBnk].do({ | r | r.reset.play;});
    controlButtonsBankMonitorFuncArray[activeControlButtonsBnk].do({ |r |r.reset.play; });
  }


}


+ APC40Mk2_Page {

  //////// bank sizes:
  numBanks { | type = 'grid' |
    switch(type,
      { 'grid' }, { ^gridBankArray.size; },
      { 'sceneLaunch' }, { ^sceneLaunchBankArray.size; },
      { 'clipStop' }, { ^clipStopBankArray.size; },
      { 'mixer' }, { ^mixerBankArray.size; },
      { 'mixerEncoder' }, { ^mixerEncoderBankArray.size },
      { 'deviceEncoder' }, { ^deviceEncoderBankArray.size },
      { 'deviceButtons' }, { ^deviceButtonsBankArray.size },
      { 'controlButtons' }, { ^controlButtonsBankArray.size }
    );
  }

  numGridBanks { this.numBanks('grid') }
  numSceneLaunchBanks { this.numBanks('sceneLaunch'); }
  numClipStopBanks { this.numBanks('clipStop'); }
  numMixerBanks { this.numBanks('mixer') }
  numMixerEncoderBanks { this.numBanks('mixerEncoder'); }
  numDeviceEncoderBanks { this.numBanks('deviceEncoder') }
  numDeviceButtonsBanks { this.numBanks('deviceButtons') }
  numControlButtonsBanks { this.numBanks('controlButtons') }

}



/*
x = Array2D.new(40, 2)
y = Array.fill(13, { | i  | i + 7});


z.at(0)

x[0, 1] = 12
z = [x, y]

z[0].at(0, 1) = 0

x = Array.fill3D(5, 8, 3, { nil });

x[3][0][0] = 4


x[3][0][0]

*/