package cn.isqing.icloud.common.utils.dao;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public interface BaseMapper<T> {

    @InsertProvider(method = "insert", type = BaseProvider.class)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(T t);

    @DeleteProvider(method = "del", type = BaseProvider.class)
    int del(T t);

    @DeleteProvider(method = "delById", type = BaseProvider.class)
    int delById(@Param("id") Long id, @Param("c") Class<T> c);

    @DeleteProvider(method = "delByCondition", type = BaseProvider.class)
    int delByCondition(Object c);

    @UpdateProvider(method = "update", type = BaseProvider.class)
    int update(T t);

    @UpdateProvider(method = "lock", type = BaseProvider.class)
    int lock(T t);

    @UpdateProvider(method = "unlock", type = BaseProvider.class)
    int unlock(T t);

    @UpdateProvider(method = "updateByCondition", type = BaseProvider.class)
    int updateByCondition(@Param("t") T t, @Param("c") Object condition);

    @SelectProvider(method = "select", type = BaseProvider.class)
    List<T> select(@Param("t") T t, @Param("fields") String fields, @Param("order") String order);

    @SelectProvider(method = "selectByCondition", type = BaseProvider.class)
    List<T> selectByCondition(Object condition);

    @SelectProvider(method = "selectByCondition", type = BaseProvider.class)
    List<Long> selectLongByCondition(Object condition);

    @SelectProvider(method = "selectByCondition", type = BaseProvider.class)
    List<String> selectStringByCondition(Object condition);

    @SelectProvider(method = "selectByCondition", type = BaseProvider.class)
    List<Object> selectObjectByCondition(Object condition);

    /**
     *
     * @param left 按照_condition规则解析 如果表名为*_condition会解析表名为*,请自定义condition类
     * @param right 按照_condition规则解析
     * @param join l.${filed}=r.${filed},...
     * @return
     */
    @SelectProvider(method = "leftJoinSelect", type = BaseProvider.class)
    List<JSONObject> leftJoinSelect(@Param("left") Object left, @Param("right") Object right, @Param("join") String join);

    /**
     *
     * @param left 按照_condition规则解析 如果表名为*_condition会解析表名为*,请自定义condition类
     * @param right 按照_condition规则解析
     * @param join l.${filed}=r.${filed},...
     * @return
     */
    @SelectProvider(method = "leftJoinCount", type = BaseProvider.class)
    Long leftJoinCount(@Param("left") Object left,@Param("right") Object right, @Param("join") String join);

    /**
     *
     * @param left 按照_condition规则解析 如果表名为*_condition会解析表名为*,请自定义condition类
     * @param right 按照_condition规则解析
     * @param join l.${filed}=r.${filed},...
     * @return
     */
    @SelectProvider(method = "innerJoinSelect", type = BaseProvider.class)
    <R> List<R> innerJoinSelect(@Param("left") Object left,@Param("right") Object right, @Param("join") String join);

    /**
     *
     * @param left 按照_condition规则解析 如果表名为*_condition会解析表名为*,请自定义condition类
     * @param right 按照_condition规则解析
     * @param join l.${filed}=r.${filed},...
     * @return
     */
    @SelectProvider(method = "innerJoinCount", type = BaseProvider.class)
    Long innerJoinCount(@Param("left") Object left,@Param("right") Object right, @Param("join") String join);

    @SelectProvider(method = "count", type = BaseProvider.class)
    Long count(T t);

    @SelectProvider(method = "countByCondition", type = BaseProvider.class)
    Long countByCondition(Object c);

    @SelectProvider(method = "countAll", type = BaseProvider.class)
    Long countAll(Class<T> c);


    /**
     * 滚动分页
     *
     * @param t
     * @param from
     * @param limit
     * @param order
     * @return
     */
    @SelectProvider(method = "getScrollList", type = BaseProvider.class)
    List<T> getScrollList(@Param("t") T t, @Param("from") Long from, @Param("limit") Integer limit,
                          @Param("order") String order);

    /**
     * 正常分页
     *
     * @param t
     * @param limit
     * @param order
     * @return
     */
    @SelectProvider(method = "getPageList", type = BaseProvider.class)
    List<T> getPageList(@Param("t") T t, @Param("limit") Integer limit, @Param("offset") Integer offset,
                        @Param("order") String order);

    @SelectProvider(method = "getPageListSimple", type = BaseProvider.class)
    List<T> getPageListSimple(@Param("c") Class<T> c, @Param("limit") Integer limit,
                              @Param("offset") Integer offset, @Param("order") String order);

    @SelectProvider(method = "first", type = BaseProvider.class)
    T first(@Param("t") T t, @Param("order") String order);

    @SelectProvider(method = "selectById", type = BaseProvider.class)
    T selectById(@Param("id") Long id, @Param("c") Class<T> c);

}
