// make a child of pattern proxy
IM_PatternSeq {
  var <pattProxy, lastPatt;
  var player;
  var <isPlaying;

  *new { ^super.new.prInit }

  prInit {
    pattProxy = PatternProxy.new(Pbind(\dur, 1));
    player = pattProxy.play(quant: 0);
    player.stop;
  }

  addKey { |key, patt|
    var tempPatt = Pbind(key, patt) <> pattProxy.source;
    lastPatt = pattProxy.source;
    pattProxy.source_(tempPatt);
  }

  undo { pattProxy.source_(lastPatt) }

  play { |clock = nil, quant = nil|
    player.play(clock, quant: quant);
    isPlaying = true;
  }

  stop {
    player.stop;
    isPlaying = false;
  }

  free { }
}

/*
IM_PresetSeq {
  var myFreq, myAmp;

  *new{ |myFreq, myAmp|
  }

  changePreset { |newFreq, newAmp|
    // take existing values, and new values, overwrite them into the pattern and interpolate
  }

  nextPreset { |dur, howFar|
    // don't play the event stream on a clock, simply call "next" on it as many times as you want. the dur will determine the granularity of interpolation

    this.setDur(dur);
    fork {
      howFar.do {
        patt.next;
        dur.wait;
      };
    };
  }

  forceNextPreset {
  }
}


IM_Preset {
}
*/

/*
x = Pbind(\instrument, \default, \degree, Pwhite(0, 9));
z = PatternProxy(x);
z.play;
z.source_(Pbind(\degree, 3));
z.source_(Pbind(\degree, \rest));
z.source_(Pbind(\degree, Pwhite(0, 9)));
b = z.source <> Pbind(\octave, Pseq([0, 3] + 5, inf));
b = z.source <> Pbind(\octave, 3);

z.source_(b);

y = x <> (dur: 0.25) <> Pbind(\octave, Prand([0, 1, 2] + 4, inf));

Pchain

y.play

PatternProxy
Pbindef

undoChain
*/