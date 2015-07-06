/*
Monday, July 6th 2015
Connections_NoteRecord.sc
prm
*/

Connections_NoteRecord {

  var <isLoaded, server;
  var <noteBufferArray, <cascadeBufferArray, <chordBufferArray, <chordSumBufferArray;
  var <input, <inBus, <group;
  var <basslineRecordRoutine, <cascadeRecordRoutine, <chordRecordRoutine, <chordSumRecordRoutine;
  var <basslineRecordNum, <cascadeRecordNum, <chordRecordNum, <chordSumRecordNum;

  *new { | relGroup = nil, addAction = 'addToHead' |
    ^super.new.prInit(relGroup, addAction);
  }

  prInit { | relGroup, addAction |
    server = Server.default;
    server.waitForBoot {
      isLoaded = false;

      basslineRecordNum = 0;
      cascadeRecordNum = 0;
      chordRecordNum = 0;
      chordSumRecordNum = 0;

      group = Group.new(relGroup, addAction);
      this.prAddSynthDefs;

      server.sync;

      inBus = Bus.audio;

      noteBufferArray = Buffer.allocConsecutive(5, server, server.sampleRate * 3.2, 1);
      cascadeBufferArray = Buffer.allocConsecutive(3, server, server.sampleRate * 0.5, 1);
      chordBufferArray = Buffer.allocConsecutive(6, server, server.sampleRate, 1);
      chordSumBufferArray = Buffer.allocConsecutive(4, server, server.sampleRate, 1);

      server.sync;

      this.makeRecordRoutines;

      isLoaded = true;
    };
  }

  prAddSynthDefs {
    SynthDef(\prm_NoteRecord_RecordBuf, {
      |inBus, buffer, loop = 0, preLevel = 0 |
      var input, record, sig;
      input = In.ar(inBus);
      record = RecordBuf.ar(input, buffer, preLevel: preLevel, loop: loop, trigger: 1, doneAction: 2);
    }).add;
  }

  //////// public functions:

  free {
    noteBufferArray.do({ | buf | buf.free; });
    cascadeBufferArray.do({ | buf | buf.free; });
    chordBufferArray.do({ | buf | buf.free; });
    chordSumBufferArray.do({ | buf | buf.free; });
    isLoaded = false;
  }

  makeRecordRoutines {

    basslineRecordRoutine = Routine {
      this.recordNoteBuffer(0);
      (basslineRecordNum = 1).yield;

      this.recordNoteBuffer(1);
      (basslineRecordNum = 2).yield;

      this.recordNoteBuffer(2);
      (basslineRecordNum = 3).yield;

      this.recordNoteBuffer(3);
      (basslineRecordNum = 4).yield;

      this.recordNoteBuffer(4);
      (basslineRecordNum = 5).yield;
    };

    cascadeRecordRoutine = Routine {
      // assumes that bassline notes have been recorded
      this.recordCascadeBuffer(0);
      (cascadeRecordNum = 1).yield;

      this.recordCascadeBuffer(1);
      (cascadeRecordNum = 2).yield;

      this.recordCascadeBuffer(2);
      (cascadeRecordNum = 3).yield;
    };

    chordRecordRoutine = Routine {
      this.recordChordBuffer(0);
      (chordRecordNum = 1).yield;

      this.recordChordBuffer(1);
      (chordRecordNum = 2).yield;

      this.recordChordBuffer(2);
      (chordRecordNum = 3).yield;

      this.recordChordBuffer(3);
      (chordRecordNum = 4).yield;

      this.recordChordBuffer(4);
      (chordRecordNum = 5).yield;

      this.recordChordBuffer(5);
      (chordRecordNum = 6).yield;
    };

    chordSumRecordRoutine = Routine { };

  }

  resetNoteBuffers { noteBufferArray.do({ | buf | buf.zero; }); }
  resetCascadeBuffers { cascadeBufferArray.do({ | buf | buf.zero; }); }
  resetChordBuffers { chordBufferArray.do({ | buf | buf.zero; }); }
  resetChordSumBuffers { chordSumBufferArray.do({ | buf | buf.zero; }); }
  resetBuffers {
    this.resetNoteBuffers;
    this.resetCascadeBuffers;
    this.resetChordBuffers;
    this.resetChordSumBuffers;
  }

  resetBasslineRecordRoutine {
    basslineRecordRoutine.reset;
    basslineRecordNum = 0;
  }
  resetCascadeRecordRoutine {
    cascadeRecordRoutine.reset;
    cascadeRecordNum = 0;
  }
  resetChordRecordRoutine {
    chordRecordRoutine.reset;
    chordRecordNum = 0;
  }
  resetChordSumRecordRoutine {
    chordSumRecordRoutine.reest;
    chordSumRecordNum = 0;
  }
  resetRoutines {
    this.resetBasslineRecordRoutine;
    this.resetCascadeRecordRoutine;
    this.resetChordRecordRoutine;
    this.resetChordSumRecordRoutine;
  }

  recordNoteBuffer { | bufNum = 0 |
    Synth(\prm_NoteRecord_RecordBuf, [\inBus, inBus, \buffer, noteBufferArray[bufNum]],
      group, \addToTail);
  }
  recordCascadeBuffer { | bufNum = 0 |
    Synth(\prm_NoteRecord_RecordBuf, [\inBus, inBus, \buffer, cascadeBufferArray[bufNum]],
      group, \addToTail);
  }
  recordChordBuffer { | bufNum = 0 |
    Synth(\prm_NoteRecord_RecordBuf, [\inBus, inBus, \buffer, chordBufferArray[bufNum]],
      group, \addToTail);
  }
  recordChordSumBuffer { | bufNum = 0 |
    Synth(\prm_NoteRecord_RecordBuf, [\inBus, inBus, \buffer, chordSumBufferArray[bufNum]],
      group, \addToTail);
  }

}