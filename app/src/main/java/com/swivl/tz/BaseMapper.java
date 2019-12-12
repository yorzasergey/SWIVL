package com.swivl.tz;

public interface BaseMapper<IN, OUT> {

    public OUT mapFrom(IN inData);
}
