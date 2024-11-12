package kopo.poly.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kopo.poly.dto.MsgDTO;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.dto.TokenDTO;
import kopo.poly.service.INoticeService;
import kopo.poly.service.ITokenAPIService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:13000", "http://localhost:14000"},
        allowedHeaders = {"POST", "GET"},
        allowCredentials = "true")
@Tag(name = "공지사항 서비스", description = "공지사항 구현을 위한 API")
@Slf4j
@RequestMapping(value = "/notice")
@RequiredArgsConstructor
@RestController
public class NoticeController {

    private final INoticeService noticeService;

    private final ITokenAPIService tokenAPIService;

    private final String HEADER_PREFIX = "Bearer ";

    @Operation(summary = "공지사항 리스트 API", description = "공지사항 리스트 정보 제공하는 API",
            responses = {@ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Page Not Found!"),
    })
    @PostMapping(value = "noticeList")
    public List<NoticeDTO> noticeList() {

        log.info(this.getClass().getName() + ".noticeList Start!");

        List<NoticeDTO> rList = Optional.ofNullable(noticeService.getNoticeList())
                        .orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + ".noticeList End!");

        return rList;
    }

    @Operation(summary = "공지사항 상세보기 결과제공 API", description = "공지사항 상세보기 결과 및 조회수 증가 API",
         parameters = {@Parameter(name = "nSeq", description = "공지사항 글번호"),
                @Parameter(name = "readCntYn", description = "조회수 증가여부")},
         responses = {@ApiResponse(responseCode = "200", description = "OK"),
                 @ApiResponse(responseCode = "404", description = "Page Not Found!"), })
    @PostMapping(value = "noticeInfo")
    public NoticeDTO noticeInfo(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".noticeInfo Start!");

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));
        String readCntYn = CmmUtil.nvl(request.getParameter("readCntYn"));

        boolean readCnt = readCntYn.equals("Y");

        log.info("nSeq" + nSeq);
        log.info("readCntYn" + readCntYn);
        log.info("readCnt" + readCnt);

        NoticeDTO pDTO = NoticeDTO.builder()
                .noticeSeq(Long.parseLong(nSeq))
                .build();

        NoticeDTO rDTO = Optional.ofNullable(noticeService.getNoticeInfo(pDTO, readCnt))
                        .orElseGet(() -> NoticeDTO.builder().build());

        log.info(this.getClass().getName() + ".noticeInfo End!");

        return rDTO;
    }

    @Operation(summary = "공지사항 등록 API", description = "공지사항 등록 및 등록결과를 제공하는 API",
            responses = {@ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Page Not Found!"),})
    @PostMapping(value = "noticeInsert")
    public MsgDTO noticeInsert(HttpServletRequest request, @CookieValue(value = "${jwt.token.access.name}") String token) {

        log.info(this.getClass().getName() + ".noticeInsert Start!");

        String msg = "";
        int res = 0;
        MsgDTO dto = null;

        try {
            TokenDTO tDTO = tokenAPIService.getTokenInfo(HEADER_PREFIX + token);
            log.info("TokenDTO : " + tDTO);

            // JWT Access 토큰으로부터 회원아이디 가져오기
            String userId = CmmUtil.nvl(tDTO.userId());
            String title = CmmUtil.nvl(request.getParameter("title"));
            String noticeYn = CmmUtil.nvl(request.getParameter("noticeYn"));
            String contents = CmmUtil.nvl(request.getParameter("contents"));

            log.info("userId : " + userId);
            log.info("title : " + title);
            log.info("noticeYn : " + noticeYn);
            log.info("contents : " + contents);

            NoticeDTO pDTO = NoticeDTO.builder()
                    .userId(userId)
                    .title(title)
                    .noticeYn(noticeYn)
                    .contents(contents)
                    .build();

            noticeService.insertNoticeInfo(pDTO);

            msg = "등록되었습니다.";
            res = 1;
        } catch (Exception e) {

            msg = "실패했습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            dto = MsgDTO.builder()
                    .result(res)
                    .msg(msg)
                    .build();

            log.info(this.getClass().getName() + ".noticeInsert End!");
        }

        return dto;
    }

    @Operation(summary = "공지사항 수정 API", description = "공지사항 수정 및 수정결과 제공 API",
            responses = {@ApiResponse(responseCode = "200", description = "OK"),
                @ApiResponse(responseCode = "404", description = "Page Not Found!"),})
    @PostMapping(value = "noticeUdate")
    public MsgDTO noticeUpdate(HttpServletRequest request,
                               @CookieValue(value = "${jwt.token.access.name}") String token) {
        log.info(this.getClass().getName() + ".noticeUpdate Start!");

        String msg = "";
        int res = 0;
        MsgDTO dto = null;

        try {
            TokenDTO tDTO = tokenAPIService.getTokenInfo(HEADER_PREFIX + token);
            log.info("TokenDTO : " + tDTO);

            String userId = CmmUtil.nvl(tDTO.userId());
            String nSeq = CmmUtil.nvl("nSeq");
            String title = CmmUtil.nvl("title");
            String noticeYn = CmmUtil.nvl("noticeYn");
            String contents = CmmUtil.nvl("contents");

            log.info("userId : " + userId);
            log.info("nSeq : " + nSeq);
            log.info("title : " + title);
            log.info("noticeYn : " + noticeYn);
            log.info("contents : " + contents);

            NoticeDTO pDTO = NoticeDTO.builder()
                    .userId(userId)
                    .noticeSeq(Long.parseLong(nSeq))
                    .title(title)
                    .noticeYn(noticeYn)
                    .contents(contents)
                    .build();

            noticeService.updateNoticeInfo(pDTO);

            msg = "수정되었습니다.";
            res = 0;

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            dto = MsgDTO.builder()
                    .result(res)
                    .msg(msg)
                    .build();

            log.info(this.getClass().getName() + ".noticeUpdate End!");
        }

        return dto;
    }
}
