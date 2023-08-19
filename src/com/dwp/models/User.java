package com.dwp.models;

import com.dwp.utils.AppUtil;

public class User extends BaseEntity {
    public User() {
        this.setId(AppUtil.getRandomId());
    }

}
