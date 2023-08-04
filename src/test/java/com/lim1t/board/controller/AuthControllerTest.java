package com.lim1t.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lim1t.board.dto.MemberDto.MemberRequest;
import com.lim1t.board.entity.Member;
import com.lim1t.board.service.JwtService;
import com.lim1t.board.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    MemberService memberService;
    @MockBean
    JwtService jwtService;

    @Test
    @WithMockUser
    @DisplayName("회원가입 성공")
    void registerSuccess() throws Exception {
        //given
        MemberRequest memberRequest = new MemberRequest("test@example.com", "password");
        Member member = Member.builder()
                .id(1L)
                .userEmail(memberRequest.getUserEmail())
                .userPassword(passwordEncoder.encode(memberRequest.getUserPassword()))
                .isBan(false)
                .build();
        given(memberService.save(any())).willReturn(member);
        //when
        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(memberRequest)));
        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("회원가입 실패 email 형식 안맞음")
    void registerFailure1() throws Exception {
        //given
        MemberRequest memberRequest = new MemberRequest("test", "password");
        Member member = Member.builder()
                .id(1L)
                .userEmail(memberRequest.getUserEmail())
                .userPassword(passwordEncoder.encode(memberRequest.getUserPassword()))
                .isBan(false)
                .build();
        given(memberService.save(any())).willReturn(member);
        //when
        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(memberRequest)));
        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("회원가입 실패 비밀번호 8자 미만")
    void registerFailure2() throws Exception {
        //given
        MemberRequest memberRequest = new MemberRequest("test@example.com", "pw");
        Member member = Member.builder()
                .id(1L)
                .userEmail(memberRequest.getUserEmail())
                .userPassword(passwordEncoder.encode(memberRequest.getUserPassword()))
                .isBan(false)
                .build();
        given(memberService.save(any())).willReturn(member);
        //when
        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(memberRequest)));
        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}