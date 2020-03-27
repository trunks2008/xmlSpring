package com.hydra.factory;

public class TempDaoFactoryBean {
    String msg1;
    String msg2;
    String msg3;

    public void test(){
        System.out.println("test");
    }

    public void setMsg1(String msg1) {
        this.msg1 = msg1;
    }

    public void setMsg2(String msg2) {
        this.msg2 = msg2;
    }

    public void setMsg3(String msg3) {
        this.msg3 = msg3;
    }
}
