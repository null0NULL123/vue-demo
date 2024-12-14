package com.example.homework.controller;

import com.example.homework.pojo.PageBean;
import com.example.homework.pojo.Result;
import com.example.homework.pojo.Article;
import com.example.homework.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public Result<Void> add(@RequestBody @Validated Article article) {
        articleService.add(article);
        return Result.success();
    }

    @GetMapping
    public Result<PageBean<Article>> list(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String state) {
        PageBean<Article> pb = articleService.list(pageNum, pageSize, categoryId, state);
        return Result.success(pb);
    }

    @GetMapping("/detail")
    public Result<Article> detail(Integer id) {
        Article a = articleService.findById(id);
        return Result.success(a);
    }

    @DeleteMapping
    public Result<Void> delete(Integer id) {
        articleService.deleteById(id);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Article article) {
        articleService.update(article);
        return Result.success();
    }
}
