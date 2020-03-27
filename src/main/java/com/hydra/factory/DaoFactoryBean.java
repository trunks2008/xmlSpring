package com.hydra.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * 如果你的类实现了FactoryBean
 * 那么spring容器当中存在两个对象
 * 一个是getObject()返回的对象
 * 还有一个是当前对象
 *
 * getObject得到对象存的是当前类指定的名字
 * 当前对象是"&"+当前类的名字
 */
@Component("daoFactoryBean")
public class DaoFactoryBean implements FactoryBean {
    String msg;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void testBean(){
        System.out.println("testBean");
    }

    //getObject方法中可以实现对目标bean的配置
    @Override
    public Object getObject() throws Exception {
        TempDaoFactoryBean bean= new TempDaoFactoryBean();
        bean.setMsg1(msg.split(",")[0]);
        bean.setMsg2(msg.split(",")[1]);
        bean.setMsg3(msg.split(",")[2]);
        return bean;
    }

    @Override
    public Class<?> getObjectType() {
        return TempDaoFactoryBean.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
