package com.sample.app12306.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sample.app12306.model.Station;

import java.util.ArrayList;
import java.util.List;


public class RecentStationDac {
    private static RecentStationDac instance;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private RecentStationDac(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DbHelper(mContext).getWritableDatabase();
    }

    public static RecentStationDac get(Context context) {
        if (instance == null)
            instance = new RecentStationDac(context);
        return instance;
    }

    public long add(Station station) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.RecentStatiionsTable.Cols.STATION_NAME, station.getStationName());
        values.put(DbSchema.RecentStatiionsTable.Cols.DATETIME, station.getDateTime());
        return mDatabase.insert(DbSchema.RecentStatiionsTable.NAME, null, values);
    }

    public int chg(Station station) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.RecentStatiionsTable.Cols.STATION_NAME, station.getStationName());
        values.put(DbSchema.RecentStatiionsTable.Cols.DATETIME, station.getDateTime());
        return mDatabase.update(DbSchema.RecentStatiionsTable.NAME, values, DbSchema.RecentStatiionsTable.Cols.ID + "=?", new String[]{Integer.toString(station.getId())});
    }

    public int del(String stationName) {
        return mDatabase.delete(DbSchema.RecentStatiionsTable.NAME,
                DbSchema.RecentStatiionsTable.Cols.STATION_NAME + " =?", new String[]{stationName});

    }

    public List<Station> all() {
        List<Station> stationList = new ArrayList<>();
        Cursor cursor = mDatabase.query(DbSchema.RecentStatiionsTable.NAME, null, null, null, null, null, DbSchema.RecentStatiionsTable.Cols.DATETIME + " DESC", "  8 ");
        int idIndex = cursor.getColumnIndex(DbSchema.RecentStatiionsTable.Cols.ID);
        int stationNameIndex = cursor.getColumnIndex(DbSchema.RecentStatiionsTable.Cols.STATION_NAME);
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
