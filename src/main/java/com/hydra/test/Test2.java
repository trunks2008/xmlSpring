package com.hydra.test;

import com.hydra.factory.DaoFactoryBean;
import com.hydra.factory.TempDaoFactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test2 {
    public static void main(String[] args) {
//        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(AppConfig.class);

        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext();
//        context.register(AppConfig.class);
        context.scan("com.hydra");
        context.refresh();

        DaoFactoryBean bean2 =(DaoFactoryBean) context.getBean("&daoFactoryBean");
        bean2.testBean();

        bean2.setMsg("mag1,msg2,msg3");

        TempDaoFactoryBean bean =(TempDaoFactoryBean) context.getBean("daoFactoryBean");
        bean.test();


    }
}
