/*
Tuesday, August 28th 2018
APC Master Func.scd
prm
*/

~apc.storageDict[\loadMasterFunc] = {

  // go home:
  ~apc.turnSessionButtonOn;
  ~apc.setSessionButtonFunc({ ~apc.setPage('main') });
  // go back a page:
  ~apc.turnRecordButtonOn;
  ~apc.setRecordButtonFunc({ ~apc.setToPreviousPage; });
  // bring Base over:
  ~apc.turnPlayButtonOn;
  ~apc.setPlayButtonFunc({ ~base.setPage(~apc.activePageKey); });

  //////// Master Volume:
  ~apc.setMasterFaderFunc({ | val |
    ~prm.systemMixer.setVol(val.ccdbfs);
  });

  ////// Sends:

  //// send bank changes:
  ~apc.setDeviceButtonsMonitorFunc(\sends, {
    if( ~apc.activeMixerEncodersBank == send0,
      { ~apc.turnDeviceButtonOn(1) }, { ~apc.turnDeviceButtonOff(1) });
    if( ~apc.activeMixerEncodersBank  == send1,
      { ~apc.turnDeviceButtonOn(2) }, { ~apc.turnDeviceButtonOff(2) });
    if( ~apc.activeMixerEncodersBank  == send2,
      { ~apc.turnDeviceButtonOn(3) }, { ~apc.turnDeviceButtonOff(3) });
    if( ~apc.activeMixerEncodersBank == send3,
      { ~apc.turnDeviceButtonOn(4) }, { ~apc.turnDeviceButtonOff(4) });
  });

  ~apc.setDeviceButtonFunc(1, { ~apc.setActiveMixerEncodersBank(send0, page); });
  ~apc.setDeviceButtonFunc(2, { ~apc.setActiveMixerEncodersBank(send1, page);  });
  ~apc.setDeviceButtonFunc(3, { ~apc.setActiveMixerEncodersBank(send2, page); });
  ~apc.setDeviceButtonFunc(4, { ~apc.setActiveMixerEncodersBank(send3, page); });

};