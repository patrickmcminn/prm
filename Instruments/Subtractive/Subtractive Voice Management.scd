/*
Copy of old voice management system for reference
as I built voice management up from
the ground again
*/

/*
Saturday, June 28th 2014
Subtractive.sc
prm
*/


  prManageOrder { | nilPos |
    var subArray1, subArray2, newArray;
    subArray1 = Array.fill(nilPos, { | i | orderArray[i]; });
    subArray2 = Array.fill(orderArray.size - (nilPos + 1), { | i | orderArray[i + nilPos + 1] });
    newArray = subArray1 ++ subArray2;
    newArray = newArray.add(nil);
    orderArray = newArray;
    orderNum = orderArray.find([nil]);
  }

  prStealVoice { | freq |
    var oldFreq, voiceArray, nilArray;
    oldFreq = orderArray[0];
    synthDict[oldFreq].steal(freq);
    synthDict[freq] = synthDict[oldFreq];
    synthDict[oldFreq] = nil;
    synthDict.removeAt(oldFreq);
    orderArray[0] = freq;
    orderArray = orderArray.rotate(-1);
  }


  //////// public functions:

  free {
    sequencerDict.do({ | sequence | sequence.free; });
    sequencerClock.clear;
    sequencerDict.stop;
    sequencerDict = nil;
    synthDict.do({ | voice | voice.free; });
    lfo.free;
    lfo = nil;
    lfoBus.free;
    lfoBus = nil;
    synthGroup.free;
    synthGroup = nil;

    this.freeModule;
  }

  playNote { | freq = 220, vol = -12 |
    {
      var playTest, order;
      playTest = try { synthDict[freq] }.isPlaying;
      if( playTest == true, { synthDict[freq].steal(freq); }, {
        if( numVoices < maxVoices, {
          // assign synth to a the synth dict:
          synthDict[freq] = Subtractive_Voice.new(freq, vol, this, synthGroup, \addToTail);
          // put synth marker in the correct order slot:
          orderArray[orderNum] = freq;
          // increment the number of voices:
          numVoices = numVoices + 1;
          // increment the order of voices:
          orderNum = orderNum + 1;
          },
          { this.prStealVoice(freq); });
      });
    }.fork;
  }

  releaseNote { | freq = 220 |
    var orderPos;
    var synth = synthDict[freq];
    {
      if( synthDict[freq].isPlaying == true, {
        synth.release;
        while( { synthDict[freq].isPlaying == true }, { 0.001.wait; });
        orderPos = orderArray.find([freq]);
        //orderPos.postln;

        if( synthDict[freq].isPlaying == false && orderPos.notNil, {
          synthDict[freq] = nil;
          synthDict.removeAt(freq);
          //orderPos.postln;
          orderArray[orderPos] = nil;
          this.prManageOrder(orderPos);
          numVoices = numVoices - 1;
        });
      });
    }.fork;
  }

  releaseAllNotes {
    orderArray.do({ | freq |
      this.releaseNote(freq);
    });
  }
}