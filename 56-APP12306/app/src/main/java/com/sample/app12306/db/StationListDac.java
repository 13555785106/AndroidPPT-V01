package com.sample.app12306.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sample.app12306.model.Station;

import java.util.ArrayList;
import java.util.List;


public class StationListDac {
    private static StationListDac instance;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private StationListDac(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DbHelper(mContext).getWritableDatabase();
    }

    public static StationListDac get(Context context) {
        if (instance == null)
            instance = new StationListDac(context);
        return instance;
    }

    public long add(Station station) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.StationsListTable.Cols.STATION_NAME, station.getStationName());
        values.put(DbSchema.StationsListTable.Cols.DATETIME, station.getDateTime());
        return mDatabase.insert(DbSchema.StationsListTable.NAME, null, values);
    }

    public int chg(Station station) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.StationsListTable.Cols.STATION_NAME, station.getStationName());
        values.put(DbSchema.StationsListTable.Cols.DATETIME, station.getDateTime());
        return mDatabase.update(DbSchema.StationsListTable.NAME, values, DbSchema.StationsListTable.Cols.ID + "=?", new String[]{Integer.toString(station.getId())});
    }

    public int del(String stationName) {
        return mDatabase.delete(DbSchema.StationsListTable.NAME,
                DbSchema.StationsListTable.Cols.STATION_NAME + " =?", new String[]{stationName});

    }

    public List<Station> all(String stationName) {
        List<Station> stationList = new ArrayList<>();
        Cursor cursor = mDatabase.query(DbSchema.StationsListTable.NAME, null, DbSchema.StationsListTable.Cols.STATION_NAME + " LIKE '%'||" + "?" + "||'%'", new String[]{stationName}, null, null, DbSchema.StationsListTable.Cols.DATETIME + " DESC");
        int idIndex = cursor.getColumnIndex(DbSchema.StationsListTable.Cols.ID);
        int stationNameIndex = cursor.getColumnIndex(DbSchema.StationsListTable.Cols.STATION_NAME);
        int dateTimeIndex = cursor.getColumnIndex(DbSchema.StationPairsTable.Cols.DATETIME);
        while (cursor.moveToNext()) {
            Station station = new Station();
            station.setId(cursor.getInt(idIndex));
            station.setStationName(cursor.getString(stationNameIndex));
            station.setDateTime(cursor.getString(dateTimeIndex));
            stationList.add(station);
        }
        cursor.close();
        return stationList;
    }
}
