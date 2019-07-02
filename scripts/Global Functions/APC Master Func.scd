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
  ~apc.setPlayButtonFunc({ try { ~base.setPage(~apc.activePageKey); } });

  //////// Master Volume:
  ~apc.setMasterFaderFunc({ | val |
    ~prm.systemMixer.setVol(0, val.ccdbfs);
  });

};