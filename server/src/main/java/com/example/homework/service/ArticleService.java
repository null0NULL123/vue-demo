package com.example.homework.service;

import com.example.homework.pojo.Article;
import com.example.homework.pojo.Category;
import com.example.homework.pojo.PageBean;


public interface ArticleService {
    void add(Article article);

    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);

    void deleteById(Integer id);

    Article findById(Integer id);

    void update(Article article);
}
