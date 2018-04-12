+ SimpleNumber {
  ccdbfs { |dbLow = -140, dbHigh = 0|
    var db = log( 128 / (this + 1) ) * -10 * (-61 / -21.07);
    ^db.linlin(-140.47168775467, 0, dbLow, dbHigh);
  }

  dbfsCC {
    var cc = 128 / ((this / -28.951115329853).exp) -1;
    ^cc;
  }
}




/*
First thing is that dB is a logarithmic scale and midi velocity is linear, so what we need is some kind of mapping from the linear scale of 0 - 127 to the logarithmic scale of -76.3 to 0.

The 3 dB rule is "every 3 dB reduction below 0 dB represents a halving of the power of the signal". This is accurate to within about 10%, so...
-3 dB = 1/2 power
-6 dB = 1/4 power
-9 dB = 1/8 power
-12 dB = 1/16 power
down to...
-72 dB = 1/16,777,216 power
-75 dB - 1/33,554,532 power

Clearly, we're more concerned with the top end of the scale than the bottom!

The simplest approach is to equate 0 dB with Midi Volume 127 and apply a logarithmic mapping, so...
127 = 0 dB
63 = -3 dB
31 = -6 dB
15 = -9 dB

In general, the formula would be,

Log (128/(Midi Volume +1) * (-10) = dB below zero, for example

Midi volume 127: Log(128/(127+1)) * (-10) = Log(1) * (-10) = 0 * (-10) = 0 dB
Midi volume 100: Log(128/(100+1)) * (-10) = Log(1.267) * (-10) = 0.1072 * (-10) = -1.07 dB
Midi voluem 63: Log (128/(63+1) * (-10) = Log(2) * (-10) = .30102 * (-10) = -3.01 dB

The problem with this is that the range of 0-127 isn't granular enough to get down to -76.3 dB. Since...

Midi volume 0: Log(128/(0+1)) * (-10) = Log(128) * (-10) = 2.107 * (-10) = -21.07 dB

To get below -21.07 we either need fractional midi velocity values or must apply a scaling factor.

Since 0 maps to -21.07 dB, to get to -76.3, multiply by (-76.3)/(-21.07). In general, multiply by (max dB below zero/(-21.07).

Thus the general formula is...

Log(128/(Midi Volume + 1)) * (-10) * (Max dB below 0/(-21.07)) = dB below zero

I plugged this into a spreadsheet and can send it to you. PM me your email address.

Two other considerations are...
1) Most VSTi's do not actually respond linearly to midi velocity. Emulator X for example, allows you to adjust both the velocity curve (how the software interprets the input from a keyboard, which affects the feel) and the volume curve (how the software maps velocity to volume). You may have to factor this mapping into your translation algorithm.

2) The human ear is most sensitive to the frequency range of 2kHz to 4kHz, which is the reasoning behind the a-weighted decibel scale (dbA). Depending on the frequency range you are playing back, you may need to adjust the mapping.

(http://www.gearslutz.com/board/music-computers/22443-audio-midi-volume-conversion.html)
*/
