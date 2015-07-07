~base.setPage('cortez');
~base.setPage('dauphine');

s.meter

s.makeWindow;

Server.killAll;


(
~dauphine = DauphineStreet.new(~prm.submixA, ~prm.submixB, ~prm.submixC, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, nil,
  ~prm.procGroup, \addToHead);
)

~prm.procGroup;

~dauphine.free;