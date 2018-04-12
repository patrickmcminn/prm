// Holds a dictionary of all possible songs, provides a method to spawn and access
// songs, with error checking
// Holds all instances of running songs, knows which ones are running
// makes sure only one is running at a time
// handles plumbing when spawning songs
// stores default values for output and effects busses

// TO DO
// * handle effects sends, outputs, proc group when spawning songs
// * integrate with AudioSystem
// * write .free method
// * standardize post formatting

/* EXAMPLES
x = IM_SongBook.new;

x.listAvailableSongs;

x.makeSong(\DonkeyJaw_Interlude);

x.getSong(\DonkeyJaw_Interlude).playInterlude;
x.getSong(\DonkeyJaw_Interlude).mixer.fadeOut;

x.listActiveSongs

x.freeSong(\DonkeyJaw_Interlude);

x.listActiveSongs
*/

IM_SongBook {
  var <songDict; // holds all songNames and an instance of the class if active, if not, 0
  var myOutBus, mySend0Bus, mySend1Bus, mySend2Bus, mySend3Bus, myRelGroup;
  var isLoaded;

  *new { |outBus = 0, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil|

    ^super.new.prInit(outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup);
  }

  prInit { |outBus, send0Bus, send1Bus, send2Bus, send3Bus, relGroup|
    isLoaded = false;

    myOutBus = outBus;
    mySend0Bus = send0Bus;
    mySend1Bus = send1Bus;
    mySend2Bus = send2Bus;
    mySend3Bus = send3Bus;
    myRelGroup = relGroup;

    this.prMakeSongDict;
    this.listAvailableSongs;

    isLoaded = true;
  }

  prMakeSongDict {
    var songPath, songNames;

    songDict = IdentityDictionary.new;

    songPath = PathName("~/Library/Application Support/SuperCollider/Extensions/Impatience Machine/Songs");

    songNames = songPath.deepFiles.select( { |tempFile| tempFile.extension == "sc" });

    songNames.do( { |tempFile|
      var parsedName = tempFile.fileNameWithoutExtension.copyToEnd(3);
      songDict.put(parsedName.asSymbol, false);
    });
  }

  // what if you want two of the same song to be running?
  // How to handle plugging in all the appropriate busses?
  makeSong { |songName = nil |
    if(songDict.includesKey(songName),
      {
        if(songDict.at(songName) == false,
          {
            var songClass = ("IM_" ++ songName).asSymbol.asClass;
            songDict.put(songName, songClass.new(outBus: myOutBus, send0Bus: mySend0Bus, send1Bus: mySend1Bus, send2Bus: mySend2Bus, send3Bus: mySend3Bus, relGroup: myRelGroup));
          },
          { (songName ++ "is already active.").postln }
        );
      },
      { (songName ++ "does not exist.").postln }
    );
    "".postln;
  }

  getSong { |songName = nil|
    if(songDict.includesKey(songName),
      {
        if(songDict.at(songName) == false,
          { (songName ++ "is not currently active").postln },
          { ^songDict.at(songName) }
        )
      },
      { (songName ++ "does not exist.").postln }
    );

    "".postln;
  }

  listAvailableSongs {
    "".postln;
    "The following songs are available:".postln;
    songDict.keysValuesDo { |songName| ("*" + songName.asString).postln };
    "".postln;
  }

  listActiveSongs {
    var noActiveBool = true;

    "".postln;

    songDict.keysValuesDo { |songName, songInstance|
      if(songInstance != false, {
        ("*" + songName.asString + "is currently active.").postln;
        noActiveBool = false;
      });
    };

    if(noActiveBool, { "* No songs are currently active.".postln });

    "".postln;
  }

  freeSong { |songName = nil|
    if(songDict.includesKey(songName),
      {
        songDict.at(songName).free;
        songDict.put(songName, false);
      },
      { (songName.asString + "is not currently active.").postln }
    );

    "".postln;
  }

  // allSongMasterStrips { }

  free {
    songDict.keysValuesDo { |songName, songInstance|
      if(songInstance != false, {
        songInstance.free;
        songInstance = nil;
      });
    };

    songDict = nil;

    myOutBus = nil;
    mySend0Bus = nil;
    mySend1Bus = nil;
    mySend2Bus = nil;
    mySend3Bus = nil;
    myRelGroup = nil;

    isLoaded = nil;
  }
}