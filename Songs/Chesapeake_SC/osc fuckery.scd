

x = NetAddr.new("127.0.0.1", 53000);

(
var temp = 101;

x.sendMsg('/cue/temp/text', ("temp:"+temp+"F").asString);

)
x.sendMsg('/cue/date/text', "8/15/2022");
x.sendMsg('/cue/time/text', "00:00");


Font.default