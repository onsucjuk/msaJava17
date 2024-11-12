package kopo.poly.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.repository.NoticeRepository;
import kopo.poly.repository.entity.NoticeEntity;
import kopo.poly.service.INoticeService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.jaxb.mapping.marshall.CacheAccessTypeMarshalling;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeService implements INoticeService {

    // autowired 어노테이션 대신 생성자를 통해 객체 주입 RequiredArgsConstructor
    private final NoticeRepository noticeRepository;

    @Override
    public List<NoticeDTO> getNoticeList() {

        log.info(this.getClass().getName() + ".getNoticeList Start!");

        // 공지사항 전체 리스트 조회하기
        List<NoticeEntity> rList = noticeRepository.findAllByOrderByNoticeSeqDesc();

        List<NoticeDTO> nList = new ObjectMapper().convertValue(rList,
                new TypeReference<List<NoticeDTO>>() {
                });

        log.info(this.getClass().getName() + ".getNoticeList End!");

        return nList;
    }

    @Transactional
    @Override
    public NoticeDTO getNoticeInfo(NoticeDTO pDTO, boolean type) throws Exception {

        log.info(this.getClass().getName() + ".getNoticeInfo Start!");

        if (type) {

            int res = noticeRepository.updateReadCnt(pDTO.noticeSeq());

            // 조회수 증가 성공여부 체크
            log.info("res : " + res);
        }

        NoticeEntity rEntity = noticeRepository.findByNoticeSeq(pDTO.noticeSeq());

        NoticeDTO rDTO = new ObjectMapper().convertValue(rEntity, NoticeDTO.class);

        log.info(this.getClass().getName() + ".getNoticeInfo End!");

        return rDTO;
    }

    @Override
    public void updateNoticeInfo(NoticeDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".updateNoticeInfo Start!");

        Long noticeSeq = pDTO.noticeSeq();

        String title = CmmUtil.nvl(pDTO.title());
        String noticeYn = CmmUtil.nvl(pDTO.noticeYn());
        String contents = CmmUtil.nvl(pDTO.contents());
        String userId = CmmUtil.nvl(pDTO.userId());

        log.info("title : " + title);
        log.info("noticeYn : " + noticeYn);
        log.info("contents : " + contents);
        log.info("userId : " + userId);

        NoticeEntity rEntity = noticeRepository.findByNoticeSeq(noticeSeq);

        NoticeEntity pEntity = NoticeEntity.builder()
                .noticeSeq(noticeSeq)
                .title(title)
                .noticeYn(noticeYn)
                .contents(contents)
                .userId(userId)
                .readCnt(rEntity.getReadCnt())
                .chgId(userId)
                .chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .build();

        noticeRepository.save(pEntity);

        log.info(this.getClass().getName() + ".updateNoticeInfo End!");
    }

    @Override
    public void deleteNoticeInfo(NoticeDTO pDTO) {

        log.info(this.getClass().getName() + ".deleteNoticeInfo Start!!");

        Long noticeSeq = pDTO.noticeSeq();

        log.info("noticeSeq : " + noticeSeq);

        noticeRepository.deleteById(noticeSeq);

        log.info(this.getClass().getName() + ".deleteNoticeInfo End!");

    }

    @Override
    public void insertNoticeInfo(NoticeDTO pDTO) {

        log.info(this.getClass().getName() + ".insertNoticeInfo Start!");

        String title = CmmUtil.nvl(pDTO.title());
        String noticeYn = CmmUtil.nvl(pDTO.noticeYn());
        String contents = CmmUtil.nvl(pDTO.contents());
        String userId = CmmUtil.nvl(pDTO.userId());

        log.info("title : " + title);
        log.info("noticeYn : " + noticeYn);
        log.info("contents : " + contents);
        log.info("userId : " + userId);

        // 0L은 0을 Long 타입으로 지정해준 값
        NoticeEntity pEntity = NoticeEntity.builder()
                .title(title)
                .noticeYn(noticeYn)
                .contents(contents)
                .userId(userId)
                .readCnt(0L)
                .regId(userId)
                .regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .chgId(userId)
                .chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .build();

        noticeRepository.save(pEntity);

        log.info(this.getClass().getName() + ".insertNoticeInfo End!");

    }
}
