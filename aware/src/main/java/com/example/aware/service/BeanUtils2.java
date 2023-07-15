package com.example.aware.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/*Bean的工具类，通过它可以查询到各种Bean*/
@Service
public class BeanUtils2 implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    public static <T> T getBean(String beanName){
        return (T) applicationContext.getBean(beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanUtils2.applicationContext=applicationContext;
    }
}
