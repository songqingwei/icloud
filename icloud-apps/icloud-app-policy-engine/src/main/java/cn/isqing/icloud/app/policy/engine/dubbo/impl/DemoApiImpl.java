package cn.isqing.icloud.app.policy.engine.dubbo.impl;

import cn.isqing.icloud.app.policy.engine.dubbo.api.DemoApi;
import cn.isqing.icloud.common.api.dto.Response;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.Map;

@Service(group = "demo", timeout = 60000, retries = -1, version = "1.0.0")
public class DemoApiImpl implements DemoApi {
    @Override
    public Response<Map<String, String>> getMap(Map<String, String> map) {
        return Response.success(map);
    }
}
