package com.lim1t.board.dto;

import com.lim1t.board.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class ArticleDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArticleRequest {
        private String title;
        private String content;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArticleListResponse {
        private int totalPage;
        private List<ArticleResponse> articleResponses;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArticleResponse {
        private Long id;
        private String title;
        private String content;
        private String writer;

        public static ArticleResponse of(Article article) {
            return new ArticleResponse(article.getId(), article.getTitle(), article.getContent(), article.getMember().getUserEmail());
        }
    }


}
