package com.project.instagramclone.controller;

import com.project.instagramclone.dto.form.JoinDto;
import com.project.instagramclone.service.form.JoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name="join service", description="join service API")
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "회원가입에 사용되는 API ") // 각 API에 대한 설명
    @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")) //응답 코드에 대한 정보
    @Parameter(name="username", description="유저 ID") //파라미터에 대한 정보
    @Parameter(name="password", description="비밀번호") //파라미터에 대한 정보
    @Parameter(name="nickname", description="닉네임" ) //파라미터에 대한 정보
    @Parameter(name="email", description="이메일") //파라미터에 대한 정보
    public String joinProc(@ModelAttribute JoinDto joinDto) {
        joinService.join(joinDto);
        return "ok";
    }

}
