package com.gly.zhongnan.entity;

import lombok.Builder;

@Builder
public class User {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String account;
    private String CreateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", CreateDate='" + CreateDate + '\'' +
                '}';
    }
}
