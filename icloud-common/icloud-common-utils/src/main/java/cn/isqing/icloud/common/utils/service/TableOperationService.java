package cn.isqing.icloud.common.utils.service;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.utils.dto.TableOperationDto;

/**
 * 通用表操作服务接口
 *
 * @author songqingwei
 * @version 1.0
 */
public interface TableOperationService {

    /**
     * 执行表操作
     *
     * @param tableOperationDto 表操作参数
     * @return 操作结果
     */
    Response<Object> execute(TableOperationDto tableOperationDto);

    /**
     * 分页查询
     *
     * @param tableOperationDto 表操作参数
     * @return 查询结果
     */
    Response<Object> pageQuery(TableOperationDto tableOperationDto);

    /**
     * 新增数据
     *
     * @param tableOperationDto 表操作参数
     * @return 新增结果
     */
    Response<Object> insert(TableOperationDto tableOperationDto);

    /**
     * 更新数据
     *
     * @param tableOperationDto 表操作参数
     * @return 更新结果
     */
    Response<Object> update(TableOperationDto tableOperationDto);

    /**
     * 删除数据
     *
     * @param tableOperationDto 表操作参数
     * @return 删除结果
     */
    Response<Object> delete(TableOperationDto tableOperationDto);

    /**
     * 查询详情（根据ID）
     *
     * @param tableOperationDto 表操作参数
     * @return 查询结果
     */
    Response<Object> detail(TableOperationDto tableOperationDto);

    /**
     * 列表查询（不分页）
     *
     * @param tableOperationDto 表操作参数
     * @return 查询结果
     */
    Response<Object> listQuery(TableOperationDto tableOperationDto);
}
