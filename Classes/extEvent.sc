+ Event {
    toJSON {arg defaultEventValues;
        var addKeyValuePair, getValue, lastValue;
        var json = "{ "; // start json object

        addKeyValuePair = {|key, value, lastValue=false|
            var comma = lastValue.not.if(",", "");
            if (value.isNumber.not) {
                value = value.asString.quote;
            };
            json = json ++ "\"%\": %% ".format(key, value, comma);
        };

        getValue = {arg key;
            this.use {|f| f.perform(key); };
        };

        defaultEventValues = defaultEventValues ?? { #[ midinote, velocity ] };

        if (this.keys.isEmpty.not) {
            lastValue = this.size - 1;
        } {
            lastValue = defaultEventValues.size - 1;
        };

        // write coerced values
        defaultEventValues.do {arg key, i;
            addKeyValuePair.(key, getValue.(key), i == lastValue);
        };

        // add the values defined in the event
        this.keysValuesDo {arg key, value, i;
            addKeyValuePair.(key, value, i == lastValue);
        };

        json = json ++ "}";
        ^json;
    }
}
