package cn.isqing.icloud.app.freeswitch.dao.mapper;

import cn.isqing.icloud.app.freeswitch.dao.entity.SipRegistrations;
import cn.isqing.icloud.common.utils.dao.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface SipRegistrationsMapper extends BaseMapper<SipRegistrations> {

    @Select("select presence_hosts from sip_registrations where sip_user=#{user} and sip_host = #{domain} limit 0,1")
    String getPresenceHosts(@Param("user") String user,@Param("domain") String domain);
}
