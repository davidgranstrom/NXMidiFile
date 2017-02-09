+ Pattern {
    writeMIDIFile {arg duration=1, path, clock(TempoClock.default);
        var eventList = this.asEventList(duration);
        var bpm = 60 * clock.tempo;

        NXMidiFile(eventList, bpm).write(path);
    }

    asEventList {arg duration=1, protoEvent, clock, timeOffset=0;
        var beats, tempo, maxTime;

        var eventList = [];
        var stream = this.asStream;
        var proto = protoEvent ?? { Event.default };

        beats = timeOffset;
        clock = clock ?? { TempoClock.default };
        tempo = clock.tempo;
        maxTime = timeOffset + duration;

        Routine {
            var ev;
            thisThread.clock = clock;
            while ({
                thisThread.beats = beats;
                ev = stream.next(proto.copy);
                (maxTime >= beats) and:{ev.notNil}
            }, {
                if (ev.isRest.not) {
                    eventList = eventList.add(
                        [ ev.timingOffset * tempo.reciprocal + ev.lag + beats, ev ]
                    );
                };
                beats = ev.delta * tempo.reciprocal + beats
            })
        }.next;
        ^eventList;
    }
}
