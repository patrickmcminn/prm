/*
Sunday, April 16th 2017
False Self Ohm.scd
prm
*/

var page = 'falseSelf';

var mainGridBank = 0;


~ohm.makePage(page);

~ohm.addGridBanks(3, page: page);

~ohm.addPageLoadFunction(\mainGlobalMixer, { ~ohm.storageDict[\loadGlobalSubmixerFunc].value; }, page);
//////// Metronome:
~ohm.addPageLoadFunction(\metronome, {
  ~falseSelf.clock.schedAbs(~falseSelf.clock.beats.ceil, { | beat |
    //~falseSelf.clock.beatInBar.postln;
    ~falseSelf.clock.playNextBar({
      ~ohm.turnControlButtonWhite(1, 0, page: page);
      { ~ohm.turnControlButtonOff(1, 0, page: page); }.defer(0.1);
    });
    ~ohm.turnControlButtonGreen(2, 0, page: page);
    { ~ohm.turnControlButtonOff(2, 0, page: page); }.defer(0.1);
    //beat.postln;
    1;
  },page);
},page: page);

//////// main grid:

~ohm.turnGridBlue(0, 1, bank: mainGridBank, page: page);
~ohm.setGridFunc(0, 1, { ~falseSelf.startSong; }, bank: mainGridBank, page: page);


~ohm.setPage('falseSelf');
~base.setPage('falseSelf');


~falseSelf.clock.schedAbs(~falseSelf.clock.beats.ceil, { | beat | beat.postln; 1 });

~falseSelf.clock.tempo = 160/60

~falseSelf.clock.schedAbs(~falseSelf.clock.nextBar, { ~falseSelf.clock.beatsPerBar_(4) });

~falseSelf.startSong;

~falseSelf.mixerA.tglMute(0);

