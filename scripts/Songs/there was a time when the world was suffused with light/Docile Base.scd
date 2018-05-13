/*
Wednesday, May 2nd 2018
Docile Base.scd
prm

to compress the world to a docile state
*/

var page = 'docile';

~base.makePage(page);

////////////////
//// Grid: ////
//////////////

/////////// noisies:
/*
blip = noisies.sampler.bufferArray[0];
blipLong = noisies.sampler.bufferArray[1];
white = noisies.sampler.bufferArray[2];
purple = noisies.sampler.bufferArray[3];
pink = noisies.sampler.bufferArray[4];
gray = noisies.sampler.bufferArray[5];
brown = noisies.sampler.bufferArray[6];
blue = noisies.sampler.bufferArray[7];
*/

//// initial conditions:
~base.turnGridWhite(0, 0, page: page);
~base.turnGridWhite(1, 0, page: page);
~base.turnGridWhite(0, 1, page: page);
~base.turnGridWhite(1, 1, page: page);
~base.turnGridWhite(2, 1, page: page);
~base.turnGridWhite(0, 2, page: page);
~base.turnGridWhite(1, 2, page: page);
~base.turnGridWhite(2, 2, page: page);

//// blip:
~base.setGridFunc(0, 0, { | vel |
  ~docile.noisies.sampler.playSampleOneShot(0, vel.ccdbfs);
  ~base.turnGridGreen(0, 0);
}, 'noteOn', page: page);
~base.setGridFunc(0, 0, { ~base.turnGridWhite(0, 0); }, 'noteOff', page: page);

//// blipLong:
~base.setGridFunc(1, 0, { | vel |
  ~docile.noisies.sampler.playSampleOneShot(1, vel.ccdbfs);
  ~base.turnGridGreen(1, 0);
}, 'noteOn', page: page);
~base.setGridFunc(1, 0, { ~base.turnGridWhite(1, 0); }, 'noteOff', page: page);

//// white:
~base.setGridFunc(0, 1, { | vel |
  ~docile.noisies.sampler.playSampleOneShot(2, vel.ccdbfs);
  ~base.turnGridGreen(0, 1);
}, 'noteOn', page: page);
~base.setGridFunc(0, 1, { ~base.turnGridWhite(0, 1); }, 'noteOff', page: page);

///// purple:
~base.setGridFunc(1, 1, { | vel |
  ~docile.noisies.sampler.playSampleOneShot(3, vel.ccdbfs);
  ~base.turnGridGreen(1, 1);
}, 'noteOn', page: page);
~base.setGridFunc(1, 1, { ~base.turnGridWhite(1, 1); }, 'noteOff', page: page);

//// pink:
~base.setGridFunc(2, 1, { | vel |
  ~docile.noisies.sampler.playSampleOneShot(4, vel.ccdbfs);
  ~base.turnGridGreen(2, 1);
}, 'noteOn', page: page);
~base.setGridFunc(2, 1, { ~base.turnGridWhite(2, 1); }, 'noteOff', page: page);

/// gray:
~base.setGridFunc(0, 2, { | vel |
  ~docile.noisies.sampler.playSampleOneShot(5, vel.ccdbfs);
  ~base.turnGridGreen(0, 2);
}, 'noteOn', page: page);
~base.setGridFunc(0, 2, { ~base.turnGridWhite(0, 2); }, 'noteOff', page: page);

/// brown:
~base.setGridFunc(1, 2, { | vel |
  ~docile.noisies.sampler.playSampleOneShot(6, vel.ccdbfs);
  ~base.turnGridGreen(1, 2);
}, 'noteOn', page: page);
~base.setGridFunc(1, 2, { ~base.turnGridWhite(1, 2); }, 'noteOff', page: page);

/// blue:
~base.setGridFunc(2, 2, { | vel |
  ~docile.noisies.sampler.playSampleOneShot(6, vel.ccdbfs);
  ~base.turnGridGreen(2, 2);
}, 'noteOn', page: page);
~base.setGridFunc(2, 2, { ~base.turnGridWhite(2, 2); }, 'noteOff', page: page);