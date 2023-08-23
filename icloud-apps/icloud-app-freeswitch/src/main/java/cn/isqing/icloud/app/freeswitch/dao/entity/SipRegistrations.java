package cn.isqing.icloud.app.freeswitch.dao.entity;

import lombok.Data;

@Data
public class SipRegistrations {

    private String callId;

    private String sipUser;

    private String sipHost;

    private String presenceHosts;

    // 注册终端的SIP地址
    private String contact;

    private String status;

    private String pingStatus;

    private Integer pingCount;

    private Long pingTime;

    private Integer forcePing;

    private String rpid;

    private Long expires;

    private Integer pingExpires;

    private String userAgent;

    private String serverUser;

    private String serverHost;

    private String profileName;

    private String hostname;

    private String networkIp;

    private String networkPort;

    private String sipUsername;

    private String sipRealm;

    private String mwiUser;

    private String mwiHost;

    private String origServerHost;

    private String origHostname;

    private String subHost;

}
