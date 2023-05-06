package cn.isqing.icloud.policy.engine.dubbo.api;

import cn.isqing.icloud.common.api.dto.Response;

import java.util.Map;

public interface DemoApi {

    Response<Map<String, String>> getMap(Map<String, String> map);

}
