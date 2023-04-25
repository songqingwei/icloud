package cn.isqing.icloud.starter.drools.dao.mapper;

import cn.isqing.icloud.common.utils.dao.BaseMapper;
import cn.isqing.icloud.starter.drools.dao.entity.FixedNumAllotterLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public interface FixedNumAllotterLogMapper extends BaseMapper<FixedNumAllotterLog> {

    @Update("update allotter_fixed_num set num=num+1,ref=ref+#{ref} where uid=#{uid} ")
    int incrByUid(@Param("uid") String uid, @Param("ref")BigDecimal ref);
}
