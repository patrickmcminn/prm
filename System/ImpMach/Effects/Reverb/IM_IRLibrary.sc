/*
Sunday, January 12th 2014

IM_IRLibrary.sc

*/

IM_IRLibrary {

  var <irDict;
  var fftBaseSize, fftWindowMul;
  var <isLoaded;

  *new {
    | path = "~/Library/Application Support/SuperCollider/Extensions/Impatience Machine/Effects/Reverb/Impulse Responses", fftMul = 2 |
    ^super.new.prInit(path, fftMul);
  }

  prInit { | path, fftMul |
    var server;
    var pathName = PathName(path);
    server = Server.default;

   server.waitForBoot {
      isLoaded = false;
      fftBaseSize = 1024;
      fftWindowMul = fftMul;

      irDict = IdentityDictionary.new;

      pathName.filesDo { | path |
        var key = path.fileNameWithoutExtension.asSymbol;
        this.prPrepareIRBuf(key, path.fullPath);
        while( { irDict[key] == nil }, { 0.0001.wait; });
      };

      isLoaded = true;
    };
  }

  prPrepareIRBuf { | key, pathString |
    var buffer, bufSize, irBuf;
    var server = Server.default;

    fork {
      buffer = Buffer.read(
        server, pathString,
        action: { | buf | bufSize = PartConv.calcBufSize(fftBaseSize * fftWindowMul, buf) }
      );

      server.sync;

      irBuf = Buffer.alloc(server, bufSize, 1);
      irBuf.preparePartConv(buffer, fftBaseSize * fftWindowMul);

      server.sync;

      buffer.free;
      irDict.put(key, irBuf);
      server.sync;
    };
  }

  prFreeIRDict {
    irDict.do({ | item | item.free; });
  }

  irKeys {
    ^irDict.keys;
  }


  returnIRDictKeyFromIndex { }

}

