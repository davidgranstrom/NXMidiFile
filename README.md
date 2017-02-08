NXMidiFile
==========

Convenience class for generating MIDI files from Patterns using [`json-to-midi`](https://github.com/davidgranstrom/json-to-midi) command line executable.

## Basic usage (writeMIDIFile convenience method)

    (
        var dur = Pseg(Pseq([ 1/4, 1 ], inf), Pseq([ 10, 10 ], inf), \sine); 

        TempoClock.default.tempo = 120/60;
        NXMidiFile.cmd = "/path/to/json2midi";

        Pbind(
            \eventType, \note, // json2midi event key
            \degree, Pseq([ 1, 3, 5, 8, 5, 3, 1 ] - 1, inf),
            \octave, Pseq([ 3, 4, 4, 5, 5, 6 ], inf),
            \velocity, 127 * Pwhite(0.25, 0.75),
            \dur, dur
        ).writeMIDIFile(60, "~/Desktop/notes.mid");

        Pbind(
            \eventType, \cc, // json2midi event key
            \ccVal, Pseg(Pseq([ 0, 127 ], inf), Pseq([ 10, 10 ], inf), \sine),
            \ccNum, 3,
            \dur, dur
        ).writeMIDIFile(60, "~/Desktop/cc.mid");
    )

## Usage

Generate an eventList from a Pattern. NXMidiFile will parse it to json format and pass it to `json2midi` which will generate the actual midi file.

    (
        var pattern, eventList;

        pattern = Pbind(
            \eventType, \note, // json2midi event key
            \degree, Pseq([ 1, 3, 5, 8, 5, 3, 1 ] - 1, inf),
            \octave, Pseq([ 3, 4, 4, 5, 5, 6 ], inf),
            \legato, 0.9,
            \stretch, 1,
            \velocity, 127 * Pwhite(0.25, 0.75),
            \dur, Pseg(Pseq([ 0.05, 1 ], inf), Pseq([ 10, 10 ], inf), \sine)
        );

        eventList = pattern.eventList(20); // generate 20 beats
        NXMidiFile(eventList).write("~/Desktop/output.mid");
    )

By default NXMidiFile expects to find the `json2midi` executable in `$PATH`, but it can also be set explicitly:

    NXMidiFile.cmd = "/path/to/json2midi";


## TODO

* Write native documentation
* Include motivation in README
