package cn.isqing.icloud.common.utils.bean;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.utils.json.JsonUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * bean转换工具
 *
 * @author
 */
@Slf4j
public class BeanConversionUtil {

    /**
     * 泛型方法，将List<A>转换为List<B>
     */
    public static <A, B> List<B> convertList(List<A> list, Supplier<B> supplier) {
        if(CollectionUtils.isEmpty(list)){
            return Collections.EMPTY_LIST;
        }
        List<B> resultList = new ArrayList<>();
        for (A a : list) {
            B b = supplier.get();
            SpringBeanUtils.copyProperties(a, b);
            resultList.add(b);
        }
        return resultList;
    }

    public static <A, B> B convert(A a, Supplier<B> supplier) {
        if (a == null) {
            return null;
        }
        B b = supplier.get();
        SpringBeanUtils.copyProperties(a, b);
        return b;
    }

    /**
     *
     * @param list
     * @param clazz
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A, B> Response<List<B>> convertListByJson(List<A> list, Class<B> clazz) {
        Response<String> res = JsonUtil.toJsonRes(list);
        if(!res.isSuccess()){
            return Response.withData(res,null);
        }
        TypeReference<List<B>> typeReference = new TypeReference<List<B>>(List.class, clazz) {};
        try {
            return Response.withData(res,JSON.parseObject(res.getData(), typeReference));
        } catch (Exception e) {
            log.warn("对象转json异常:{},{}", e.getMessage(),res.getData());
            return Response.error("对象转json异常");
        }
    }

}
