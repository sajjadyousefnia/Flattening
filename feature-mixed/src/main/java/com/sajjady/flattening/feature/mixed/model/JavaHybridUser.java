package com.sajjady.flattening.feature.mixed.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serial;
import java.io.Serializable;

public class JavaHybridUser implements Parcelable, Serializable {
    @Serial
    private static final long serialVersionUID = 7L;

    private final long id;
    private final String name;
    private final String email;

    public JavaHybridUser(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    protected JavaHybridUser(Parcel in) {
        id = in.readLong();
        name = in.readString();
        email = in.readString();
    }

    public static final Creator<JavaHybridUser> CREATOR = new Creator<JavaHybridUser>() {
        @Override
        public JavaHybridUser createFromParcel(Parcel in) {
            return new JavaHybridUser(in);
        }

        @Override
        public JavaHybridUser[] newArray(int size) {
            return new JavaHybridUser[size];
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
        return "JavaHybridUser{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            '}';
    }
}
