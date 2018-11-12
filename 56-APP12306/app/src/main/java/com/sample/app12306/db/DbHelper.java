package com.sample.app12306.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "app12306.db";
    private static final String stationPairsSqlCreateTable = "create table " + DbSchema.StationPairsTable.NAME + "(" +
            DbSchema.StationPairsTable.Cols.ID + " integer primary key autoincrement, " +
            DbSchema.StationPairsTable.Cols.STATION_FROM + " varchar(128) not null, " +
            DbSchema.StationPairsTable.Cols.STATION_TO + " varchar(128) not null, " +
            DbSchema.StationPairsTable.Cols.DATETIME + " varchar(128) not null" +
            ")";
    private static final String recentStationsSqlCreateTable = "create table " + DbSchema.RecentStatiionsTable.NAME + "(" +
            DbSchema.RecentStatiionsTable.Cols.ID + " integer primary key autoincrement, " +
            DbSchema.RecentStatiionsTable.Cols.STATION_NAME + " varchar(128) not null, " +
            DbSchema.RecentStatiionsTable.Cols.DATETIME + " varchar(128) not null" +
            ")";

    private static final String stationsListSqlCreateTable = "create table " + DbSchema.StationsListTable.NAME + "(" +
            DbSchema.StationsListTable.Cols.ID + " integer primary key autoincrement, " +
            DbSchema.StationsListTable.Cols.STATION_NAME + " varchar(128) not null, " +
            DbSchema.StationsListTable.Cols.DATETIME + " varchar(128) not null" +
            ")";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(stationPairsSqlCreateTable);
        db.execSQL(recentStationsSqlCreateTable);
        db.execSQL(stationsListSqlCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
