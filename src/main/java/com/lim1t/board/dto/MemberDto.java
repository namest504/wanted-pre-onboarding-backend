package com.lim1t.board.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MemberDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberRequest {

        @Email(message = "올바르지 않은 입력값")
        private String userEmail;

        @Size(min = 8,message = "올바르지 않은 입력값")
        private String userPassword;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginMemberResponse {
        private String accessToken;
    }
}
