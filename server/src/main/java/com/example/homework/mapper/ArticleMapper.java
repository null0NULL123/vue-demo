package com.example.homework.mapper;

import com.example.homework.pojo.Article;
import com.example.homework.pojo.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

    @Select("select * from article where id = #{id}")
    Article findById(Integer id);

    @Insert("insert into article(title,content,cover_img,state,category_id,create_user,create_time,update_time) " +
            "values(#{title},#{content},#{coverImg},#{state},#{categoryId},#{createUser},#{createTime},#{updateTime})")
    void add(Article article);

    List<Article> list(Integer categoryId, String state);

    @Delete("delete from article where id=#{id}")
    void deleteById(Integer id);

    @Update("update article set title=#{title},content=#{content},state=#{state},update_time=#{updateTime} where id=#{id}")
    void update(Article article);
}
