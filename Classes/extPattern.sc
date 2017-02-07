+ Pattern {
    eventList {arg duration = 10, eventPrototype, clock;
        var event, beats, tempo, maxTime;
        var timeOffset = 0;
        var eventList = [];
        var stream = this.asStream;
        var proto = eventPrototype ?? { () };

        event = event ? Event.default;
        event = event.copy.putAll(proto);
        beats = timeOffset;

        clock = clock ?? { TempoClock.default };
        tempo = clock.tempo;

        maxTime = timeOffset + duration;

        Routine {
            var ev;
            thisThread.clock = clock;
            while ({
                thisThread.beats = beats;
                ev = stream.next(event.copy);
                (maxTime >= beats) and:{ev.notNil}
            }, {
                ev.putAll(proto);
                if (ev.isRest.not) {
                    eventList = eventList.add([ beats, ev ]);
                };
                beats = ev.delta * tempo.reciprocal + beats
            })
        }.next;

        ^eventList;
    }
}
