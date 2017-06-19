/*
Friday, June 16th 2017
Foundation_SongBuffers.sc
prm
*/


Foundation_SongBuffers {

  var server, <isLoaded;
  var <lowDBuffer, <aBuffer, <highDBuffer;
  var <inBus;
  var group;

  *new { | relGroup |
    ^super.new.prInit(relGroup);
  }

  prInit { | relGroup |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      group = Group.new(relGroup, \addToHead);

      inBus = Bus.audio(server, 1);
      server.sync;

      this.prAddSynthDef;

      lowDBuffer = Buffer.alloc(server, server.sampleRate*4, 1);
      aBuffer = Buffer.alloc(server, server.sampleRate*4, 1);
      highDBuffer = Buffer.alloc(server, server.sampleRate*4, 1);

      server.sync;

      isLoaded = true;
    }
  }

  prAddSynthDef {
    SynthDef(\prm_foundation_bufRec, {
      | inBus = 0, buffer |
      var input, record;
      input = In.ar(inBus);
      record = RecordBuf.ar(input, buffer, loop: 0, doneAction: 2);
    }).add;
  }

  //////// public functions:

  free {
    isLoaded = false;
    group.free;
    inBus.free;
    lowDBuffer.free;
    aBuffer.free;
    highDBuffer.free;
  }

  recordLowDBuffer {
    Synth(\prm_foundation_bufRec, [\inBus, inBus, \buffer, lowDBuffer],
      group, \addToHead);
  }

  recordABuffer {
    Synth(\prm_foundation_bufRec, [\inBus, inBus, \buffer, aBuffer],
      group, \addToHead);
  }

  recordHighDBuffer {
    Synth(\prm_foundation_bufRec, [\inBus, inBus, \buffer, highDBuffer],
      group, \addToHead);
  }

}