package com.hydra.test;

import com.hydra.service.UserService;
import org.spring.util.BeanFactory;

public class Test {
    public static void main(String[] args) {
        BeanFactory beanFactory=new BeanFactory("spring.xml");
        UserService service=(UserService) beanFactory.getBean("service");
        service.find();
    }
}
