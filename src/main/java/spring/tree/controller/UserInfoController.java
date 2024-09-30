package spring.tree.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.tree.auth.AuthInfo;
import spring.tree.auth.UserRole;
import spring.tree.controller.response.CommonResponse;
import spring.tree.dto.*;
import spring.tree.repository.entity.UserInfoEntity;
import spring.tree.service.IFavoriteTourService;
import spring.tree.service.IReservationService;
import spring.tree.service.ITourService;
import spring.tree.service.IUserInfoService;
import spring.tree.util.CmmUtil;
import spring.tree.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;


import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping(value = "/user")
@RequiredArgsConstructor
@Controller
public class UserInfoController {

    private final IUserInfoService userInfoService;
    private final ITourService tourService;
    private final IReservationService reservationService;
    private final IFavoriteTourService favoriteTourService;
    private final PasswordEncoder bCryptPasswordEncoder;


    @GetMapping(value = "userRegForm")
    public String userRegForm() {
        log.info(this.getClass().getName() + ".user/userRegForm Start!");

        log.info(this.getClass().getName() + ".user/userRegForm End!");

        return "user/userRegForm";
    }

    @PostMapping(value = "getUserIdExists")
    public ResponseEntity<CommonResponse> getUserIdExists(@RequestBody UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getUserIdExists Start!");

        UserInfoDTO rDTO = userInfoService.getUserIdExists(pDTO);

        log.info(this.getClass().getName() + ".getUserIdExists End!");

        log.info("pDTO: " + pDTO);

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rDTO));
    }


    @PostMapping(value = "getUserEmailExists")
    public ResponseEntity<CommonResponse> getUserEmailExists(@RequestBody UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getUserEmailExists Start!");

        UserInfoDTO rDTO = userInfoService.getUserEmailExists(pDTO);

        log.info(this.getClass().getName() + ".getUserEmailExists End!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rDTO));
    }

    @PostMapping(value = "getNickNameExists")
    public ResponseEntity<CommonResponse> getNickNameExists(@RequestBody UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getNickNameExists Start!");

        UserInfoDTO rDTO = userInfoService.getNickNameExists(pDTO);

        log.info(this.getClass().getName() + ".getNickNameExists End!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rDTO));
    }

    @PostMapping(value = "insertUserInfo")
    public ResponseEntity<CommonResponse> insertUserInfo(@Valid @RequestBody UserInfoDTO pDTO,
                                                         BindingResult bindingResult) {

        log.info(this.getClass().getName() + ".insertUserInfo Start!");

        if (bindingResult.hasErrors()) { // Spring Validation 맞춰 잘 바인딩되었는지 체크
            return CommonResponse.getErrors(bindingResult); // 유효성 검증 결과에 따른 에러 메시지 전달

        }

        int res = 0; // 회원가입 결과
        String msg = ""; //회원가입 결과에 대한 메시지를 전달할 변수
        MsgDTO dto; // 결과 메시지 구조

        // 	 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함, 반드시 작성할 것
        log.info("pDTO : " + pDTO);

        try {
            // 웹으로 입력받은 정보와 비밀번호, 권한 추가한 회원 가입 정보 생성하기
            UserInfoDTO nDTO = UserInfoDTO.createUser(
                    pDTO, bCryptPasswordEncoder.encode(pDTO.password()), UserRole.USER.getValue());


            res = userInfoService.insertUserInfo(nDTO);

            log.info("회원가입 결과(res) : " + res);

            if (res == 1) {
                msg = "회원가입되었습니다.";

            } else if (res == 2) {
                msg = "이미 가입된 아이디입니다.";

            } else {
                msg = "오류로 인해 회원가입이 실패하였습니다.";

            }

        } catch (Exception e) {
            //저장이 실패되면 사용자에게 보여줄 메시지
            msg = "실패하였습니다. : " + e;
            res = 2;
            log.info(e.toString());

        } finally {
            dto = MsgDTO.builder().result(res).msg(msg).build();

            log.info(this.getClass().getName() + ".insertUserInfo End!");
        }

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), dto));
    }

    @GetMapping(value = "login")
    public String login() {

        log.info(this.getClass().getName() + ".user/login Start!");

        log.info(this.getClass().getName() + ".user/login End!");

        return "user/login";
    }


    @ResponseBody
    @PostMapping(value = "loginProc")
    public MsgDTO loginProc(HttpServletRequest request, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".loginProc Start!");

        String user_id = CmmUtil.nvl(request.getParameter("user_id"));
        String password = CmmUtil.nvl(request.getParameter("password"));

        log.info("user_id : " + user_id);
        log.info("Entered password : " + password);

        // 사용자 정보 조회
        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userId(user_id)
                .build();

        UserInfoDTO userInfo = userInfoService.getUserIdExists(pDTO);

        if (userInfo != null) {
            log.info("Stored password: " + userInfo.password());  // DB에 저장된 암호화된 비밀번호 로그
            if (bCryptPasswordEncoder.matches(password, userInfo.password())) {  // BCrypt로 비밀번호 비교
                log.info("Password match successful!");
                session.setAttribute("SS_USER_ID", user_id);
                session.setAttribute("SS_USER_NICKNAME", userInfo.nickName());
                return MsgDTO.builder().msg("로그인 성공!").build();
            } else {
                log.info("Password does not match!");
            }
        } else {
            log.info("No user found with user_id: " + user_id);
        }

        log.info(this.getClass().getName() + ".loginProc End!");
        return MsgDTO.builder().msg("아이디 또는 비밀번호가 올바르지 않습니다.").build();
    }


    @PostMapping(value = "loginSuccess")
    public ResponseEntity<CommonResponse> loginSuccess(@AuthenticationPrincipal AuthInfo authInfo, HttpSession session) {

        log.info(this.getClass().getName() + ".loginSuccess Start!");

        // Spring Security에 저장된 정보 가져오기
        UserInfoDTO rDTO = Optional.ofNullable(authInfo.getUserInfoDTO()).orElseGet(() -> UserInfoDTO.builder().build());

        String userId = CmmUtil.nvl(rDTO.userId());
        String userName = CmmUtil.nvl(rDTO.userName());
        String userRoles = CmmUtil.nvl(rDTO.roles());
        String nickName = CmmUtil.nvl(rDTO.nickName());


        log.info("userId : " + userId);
        log.info("userName : " + userName);
        log.info("userRoles : " + userRoles);
        log.info("nickName : " + nickName);

        session.setAttribute("SS_USER_ID", userId);
        session.setAttribute("SS_USER_NAME", userName);
        session.setAttribute("SS_USER_ROLE", userRoles);
        session.setAttribute("SS_USER_NICKNAME", nickName);

        // 결과 메시지 전달하기
        MsgDTO dto = MsgDTO.builder().result(1).msg(userName + "님 나도가이드에 오신 걸 환영합니다!").build();

        log.info(this.getClass().getName() + ".loginSuccess End!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), dto));

    }

    @PostMapping(value = "loginFail")
    public ResponseEntity<CommonResponse> loginFail() {

        log.info(this.getClass().getName() + ".loginFail Start!");

        // 결과 메시지 전달하기
        MsgDTO dto = MsgDTO.builder().result(1).msg("로그인이 실패하였습니다.").build();

        log.info(this.getClass().getName() + ".loginFail End!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), dto));

    }

//    @ResponseBody
//    @PostMapping(value = "logout")
//    public MsgDTO logout(HttpSession session) {
//
//        log.info(this.getClass().getName() + ".logout Start!");
//
//        session.setAttribute("SS_USER_ID", "");
//        session.removeAttribute("SS_USER_ID");
//
//        MsgDTO dto = MsgDTO.builder()
//                .result(1)
//                .msg("로그아웃하였습니다")
//                .build();
//
//        log.info(this.getClass().getName() + ".logout End!");
//
//        return dto;
//    }


    @GetMapping(value = "findId")
    public String searchUserId() {
        log.info(this.getClass().getName() + ".user/searchUserId Start!");

        log.info(this.getClass().getName() + ".user/searchUserId End!");

        return "user/findId";
    }

    @ResponseBody
    @PostMapping(value = "searchUserIdProc")
    public MsgDTO searchUserIdProc(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".user/searchUserIdProc Start!");

        String msg;

        String userName = CmmUtil.nvl(request.getParameter("userName"));
        String email = CmmUtil.nvl(request.getParameter("email"));

        log.info("userName : " + userName);
        log.info("email : " + email);

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userName(userName)
                .email(EncryptUtil.encAES128CBC(email))
                .build();

        String res = userInfoService.searchUserIdProc(pDTO);

        log.info("res: " + res);

        if (!Objects.equals(res, "")) {
            msg = userName + " 회원님의 아이디는 " + res + "입니다.";
        } else {
            msg = "회원정보가 일치하지 않습니다.";
        }

        MsgDTO dto = MsgDTO.builder().msg(msg).build();

        log.info(this.getClass().getName() + ".user/searchUserIdProc End!");

        return dto;
    }

    @ResponseBody
    @PostMapping(value = "/searchPasswordProc")
    public MsgDTO searchPasswordProc(HttpServletRequest request, HttpSession session) throws Exception {
        log.info(this.getClass().getName() + ".searchPasswordProc Start!");

        String userId = CmmUtil.nvl(request.getParameter("userId"));
        String userName = CmmUtil.nvl(request.getParameter("userName"));
        String email = CmmUtil.nvl(request.getParameter("email"));

        log.info("userId: " + userId);
        log.info("userName: " + userName);
        log.info("email: " + email);

        // 이메일 암호화
        String encryptedEmail = EncryptUtil.encAES128CBC(email);

        // 사용자 정보 조회 (아이디, 이름, 암호화된 이메일 모두 일치하는지 확인)
        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userId(userId)
                .userName(userName)
                .email(encryptedEmail) // 암호화된 이메일로 검색
                .build();

        UserInfoDTO rDTO = userInfoService.getUserInfoByUserIdAndNameAndEmail(pDTO);

        // 응답 메시지
        String msg;
        if (rDTO != null && rDTO.existsYn().equals("Y")) {
            log.info("사용자를 찾았습니다. 비밀번호를 재설정할 수 있습니다.");

            // 세션에 사용자 ID 저장
            session.setAttribute("SS_USER_ID", userId);

            msg = "success";
        } else {
            log.info("사용자를 찾을 수 없습니다.");
            msg = "fail";
        }

        log.info(this.getClass().getName() + ".searchPasswordProc End!");

        return MsgDTO.builder().msg(msg).build();
    }





    @ResponseBody
    @PostMapping(value = "/newPasswordProc")
    public MsgDTO newPasswordProc(HttpSession session, HttpServletRequest request) {
        log.info(this.getClass().getName() + ".newPasswordProc Start!");

        String userId = (String) session.getAttribute("SS_USER_ID"); // 세션에서 사용자 ID 가져오기
        String newPassword = request.getParameter("password"); // 새 비밀번호 가져오기

        log.info("사용자 ID: " + userId);
        log.info("새 비밀번호: " + newPassword);

        String msg = "";

        try {
            // 사용자 정보 조회
            UserInfoDTO userDTO = userInfoService.getUserInfoByUserId(userId);
            if (userDTO != null) {
                // 비밀번호 암호화
                String encodedPassword = bCryptPasswordEncoder.encode(newPassword);

                // DTO에 암호화된 비밀번호 업데이트
                userDTO = UserInfoDTO.builder()
                        .userId(userDTO.userId())
                        .password(encodedPassword) // 새로운 암호화된 비밀번호 설정
                        .build();

                // 비밀번호 업데이트
                userInfoService.updatePassword(userDTO);

                msg = "success";
                log.info("비밀번호가 성공적으로 재설정되었습니다.");
            } else {
                msg = "fail";
                log.info("사용자를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            msg = "fail";
            log.error("비밀번호 재설정 중 오류 발생: ", e);
        }

        log.info(this.getClass().getName() + ".newPasswordProc End!");

        return MsgDTO.builder().msg(msg).build();
    }


    @GetMapping(value = "newPassword")
    public String newPassword() {
        log.info(this.getClass().getName() + ".user/searchUserId Start!");

        log.info(this.getClass().getName() + ".user/searchUserId End!");

        return "user/newPassword";
    }


    @GetMapping(value = "passwordsearch")
    public String searchPassword(HttpSession session) {
        log.info(this.getClass().getName() + ".user/searchPassword Start!");

        session.setAttribute("NEW_PASSWORD", "");
        session.removeAttribute("NEW_PASSWORD");

        log.info(this.getClass().getName() + ".user/searchPassword End!");

        return "user/passwordsearch";

    }


//    @ResponseBody
//    @PostMapping(value = "updateNickName")
//    public MsgDTO updateNickName(HttpSession session, HttpServletRequest request) {
//        log.info(this.getClass().getName() + ".updateNickName Start!");
//
//        String msg = "";
//        MsgDTO dto = null;
//
//        try {
//            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
//            String nickName = CmmUtil.nvl(request.getParameter("nickName"));
//
//            log.info("userId : " + userId);
//            log.info("nickName : " + nickName);
//
//            UserInfoDTO pDTO = UserInfoDTO.builder()
//                    .userId(userId)
//                    .nickName(nickName)
//                    .build();
//
//            userInfoService.updateNickName(pDTO);
//
//            msg = "닉네임이 수정되었습니다.";
//            session.setAttribute("SS_USER_NICKNAME", nickName);
//
//        } catch (Exception e) {
//            msg = e.getMessage();
//            log.error(e.toString(), e);
//        } finally {
//            dto = MsgDTO.builder().msg(msg).build();
//            log.info(this.getClass().getName() + ".updateNickName End!");
//        }
//        return dto;
//    }

    @ResponseBody
    @PostMapping(value = "updateNickName")
    public MsgDTO updateNickName(HttpSession session, HttpServletRequest request) {
        log.info(this.getClass().getName() + ".updateNickName Start!");

        String msg = "";
        MsgDTO dto = null;

        try {
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String nickName = CmmUtil.nvl(request.getParameter("nickName"));

            log.info("userId : " + userId);
            log.info("nickName : " + nickName);

            UserInfoDTO pDTO = UserInfoDTO.builder()
                    .userId(userId)
                    .nickName(nickName)
                    .build();

            // 닉네임 변경
            userInfoService.updateNickName(pDTO);

            // 세션에 저장된 닉네임 값 업데이트
            session.setAttribute("SS_USER_NICKNAME", nickName);

            msg = "닉네임이 수정되었습니다.";
        } catch (Exception e) {
            msg = e.getMessage();
            log.error(e.toString(), e);
        } finally {
            dto = MsgDTO.builder().msg(msg).build();
            log.info(this.getClass().getName() + ".updateNickName End!");
        }
        return dto;
    }



    @ResponseBody
    @PostMapping(value = "userDelete")
    public MsgDTO userDelete(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".Delete Start!");

        String msg = "";
        MsgDTO dto = null;

        try {
            String userId = CmmUtil.nvl(request.getParameter("userId"));

            log.info("userId : " + userId);

            UserInfoDTO pDTO = UserInfoDTO.builder().userId(userId).build();

            userInfoService.deleteUserInfo(pDTO);

            session.setAttribute("SS_USER_ID", "");
            session.removeAttribute("SS_USER_ID");

            msg = "탈퇴되었습니다. 이용해주셔서 감사합니다";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = MsgDTO.builder().msg(msg).build();

            log.info(this.getClass().getName() + ".UserInfoDelete End!");

        }
        return dto;
    }

//    @GetMapping(value = "myPage")
//    public String myPage(HttpSession session, ModelMap model, HttpServletRequest request) {
//        log.info(this.getClass().getName() + ".user/myPage Start!");
//
//        log.info("Fetching calendar data from the database...");
//
//        // 세션에서 사용자 아이디 가져오기
//        String userId = (String) session.getAttribute("SS_USER_ID");
//
//        log.info("User ID: " + userId);
//
//        // 사용자 정보 리스트 가져오기
//        List<UserInfoDTO> rList = Optional.ofNullable(userInfoService.getUserList(userId))
//                .orElseGet(ArrayList::new);
//
//        // 사용자가 등록한 투어와 예약한 투어의 개수 계산
//        long registeredToursCount = tourService.getTourList().stream()
//                .filter(tour -> tour.userId().equals(userId))
//                .count();
//
//        long reservedToursCount = reservationService.getReservationsForUser(userId).size();
//
//        model.addAttribute("SS_USER_ID", userId);
//        model.addAttribute("rList", rList);
//        model.addAttribute("registeredToursCount", registeredToursCount);
//        model.addAttribute("reservedToursCount", reservedToursCount);
//
//        log.info("확인: " + rList);
//
//        log.info(this.getClass().getName() + ".user/myPage End!");
//
//        return "user/myPage";
//    }
//
//    @GetMapping(value = "tourManagement")
//    public String tourManagement(HttpSession session, ModelMap model) throws Exception {
//        log.info(this.getClass().getName() + ".user/tourManagement Start!");
//
//        String userId = (String) session.getAttribute("SS_USER_ID");
//
//        // 사용자가 등록한 투어 목록
//        List<TourDTO> registeredTours = tourService.getTourList().stream()
//                .filter(tour -> tour.userId().equals(userId))
//                .collect(Collectors.toList());
//
//        long registeredToursCount = registeredTours.size();
//
//        List<ReservationDTO> reservedTours = reservationService.getReservationsForUser(userId);
//
//        List<TourDTO> tourList = tourService.getTourList();
//        Map<Long, TourDTO> tourMap = tourList.stream()
//                .collect(Collectors.toMap(TourDTO::tourSeq, tour -> tour));
//
//        List<Map<String, Object>> reservationsWithTourInfo = reservedTours.stream().map(reservation -> {
//            Map<String, Object> map = new HashMap<>();
//            map.put("reservationId", reservation.reservationId());
//            TourDTO tour = tourMap.get(reservation.tourSeq());
//            map.put("tourTitle", tour.tourTitle());
//            map.put("tourDate", tour.tourDate());
//            return map;
//        }).collect(Collectors.toList());
//
//        // 투어에 예약한 사용자들의 닉네임과 이메일 정보를 추가
//        Map<Long, List<Map<String, String>>> tourReservationsInfo = new HashMap<>();
//        for (TourDTO tour : registeredTours) {
//            List<ReservationDTO> tourReservations = reservationService.getReservationsForTour(tour.tourSeq());
//            List<Map<String, String>> reservationDetails = new ArrayList<>();
//            for (ReservationDTO reservation : tourReservations) {
//                UserInfoDTO userInfo = userInfoService.getUserInfoByUserId(reservation.userId());
//                Map<String, String> userDetail = new HashMap<>();
//                userDetail.put("nickName", userInfo.nickName());
//                userDetail.put("email", userInfo.email());
//                reservationDetails.add(userDetail);
//            }
//            tourReservationsInfo.put(tour.tourSeq(), reservationDetails);
//        }
//
//        // 찜한 투어 목록 추가
//        List<FavoriteTourDTO> favoriteTours = favoriteTourService.getFavoriteToursForUser(userId);
//        List<Map<String, Object>> favoriteToursWithInfo = favoriteTours.stream().map(favorite -> {
//            Map<String, Object> map = new HashMap<>();
//            TourDTO tour = tourMap.get(favorite.tourSeq());
//            map.put("tourSeq", tour.tourSeq());
//            map.put("tourTitle", tour.tourTitle());
//            map.put("tourDate", tour.tourDate());
//            return map;
//        }).collect(Collectors.toList());
//
//        model.addAttribute("registeredToursCount", registeredToursCount);
//        model.addAttribute("reservedToursCount", reservedTours.size());
//        model.addAttribute("reservationsWithTourInfo", reservationsWithTourInfo);
//        model.addAttribute("registeredTours", registeredTours);
//        model.addAttribute("tourReservationsInfo", tourReservationsInfo);
//        model.addAttribute("favoriteTours", favoriteToursWithInfo);
//
//        log.info(this.getClass().getName() + ".user/tourManagement End!");
//
//        return "user/tourManagement";
//    }


    @GetMapping(value = "myPage")
    public String myPage(HttpSession session, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + ".user/myPage Start!");

        // 세션에서 사용자 아이디 가져오기
        String userId = (String) session.getAttribute("SS_USER_ID");

        log.info("User ID: " + userId);

        // 사용자 정보 리스트 가져오기
        List<UserInfoDTO> rList = Optional.ofNullable(userInfoService.getUserList(userId))
                .orElseGet(ArrayList::new);

        // 사용자가 등록한 투어 목록 가져오기
        List<TourDTO> registeredTours = tourService.getTourList().stream()
                .filter(tour -> tour.userId().equals(userId))
                .collect(Collectors.toList());

        long registeredToursCount = registeredTours.size();

        // 사용자가 예약한 투어 목록 가져오기
        List<ReservationDTO> reservedTours = reservationService.getReservationsForUser(userId);
        List<TourDTO> tourList = tourService.getTourList();
        Map<Long, TourDTO> tourMap = tourList.stream()
                .collect(Collectors.toMap(TourDTO::tourSeq, tour -> tour));

        List<Map<String, Object>> reservationsWithTourInfo = reservedTours.stream().map(reservation -> {
            Map<String, Object> map = new HashMap<>();
            map.put("reservationId", reservation.reservationId());
            TourDTO tour = tourMap.get(reservation.tourSeq());
            map.put("tourTitle", tour.tourTitle());
            map.put("tourDate", tour.tourDate());
            return map;
        }).collect(Collectors.toList());

        // 투어에 예약한 사용자들의 닉네임과 이메일 정보를 추가
        Map<Long, List<Map<String, String>>> tourReservationsInfo = new HashMap<>();
        for (TourDTO tour : registeredTours) {
            List<ReservationDTO> tourReservations = reservationService.getReservationsForTour(tour.tourSeq());
            List<Map<String, String>> reservationDetails = new ArrayList<>();
            for (ReservationDTO reservation : tourReservations) {
                UserInfoDTO userInfo = userInfoService.getUserInfoByUserId(reservation.userId());
                Map<String, String> userDetail = new HashMap<>();
                userDetail.put("nickName", userInfo.nickName());
                userDetail.put("email", userInfo.email());
                reservationDetails.add(userDetail);
            }
            tourReservationsInfo.put(tour.tourSeq(), reservationDetails);
        }

        // 찜한 투어 목록 가져오기
        List<FavoriteTourDTO> favoriteTours = favoriteTourService.getFavoriteToursForUser(userId);
        List<Map<String, Object>> favoriteToursWithInfo = favoriteTours.stream().map(favorite -> {
            Map<String, Object> map = new HashMap<>();
            TourDTO tour = tourMap.get(favorite.tourSeq());
            map.put("tourSeq", tour.tourSeq());
            map.put("tourTitle", tour.tourTitle());
            map.put("tourDate", tour.tourDate());
            return map;
        }).collect(Collectors.toList());

        // 모델에 속성 추가
        model.addAttribute("SS_USER_ID", userId);
        model.addAttribute("rList", rList);
        model.addAttribute("registeredToursCount", registeredToursCount);
        model.addAttribute("reservedToursCount", reservedTours.size());
        model.addAttribute("reservationsWithTourInfo", reservationsWithTourInfo);
        model.addAttribute("registeredTours", registeredTours);
        model.addAttribute("tourReservationsInfo", tourReservationsInfo);
        model.addAttribute("favoriteTours", favoriteToursWithInfo);

        log.info(this.getClass().getName() + ".user/myPage End!");

        return "user/myPage";
    }

}