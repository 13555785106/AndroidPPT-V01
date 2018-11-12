package com.telecom.criminalintent;

import android.content.Context;
import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by xiaojf on 17/12/24.
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Map<UUID, Crime> mCrimes;
    private List<UUID> mIds;

    private CrimeLab(Context context) {
        mCrimes = new HashMap<>();
        mIds = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);
            mCrimes.put(crime.getId(), crime);
            mIds.add(crime.getId());
        }
    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimes() {
        List<Crime> list = new ArrayList<>();
        for (UUID id : mIds) {

            long start = System.nanoTime();
            Crime crime = mCrimes.get(id);
            long end = System.nanoTime();
            System.out.println(end - start);
            list.add(crime);
        }
        return list;
    }

    public Crime getCrime(UUID id) {
        return mCrimes.get(id);
    }

    public int getIndex(UUID id) {
        return mIds.indexOf(id);
    }
}
