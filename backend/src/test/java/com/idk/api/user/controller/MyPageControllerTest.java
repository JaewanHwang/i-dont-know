package com.idk.api.user.controller;

import com.idk.api.MvcTest;
import com.idk.api.common.category.Category;
import com.idk.api.common.category.SubCategory;
import com.idk.api.districtcode.domain.entity.DistrictCode;
import com.idk.api.user.domain.entity.User;
import com.idk.api.user.dto.MyPageRequest;
import com.idk.api.user.dto.MyPageResponse;
import com.idk.api.user.dto.UserResponse;
import com.idk.api.user.service.MyPageService;
import com.idk.api.vote.domain.entity.Vote;
import com.idk.api.vote.dto.VoteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("MyPage API ?????????")
@WebMvcTest(MyPageController.class)
public class MyPageControllerTest extends MvcTest {

    @MockBean
    private MyPageService myPageService;
    private User user1, user2;
    private DistrictCode districtCode;
    private Vote vote1, vote2;
    private final List<Vote> voteList = new ArrayList<>();

    @BeforeEach
    public void setup(){
        districtCode = DistrictCode.builder()
                .id(1)
                .name("?????????")
                .build();

        user1 = User.builder()
                .id(1L)
                .name("????????????")
                .email("chickenLover@idontknow.com")
                .districtCode(districtCode)
                .gender("M")
                .age(20).build();
        user2 = User.builder()
                .id(1L)
                .name("???????????????")
                .email("chickenMania@idontknow.com")
                .districtCode(districtCode)
                .gender("F")
                .age(20)
                .build();

        vote1 = Vote.builder()
                .id(1L)
                .user(user1)
                .title("?????? ?????? ??????????")
                .optionA("????????????")
                .optionB("????????????")
                .category(Category.MENU)
                .subCategory(SubCategory.CHICKEN)
                .hitCount(3)
                .commentCount(3)
                .aCount(1)
                .bCount(1)
                .status(false)
                .build();
        vote1.setCreatedAt(LocalDateTime.now().minusDays(3L));

        vote2 = Vote.builder()
                .id(2L)
                .user(user2)
                .title("?????? ????????? ?????? ?????? ??????????")
                .optionA("BBQ")
                .optionB("KFC")
                .category(Category.MENU)
                .subCategory(SubCategory.CHICKEN)
                .hitCount(2)
                .commentCount(0)
                .aCount(0)
                .bCount(0)
                .status(true)
                .build();
        vote2.setCreatedAt(LocalDateTime.now().minusDays(2L));
        vote2.setDeletedAt(LocalDateTime.now());

        voteList.add(vote1);
        voteList.add(vote2);
    }

