package cn.isqing.icloud.common.utils.dto;

import cn.isqing.icloud.common.utils.kit.LockUtil;

import java.util.ArrayList;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class Demo {
    public static void main(String[] args) {
        try {
            boolean b = LockUtil.renewalPo(null, null, null);
        } catch (Exception e) {

        }
        while (true) {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
