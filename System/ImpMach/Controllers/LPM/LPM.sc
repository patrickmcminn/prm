// An LPM is basically a dictionary of LPM pages, with global LED refresh functionality
// (which should probably live in a cell class anyway)
LPM {
  var pageDict, <activePageKey;

  *new {
    ^super.new.prInit();
  }

  prInit {
    pageDict = IdentityDictionary.new(0);
    pageDict.add(\root -> LPM_Page(true) );
    activePageKey = \root;
  }

  addPage { |name|
    if(name != nil,
      {
        if(pageDict.includesKey(name).not,
          { pageDict.add(name -> LPM_Page(false) ) },
          { "A page by that name already exists. Please choose a new name.".postln }
        );
      },
      { "Please provide a name for the page, formatted as a symbol.".postln }
    );
  }

  removePage { |name|
    if(pageDict.includesKey(name),
      {
        if(name != \root,
          {
            if(name == activePageKey, {
              activePageKey = \root;
              this.setPage(\root);
            });
            pageDict.removeAt(name);
          },
          { "Removal of \root page not allowed.".postln },
        );
      },
      { "A page by that name does not exist.".postln }
    );
  }

  setPage { |name|
    if(pageDict.includesKey(name),
      {
        pageDict[activePageKey].disable;
        activePageKey = name;
        pageDict[activePageKey].enable;
      },
      { "A page by that name does not exist.".postln }
    );
  }

  active { ^pageDict[activePageKey] }
  page { |name| ^pageDict.atFail(name, { "Invalid page name.".postln }); } // Use better error handling

  // Wrapper functions for convenient access to internals of the active page
  top { ^this.active.top }
  side { ^this.active.side }
  grid { ^this.active.grid }

  // Allows numerical access to banks, used to access inactive banks
  topBank { |index = 0| ^this.active.topBank(index) }
  sideBank { |index = 0| ^this.active.sideBank(index) }
  gridBank { |index = 0| ^this.active.gridBank(index) }

  setTop { |index = 0| this.active.setTop(index) }
  setSide { |index = 0| this.active.setSide(index) }
  setGrid { |index = 0| this.active.setGrid(index) }

  setAllBanks { |topIndex = 0, sideIndex = 0, gridIndex = 0|
    this.setTop(topIndex);
    this.setSide(sideIndex);
    this.setGrid(gridIndex);
  }

  addTop { this.active.addTop }
  addSide { this.active.addSide }
  addGrid { this.active.addGrid }

  removeTop { |index| this.active.removeTop }
  removeSide { |index| this.active.removeSide }
  removeGrid { |index| this.active.removeGrid }

  pause { this.active.pause }
  play { this.active.play }

  free {
    activePageKey = nil;
    pageDict.do { |item| item.free };
    pageDict = nil;
  }
}