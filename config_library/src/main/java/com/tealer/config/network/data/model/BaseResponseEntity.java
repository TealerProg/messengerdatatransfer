package com.tealer.config.network.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

/**
 * 网络响应结果对应的实体类
 *
 * @author Created by lipengbo
 * @email 1162947801@qq.com
 * @time Created on 2022/11/14 12:07
 */
public class BaseResponseEntity implements Parcelable {

    public int httpCode;
    public @Nullable
    String data;
    public Meta meta;

    public BaseResponseEntity() { }

    protected BaseResponseEntity(Parcel in) {
        httpCode = in.readInt();
        data = in.readString();
        meta = in.readParcelable(Meta.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(httpCode);
        dest.writeString(data);
        dest.writeParcelable(meta, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BaseResponseEntity> CREATOR = new Creator<BaseResponseEntity>() {
        @Override
        public BaseResponseEntity createFromParcel(Parcel in) {
            return new BaseResponseEntity(in);
        }

        @Override
        public BaseResponseEntity[] newArray(int size) {
            return new BaseResponseEntity[size];
        }
    };

    @Override
    public String toString() {
        return "BaseResponseEntity{" +
                "httpCode=" + httpCode +
                ", data='" + data + '\'' +
                ", meta=" + meta +
                '}';
    }

    public static class Meta implements Parcelable{
        public int ecode;
        public String emsg;

        public Meta() { }

        public Meta(int ecode, String emsg) {
            this.ecode = ecode;
            this.emsg = emsg;
        }

        protected Meta(Parcel in) {
            ecode = in.readInt();
            emsg = in.readString();
        }

        public static final Creator<Meta> CREATOR = new Creator<Meta>() {
            @Override
            public Meta createFromParcel(Parcel in) {
                return new Meta(in);
            }

            @Override
            public Meta[] newArray(int size) {
                return new Meta[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(ecode);
            dest.writeString(emsg);
        }

        @Override
        public String toString() {
            return "Meta{" +
                    "ecode='" + ecode + '\'' +
                    ", emsg='" + emsg + '\'' +
                    '}';
        }
    }
}
