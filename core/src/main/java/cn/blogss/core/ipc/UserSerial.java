package cn.blogss.core.ipc;

import java.io.Serializable;

/**
 * @Author: Thatcher Li
 * @Date: 2021/2/2
 * @LastEditors: Thatcher Li
 * @LastEditTime: 2021/2/2
 * @Descripttion: 可序列化的一个 bean
 */
public class UserSerial implements Serializable {
    /**
     * 序列化注意两点：
     * 1. 静态成员属于类，不属于对象，所以不会参与序列化过程
     * 2. 用 transient 关键字标记的成员变量不参与序列化过程
     */

    private static final long serialVersionUID = 1L;    // 指定 serialVersionUID ,保障在类结构发生改变的时候，仍然能够反序列化成功

    private int userId;

    private String userName;

    private boolean isMale;

    public UserSerial(int userId, String userName, boolean isMale) {
        this.userId = userId;
        this.userName = userName;
        this.isMale = isMale;
    }
}
