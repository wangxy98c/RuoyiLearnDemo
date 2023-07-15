package com.example.aware.service;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Service;

@Service
public class UserService implements BeanNameAware {
    public void sayHello(){
        System.out.println("hello javaboy");
    }

    @Override
    public void setBeanName(String s) {
        System.out.println("bean name is:"+s);
    }
}
