package cn.isqing.icloud.starter.drools;

import cn.isqing.icloud.common.utils.constants.SqlConstants;
import cn.isqing.icloud.common.utils.enums.status.YesOrNo;
import cn.isqing.icloud.common.utils.kit.ParallelStreamUtil;
import cn.isqing.icloud.common.utils.kit.StrUtil;
import cn.isqing.icloud.common.utils.log.MDCUtil;
import cn.isqing.icloud.common.utils.time.TimeUtil;
import cn.isqing.icloud.starter.drools.dao.entity.*;
import cn.isqing.icloud.starter.drools.dao.mapper.RuleTemplateBusiMapper;
import cn.isqing.icloud.starter.drools.dao.mapper.RuleTemplateMapper;
import cn.isqing.icloud.starter.drools.service.event.impl.RuleTemplateChangeContext;
import cn.isqing.icloud.starter.drools.service.event.impl.RuleTemplateChangeFlow;
import cn.isqing.icloud.starter.drools.service.msg.MsgParserService;
import cn.isqing.icloud.starter.drools.service.msg.dto.TplChangeMsg;
import cn.isqing.icloud.starter.variable.api.VariableInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ApplicationListenerImpl implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${i.drools.init.ruleTpl.pageSize:100}")
    private Integer pageSize;
    @Value("${i.drools.init.ruleTpl.timeOut:300000}")
    private Integer timeOut;
    @Autowired
    private RuleTemplateMapper mapper;
    @Autowired
    private RuleTemplateBusiMapper busiMapper;
    @Autowired
    private MsgParserService msgParserService;
    @Autowired
    private RuleTemplateChangeFlow changeFlow;

    @DubboReference(group = "${i.variable.dubbo.group:iVariable}", timeout = -1, retries = -1, version = "1.0.0")
    private VariableInterface variableInterface;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        init();
    }

    /**
     * 每次重启应用需要缓存规则相关内容
     */
    public void init() {
        MDCUtil.appendTraceId();
        log.info("缓存规则开始...");
        //分页查询规则配置
        List<RuleTemplate> list;
        RuleTemplateCondition condition = new RuleTemplateCondition();
        condition.setIsDel(YesOrNo.NO.ordinal());
        condition.setIsActive(YesOrNo.YES.ordinal());
        condition.setOrderBy(SqlConstants.ID_ASC);
        condition.setSelectFiled(RuleTemplateFiled.ID, RuleTemplateFiled.DOMAIN, RuleTemplateFiled.ACTION_ID);
        condition.setIdConditionMin(0L);
        condition.setLimit(pageSize);
        // 去重容器
        ConcurrentHashMap<String, Long> uniMap = new ConcurrentHashMap<>();
        LocalDateTime now = TimeUtil.now();
        int page = 0;
        do {
            log.info("page:{}", ++page);
            list = mapper.selectByCondition(condition);
            if (list.isEmpty()) {
                break;
            }
            condition.setIdConditionMin(list.get(list.size() - 1).getId() + 1);
            try {
                ParallelStreamUtil.exec(list, r -> {
                    // 查询busiCode
                    RuleTemplateBusiCondition busi = new RuleTemplateBusiCondition();
                    busi.setTid(r.getId());
                    busi.setSelectFiled(RuleTemplateBusiFiled.ID, RuleTemplateBusiFiled.BUSI_CODE);
                    List<RuleTemplateBusi> busiList = busiMapper.selectByCondition(busi);
                    busiList.forEach(b -> {
                        String key = StrUtil.assembleKey(r.getDomain().toString(), r.getActionId().toString(), b.getBusiCode());
                        Long cacheId = uniMap.computeIfAbsent(key, k -> b.getId());
                        if (!cacheId.equals(b.getId())) {
                            return;
                        }
                        TplChangeMsg msg = new TplChangeMsg();
                        msg.setDomain(r.getDomain());
                        msg.setActionId(r.getActionId());
                        msg.setBusiCode(b.getBusiCode());
                        msg.setCreateTime(now);
                        RuleTemplateChangeContext context = new RuleTemplateChangeContext();
                        context.setMsgReq(msgParserService.assembleMsg(msg));
                        changeFlow.exec(context);
                    });
                }, timeOut);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } while (true);

    }
}