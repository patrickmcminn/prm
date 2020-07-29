/*
Tuesday, July 28th 2020
CV_Suite APC.scd
prm
*/

var page = 'cvSuite';

var main = 0;
var lfos;

~apc.makePage(page);

~apc.addPageLoadFunction(\master, { ~apc.storageDict[\loadMasterFunc].value; }, page);
~apc.storageDict[\loadMasterFunc].value;