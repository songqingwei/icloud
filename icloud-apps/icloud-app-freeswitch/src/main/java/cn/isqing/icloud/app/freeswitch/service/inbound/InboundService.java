package cn.isqing.icloud.app.freeswitch.service.inbound;

public interface InboundService {
    void call(String serverId, String caller, String callee);
}
