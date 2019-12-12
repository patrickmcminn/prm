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
  //~apc.setPlayButtonFunc({ try { ~base.setPage(~apc.activePageKey); } });
	~apc.setPlayButtonFunc({ if( ~base.isPage(~apc.activePageKey),
		{ ~base.setPage(~apc.activePageKey) }, { "no such page".postln; });
	});

  //////// Master Volume:
  ~apc.setMasterFaderFunc({ | val |
    ~prm.systemMixer.setVol(0, val.ccdbfs);
  });

};