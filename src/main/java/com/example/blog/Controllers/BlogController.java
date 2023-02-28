package com.example.blog.Controllers;

import com.example.blog.Models.Post;
import com.example.blog.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BlogController {
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/blog")
    public String blogMain(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        // "posts" бул озуно posts ту значение катары алып, аны blog-main шаблонуна жонотот
        return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model) {
        return "blog-add";
    }

    //Жогорку getmaping(/blog/add) сайтынан келген маалыматтар ушул posеmaping иштетилет
    @PostMapping("/blog/add")
    public String blogPostAdd(
            //title, anons, full_text булар blog-add шаблонунандагы name значениясы
            @RequestParam String title,
            @RequestParam String anons,
            @RequestParam String full_text,
            Model model) {
        Post post = new Post(title, anons, full_text);// Муну жазуу менен базага эч кандай данный сакталбайт
        //Сохранит на базу данных
        postRepository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    // @PathVariable(value = "id") шаблондон келген id ни Long id ге ыйгарып, анан ошо мене иштейбиз
    public String blogDetails(@PathVariable(value = "id") long id, Model model) {
        // .existsById(id) бул функция эгерде dbте ушул индекс бар болсо true
        if(!postRepository.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        // blog-details шаблонуна "post":res беребиз
        model.addAttribute("post", res);
        return "blog-details";
    }

    @GetMapping("/blog/{id}/edit")
    // @PathVariable(value = "id") шаблондон келген id ни Long id ге ыйгарып, анан ошо мене иштейбиз
    public String blogEdit(@PathVariable(value = "id") long id, Model model) {
        // .existsById(id) бул функция эгерде dbте ушул индекс бар болсо true
        if(!postRepository.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        // blog-details шаблонуна "post":res беребиз
        model.addAttribute("post", res);
        return "blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogPostUpdate(
            @PathVariable(value = "id") long id,
            //title, anons, full_text булар blog-add шаблонунандагы name значениясы
            @RequestParam String title,
            @RequestParam String anons,
            @RequestParam String full_text,
            Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        // Келген маалыматтарды Post getter and setter меттоддорунун жардамы менен мааниледи установка кылабыз
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        // И обновить данные
        postRepository.save(post);
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/remove")
    public String blogPostDelete(@PathVariable(value = "id") long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        // Удалить данные
        postRepository.delete(post);
        return "redirect:/blog";
    }
}
