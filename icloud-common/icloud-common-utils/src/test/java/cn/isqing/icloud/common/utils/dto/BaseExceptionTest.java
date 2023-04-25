package cn.isqing.icloud.common.utils.dto;

public class BaseExceptionTest {

    public static void main(String[] args) {
        try {
            m4();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void m1(){
        Integer.parseInt("null");
    }

    public static void m2(){
        m1();
    }
    public static void m3(){
        m2();
    }

    public static void m4(){
        try {
            m3();
        } catch (Exception e) {
            throw e;
        }
    }

}