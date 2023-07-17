package com.example.springexpressionlanguage;

import org.springframework.stereotype.Service;

@Service("bs")
public class BookService {
    public String showBook(){
        return "红楼";
    }
}
