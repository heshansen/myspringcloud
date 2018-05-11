package com.topbaby.cloud.authservice.controllers;

import com.topbaby.cloud.authservice.entity.BaseUserDetails;
import com.topbaby.cloud.authservice.entity.dto.CoreUserDTO;
import com.topbaby.cloud.authservice.service.UserService;
import com.topbaby.data.exception.BaseCoreException;
import com.topbaby.data.model.MessageDTO;
import com.topbaby.data.model.MessageType;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Principal getUser(Principal user) {
        return user;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public BaseUserDetails addUser(@Valid @RequestBody BaseUserDetails baseUserDetails) throws BaseCoreException {
        return this.userService.add(baseUserDetails);
    }

    /**
     * 获取用户信息
     *
     * @return CoreUserDTO
     */
    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    public CoreUserDTO getInfo(Principal user) {

        // TODO: 17-11-30 替换导购员ID

        BaseUserDetails baseUserDetails = userService.loadUserByUsername(user.getName());

        CoreUserDTO userDTO = new CoreUserDTO();
        userDTO.setFirstLogin(baseUserDetails.getFirstLogin());
        return userDTO;
    }

    /**
     * 修改用户密码
     *
     * @return MessageDTO
     * @userDTO CoreUserDTO 用户对象
     */
    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public MessageDTO updatePassword(@RequestBody CoreUserDTO userDTO, Principal user) throws BaseCoreException {

        DefaultPasswordService defaultPasswordService = new DefaultPasswordService();
        BaseUserDetails baseUserDetails = userService.loadUserByUsername(user.getName());

        //初次登录标识：0-正常，1-初次登录
        baseUserDetails.setFirstLogin(0);
        baseUserDetails.setUserSeq(baseUserDetails.getUserSeq());
        baseUserDetails.setPassword(defaultPasswordService.encryptPassword(userDTO.getPassword()));
        userService.update(baseUserDetails);

        return new MessageDTO(MessageType.SUCCESS, "修改成功");
    }
}
