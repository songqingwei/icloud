package cn.isqing.icloud.common.utils.dto;

import java.util.ArrayList;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class Demo {
    public static void main(String[] args) {
        ArrayList<Object> list = new ArrayList<>();
        Object list1 = list;
        System.out.println(list1.getClass());
    }
}
