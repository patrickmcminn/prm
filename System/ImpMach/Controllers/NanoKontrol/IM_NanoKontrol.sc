IM_NanoKontrol {
  var stripArray;

  *new {
    ^super.new.prInit;
  }

  prInit {
    stripArray = Array.fill(9, { |index| IM_NanoKontrolStrip(index, index + 9, index, index + 9) });
  }

  knobFunc_ { |index = 0, func = nil| stripArray[index].knobFunc_(func) }
  faderFunc_ { |index = 0, func = nil| stripArray[index].faderFunc_(func) }
  btmBtnOnFunc_ { |index = 0, func = nil| stripArray[index].btmBtnOnFunc_(func) }
  btmBtnOffFunc_ { |index = 0, func = nil| stripArray[index].btmBtnOffFunc_(func) }
  topBtnOnFunc_ { |index = 0, func = nil| stripArray[index].topBtnOnFunc_(func) }
  topBtnOffFunc_ { |index = 0, func = nil| stripArray[index].topBtnOffFunc_(func) }

  resetAllFuncs {
    stripArray.do { |strip| strip.resetAllFuncs };
  }

  free {
    stripArray.do { |strip| strip.free };
  }
}