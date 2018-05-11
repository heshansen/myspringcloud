package com.topbaby.cloud.authservice.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>会员DO</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-23
 * @since 1.0.0
 */
public class MemberDO implements Serializable {

    private static final long serialVersionUID = -1011237588626489822L;
    private Long memberSeq;
    private String grade;
    private String password;
    private String name;
    private String status;
    private String nickName;
    /**
     * 性别<br>
     * '0'--男<br>
     * '1'--女<br>
     * '2'--非自然人<br>
     */
    private String gender;
    private Date birthday;
    /**
     * 头像id
     */
    private Long portraitId;
    /**
     * 现居城市
     */
    private String city;
    /**
     * 欢迎信息
     */
    private String welcomeMsg;
    private String idType;
    private String idNo;
    private Integer idVerified;
    private String mobile;
    private Integer mobileVerified;
    private Integer loginSuccessCount;
    private Date lastLoginSuccessTime;
    private Integer loginFailureCount;
    private Date lastLoginFailureTime;
    private Date createDate;
    private String createUser;
    private String lastUpdateUser;
    private Date lastUpdateDate;
    private boolean firstLogin;
    //孕育状态（0-无备孕打算；1-备孕中；2-怀孕中；3-已有宝宝）
    private String pregnantStatus;
    //预产期
    private Date expectDate;

    public Long getMemberSeq() {
        return memberSeq;
    }

    public void setMemberSeq(Long memberSeq) {
        this.memberSeq = memberSeq;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Long getPortraitId() {
        return portraitId;
    }

    public void setPortraitId(Long portraitId) {
        this.portraitId = portraitId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWelcomeMsg() {
        return welcomeMsg;
    }

    public void setWelcomeMsg(String welcomeMsg) {
        this.welcomeMsg = welcomeMsg;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public Integer getIdVerified() {
        return idVerified;
    }

    public void setIdVerified(Integer idVerified) {
        this.idVerified = idVerified;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(Integer mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public Integer getLoginSuccessCount() {
        return loginSuccessCount;
    }

    public void setLoginSuccessCount(Integer loginSuccessCount) {
        this.loginSuccessCount = loginSuccessCount;
    }

    public Date getLastLoginSuccessTime() {
        return lastLoginSuccessTime;
    }

    public void setLastLoginSuccessTime(Date lastLoginSuccessTime) {
        this.lastLoginSuccessTime = lastLoginSuccessTime;
    }

    public Integer getLoginFailureCount() {
        return loginFailureCount;
    }

    public void setLoginFailureCount(Integer loginFailureCount) {
        this.loginFailureCount = loginFailureCount;
    }

    public Date getLastLoginFailureTime() {
        return lastLoginFailureTime;
    }

    public void setLastLoginFailureTime(Date lastLoginFailureTime) {
        this.lastLoginFailureTime = lastLoginFailureTime;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public String getPregnantStatus() {
        return pregnantStatus;
    }

    public void setPregnantStatus(String pregnantStatus) {
        this.pregnantStatus = pregnantStatus;
    }

    public Date getExpectDate() {
        return expectDate;
    }

    public void setExpectDate(Date expectDate) {
        this.expectDate = expectDate;
    }
}
