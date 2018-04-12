/*
Thursday, June 19th 2014
Elephant Music.sc
prm
*/

IM_ElephantMusic : IM_Module {

  var <isLoaded;
  var <looper, <chimes;
  var <trumpetInput, <flugelInput;

  *new {
    |
    outBus = 0, bufLength = 10, send0Bus = nil, send1Bus = nil, send2Bus = nil, send3Bus = nil,
    relGroup = nil, addAction = \addToHead
    |

    ^super.new(2, outBus, send0Bus, send1Bus, send2Bus, send3Bus, false, relGroup, addAction).prInit(bufLength);
  }

  prInit { | bufLength = 10 |
    var server = Server.default;
    server.waitForBoot {
      isLoaded = false;
      //while( { try { mixer.chanMono(1) } == nil }, { 0.01.wait });
      server.sync;
      while( { mixer.isLoaded.not }, { 0.001.wait; });

      chimes = IM_Elephant_Chimes.new(mixer.chanStereo(0), relGroup: group, addAction: \addToHead);
      server.sync;
      while({ chimes.isLoaded.not }, { 0.001.wait; });

      looper = IM_Elephant_Looper.new(mixer.chanStereo(1), bufLength, relGroup: group, addAction: \addToHead);
      server.sync;
      while({ looper.isLoaded.not }, { 0.001.wait; });

      //while({ try { looper.mixer.chanMono(0) } == nil }, { 0.01.wait; });

      trumpetInput = IM_HardwareIn.new(1, looper.inBus.subBus(0), group, \addToHead);
      server.sync;
      while({ trumpetInput.isLoaded.not }, { 0.001.wait; });

      flugelInput = IM_HardwareIn.new(2, looper.inBus.subBus(1), group, \addToHead);
      server.sync;
      while({ flugelInput.isLoaded.not }, { 0.001.wait; });

      isLoaded = true;
    }
  }

  free {
    isLoaded = false;
    chimes.free;
    looper.free;
    chimes = nil;
    looper = nil;

    trumpetInput.free;
    flugelInput.free;
    trumpetInput = nil;
    flugelInput = nil;

    this.freeModule;
  }
}