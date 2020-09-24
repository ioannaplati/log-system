namespace java thriftGenerated

enum Level {
    INFO = 0,
    WARN = 1,
    ERROR = 2
}

enum Room {
    KITCHEN = 0,
    BEDROOM = 1,
    LIVING_ROOM = 2,
    BATHROOM = 3
}

enum Temperature {
    ROOM_TEMP = 0,
    A_BIT_COLD = 1,
    A_BIT_WARM = 2,
    TOO_COLD = 3,
    TOO_WARM = 4
}

struct LoggingEvent {
    1: required i16 v,
    2: required i64 time,
    3: required string m,
    4: optional Room room,
    5: optional Level level,
    6: optional Temperature temperature
}

service ThriftService {
    void logRoomTemperature(1: LoggingEvent e)
}