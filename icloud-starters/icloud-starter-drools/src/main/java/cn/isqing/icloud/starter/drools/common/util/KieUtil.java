package cn.isqing.icloud.starter.drools.common.util;

import cn.isqing.icloud.common.api.enums.ResCodeEnum;
import cn.isqing.icloud.common.utils.dto.BaseException;
import cn.isqing.icloud.starter.drools.common.dto.RuleKeyDto;
import cn.isqing.icloud.starter.drools.common.dto.SyncResVariableDto;
import cn.isqing.icloud.starter.drools.dao.entity.Component;
import cn.isqing.icloud.starter.drools.dao.entity.RuleTemplate;
import cn.isqing.icloud.starter.variable.api.dto.ApiVariableDto;
import lombok.extern.slf4j.Slf4j;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieBase;
import org.kie.internal.utils.KieHelper;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
public class KieUtil {
    private KieUtil() {
    }

    public static final String PACKAGE_NAME = "cn.isqing.icloud.starter.drools.rules";

    public static final Map<RuleKeyDto, KieHelper> helperMap = new ConcurrentHashMap<>();
    public static final Map<RuleKeyDto, KieBase> baseMap = new ConcurrentHashMap<>();
    public static final Map<RuleKeyDto, Map<String, ApiVariableDto>> variableMap = new ConcurrentHashMap<>();
    //  Map<Long, SyncResVariableDto> <规则id,>
    public static final Map<RuleKeyDto, Map<Long, SyncResVariableDto>> syncResMap = new ConcurrentHashMap<>();
    public static final Map<RuleKeyDto, List<List<Component>>> actionMap = new ConcurrentHashMap<>();


    public static KieBase getKieBase(RuleKeyDto key) {
        return baseMap.get(key);
    }

    public static KieHelper getKieHelper(RuleKeyDto key) {
        return helperMap.computeIfAbsent(key, k -> new KieHelper());
    }

    public static String getDrl(RuleKeyDto key, List<RuleTemplate> list, Map<Long, String> map) {
        List<Map<String, String>> ruleAttributes = new ArrayList<>();

        list.forEach(r -> {
            Map<String, String> rule = new HashMap<>();
            rule.put("id", r.getId().toString());
            rule.put("domain", r.getDomain().toString());
            rule.put("busiCode", key.getBusiCode());
            rule.put("condition", map.get(r.getId()));
            rule.put("orgId", r.getOrgId().toString());
            rule.put("cron", r.getCron());

            if (r.getRefId().longValue()==0L) {
                rule.put("refFunction", "null");
            } else {
                rule.put("refFunction", "$data.getV"+r.getRefId().longValue()+"().toString()");
            }

            ruleAttributes.add(rule);
        });

        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String drl;
        try {
            FileInputStream inputStream = new FileInputStream(ResourceUtils.getFile("classpath:RuleTemplate.drt"));
            drl = compiler.compile(ruleAttributes, inputStream);
        } catch (FileNotFoundException e) {
            String tip = "模版文件不存在";
            log.error(tip, e);
            throw new BaseException(ResCodeEnum.NOTFIND.getCode(), "模版文件不存在", e);
        }
        return drl;
    }
}
