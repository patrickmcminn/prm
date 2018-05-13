/*
Thursday, April 26th 2018
Sorrow Base.scd
prm

there is a sorrow in beginning to understand
*/

var page = 'sorrow';

var trumpet2Array = [-12, -9, -5,-2, 0, 3, 5, 7 ];
var trumpet3Array = [-12, -11, -10, -9, -7, -5, -4, -3, -2, 0, 2, 3 ];

~base.makePage(page);

////////////////
//// Grid: ////
//////////////

//// trumpet 3:

8.do({ | i |
  ~base.turnGridMagenta(i, 0, page: page);
  ~base.setGridFunc(i, 0, {
    //~sorrow.trumpets.trumpet3.releaseNote(('note' ++ i).asSymbol);
    ~sorrow.trumpets.trumpet3.playNote(('note' ++ i).asSymbol, trumpet3Array[i], -5);
    ~base.turnGridGreen(i, 0);
  }, 'noteOn', page: page);
  ~base.setGridFunc(i, 0, {
    ~sorrow.trumpets.trumpet3.releaseNote(('note' ++ i).asSymbol);
    ~base.turnGridMagenta(i, 0);
  }, 'noteOff', page: page);
});

4.do({ | i |
  ~base.turnGridMagenta(i, 2, page: page);
  ~base.setGridFunc(i, 2, {
    //~sorrow.trumpets.trumpet3.releaseNote(('note' ++ (i+8)).asSymbol);
    ~sorrow.trumpets.trumpet3.playNote(('note' ++ (i+8)).asSymbol, trumpet3Array[i+8], -5);
    ~base.turnGridGreen(i, 2);
  }, 'noteOn', page: page);
  ~base.setGridFunc(i, 2, {
    ~sorrow.trumpets.trumpet3.releaseNote(('note' ++ (i+8)).asSymbol);
    ~base.turnGridMagenta(i, 2);
  }, 'noteOff', page: page);
});

//// trumpet 2:
8.do({ | i |
  ~base.turnGridCyan(i, 1, page: page);
  ~base.setGridFunc(i, 1, {
    ~sorrow.trumpets.trumpet2.playNote(('note' ++ i).asSymbol, trumpet2Array[i], -5);
    ~base.turnGridGreen(i, 1);
  }, 'noteOn', page: page);
  ~base.setGridFunc(i, 1, {
    ~sorrow.trumpets.trumpet2.releaseNote(('note' ++ i).asSymbol);
    ~base.turnGridCyan(i, 1);
  }, 'noteOff', page: page);
});