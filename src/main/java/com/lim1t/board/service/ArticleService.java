package com.lim1t.board.service;

import com.lim1t.board.entity.Article;
import com.lim1t.board.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public Page<Article> findAllPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Article> allPage = articleRepository.findAll(pageRequest);
        return allPage;
    }

    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    public Long deleteById(Long id) {
        articleRepository.deleteById(id);
        return id;
    }
}
