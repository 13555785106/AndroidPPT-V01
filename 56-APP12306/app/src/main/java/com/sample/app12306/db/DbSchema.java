package com.sample.app12306.db;

import java.text.SimpleDateFormat;

/**
 * Created by xiaojf on 18/9/16.
 */

public final class DbSchema {
    public static SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static final class StationPairsTable {
        public static final String NAME = "stationPairs";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String STATION_FROM = "stationFrom";
            public static final String STATION_TO = "stationTo";
            public static final String DATETIME = "dateTime";
        }
    }

    public static final class RecentStatiionsTable {
        public static final String NAME = "recentStations";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String STATION_NAME = "stationName";
            public static final String DATETIME = "dateTime";
        }
    }

    public static final class StationsListTable {
        public static final String NAME = "stationsList";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String STATION_NAME = "stationName";
            public static final String DATETIME = "dateTime";
        }
    }
}
