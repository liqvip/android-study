package cn.blogss.core.ipc;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Author: Thatcher Li
 * @Date: 2021/2/2
 * @LastEditors: Thatcher Li
 * @LastEditTime: 2021/2/2
 * @Descripttion: 实现 Parcelable 接口的 bean
 */
public class UserParcelable implements Parcelable {
    private int userId;
    private String userName;
    private boolean isMale;

    public UserParcelable(int userId, String userName, boolean isMale) {
        this.userId = userId;
        this.userName = userName;
        this.isMale = isMale;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 序列化，Parcel 内部包装了可序列化的数据，可以在 Binder 中自由传输
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeInt(isMale ? 1 : 0);
    }

    /**
     * 反序列化，由 Creator 来完成，其内部标明了如何创建序列化对象和数组
     */
    public static final Parcelable.Creator<UserParcelable> CREATOR = new Parcelable.Creator<UserParcelable>(){

        @Override
        public UserParcelable createFromParcel(Parcel source) {
            return new UserParcelable(source);
        }

        @Override
        public UserParcelable[] newArray(int size) {
            return new UserParcelable[0];
        }
    };


    public UserParcelable(Parcel source) {
        userId = source.readInt();
        userName = source.readString();
        isMale = source.readInt() == 1;
    }
}
