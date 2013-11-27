Dongs {

  *new {
    ^super.new.init;
  }

  init {

    "dongs loaded!".postln;
  }

  add { | num1 = 9, num2 = 4 |
    var adder = num1 + num2;
    ^adder;
  }



}