    @Test
    @DisplayName("????????????_??????")
    public void getUserInfo() throws Exception{
        MyPageResponse.UserInfo response = MyPageResponse.UserInfo.build(user1);

        given(myPageService.getUserInfo(any())).willReturn(response);
        ResultActions results = mvc.perform(RestDocumentationRequestBuilders.get("/api/mypage/users/{userId}/info", 1L));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-user-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("?????? ID")

                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("districtId").type(JsonFieldType.NUMBER).description("????????? ID"),
                                fieldWithPath("districtName").type(JsonFieldType.STRING).description("????????? ??????"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("??????")
                        )
                ));
        verify(myPageService).getUserInfo(any());

    }

    @Test
    @DisplayName("????????????_??????")
    public void updateUserInfo() throws Exception{

        MyPageResponse.UserInfo response = MyPageResponse.UserInfo.build(user1);
        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        requestParam.set("districtId", "0");
        requestParam.set("gender", "M");
        requestParam.set("age", "20");

        given(myPageService.updateUserInfo(anyLong(), any(), anyInt(), any(), anyInt())).willReturn(response);
        ResultActions results = mvc.perform(RestDocumentationRequestBuilders.patch("/api/mypage/users/{userId}/info", 1L)
                .params(requestParam)
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("update-user-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("?????? ID")

                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("districtId").type(JsonFieldType.NUMBER).description("????????? ID"),
                                fieldWithPath("districtName").type(JsonFieldType.STRING).description("????????? ??????"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("??????")
                        )
                ));
        verify(myPageService).updateUserInfo(anyLong(), any(), anyInt(), any(), anyInt());

    }

    @Test
    @DisplayName("????????????_??????")
    public void deleteUserInfo() throws Exception{

        UserResponse.OnlyId response = UserResponse.OnlyId.build(user1);

        given(myPageService.deleteUserInfo(any(), any())).willReturn(response);
        ResultActions results = mvc.perform(RestDocumentationRequestBuilders.patch("/api/mypage/users/{userId}", 1L));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("delete-user-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("?????? ID")

                        ),
                        responseFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("?????? ID")
                        )
                ));
        verify(myPageService).deleteUserInfo(any(), any());

    }

    @Test
    @DisplayName("????????????_??????")
    public void changePassword() throws Exception{
        MyPageRequest.UserPassword request = MyPageRequest.UserPassword.builder()
                .curPassword("1234")
                .newPassword("4321")
                .build();

        UserResponse.OnlyId response = UserResponse.OnlyId.build(user1);

        given(myPageService.updateUserPassword(any(), any(), any())).willReturn(response);
        ResultActions results = mvc.perform(RestDocumentationRequestBuilders.patch("/api/mypage/users/{userId}/pw", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("update-password",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("?????? ID")

                        ),
                        requestFields(
                                fieldWithPath("curPassword").type(JsonFieldType.STRING).description("?????? ????????????"),
                                fieldWithPath("newPassword").type(JsonFieldType.STRING).description("????????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("?????? ID")
                        )
                ));
        verify(myPageService).updateUserPassword(any(), any(), any());

    }

    @Test
    @DisplayName("?????????_??????_??????_??????")
    public void getVoteList() throws Exception {
        Page<Vote> votePage = new PageImpl<>(voteList, PageRequest.of(0, 2), voteList.size());
        Page<VoteResponse.GetOne> response = votePage.map(VoteResponse.GetOne::build);
        given(myPageService.getVoteList(anyLong(), anyLong(), any(), anyBoolean())).willReturn(response);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders.get("/api/mypage/users/{userId}/votes", 1L)
                .queryParam("status", "false")
                .queryParam("lastVoteId", "0")
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user_votes_list",
                        requestParameters(
                                parameterWithName("status").description("?????? ?????? : false(??????)/true(??????)"),
                                parameterWithName("lastVoteId").description("????????? ?????? ?????????(?????? ?????? ??? 0)")
                        ),
                        responseFields(
                                fieldWithPath("content[].voteId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                fieldWithPath("content[].category").type(JsonFieldType.STRING).description("?????? ????????????"),
                                fieldWithPath("content[].subCategory").type(JsonFieldType.STRING).description("?????? ??????????????????"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("content[].userId").type(JsonFieldType.NUMBER).description("?????? ????????? ?????????"),
                                fieldWithPath("content[].name").type(JsonFieldType.STRING).description("?????? ????????? ??????"),
                                fieldWithPath("content[].hitCount").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                fieldWithPath("content[].commentCount").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                fieldWithPath("content[].optionA").type(JsonFieldType.STRING).description("?????? ????????? A"),
                                fieldWithPath("content[].optionB").type(JsonFieldType.STRING).description("?????? ????????? B"),
                                fieldWithPath("content[].createdAt").type(JsonFieldType.STRING).description("?????? ????????????"),
                                fieldWithPath("content[].status").type(JsonFieldType.BOOLEAN).description("?????? ?????? (false : ??????, true : ??????)"),
                                fieldWithPath("totalPages").description("?????? ????????? ???"),
                                fieldWithPath("totalElements").description("?????? ?????? ??????"),
                                fieldWithPath("last").description("????????? ??????????????? ???????????? ???"),
                                fieldWithPath("pageable.sort.unsorted").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("pageable.sort.sorted").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("pageable.sort.empty").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("pageable.offset").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("pageable.pageNumber").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("pageable.pageSize").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("pageable.paged").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("pageable.unpaged").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("number").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("sort.unsorted").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("sort.sorted").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("sort.empty").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("size").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("first").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("numberOfElements").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("empty").description("pageable ?????? ??? ?????? - ?????? x")

                        )
                ));
        verify(myPageService).getVoteList(anyLong(), anyLong(), any(), anyBoolean());
    }


    @Test
    @DisplayName("?????????_??????_??????_??????")
    public void getBallotList() throws Exception {
        Page<Vote> votePage = new PageImpl<>(voteList, PageRequest.of(0, 2), voteList.size());
        Page<VoteResponse.GetOne> response = votePage.map(VoteResponse.GetOne::build);
        given(myPageService.getBallotList(anyLong(), anyLong(), any(), anyBoolean())).willReturn(response);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders.get("/api/mypage/users/{userId}/ballots", 1L)
                .queryParam("status", "false")
                .queryParam("lastVoteId", "0")
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user_ballots_list",
                        requestParameters(
                                parameterWithName("status").description("?????? ?????? : false(??????)/true(??????)"),
                                parameterWithName("lastVoteId").description("????????? ?????? ?????????(?????? ?????? ??? 0)")
                        ),
                        responseFields(
                                fieldWithPath("content[].voteId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                fieldWithPath("content[].category").type(JsonFieldType.STRING).description("?????? ????????????"),
                                fieldWithPath("content[].subCategory").type(JsonFieldType.STRING).description("?????? ??????????????????"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("content[].userId").type(JsonFieldType.NUMBER).description("?????? ????????? ?????????"),
                                fieldWithPath("content[].name").type(JsonFieldType.STRING).description("?????? ????????? ??????"),
                                fieldWithPath("content[].hitCount").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                fieldWithPath("content[].commentCount").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                fieldWithPath("content[].optionA").type(JsonFieldType.STRING).description("?????? ????????? A"),
                                fieldWithPath("content[].optionB").type(JsonFieldType.STRING).description("?????? ????????? B"),
                                fieldWithPath("content[].createdAt").type(JsonFieldType.STRING).description("?????? ????????????"),
                                fieldWithPath("content[].status").type(JsonFieldType.BOOLEAN).description("?????? ?????? (false : ??????, true : ??????)"),
                                fieldWithPath("totalPages").description("?????? ????????? ???"),
                                fieldWithPath("totalElements").description("?????? ?????? ??????"),
                                fieldWithPath("last").description("????????? ??????????????? ???????????? ???"),
                                fieldWithPath("pageable.sort.unsorted").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("pageable.sort.sorted").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("pageable.sort.empty").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("pageable.offset").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("pageable.pageNumber").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("pageable.pageSize").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("pageable.paged").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("pageable.unpaged").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("number").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("sort.unsorted").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("sort.sorted").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("sort.empty").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("size").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("first").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("numberOfElements").description("pageable ?????? ??? ?????? - ?????? x"),
                                fieldWithPath("empty").description("pageable ?????? ??? ?????? - ?????? x")

                        )
                ));
        verify(myPageService).getBallotList(anyLong(), anyLong(), any(), anyBoolean());
    }

    @Test
    @DisplayName("?????????_??????")
    public void getRate() throws Exception{
        MyPageResponse.Rate response = MyPageResponse.Rate.builder()
                .id(user1.getId())
                .ballotCount(2)
                .correctCount(1)
                .build();

        given(myPageService.getRate(anyLong())).willReturn(response);
        ResultActions results = mvc.perform(RestDocumentationRequestBuilders.get("/api/mypage/users/{userId}/rate", 1L));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-user-rate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("?????? ID")

                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("ballotCount").type(JsonFieldType.NUMBER).description("????????? ?????? ??????"),
                                fieldWithPath("correctCount").type(JsonFieldType.NUMBER).description("?????? ?????? ??????")
                        )
                ));
        verify(myPageService).getRate(anyLong());

    }

    @Test
    @DisplayName("????????????")
    public void logout() throws Exception{
        UserResponse.OnlyId response = UserResponse.OnlyId.build(user1);

        given(myPageService.logout(anyLong(), any())).willReturn(response);
        ResultActions results = mvc.perform(RestDocumentationRequestBuilders.get("/api/mypage/users/{userId}/logout", 1L));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("logout",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("?????? ID")

                        ),
                        responseFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("?????? ID")
                        )
                ));
        verify(myPageService).logout(anyLong(), any());

    }


}
