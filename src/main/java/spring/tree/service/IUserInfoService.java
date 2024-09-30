package spring.tree.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import spring.tree.dto.UserInfoDTO;

import java.util.List;

public interface IUserInfoService extends UserDetailsService {

    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getNickNameExists(UserInfoDTO pDTO) throws Exception;

    int insertUserInfo(UserInfoDTO pDTO) throws Exception;

    int getUserLogin(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getUserEmailExists(UserInfoDTO pDTO) throws Exception;

    String searchUserIdProc(UserInfoDTO pDTO) throws Exception;

    void updatePassword(UserInfoDTO pDTO) throws Exception;

    void deleteUserInfo(UserInfoDTO pDTO) throws Exception;

    List<UserInfoDTO> getUserList(String userId);

    void updateNickName(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getUserInfoByUserId(String userId) throws Exception;

    UserInfoDTO getUserInfoByUserIdAndNameAndEmail(UserInfoDTO pDTO) throws Exception;




}
