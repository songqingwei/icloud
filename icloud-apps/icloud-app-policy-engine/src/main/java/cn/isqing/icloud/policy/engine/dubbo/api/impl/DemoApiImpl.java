package cn.isqing.icloud.policy.engine.dubbo.api.impl;

import cn.isqing.icloud.common.utils.dto.Response;
import cn.isqing.icloud.policy.engine.dubbo.api.DemoApi;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.Map;
@Service(group = "demo", timeout = 60000, retries = -1, version = "1.0.0")
public class DemoApiImpl implements DemoApi {
    @Override
    public Response<Map<String, String>> getMap(Map<String, String> map) {
        return Response.success(map);
    }
}
