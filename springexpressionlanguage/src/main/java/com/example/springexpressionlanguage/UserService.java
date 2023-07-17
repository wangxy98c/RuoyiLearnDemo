package com.example.springexpressionlanguage;

public class UserService {
    public String sayHello(){
        return "Hello UserService";
    }
    public String sayHello(String name){
        return "Hello "+name;
    }
}
