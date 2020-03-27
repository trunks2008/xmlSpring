package org.spring.util;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BeanFactory {

    Map<String, Object> map = new HashMap<>();

    public BeanFactory(String xml) {
        parseXml(xml);
    }

    public void parseXml(String xml) {
        File file = new File(this.getClass().getResource("/").getPath() + "//" + xml);
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(file);
            Element elementRoot = document.getRootElement();
            Attribute attribute = elementRoot.attribute("default"); //判断是否自动装配
            // 实际spring中手动装配优先级高于自动装配
            boolean flag = false;
            if (attribute != null) {
                flag = true;
            }

            for (Iterator<Element> itFirst = elementRoot.elementIterator(); itFirst.hasNext(); ) {
                /**
                 * setup1、实例化对象
                 */
                Element elementFirstChild = itFirst.next();
                Attribute attributeId = elementFirstChild.attribute("id");
                String beanName = attributeId.getValue();

                Attribute attributeClazz = elementFirstChild.attribute("class");
                String clazzName = attributeClazz.getValue();
                Class clazz = Class.forName(clazzName);

                //不能直接newInstance，因为如果有构造函数会报错
                //Object object = clazz.newInstance();

                /**
                 * 维护依赖关系
                 * 看这个对象有没有依赖（判断是否有property。或者判断类是否有属性）
                 * 如果有则注入
                 */
                Object object = null;
                //遍历子标签
                for (Iterator<Element> itSecond = elementFirstChild.elementIterator(); itSecond.hasNext(); ) {
                    // 得到ref的value，通过value得到对象（map）
                    // 得到name的值，然后根据值获取一个Filed的对象
                    //通过field的set方法set那个对象

                    //<property name="dao" ref="dao"></property>
                    Element elementSecondChild = itSecond.next();
                    if (elementSecondChild.getName().equals("property")) {
                        //由于是setter，沒有特殊的构造方法，可以直接new
                        object = clazz.newInstance();

                        String refValue = elementSecondChild.attribute("ref").getValue();//获取ref值
                        //Object injectObject = map.get(refValue);
                        Object injectObject = getBean(refValue); //根据refValue获取注入对象

                        String nameValue = elementSecondChild.attribute("name").getValue();//通过name值获取field属性
                        Field field = clazz.getDeclaredField(nameValue);
                        field.setAccessible(true);
                        field.set(object, injectObject); //set(目标对象，属性) 注入
                    } else {
                        //证明有特殊的构造方法，应该判断 if (elementSecondChild.getName().equals("constructor-arg"))
                        //通过class获得特定的构造方法，使用特殊的构造方法
                        String refValue = elementSecondChild.attribute("ref").getValue();
                        //实际不应该通过ref，而应该通过name,拿属性
                        Object injectObject = map.get(refValue);
                        Class injectObjectClazz = injectObject.getClass();
                        Constructor constructor = clazz.getConstructor(injectObjectClazz.getInterfaces()[0]);//根据参数获取构造方法
                        object = constructor.newInstance(injectObject); //创建对象
                    }
                }

                //判读自动装配情况，byType中永远没有子标签
                //如果已经手动装配，自动装配不起效果
                if (object == null) {
                    if (flag) {
                        if (attribute.getValue().equals("byType")) {
                            //判断是否有依赖
                            Field[] fileds = clazz.getDeclaredFields();
                            for (Field filed : fileds) {
                                //得到属性的类型，比如String aa
                                Class injectObjectClazz = filed.getType();
                                /**
                                 * 由於是byType 所以需要遍历map当中的所有对象
                                 * 判断对象的类型是不是和这个injectObjectClazz相同
                                 */
                                int count = 0;
                                Object injectObject = null;
                                for (String key : map.keySet()) {
                                    Class temp = map.get(key).getClass().getInterfaces()[0];
                                    if (temp.getName().equals(injectObjectClazz.getName())) {
                                        injectObject = map.get(key);
                                        //记录找到一个，因为可能找到多个count
                                        count++;
                                    }
                                }
                                if (count > 1) {
                                    throw new MySpringException("需要一个对象，但是找到了多个对象");
                                } else {
                                    object = clazz.newInstance();
                                    filed.setAccessible(true);
                                    filed.set(object, injectObject);
                                }
                            }
                        }
                    }
                }

                //如果没有字标签，当前object=null
                if (object == null) {
                    object = clazz.newInstance();
                }

                map.put(beanName, object);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(map);
    }

    public Object getBean(String beanName) {
        return map.get(beanName);
    }

}
