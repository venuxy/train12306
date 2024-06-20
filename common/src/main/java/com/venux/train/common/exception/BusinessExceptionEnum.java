package com.venux.train.common.exception;

public enum BusinessExceptionEnum {
    MEMBER_MOBILE_EXIST("手机号已存在"),
    MEMBER_MOBILE_NOT_EXIST("请先获取验证码"),
    MEMBER_MOBILE_CODE_ERROR("验证码错误");


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BusinessExceptionEnum{");
        sb.append("desc='").append(desc).append('\'');
        sb.append('}');
        return sb.toString();
    }

    private String desc;
    BusinessExceptionEnum(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
