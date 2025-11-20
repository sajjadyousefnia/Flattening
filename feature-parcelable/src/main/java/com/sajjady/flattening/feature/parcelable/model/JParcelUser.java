package com.sajjady.flattening.feature.parcelable.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class JParcelUser implements Parcelable {
    private final long id;
    private final String name;
    private final String email;

    public JParcelUser(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    protected JParcelUser(Parcel in) {
        id = in.readLong();
        name = in.readString();
        email = in.readString();
    }

    public static final Creator<JParcelUser> CREATOR = new Creator<JParcelUser>() {
        @Override
        public JParcelUser createFromParcel(Parcel in) {
            return new JParcelUser(in);
        }

        @Override
        public JParcelUser[] newArray(int size) {
            return new JParcelUser[size];
        }
    };

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(email);
    }

    @NonNull
    @Override
    public String toString() {
        return "JParcelUser{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            '}';
    }
}
