package com.warmup.main.controllers

import com.warmup.main.dao.ArticleRepository
import com.warmup.main.entities.Article
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class ArticleController (private val articleRepository: ArticleRepository){

    @GetMapping("/articles")
    fun getAllArticles():List<Article> =articleRepository.findAll()

    @PostMapping("/articles")
    fun createNewArticle(@Valid @RequestBody article:Article):Article =
        articleRepository.save(article)

    @GetMapping("/articles/{id}")
    fun getArticle(@PathVariable(value="id") articleId:Long):ResponseEntity<Article>{
        return articleRepository.findById(articleId).map {
            article-> ResponseEntity.ok(article)
        }.orElse( ResponseEntity.notFound().build())
    }
    @PatchMapping("/articles/{id}")
    fun updateArticle(@PathVariable(value="id") articleId:Long,
                      @Valid @RequestBody newArticle:Article):ResponseEntity<Article>{
        return articleRepository.findById(articleId).map {
            existingArticle ->
            val updatedArticle:Article=existingArticle.copy(title=newArticle.title,
            content=newArticle.content)
            ResponseEntity.ok(articleRepository.save(updatedArticle))
        }.orElse( ResponseEntity.notFound().build())
    }

    @DeleteMapping("/articles/{id}")
    fun deleteArticle(@PathVariable(value="id") articleId:Long):ResponseEntity<Void>{
        return articleRepository.findById(articleId).map {
            article -> articleRepository.delete(article)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse( ResponseEntity.notFound().build())
    }
}