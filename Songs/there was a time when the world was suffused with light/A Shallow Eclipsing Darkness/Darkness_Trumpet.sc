/*
Tuesday, April 10th 2018
Darkness_Trumpet.sc
A Shallow Eclipsing Darkness
prm
*/

/*
input

Distortion


MicroLooper

out TO MODULAR

in FROM modular

EQ

SPLIT:


1 -- normal
-- LowPassFilter
--(LOWPASS filter needs to be modulated)

2 -- 8va w/ DELAY


*/

/*

Darkness_Trumpet : IM_Processor { | outBus = 0, modularOut = 3, modularIn = 3, relGroup, addAction = 'addToHead' |

  *new {
    ^super.new(1, 1, outBus, relGroup: relGroup, addAction: addAction).prInit;
  }

  LowPassFilter

}
*/