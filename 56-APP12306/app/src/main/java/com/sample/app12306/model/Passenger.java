package com.sample.app12306.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Passenger implements Parcelable {
    public static final Parcelable.Creator<Passenger> CREATOR = new Creator<Passenger>() {
        @Override
        public Passenger[] newArray(int size) {
            return new Passenger[size];
        }

        @Override
        public Passenger createFromParcel(Parcel in) {
            return new Passenger(in);
        }
    };
    private int id = -1;
    private String name = "";
    private String idCardNumber = "";
    private String phoneNumber = "";

    public Passenger() {
    }

    public Passenger(Parcel in) {
        id = in.readInt();
        name = in.readString();
        idCardNumber = in.readString();
        phoneNumber = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", idCardNumber='" + idCardNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(name);
        out.writeString(idCardNumber);
        out.writeString(phoneNumber);
    }
}
