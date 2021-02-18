/*
Monday Februrary 1st 2021
Plague2.scd
prm

Plague Songs 2
*/

~group = Group.new(~prm.procGroup, \addToHead);
~sequencer = ~pyr.uid

~mixer = IM_Mixer.new(8, ~prm.audioIn, ~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,
	false, ~group, \addToHead);


~mixer.setSendVol(0, 0, -12);
~mixer.setSendVol(0, 1, -6);
~mixer.setSendVol(0, 2, -inf);
~mixer.setSendVol(0, 1, -inf);

~mixer.setVol(0, -9);


~tptEQ = Equalizer.newStereo(~mixer.chanStereo, ~group, \addToHead);
~tptShift = IM_MultiShift.new(~tptEQ.inBus, [-14, -7, 7], 0.9, ~group, \addToHead);
~tptInput = IM_HardwareIn.new(~pickupIn, ~tptShift.inBus, ~group, \addToHead);

~tptEQ.setHighPassCutoff(200);
~tptEQ.setLowPassCutoff(1200);


~tptShift.setShiftArray([7, -7, -14]);
~tptShift.setShiftArray([-10, -3, 7]);
~tptShift.setShiftArray([-10, -3, 7]);

~tptShift.setShiftArray([-2, 2, 3]);
~tptShift.setShiftArray([-2, 3, 7]);

~tptShift.setShiftArray([-12, -10, -9]);

~mixer.tglMute(0);

~midiVolArray = Array.newClear(4);
~midiVolArray[0] = MIDIFunc.cc({ | val | ~tptShift.setShiftVol(0, val.ccdbfs) }, 1, 0, ~sequencer);
~midiVolArray[1] = MIDIFunc.cc({ | val | ~tptShift.setShiftVol(1, val.ccdbfs) }, 2, 0, ~sequencer);
~midiVolArray[2] = MIDIFunc.cc({ | val | ~tptShift.setShiftVol(2, val.ccdbfs) }, 3, 0, ~sequencer);
~midiVolArray[3] = MIDIFunc.cc({ | val | ~tptShift.setDryVol(val.ccdbfs) }, 4, 0, ~sequencer);



~tptShift.free;
~tptInput.free;



~prm.reverb.makeGUI;

(
~p2 = Plague2.new(~prm.audioIn, ~micIn, ~pickupIn, ~pyr, ~modOutput[0], ~modOutput[1], ~modOutput[4], ~modOutput[5], ~modOutput[6], ~modOutput[7], ~modInput[0], ~modInput[1], ~modInput[2], ~modInput[3],~prm.reverb.inBus, ~prm.granulator.inBus, ~prm.modularSend.inBus, ~prm.delay.inBus,~prm.procGroup, \addToHead);
)

~p2.isLoaded;

~p2.subtractive.playNote(220);
~p2.subtractive.releaseNote(220);

~p2.tptShift;
~p2.isLoaded;

~p2.mixer.tglMute(3)

~p2.subtractive.readPreset('plague2-3');

~apc.setPage('plague2');

-3.dbfsCC;

s.meter

~p2.tptEQ.mixer.tglMute;

~p2.mixer.setSendVol(2, 0, -12);

~prm.reverb.makeGUI;


