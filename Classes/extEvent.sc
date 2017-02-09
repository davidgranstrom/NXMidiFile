+ Event {
    toJSON {arg defaultEventValues;
        var addKeyValuePair, getValue, lastValue, validKeys;
        var filteredEvent;
        var json = "{"; // start json object

        addKeyValuePair = {|key, value, lastValue=false|
            var comma = lastValue.not.if(",", "");
            if (value.isNumber.not) {
                value = value.asString.quote;
            };
            json = json ++ "\"%\":%%".format(key, value, comma);
        };

        getValue = {arg key;
            this.use {|f| f.perform(key); };
        };

        // json2midi types
        validKeys = #[
            eventType,
            absTime,
            midinote,
            channel,
            velocity,
            duration,
            ccNum,
            ccVal,
        ];

        defaultEventValues = defaultEventValues ?? { #[ midinote, velocity ] };

        // only select the keys that makes sense for midi2json
        filteredEvent = this.select {|item, key| validKeys.includes(key) };
        defaultEventValues = defaultEventValues.reject {|key| filteredEvent.keys.includes(key) };

        if (filteredEvent.isEmpty.not) {
            lastValue = filteredEvent.size - 1;
        } {
            lastValue = defaultEventValues.size - 1;
        };

        defaultEventValues.do {arg key, i;
            addKeyValuePair.(key, getValue.(key), i == lastValue);
        };

        filteredEvent.keysValuesDo {arg key, value, i;
            addKeyValuePair.(key, value, i == lastValue);
        };

        json = json ++ "}";
        ^json;
    }
}
