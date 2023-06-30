package cn.isqing.icloud.app.freeswitch.common.dto;

import lombok.Data;

@Data
public class BridgeDto {

    private String caller;
    private String callerUuid;

    private String callee;
    private String calleeUuid;

    private boolean calleeOk = false;

    private String busiUuid;
}
