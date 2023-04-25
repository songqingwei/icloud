package cn.isqing.icloud.starter.drools.dao.mapper;

import cn.isqing.icloud.common.utils.dao.BaseMapper;
import cn.isqing.icloud.starter.drools.dao.entity.RunLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Repository
public interface RunLogMapper extends BaseMapper<RunLog> {
    @Select("select * from run_log order by id desc limit 0,1")
    RunLog getLastRecord();

    @Select("select id from run_log where busi_date=#{busiDate} limit 0,1")
    Long getOneIdBusiDate(@Param("busiDate") String busiDate);

    @Select("select id from run_log where id=#{id} busi_date=#{busiDate} ")
    Long matchBusiDate(@Param("id") Long id, @Param("busiDate") String busiDate);

    @Select("select id from run_log where id=#{id} busi_date>#{busiDate} ")
    Long greaterBusiDate(@Param("id") Long id, @Param("busiDate") String busiDate);

    @Select("select id from run_log where id=#{id} busi_date<#{busiDate} ")
    Long lessBusiDate(@Param("id") Long id, @Param("busiDate") String busiDate);

    @Select("select * from run_log where id>#{from} and id<=#{end} and fail_num<=#{maxRetry} busi_date<#{busiDate} ")
    List<RunLog> getPendingSubJobRange(@Param("from") Long from,@Param("end") Long end,@Param("maxRetry") Integer maxRetry, @Param("busiDate") String busiDate);
}
