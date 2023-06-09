package cn.isqing.icloud.starter.variable.service.datasource;

import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.PageResDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.variable.common.dto.UpdateStatusDto;
import cn.isqing.icloud.starter.variable.service.datasource.dto.DataSourceDto;
import cn.isqing.icloud.starter.variable.service.datasource.dto.DataSourceListReq;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public interface DataSourceService {
    Response<PageResDto<DataSourceDto>> list(PageReqDto<DataSourceListReq> dto);

    Response<DataSourceDto> getText(Long id);

    Response<Object> add(DataSourceDto dto);

    Response<Object> edit(DataSourceDto dto);

    Response<Object> sw(UpdateStatusDto dto);

    Response<Object> del(Long id);
}
