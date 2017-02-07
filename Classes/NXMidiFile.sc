NXMidiFile {
    var eventList, bpm, timeSig;

    *new {arg eventList, bpm=60, timeSig="4/4";
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
            event.duration = event.use {|ev| ev.sustain };
            file.write(event.toJSON ++ comma);
            idx = idx + 1;
        };

        file.write("]]");
        file.write("}");
        file.close;

        "Wrote file to: %\n".postf(path);
    }
}
