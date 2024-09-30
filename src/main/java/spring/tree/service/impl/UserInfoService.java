package spring.tree.service.impl;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.tree.auth.AuthInfo;
import spring.tree.dto.UserInfoDTO;
import spring.tree.repository.UserInfoRepository;
import spring.tree.repository.entity.UserInfoEntity;
import spring.tree.service.IUserInfoService;
import spring.tree.util.CmmUtil;
import spring.tree.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.tree.util.EncryptUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInfoService implements IUserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".getUserIdExists Start!");

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(pDTO.userId());

        UserInfoDTO rDTO;
        if (rEntity.isPresent()) {
            UserInfoEntity userInfoEntity = rEntity.get();
            String decryptedEmail = EncryptUtil.decAES128CBC(userInfoEntity.getEmail());
            rDTO = UserInfoDTO.builder()
                    .userId(userInfoEntity.getUserId())
                    .nickName(userInfoEntity.getNickName())
                    .email(decryptedEmail)
                    .existsYn("Y")
                    .build();
        } else {
            rDTO = UserInfoDTO.builder().existsYn("N").build();
        }

        log.info(this.getClass().getName() + ".getUserIdExists End!");
        return rDTO;

    }

    @Override
    public UserInfoDTO getNickNameExists(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getNickNameExists Start!");

        UserInfoDTO rDTO = null;

        String nickName = CmmUtil.nvl(pDTO.nickName());

        log.info("nickName : " + nickName);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByNickName(nickName);

        if (rEntity.isPresent()) {
            UserInfoEntity userInfoEntity = rEntity.get();
            String decryptedEmail = EncryptUtil.decAES128CBC(userInfoEntity.getEmail());
            rDTO = UserInfoDTO.builder()
                    .userId(userInfoEntity.getUserId())
                    .userName(userInfoEntity.getUserName())
                    .email(decryptedEmail)
                    .nickName(userInfoEntity.getNickName())
                    .existsYn("Y")
                    .build();

        } else {
            rDTO = UserInfoDTO.builder().existsYn("N").build();
        }

        log.info(this.getClass().getName() + ".getNickNameExists End!");

        return rDTO;
    }

    @Override
    public int insertUserInfo(UserInfoDTO pDTO) {

        log.info(this.getClass().getName() + ".insertUserInfo Start!");

        int res = 0; // 회원가입 성공 : 1, 아이디 중복으로인한 가입 취소 : 2, 기타 에러 발생 : 0

        log.info("pDTO : " + pDTO);

        // 회원 가입 중복 방지를 위해 DB에서 데이터 조회
        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(pDTO.userId());

        // 값이 존재한다면... (이미 회원가입된 아이디)
        if (rEntity.isPresent()) {
            res = 2;

        } else {
            // 회원가입을 위한 Entity 생성
            UserInfoEntity pEntity = UserInfoDTO.of(pDTO);

            // 회원정보 DB에 저장
            userInfoRepository.save(pEntity);

            res = 1;

        }

        log.info(this.getClass().getName() + ".insertUserInfo End!");

        return res;
    }

    @Override
    public int getUserLogin(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".getUserLoginCheck Start!");

        // 로그인 성공 : 1, 실패 : 0
        int res = 0;

        String userId = CmmUtil.nvl(pDTO.userId()); // 아이디
        String password = CmmUtil.nvl(pDTO.password()); // 비밀번호 (평문)

        log.info("userId : " + userId);
        log.info("password : " + password);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

        if (rEntity.isPresent()) {
            UserInfoEntity userInfoEntity = rEntity.get();
            log.info("DB password: " + userInfoEntity.getPassword());

            if (bCryptPasswordEncoder.matches(password, userInfoEntity.getPassword())) {
                res = 1;
            }
        }

        log.info(this.getClass().getName() + ".getUserLoginCheck End!");
        return res;
    }


    @Override
    public UserInfoDTO getUserEmailExists(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".getUserEmailExists Start!");

        UserInfoDTO rDTO;

        String email = EncryptUtil.encAES128CBC(CmmUtil.nvl(pDTO.email())); // 이메일 암호화

        log.info("email : " + email);

        // 이메일 중복 체크
        Optional<UserInfoEntity> rEntity = userInfoRepository.findByEmail(email);

        if (rEntity.isPresent()) {
            rDTO = UserInfoDTO.builder().existsYn("Y").build();
        } else {
            rDTO = UserInfoDTO.builder().existsYn("N").build();
        }

        log.info(this.getClass().getName() + ".getUserEmailExists End!");

        return rDTO;
    }


    @Override
    public String searchUserIdProc(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".searchUserIdProc Start!");

        String res = "";

        String userName = CmmUtil.nvl(pDTO.userName());
        String email = CmmUtil.nvl(pDTO.email());

        log.info("pDTO userName : " + userName);
        log.info("pDTO email : " + email);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserNameAndEmail(pDTO.userName(), pDTO.email());

        log.info("rEntity : " + rEntity);

        if (rEntity.isPresent()) {
            UserInfoEntity userInfoEntity = rEntity.get();
            String userId = userInfoEntity.getUserId();
            log.info("Found userId: " + userId); // userId가 성공적으로 조회되었을 때 로그 출력
            res = userId;
        }

        log.info(this.getClass().getName() + ".searchUserIdProc End!");

        return res;
    }


    @Override
    public void updatePassword(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".updatePassword Start!");

        // 사용자 정보 가져오기
        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(pDTO.userId());

        if (rEntity.isPresent()) {
            UserInfoEntity userInfoEntity = rEntity.get();

            // 비밀번호 업데이트 (다른 필드는 기존 값 유지)
            userInfoEntity = UserInfoEntity.builder()
                    .userId(userInfoEntity.getUserId())
                    .userName(userInfoEntity.getUserName())  // 기존 사용자 이름 유지
                    .nickName(userInfoEntity.getNickName())  // 기존 닉네임 유지
                    .email(userInfoEntity.getEmail())        // 기존 이메일 유지
                    .password(pDTO.password())              // 새로운 암호화된 비밀번호 설정
                    .roles(userInfoEntity.getRoles())        // 기존 역할(roles) 유지
                    .addr1(userInfoEntity.getAddr1())        // 기존 주소 정보 유지
                    .addr2(userInfoEntity.getAddr2())        // 기존 주소 정보 유지
                    .regId(userInfoEntity.getRegId())        // 등록 ID 유지
                    .regDt(userInfoEntity.getRegDt())        // 등록 날짜 유지
                    .chgId(userInfoEntity.getUserId())       // 변경 ID를 현재 사용자로 설정
                    .chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))  // 변경 날짜 업데이트
                    .build();

            // 업데이트된 사용자 정보 저장
            userInfoRepository.save(userInfoEntity);
            log.info("비밀번호 및 역할 정보가 성공적으로 업데이트되었습니다.");
        } else {
            log.info("사용자를 찾을 수 없습니다.");
        }

        log.info(this.getClass().getName() + ".updatePassword End!");
    }

    @Override
    public void updateNickName(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".updateNickName Start!");

        String userId = CmmUtil.nvl(pDTO.userId());
        String nickName = CmmUtil.nvl(pDTO.nickName());

        log.info("userId : " + userId);
        log.info("nickName : " + nickName);

        Optional<UserInfoEntity> existingNickNameEntity = userInfoRepository.findByNickName(nickName);

        if (existingNickNameEntity.isPresent() && !existingNickNameEntity.get().getUserId().equals(userId)) {
            log.info("닉네임이 이미 존재합니다: " + nickName);
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        Optional<UserInfoEntity> uEntity = userInfoRepository.findByUserId(userId);

        if (uEntity.isPresent()) {
            UserInfoEntity rEntity = uEntity.get();

            log.info("rEntity userId : " + rEntity.getUserId());
            log.info("rEntity nickName : " + rEntity.getNickName());

            // NickName을 업데이트하되, roles는 그대로 유지
            UserInfoEntity pEntity = UserInfoEntity.builder()
                    .userId(rEntity.getUserId())
                    .userName(rEntity.getUserName())
                    .password(rEntity.getPassword())
                    .email(rEntity.getEmail())
                    .nickName(nickName)                    // 변경된 닉네임
                    .roles(rEntity.getRoles())              // 기존 역할 정보 유지
                    .addr1(rEntity.getAddr1())
                    .addr2(rEntity.getAddr2())
                    .regId(rEntity.getRegId())
                    .regDt(rEntity.getRegDt())
                    .chgId(rEntity.getUserId())
                    .chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                    .build();

            // DB에 업데이트된 사용자 정보 저장
            userInfoRepository.save(pEntity);

            log.info("NickName updated in database.");
        } else {
            log.info("No user found with userId: " + userId);
        }

        log.info(this.getClass().getName() + ".updateNickName End!");
    }



    @Override
    public void deleteUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteUserInfo Start!");

        String userId = pDTO.userId();

        log.info("userId : " + userId);

        userInfoRepository.deleteById(userId);


        log.info(this.getClass().getName() + ".deleteUserInfo End!");

    }

    @Override
    public List<UserInfoDTO> getUserList(String userId) {
        log.info("Fetching data for user: {}", userId);

        // 사용자 정보 엔티티 리스트 가져오기
        List<UserInfoEntity> rList = userInfoRepository.findAllByUserIdOrderByUserIdDesc(userId);

        // 복호화된 사용자 정보 DTO 리스트 생성
        List<UserInfoDTO> nList = rList.stream()
                .map(userInfoEntity -> {
                    try {
                        // 이메일 복호화
                        String decryptedEmail = EncryptUtil.decAES128CBC(userInfoEntity.getEmail());
                        // UserInfoDTO 객체 생성하여 반환
                        return UserInfoDTO.builder()
                                .userId(userInfoEntity.getUserId())
                                .password(userInfoEntity.getPassword())
                                .addr1(userInfoEntity.getAddr1())
                                .addr2(userInfoEntity.getAddr2())
                                .userName(userInfoEntity.getUserName())
                                .email(decryptedEmail)
                                .nickName(userInfoEntity.getNickName())
                                .build();
                    } catch (Exception e) {
                        log.error("Error decrypting email for user: {}", userId, e);
                        return null; // 복호화에 실패한 경우 null 반환
                    }
                })
                .collect(Collectors.toList());

        log.info("Calendar data fetched successfully for user: {}", userId);

        return nList;
    }

    @Override
    public UserInfoDTO getUserInfoByUserId(String userId) {
        log.info("getUserInfoByUserId Start!");

        UserInfoDTO rDTO = null;

        try {
            Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

            if (rEntity.isPresent()) {
                UserInfoEntity userInfoEntity = rEntity.get();
                String decryptedEmail = EncryptUtil.decAES128CBC(userInfoEntity.getEmail());
                rDTO = UserInfoDTO.builder()
                        .userId(userInfoEntity.getUserId())
                        .nickName(userInfoEntity.getNickName())
                        .email(decryptedEmail)
                        .build();
            } else {
                log.info("No user found with userId: " + userId);
            }
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException |
                 InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException |
                 BadPaddingException e) {
            log.error("Error decrypting email for user: {}", userId, e);
        }

        log.info("getUserInfoByUserId End!");

        return rDTO;
    }


    @Override
    public UserInfoDTO getUserInfoByUserIdAndNameAndEmail(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".getUserInfoByUserIdAndNameAndEmail Start!");

        // Optional로 검색 결과를 가져오기
        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserIdAndUserNameAndEmail(
                pDTO.userId(), pDTO.userName(), pDTO.email());

        // 결과 DTO 초기화
        UserInfoDTO rDTO;

        if (rEntity.isPresent()) {
            UserInfoEntity userInfoEntity = rEntity.get();
            String decryptedEmail = EncryptUtil.decAES128CBC(userInfoEntity.getEmail());
            rDTO = UserInfoDTO.builder()
                    .userId(userInfoEntity.getUserId())
                    .nickName(userInfoEntity.getNickName())
                    .email(decryptedEmail)
                    .existsYn("Y")
                    .build();
        } else {
            // 일치하는 사용자가 없을 때
            rDTO = UserInfoDTO.builder().existsYn("N").build();
        }

        log.info(this.getClass().getName() + ".getUserInfoByUserIdAndNameAndEmail End!");

        return rDTO;
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info(this.getClass().getName() + ".loadUserByUsername Start!");

        log.info("userId : " + userId);

        // 로그인 요청한 사용자 아이디를 검색함
        // SELECT * FROM USER_INFO WHERE USER_ID = 'hglee67'
        UserInfoEntity rEntity = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException(userId + " Not Found User"));

        // rEntity 데이터를 DTO로 변환하기
        UserInfoDTO rDTO = UserInfoDTO.from(rEntity);

        // 비밀번호가 맞는지 체크 및 권한 부여를 위해 rDTO를 UserDetails를 구현한 AuthInfo에 넣어주기
        return new AuthInfo(rDTO);
    }

}
