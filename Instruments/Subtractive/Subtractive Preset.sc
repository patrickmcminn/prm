+ Subtractive {

	printPresetFilePath { this.presetPath.postln; }

	prBuildPresetDict {
		var presetArray = SemiColonFileReader.read(this.presetPath);
		presetArray.size.do({ | i | masterPresetDict[presetArray[i][0].asSymbol] = presetArray[i][1].interpret });
	}

	writePreset { | name |
		var array;
		var file = File(this.presetPath, "a");
		this.prMakeParameterArray;
		this.prBuildPresetDict;
		array = parameterArray;
		if( masterPresetDict.includesKey(name),
			{ ^"preset already exists" },
			{
				//file.write("\n"++name++";"++array++";");
				file.write("\n"++name++";");
				file.write("[");
				array.size.do({ | i | file.write(array[i].asString++","); });
				file.write("]");
				file.close;
				this.prBuildPresetDict;
		});
	}

	readPreset { | name |
		var pArray;
		this.prBuildPresetDict;
		pArray = masterPresetDict[name];
		this.setAllParameters(pArray);
	}

}

/*


x = Array.newClear(256);

x[1]=15;
x[2]=yes;
x[3]=[ test, [ 0, 1, 2, 2.5, 9.5, 12.7, 22.5, 100.15 ] ];
x[3]=[ test, [ 0, 1, 2, 2.5, 9.5, 12.7, 22.5, 100.15 ] ];
x[3]=[ test, [ 0, 1, 2, 2.5, 9.5, 12.7, 22.5, 100.15 ] ];
x[3]=[ test, [ 0, 1, 2, 2.5, 9.5, 12.7, 22.5, 100.15 ] ];
x[3]=[ /test, [ 0, 1, 2, 2.5, 9.5, 12.7, 22.5, 100.15 ] ];
x[3]=[ 	est, [ 0, 1, 2, 2.5, 9.5, 12.7, 22.5, 100.15 ] ];
x[2][niceTry.asSymbol,[ 0, 1, 5, 129 ]]
x[2][niceTry.asSymbol,[ 0, 1, 5, 129 ]]
x[2][niceTry.asSymbol,[ 0, 1, 5, 129 ]]
x[2][\niceTry[ 0, 1, 5, 129 ]]
x[2][\niceTry[ 0, 1, 5, 129 ]];
x[2][\niceTry,[ 0, 1, 5, 129 ]];
x[2] = [\niceTry,[ 0, 1, 5, 129 ]];


*/