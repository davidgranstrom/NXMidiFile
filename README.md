NXMidiFile
==========

Convenience class for generating MIDI files from Patterns using [`json-to-midi`](https://github.com/davidgranstrom/json-to-midi) command line executable.

## Usage

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
