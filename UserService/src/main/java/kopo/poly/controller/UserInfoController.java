package kopo.poly.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kopo.poly.auth.JwtTokenProvider;
import kopo.poly.auth.JwtTokenType;
import kopo.poly.dto.TokenDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:12000",
        "http://localhost:13000", "http://localhost:14000"},
        allowedHeaders = {"POST", "GET", "FEIGN"},
        allowCredentials = "true"
)
@Tag(name = "로그인된 사용자들이 접근하는 API", description = "로그인된 사용자들이 접근하는 API 설명합니다.")
@Slf4j
@RequestMapping(value = "/user")
@RequiredArgsConstructor
@RestController
public class UserInfoController {

    // JWT 객체
    private final JwtTokenProvider jwtTokenProvider;

    // 회원 서비스
    private final IUserInfoService userInfoSsService;

    /**
     *  JWT Access Token으로부터 user_id 가져오기
     */
    @Operation(summary = "토큰에 저장된 회원정보 가져오기 API", description = "토큰에 저장된 회원정보 가져오기 API",
              responses = {
                      @ApiResponse(responseCode = "200", description = "Ok"),
                      @ApiResponse(responseCode = "404", description = "Page Not Found!"),
              }
    )
    @PostMapping(value = "getTokenInfo")
    private TokenDTO getTokenInfo(HttpServletRequest request) {

        log.info(this.getClass().getName() + ".getTokenInfo Start!");

        //JWT Access 토큰 가져오기
        String jwtAccessToken = CmmUtil.nvl(jwtTokenProvider.resolveToken(request, JwtTokenType.ACCESS_TOKEN));
        log.info("jwtAccessToken : " + jwtAccessToken);

        TokenDTO dto = Optional.ofNullable(jwtTokenProvider.getTokenInfo(jwtAccessToken))
                .orElseGet(() -> TokenDTO.builder().build());

        log.info("TokenDTO : " + dto);

        log.info(this.getClass().getName() + ".getTokenInfo End!");

        return dto;

    }

    @Operation(summary = "회원정보 상세보기 API", description = "회원정보 상세보기 API",
            responses = {
                @ApiResponse(responseCode = "200", description = "OK"),
                @ApiResponse(responseCode = "404", description = "Page Not Found!"),
            }
    )
    @PostMapping(value = "userInfo")
    public UserInfoDTO userInfoDTO(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".userInfo Start!");

        // Access Token에 저장된 회원아이디 가져오기
        // request의 Access Token 정보를 가져오는 getTokenInfo 메서드 실행
        // this.getTokenInfo(request)
        String userId = CmmUtil.nvl(this.getTokenInfo(request).userId());

        UserInfoDTO rDTO = UserInfoDTO.builder()
                .userId(userId)
                .build();

        log.info(this.getClass().getName() + ".userInfo End!");

        return rDTO;
    }
}
