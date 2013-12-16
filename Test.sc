Test {

  *new { | yeah = 0, yeahTwo = 1 |
    ^super.new.init(yeah, yeahTwo);
  }

  init { | yeah = 0, yeahTwo = 1 |
    ^[yeah, yeahTwo];
    //"dongs loaded!".postln;
  }

  add { | num1 = 9, num2 = 4 |
    var adder = num1 + num2;
    ^adder;
  }



}