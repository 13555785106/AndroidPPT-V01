package com.sample.app12306.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sample.app12306.model.StationPair;

import java.util.ArrayList;
import java.util.List;

public class StationPairDac {
    private static StationPairDac instance;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private StationPairDac(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DbHelper(mContext).getWritableDatabase();
    }

    public static StationPairDac get(Context context) {
        if (instance == null)
            instance = new StationPairDac(context);
        return instance;
    }

    public long add(StationPair stationPair) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.StationPairsTable.Cols.STATION_FROM, stationPair.getStationFrom());
        values.put(DbSchema.StationPairsTable.Cols.STATION_TO, stationPair.getStationTo());
        values.put(DbSchema.StationPairsTable.Cols.DATETIME, stationPair.getDateTime());
        return mDatabase.insert(DbSchema.StationPairsTable.NAME, null, values);
    }

    public int chg(StationPair stationPair) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.StationPairsTable.Cols.STATION_FROM, stationPair.getStationFrom());
        values.put(DbSchema.StationPairsTable.Cols.STATION_TO, stationPair.getStationTo());
        values.put(DbSchema.StationPairsTable.Cols.DATETIME, stationPair.getDateTime());
        return mDatabase.update(DbSchema.StationPairsTable.NAME, values, DbSchema.StationPairsTable.Cols.ID + "=?", new String[]{Integer.toString(stationPair.getId())});
    }

    public int del(String stationFrom, String stationTo) {
        return mDatabase.delete(DbSchema.StationPairsTable.NAME,
                DbSchema.StationPairsTable.Cols.STATION_FROM + " =? AND "
                        + DbSchema.StationPairsTable.Cols.STATION_TO + "=? ", new String[]{
                        stationFrom, stationTo});

    }

    public List<StationPair> all() {
        List<StationPair> stationPairList = new ArrayList<>();
        Cursor cursor = mDatabase.query(DbSchema.StationPairsTable.NAME, null, null, null, null, null, DbSchema.StationPairsTable.Cols.DATETIME + " DESC");
        int idIndex = cursor.getColumnIndex(DbSchema.StationPairsTable.Cols.ID);
        int stationFromIndex = cursor.getColumnIndex(DbSchema.StationPairsTable.Cols.STATION_FROM);
        int stationToIndex = cursor.getColumnIndex(DbSchema.StationPairsTable.Cols.STATION_TO);
        int dateTimeIndex = cursor.getColumnIndex(DbSchema.StationPairsTable.Cols.DATETIME);
        while (cursor.moveToNext()) {
            StationPair stationPair = new StationPair();
            stationPair.setId(cursor.getInt(idIndex));
            stationPair.setStationFrom(cursor.getString(stationFromIndex));
            stationPair.setStationTo(cursor.getString(stationToIndex));
            stationPair.setDateTime(cursor.getString(dateTimeIndex));
            stationPairList.add(stationPair);
        }
        cursor.close();
        return stationPairList;
    }
}
