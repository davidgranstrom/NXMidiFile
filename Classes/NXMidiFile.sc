NXMidiFile {
    classvar <>cmd;
    var eventList, bpm, timeSig;
    var jsonPath;

    *new {arg eventList, bpm=60, timeSig="4/4";
        ^super.new.init(eventList, bpm, timeSig);
    }

    *initClass {
        // expected to be in $PATH, but can be set to custom path
        cmd = "json2midi";
    }

    init {arg ... args;
        // write json to /tmp by default
        jsonPath = NXJsonFileWriter(*args).write;
    }

    write {arg path;
        var exe = cmd ++ " --input % --output %";
        path = path.standardizePath;
        exe.format(jsonPath, path).unixCmd {arg res, pid;
            if (res == 0) {
                "Wrote MIDI file to: %\n".postf(path);
            } {
                "Error writing file".error;
            }
        };
    }
}

NXJsonFileWriter {
    var eventList, bpm, timeSig;

    *new {arg eventList, bpm, timeSig;
        ^super.newCopyArgs(eventList, bpm, timeSig);
    }

    write {arg path;
        var deltas, events, file;
        var lastEvent, idx = 0;

        path = path ?? {
            PathName.tmp +/+ "eventlist-%.json".format(UniqueID.next);
        };

        file = File(path.standardizePath, "w");
        file.write("{");
        file.write("\"bpm\":%,".format(bpm));
        file.write("\"timeSig\":%,".format(timeSig.quote));
        file.write("\"tracks\":[[");

        #deltas, events = eventList.flop;
        lastEvent = events.size - 1; // should always be equal to deltas.size
        idx = 0;

        [ deltas, events ].flopWith {|delta, event|
            var comma = (idx == lastEvent).if("", ",");
            event.absTime = delta;
            event.duration = event.use {|ev| ev.sustain / (bpm/60) };
            file.write(event.toJSON ++ comma);
            idx = idx + 1;
        };

        file.write("]]");
        file.write("}");
        file.close;
        ^path;
    }
}
