package com.lim1t.board.controller;

import com.lim1t.board.dto.ArticleDto.ArticleListResponse;
import com.lim1t.board.dto.ArticleDto.ArticleRequest;
import com.lim1t.board.dto.ArticleDto.ArticleResponse;
import com.lim1t.board.entity.Article;
import com.lim1t.board.exception.CustomException;
import com.lim1t.board.security.MemberPrincipal;
import com.lim1t.board.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    //과제 3. 새로운 게시글을 생성하는 엔드포인트
    @PostMapping("/article")
    public ResponseEntity<?> createArticle(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestBody ArticleRequest articleRequest
    ) {
        log.info("과제 3. 새로운 게시글을 생성하는 엔드포인트");

        if (memberPrincipal == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인 필요");
        }

        Article save = articleService.save(Article.builder()
                .title(articleRequest.getTitle())
                .content(articleRequest.getContent())
                .member(memberPrincipal.getMember())
                .build());

        return ResponseEntity.ok()
                .body(save.getId() + " 게시글 생성 성공");
    }

    //과제 4. 게시글 목록을 조회하는 엔드포인트
    @GetMapping("/article/page/{page}")
    public ResponseEntity<?> getArticleList(
            @PathVariable("page") int page,
            @RequestParam int size
    ) {
        log.info("과제 4. 게시글 목록을 조회하는 엔드포인트");
        Page<Article> allPage = articleService.findAllPage(page - 1, size);
        List<ArticleResponse> articleResponseList = allPage
                .stream()
                .map(ArticleResponse::of)
                .toList();

        return ResponseEntity.ok()
                .body(new ArticleListResponse(allPage.getTotalPages(), articleResponseList));
    }


    //과제 5. 특정 게시글을 조회하는 엔드포인트
    @GetMapping("/article/{id}")
    public ResponseEntity<?> getArticleDetail(
            @PathVariable("id") Long id

    ) {
        log.info("과제 5. 특정 게시글을 조회하는 엔드포인트");
        Article article = articleService.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 게시글"));


        return ResponseEntity.ok()
                .body(ArticleResponse.of(article));
    }

    //과제 6. 특정 게시글을 수정하는 엔드포인트
    @PatchMapping("/article/{id}")
    public ResponseEntity<?> modifyArticle(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @PathVariable("id") Long id,
            @RequestBody ArticleRequest articleRequest
    ) {
        log.info("과제 6. 특정 게시글을 수정하는 엔드포인트");

        if (memberPrincipal == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인 필요");
        }

        Article article = articleService.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 게시글"));

        if (article.getMember().getId() != memberPrincipal.getMember().getId()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "본인 게시글만 수정 가능");
        }

        Article save = articleService.save(Article.builder()
                .id(id)
                .title(articleRequest.getTitle())
                .content(articleRequest.getContent())
                .member(article.getMember())
                .build());

        return ResponseEntity.ok()
                .body(save.getId() + " 게시글 수정 성공");
    }

    //과제 7. 특정 게시글을 삭제하는 엔드포인트
    @DeleteMapping("/article/{id}")
    public ResponseEntity<?> deleteArticle(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @PathVariable("id") Long id
    ) {
        log.info("과제 7. 특정 게시글을 삭제하는 엔드포인트");

        if (memberPrincipal == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인 필요");
        }

        Article article = articleService.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 게시글"));

        if (article.getMember().getId() != memberPrincipal.getMember().getId()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "본인 게시글만 삭제 가능");
        }

        Long deleteById = articleService.deleteById(id);

        return ResponseEntity.ok()
                .body(deleteById + " 게시글 삭제 성공");
    }
}
