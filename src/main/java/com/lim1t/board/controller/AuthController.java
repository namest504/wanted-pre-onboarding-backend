package com.lim1t.board.controller;

import com.lim1t.board.dto.MemberDto.LoginMemberResponse;
import com.lim1t.board.dto.MemberDto.MemberRequest;
import com.lim1t.board.entity.Member;
import com.lim1t.board.exception.CustomException;
import com.lim1t.board.service.JwtService;
import com.lim1t.board.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    //과제 1. 사용자 회원가입 엔드포인트
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid MemberRequest memberRequest) {

        boolean present = memberService.findMemberByEmail(memberRequest.getUserEmail())
                .isPresent();
        if (present) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "아이디 중복");
        }
        Member save = memberService.save(Member.builder()
                .userEmail(memberRequest.getUserEmail())
                .userPassword(passwordEncoder.encode(memberRequest.getUserPassword()))
                .isBan(false)
                .build());
        return ResponseEntity.ok()
                .body(save.getUserEmail() + " 회원가입 성공");
    }

    //과제 2. 사용자 로그인 엔드포인트
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid MemberRequest memberRequest) {
        Member member = memberService.findMemberByEmail(memberRequest.getUserEmail())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "입력값 검증 필요"));

        if (!passwordEncoder.matches(memberRequest.getUserPassword(), member.getUserPassword())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "입력값 검증 필요");
        }

        String accessToken = jwtService.createAccessToken(member.getId());

        return ResponseEntity.ok()
                .body(new LoginMemberResponse(accessToken));
    }
}
