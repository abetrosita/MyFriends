package com.example.abetrosita.myfriends;

/**
 * Created by AbetRosita on 1/8/2017.
 */

public class Friend {
    private int m_Id;
    private String mName;
    private String mPhone;
    private String mEmail;

    public Friend(int _id, String name, String phone, String email) {
        m_Id = _id;
        mName = name;
        mPhone = phone;
        mEmail = email;
    }

    public int getId() {
        return m_Id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }
}