package com.springmvc.controller;

import com.springmvc.service.UserService;
import com.springmvc.util.MD5Utils;
import com.springmvc.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhang on 2017/10/8.
 */
@Controller
public class IndexController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout( HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String HelloWorld(Model model, HttpServletRequest request, @RequestParam("userName") String userName, @RequestParam("passWord") String passWord) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        //加密后的字符串
        User user = userService.login(userName, MD5Utils.getPwd(passWord));
        HttpSession session = request.getSession();
        if (user != null) {
            if(user.isEnable()){
                session.setAttribute("user",user);
                return "index";
            }else {
                model.addAttribute("message","账号已无效");
                return "login";
            }
        } else {
            model.addAttribute("message","账号或密码错误");
            return "login";
        }

    }

}