package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.auth.AuthInfo;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.repository.UserInfoRepository;
import kopo.poly.repository.entity.UserInfoEntity;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInfoService implements IUserInfoService {

    private final UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info(this.getClass().getName() + ".loadUserByUsername Start!");

        UserInfoEntity rEntity = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException(userId + " Not Found User"));

        // rEntity 데이터를 DTO로 변환하기
        UserInfoDTO rDTO = new ObjectMapper().convertValue(rEntity, UserInfoDTO.class);

        // 비밀번호가 맞는지 체크 및 권한 부여를 위해 rDTO를 UserDetails를 구현한 AuthInfo에 넣어주기
        return new AuthInfo(rDTO);
    }

    @Override
    public int insertUserInfo(UserInfoDTO pDTO) {

        log.info(this.getClass().getName() + ".insertUserInfo Start!");

        int res = 0; // 회원가입 성공 : 1, 아이디 중복으로인한 가입 취소 : 2, 기타 에러 발생 : 0

        String userId = CmmUtil.nvl(pDTO.userId());
        String userName = CmmUtil.nvl(pDTO.userName());
        String password = CmmUtil.nvl(pDTO.password());
        String email = CmmUtil.nvl(pDTO.email());
        String addr1 = CmmUtil.nvl(pDTO.addr1());
        String addr2 = CmmUtil.nvl(pDTO.addr2());
        String roles = CmmUtil.nvl(pDTO.roles());

        log.info("userId : " + userId);
        log.info("userName : " + userName);
        log.info("password : " + password);
        log.info("email : " + email);
        log.info("addr1 : " + addr1);
        log.info("addr2 : " + addr2);
        log.info("roles : " + roles);

        // 회원 가입 중복 방지를 위해 DB에서 데이터 조회
        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

        // 값이 존재한다면... (이미 회원가입된 아이디)
        if (rEntity.isPresent()) {
            res = 2;
        } else {
            UserInfoEntity pEntity = UserInfoEntity.builder()
                    .userId(userId)
                    .userName(userName)
                    .password(password)
                    .email(email)
                    .addr1(addr1)
                    .addr2(addr2)
                    .roles(roles)
                    .regId(userId)
                    .regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                    .chgId(userId)
                    .chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                    .build();

            // 회원정보 DB에 저장
            userInfoRepository.save(pEntity);

            // 저장 후 Id 조회 후 있으면 실행, 없다면 제대로 저장 안된 것임
            rEntity = userInfoRepository.findByUserId(userId);

            if(rEntity.isPresent()){ // 값이 존재한다면... (회원가입 성공)
                res = 1;
            }
        }

        log.info(this.getClass().getName() + ".insertUserInfo End!");

        return res;
    }

    @Override
    public UserInfoDTO getUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getUserInfo Start!");

        // 회원아이디
        String userId = CmmUtil.nvl(pDTO.userId());

        log.info("userId : " + userId);

        UserInfoDTO rDTO = null;

        // select * from user_info where user_id = 'userId'
        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

        if (rEntity.isPresent()) {

            // Entity -> DTO로 변경
            rDTO = UserInfoDTO.builder()
                    .userId(CmmUtil.nvl(rEntity.get().getUserId()))
                    .userName(CmmUtil.nvl(rEntity.get().getUserName()))
                    .userName(EncryptUtil.decAES128CBC(CmmUtil.nvl(rEntity.get().getEmail())))
                    .userName(CmmUtil.nvl(rEntity.get().getAddr1()))
                    .userName(CmmUtil.nvl(rEntity.get().getAddr2()))
                    .build();
        }

        log.info(this.getClass().getName() + ".getUserInfo End!");

        return rDTO;
    }


}
