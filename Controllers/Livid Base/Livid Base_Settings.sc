/*
Tuesday, April 15th 2014
*/

+ Base {

  setLocalControl { | setting = 'allOff' |
    switch(setting,
      { \allOff }, { midiOutPort.control(16, 122, 64) },
      { \allOn }, { midiOutPort.control(16, 122, 127) },
      { \analogOnly }, { midiOutPort.control(16, 122, 66) },
      { \encoderRelativeOnly }, { midiOutPort.control(16, 122, 68) },
      { \analogAndEncoderRelative }, { midiOutPort.control(16, 122, 70) },
      { \encoderAbsoluteOnly }, { midiOutPort.control(16, 122, 72) },
      { \analogAndEncoderAbsolute }, { midiOutPort.control(16, 122, 74) },
      { \encoderRelativeAndEncoderAbsolute }, { midiOutPort.control(16, 122, 76) },
      { \analogAndEncoders }, { midiOutPort.control(16, 122, 78) },
      { \buttonToggleOnly }, { midiOutPort.control(16, 122, 80) },
      { \analogAndButtonToggle }, { midiOutPort.control(16, 122, 82) }
    );
  }

}