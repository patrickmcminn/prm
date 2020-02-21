~path = "/Users/patrickmcminn/Music/Patrick McMinn/The Way of Xin/Juno Chorale.aif"
~path = "/Users/patrickmcminn/Music/Patrick McMinn/The Way of Xin/Juno Chorale Processed .aif"

~buf = Buffer.read(s, ~path);


v = { Warp1.ar(2, ~buf, Line.kr(0, 0.125, 150), 1) }.play
w = { Warp1.ar(2, ~buf, Line.kr(0.125, 0.25, 150), 0.5) }.play
x = { Warp1.ar(2, ~buf, Line.kr(0, 0.125, 150), 0.25) }.play
y = { Warp1.ar(2, ~buf, Line.kr(0, 0.125, 150), 0.125) }.play
z = { Warp1.ar(2, ~buf, Line.kr(0.25, 0.125, 150), 1) }.play

z.free
v = { Warp1.ar(2, ~buf, Line.kr(0, 1, 1200), 1) }.play

x.free;
FalseSelf_MainTrumpet

y.free

v.free;
w.free
x.free
z.free;

a = { Warp1.ar(2, ~buf, Line.kr(0, 0.5, 150, doneAction: 2), 1) }.play;
b = { Warp1.ar(2, ~buf, Line.kr(0, 0.5, 150, doneAction: 2), 0.5) }.play
c = { Warp1.ar(2, ~buf, Line.kr(0, 0.5, 150, doneAction: 2), 0.25) }.play
d = { Warp1.ar(2, ~buf, Line.kr(0, 0.5, 150, doneAction: 2), 0.125) }.play
f = { Warp1.ar(2, ~buf, Line.kr(0, 0.5, 150, doneAction: 2), 3/2) }.play

e.free;
a.free;

g = { Warp1.ar(2, ~buf, LFNoise2.kr(0.05).range(0, 1), 1) }.play
h = { Warp1.ar(2, ~buf, LFNoise2.kr(0.06).range(0, 1), 0.5) }.play
i = { Warp1.ar(2, ~buf, LFNoise2.kr(0.04).range(0, 1), 0.25) }.play
j = { Warp1.ar(2, ~buf, LFNoise2.kr(0.07).range(0, 1), 0.125) }.play
k = { Warp1.ar(2, ~buf, LFNoise2.kr(0.03).range(0, 1), 2) }.play
l = { Warp1.ar(2, ~buf, LFNoise2.kr(0.02).range(0, 1), 3/2) }.play



k = { Warp1.ar(2, ~buf, LFNoise2.kr(0.1).range(0, 1), 0.001, LFNoise2.kr(19).range(0.1, 0.3)) }.play

l.free



s.makeWindow;

Server.killAll;