package com.example.aware.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;

/*Bean的工具类，通过它可以查询到各种Bean*/
@Service
public class BeanUtils implements BeanFactoryAware {
    private static BeanFactory beanFactory;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        BeanUtils.beanFactory=beanFactory;
    }// ##note 完成后，可以随心所欲地查询某个Bean
    public static <T> T getBean(String beanName){
        return (T) beanFactory.getBean(beanName);
    }
}
