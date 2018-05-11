package com.topbaby.cloud.authservice.entity;

import java.io.Serializable;

/**
 * <p>会员登录信息 DO</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-23
 * @since 1.0.0
 */
public class MemberCredentialsDO implements Serializable {
    private String credential;

    private Long memberSeq;

    private String credentialType;

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public Long getMemberSeq() {
        return memberSeq;
    }

    public void setMemberSeq(Long memberSeq) {
        this.memberSeq = memberSeq;
    }

    public String getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
    }

    @Override
    public String toString() {
        return "MemberCredentialsDO{" +
                                          "credential='" + credential + '\'' +
                                          ", memberSeq=" + memberSeq +
                                          ", credentialType='" + credentialType + '\'' +
                                          '}';
    }
}
