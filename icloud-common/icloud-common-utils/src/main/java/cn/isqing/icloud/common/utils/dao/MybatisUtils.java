package cn.isqing.icloud.common.utils.dao;

import cn.isqing.icloud.common.utils.dto.BaseException;
import cn.isqing.icloud.common.api.enums.ResCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
public class MybatisUtils {

    /**
     * 每次处理100条
     */
    private static final int BATCH_SIZE = 100;

    /**
     * 批量处理修改或者插入:有select的话会被刷新
     *
     * @param list        需要被处理的数据
     * @param mapperClass Mybatis的Mapper类
     * @param consumer    自定义处理逻辑
     */
    public static <T, M> void batchSave(SqlSessionFactory sqlSessionFactory, List<T> list, Class<M> mapperClass,BiConsumer<T, M> consumer) {
        SqlSession batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        // 如果是默认的DefaultSqlSessionFactory 里面是关闭了自动提交的
        try {
            M mapper = batchSqlSession.getMapper(mapperClass);
            int size = list.size();
            for (int i = 0; i < size; i++) {
                consumer.accept(list.get(i), mapper);
                if ((i % BATCH_SIZE == 0) || i == size - 1) {
                    batchSqlSession.flushStatements();
                }
            }
            // TransactionSynchronizationManager.isActualTransactionActive() 是否开起来事务 也就是将自动提交关闭
            // 如果不强制提交：没有updates/deletes/inserts调用的话，不会提交事务
            batchSqlSession.commit(true);
        } catch (Exception e) {
            batchSqlSession.rollback();
            String msg = "批处理异常";
            log.error(msg + ":" + e.getMessage(), e);
            throw new BaseException(ResCodeEnum.ERROR.getCode(),msg,e);
        } finally {
            batchSqlSession.close();
        }
    }

}
