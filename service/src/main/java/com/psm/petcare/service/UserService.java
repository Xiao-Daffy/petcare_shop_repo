package com.psm.petcare.service;

import com.psm.petcare.entity.User;
import com.psm.petcare.entity.UserAddress;
import com.psm.petcare.vo.ResultVO;
import org.springframework.stereotype.Repository;


public interface UserService {

    public ResultVO userRegister(String email,String pwd);

    public ResultVO checkLogin(String email, String pwd);

    public ResultVO restPassword(String email, String password);

    public ResultVO getProfile(String userId);

    public ResultVO updateProfile(String userId, User user);

    // 1. get list of address
    public ResultVO getListAddress(String userId);

    //2. delete address
    public ResultVO deleteAddress(String addrId);

    //3. add address
    public ResultVO addAddress(UserAddress userAddress);

    // get address
    public ResultVO getAddress(String addrId);

    // update address
    public ResultVO updateAddressInfo( UserAddress userAddress);
}
