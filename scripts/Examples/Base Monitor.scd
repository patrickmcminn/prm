x = Base.new("iConnectAudio4+", "HST3");


x.turnGridBlue(0, 0);

x.makePage('other');




x.setGridMonitorFunc(0, 0, {
  if( ~test == true, { x.turnGridGreen(0, 0); }, { x.turnGridRed(0, 0) }); });


x.setGridMonitorFunc(3, 0, {
  if( ~test == true, { x.turnGridGreen(3, 0); }, { x.turnGridRed(3, 0) }) }, page: 'other');

~test = false;
~test = true;


x.addGridBanks(3);

x.setActiveGridBank(0);

x.setPage('main');


x = Base_Page.n

x.stopActiveBankMonitorRoutines;

~yeahOk = 127;

~yeahOk = 7

~ohm = OhmRGB.new("iConnectAudio4+", "HST1");

~ohm.setLeftKnobFunc(0, 0, { | val | ~yeahOk = val; val.postln });

x.setFaderMonitorFunc(1, { x.setFaderValue(1, ~yeahOk); }, page: 'main');

x.setFaderMonitorFunc(2, { x.setFaderValue(2, 127-~yeahOk); }, page: 'other');

x.setFaderMode(2, 'greenFill');


x.setControlButtonMonitorFunc(1, { if ( ~test == true, { x.turnControlButtonBlue(1, 'left') }, { x.turnControlButtonRed(1, 'left'); })}, page: 'main');

x.setControlButtonMonitorFunc(1, { });


x.turnControlButtonGreen(2, 'both');


x.setTouchButtonMonitorFunc(1, {
  if ( ~test == true, { x.turnTouchButtonBlue(1, 'middle'); }, { x.turnTouchButtonRed(1, 'middle'); });
}, page: 'main');

x.turnControlButtonWhite(1, 'right');

    x.turnTouchButtonBlue(1);

x.turnButtonColor(18, 'green');

x.turnControlButtonYellow(3, 'right');

x.activePage.touchButtonsBankArray;