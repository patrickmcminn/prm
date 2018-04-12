LPM_Cell {
  var enabled;
  var myNum, myType;
  var midiOutPort;

  var onFunc, offFunc;
  var greenBit, redBit, intensity, animationTask;


  *new { |num = 0, type = \note, isEnabled = true|
    ^super.new.prInit(num, type, isEnabled);
  }

  prInit { |num, type, isEnabled|
    enabled = isEnabled;

    myNum = num;
    myType = type;

    this.prInitMIDI;

    this.prMakeMIDIResponders(num);
    this.onFunc_( { "No on function.".postln } );
    this.offFunc_( { "No off function.".postln } );

    this.prMakeColor(\green, 0);
    this.prMakeAnimationTask;

    if(enabled == false, { this.disable });
  }


  // MIDI

  prInitMIDI {
    if( MIDIClient.initialized.not, { MIDIClient.init; MIDIIn.connectAll; }, { } );
    midiOutPort = MIDIOut.newByName("Launchpad Mini", "Launchpad Mini").latency_(0);
  }

  prMakeMIDIResponders { |num|
    var id = MIDIIn.findPort("Launchpad Mini", "Launchpad Mini").uid;
    switch(myType,
      \note, { this.prMakeNoteResponder(num, id) },
      \cc, { this.prMakeCCResponder(num, id) }
    );
  }

  prMakeNoteResponder { |num, id|
    onFunc = MIDIFunc({ |vel, noteNum| ^nil }, num, 0, \noteOn, id).fix;
    offFunc = MIDIFunc({ |vel, noteNum| ^nil }, num, 0, \noteOff, id).fix;
  }

  prMakeCCResponder { |num, id|
    onFunc = MIDIFunc({ |val, ccNum| if(val == 127, { ^nil }) }, num, 0, \control, id).fix;
    offFunc = MIDIFunc({ |val, ccNum| if(val == 0, { ^nil }) }, num, 0, \control, id).fix;
  }

  // How to pass val and noteNum/ccNum along into the MIDIFunc when resetting it?
  onFunc_ { |func = nil|
    if(myType == \note,
      { onFunc.prFunc_(func) },
      { onFunc.prFunc_({ |val, ccNum| if(val == 127, func) }) }  // if it's a CC button
    );
  }

  offFunc_ { |func = nil|
    if(myType == \note,
      { offFunc.prFunc_(func) },
      { offFunc.prFunc_({ |val, ccNum| if(val == 0, func) }) }  // if it's a CC button
    );
  }

  updateLED {
    if(enabled, {
      if(myType == \note,
        { midiOutPort.noteOn(0, myNum, this.colorToVel) },
        { midiOutPort.control(0, myNum, this.colorToVel) }
      );
    });
  }

  /*
  // For now, avoid using this in animation routines (the model will get out of sync w
  // the hardware)
  // Is this more dangerous than it is useful?
  prMuteLED {
    if(myType == \note,
      { midiOutPort.noteOn(0, myNum, 12) },
      { midiOutPort.control(0, myNum, 12) }
    );
  }

  // Set duty cycle of all lights, according to calculation described in the
  // manual. Medium brightness LED is always twice the number of the low one.
  // Numerator must be between 1 and 16, and denominator between 3 and 18
  prSetLEDDuty { |numerator = 1, denominator = 3|
    var tempVelocity;
    if(numerator < 9,
      {
        tempVelocity = (16 * (numerator - 1)) + (denominator - 3);
        midiOutPort.noteOn(176, 30, tempVelocity);
      },
      {
        tempVelocity = (16 * (numerator - 9)) + (denominator - 3);
        midiOutPort.noteOn(176, 31, tempVelocity);
      }
    );
  }
*/

  disable {
    onFunc.disable;
    offFunc.disable;
    this.pause;
    enabled = false;
  }

  enable {
    onFunc.enable;
    offFunc.enable;
    this.play;
    enabled = true;
    this.updateLED;
  }


  // COLOR

  // TODO: Add functions to set each color without affecting intensity
  // useful for animations (won't reset the animating intensity

  // intensity can be 0 - 3
  prMakeColor { |color, intensityVal|
    switch(color,
      \red, { this.red(intensityVal) },
      \green, { this.green(intensityVal) },
      \amber, { this.amber(intensityVal) }
    );
  }

  prSetColor { |green = 0, red = 0, intensityVal|
    greenBit = green;
    redBit = red;
    intensity = intensityVal;
    this.updateLED;
  }

  red { |intensityVal = 3| this.prSetColor(0, 1, intensityVal) }
  green { |intensityVal = 3| this.prSetColor(1, 0, intensityVal) }
  amber { |intensityVal = 3| this.prSetColor(1, 1, intensityVal) }


  intensity_ { |intensityVal = 3| this.prSetColor(greenBit, redBit, intensityVal) }
  black { this.intensity_(0) }

  brighten {
    if(intensity < 3, { this.intensity_(intensity + 1) });
  }

  darken {
    if(intensity > 0, { this.intensity_(intensity - 1) });
  }

  toggle {
    if(intensity == 0, { this.intensity_(3) }, { this.darkest });
  }

  // Internal conversion equation for the LPM
  colorToVel {
    ^(16 * (greenBit * intensity)) + (redBit * intensity) + 12;
  }

  colorStatus {
    ^[greenBit, redBit, intensity];
  }


  // ANIMATION

  // Is there a way to consolidate the guts of the animation code so there's
  // not as much redundancy?
  // Add duty percentage to all the animation routines
  // Maybe change out stepWait timing for ratios of underlying clock tempo

  // The init method needs a different name than the one in the superclass,
  // otherwise it'll override it
  prMakeAnimationTask { animationTask = TaskProxy.new.play }

  blink { |stepWait = 0.5, duty = 0.4, type = \loop, sync = false|
    var adjustedWait = stepWait / 2;
    switch(type,
      \once, {
        animationTask.source = {
          var tempIntensity = 3;
          if(intensity == 0,
            {
              this.intensity_(3);
              (adjustedWait * duty).wait;
              this.black;
            },
            {
              tempIntensity = intensity;
              this.black;
              (adjustedWait * (1 - duty)).wait;
              this.intensity_(tempIntensity);
            }
          );
        };
      },
      \loop, {
        animationTask.source = {
          var tempIntensity = 3;
          loop {
            if(intensity == 0,
              {
                this.intensity_(tempIntensity);
                (adjustedWait * duty).wait;
              },
              {
                if(intensity > 0, { tempIntensity = intensity }, { tempIntensity = 3 });
                this.black;
                (adjustedWait * (1 - duty)).wait;
              }
            );
          };
        };
      }
    );
    this.play(sync);
  }

  fadeIn { |stepWait = 0.5, type = \loop, sync = false|
    var adjustedWait = stepWait / 4;
    switch(type,
      \once, {
        animationTask.source = {
          while({ intensity < 3 }, { this.brighten; adjustedWait.wait; });
        };
      },
      \loop, {
        animationTask.source = {
          loop {
            while({ intensity < 3 }, { this.brighten; adjustedWait.wait; });
            this.intensity_(0);
            adjustedWait.wait;
          };
        };
      }
    );
    this.play(sync);
  }

  fadeOut { |stepWait = 0.5, type = \loop, sync = false|
    var adjustedWait = stepWait / 4;
    switch(type,
      \once, {
        animationTask.source = {
          while({ intensity > 0 }, { this.darken; adjustedWait.wait; });
        };
      },
      \loop, {
        animationTask.source = {
          loop {
            while({ intensity > 0 }, { this.darken; adjustedWait.wait; });
            this.intensity_(3);
            adjustedWait.wait;
          };
        };
      }
    );
    this.play(sync);
  }

  // TO DO: make single pulse cycle around intensity settings and end where it started
  pulse { |stepWait = 0.5, type = \loop, sync = false|
    var adjustedWait = stepWait / 6;
    switch(type,
      \once, {
        animationTask.source = {
          while({ intensity < 3 }, { this.brighten; adjustedWait.wait; });
          while({ intensity > 0 }, { this.darken; adjustedWait.wait; });
        }
      },
      \loop, {
        animationTask.source = {
          loop {
            while({ intensity < 3 }, { this.brighten; adjustedWait.wait; });
            while({ intensity > 0 }, { this.darken; adjustedWait.wait; });
          };
        }
      }
    );
    this.play(sync);
  }

  // Switches through an array of colors
  // TO DO: add black as an option (how to retain color intensity?)
  cycle { |stepWait = 0.5, colorArray, type = \loop, sync = false|
    var adjustedWait;
    if(colorArray == nil, { colorArray = [\green, \red, \amber, \red, \green] });
    adjustedWait = stepWait / colorArray.size;

    switch(type,
      \once, {
        animationTask.source = {
          var tempIntensity;
          colorArray.do { |color|
            if(intensity > 0, { tempIntensity = intensity }, { tempIntensity = 3 });
            switch(color,
              \red, { this.red(tempIntensity) },
              \green, { this.green(tempIntensity) },
              \amber, { this.amber(tempIntensity) }
            );
            adjustedWait.wait;
          };
        };
      },
      \loop, {
        animationTask.source = {
          var tempIntensity;
          loop {
            colorArray.do { |color|
              if(intensity > 0, { tempIntensity = intensity }, { tempIntensity = 3 });
              switch(color,
                \red, { this.red(tempIntensity) },
                \green, { this.green(tempIntensity) },
                \amber, { this.amber(tempIntensity) }
              );
              adjustedWait.wait;
            }
          };
        };
      }
    );
    this.play(sync);
  }

  idle {
    this.pause;
    animationTask.source = nil;
  }

  // Animation control
  play { |sync = false|
    var quant = if(sync, { 1 }, { nil });
    if(animationTask.player.isPlaying.not, { animationTask.resume(quant) })
  }

  pause { if(animationTask.player.isPlaying, { animationTask.pause }) }

  pauseTgl { |sync = false|
    var quant = if(sync, { 1 }, { nil });
    if(animationTask.player.isPlaying, { animationTask.pause }, { animationTask.play(quant) });
  }


  // CLEAN UP

  free {
    onFunc.free;
    onFunc = nil;

    offFunc.free;
    offFunc = nil;

    animationTask.stop;
    animationTask = nil;

    this.prSetColor(0, 0, 0);

    greenBit = nil;
    redBit = nil;
    intensity = nil;

    midiOutPort = nil;

    myNum = nil;
    myType = nil;

    enabled = nil;
  }
}