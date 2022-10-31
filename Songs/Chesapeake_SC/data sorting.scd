/*
*/

// increments of 6 minutes
// 10 entries an hour
// 240 entries per day
// 7440 entries total

// I would like each day (240 samples) to take 4 minutes (240 seconds)
// 1 sample per second


// qual rate
// increments of 15 minutes
// 4 entries per hour
// 96 entries per day
// 31 days
// 2976 entries total
// I would like each days (96 samples) to take 4 minutes (240 seconds)
// 0.4 samples per second




s.blockSize;

~controlRate = s.sampleRate / s.options.blockSize;

~rate = 1/~controlRate;
~otherRate = 0.4/~controlRate;


~ft[10];
~bus.getSynchronous({ | val | val.postln; });





// how long this would take to play through:
7440 / s.sampleRate
0.1687074829932

// how long would I like it to take?
// 2.5 hrs
// 150 minutes
// 9000 seconds



~path = "/Users/patrickmcminn/Dropbox/Mac/Music/Patrick McMinn/Current Projects/Chesapeake/CO-OPS_8574680_wl 6 minutes.csv"

b = CSVFileReader.read(~path, true);

y = b.flop

y[2][1].interpret

String

~ft = Array.fill((y[2].size)-1, { | i | y[2][i+1].interpret.asFloat });
~ft = ~ft.normalize;

~ftBuf = Buffer.loadCollection(s, ~ft);

~ftBuf.plot

~ftBuf.plot
~ftBuf.numFrames

~ft[3]

~ph = Array.fill((x[6].size)-1, { | i | x[6][i+1].asFloat });

~ftBuf.numFrames / s.sampleRate;



~bus = Bus.control;
~tidalPlayer = { Out.kr(~bus, PlayBuf.kr(1, ~ftBuf, ~rate, loop: 1)) }.play
~tidalPlayer.free;
~bus.getSynchronous({ | val | val.postln; });
~tidalPlayer.free;
Bus

~tidalPlayer = { Out.kr(~bus, BufRd.kr(1, ~ftBuf, Phasor.kr(0, BufRateScale.kr(~ftBuf) * ~rate, 0, BufFrames.kr(~ftBuf)), 1)) }.play;


~trial = { Out.ar(0, SinOsc.ar((200 + PlayBuf.ar(1, ~ftBuf, ~rate, loop: 1).p

(8040 * s.sampleRate) / ~ftBuf.numFrames;

y[1][0]
x[0]

x[6][1].isFloat
~ph = Array.fill((x[6].size)-1, { | i | x[6][i+1].asFloat });

z = Buffer.loadCollection(s, ~ph);
z.numFrames

(z.numFrames * 324496.67858968 ) /s.sampleRate

{ Out.ar(0, SinOsc.ar((50 * PlayBuf.ar(1, z, 1/324496.67858968, loop: 1).poll))) }.play

	(PlayBuf.ar(1, z, 1, loop: 1) * 5))) }.play
BufRateScale

//magic number for this array
1/324496.67858968


~normPH = ~ph.normalize

~ph

Array

.range

(14400 * s.sampleRate) / z.numFrames


Buffer

BufRd
BufRateScale
BufFrames


z.plot
~ph[0].isFloat

threshold
change

z = { BufDur.kr(~ftBuf) .poll }.play
z.free;


Sweep