package com.springmvc.controller;

import com.springmvc.service.OrderService;
import com.springmvc.service.UserService;
import com.springmvc.util.MD5Utils;
import com.springmvc.vo.Order;
import com.springmvc.vo.OrderSearchVo;
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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by zhang on 2017/10/8.
 */
@Controller
public class IndexController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping(value = "/index")
    public String index(Model model,HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String myUrl = "http://" + request.getServerName() + ":"
                + request.getServerPort()           //端口号
                + request.getContextPath() + "/buy?userId=" + user.getId();
        model.addAttribute("myUrl",myUrl);
        String userId = null;
        if(!"admin".equals(user.getUserName())){
            userId = String.valueOf(user.getId());
        }
        List list = orderService.getUserOrderCount(new OrderSearchVo().setStartDate(simpleDateFormat.format(Calendar.getInstance().getTime())).setUserId(userId));
        BigInteger sum =BigInteger.ZERO;
        for(Object order:list){
            BigInteger count = (BigInteger)((Object[])order)[1];
            sum = sum.add(count);
        }
        model.addAttribute("sum",sum.longValue());
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
    public String login(Model model, HttpServletRequest request, @RequestParam("userName") String userName, @RequestParam("passWord") String passWord) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        //加密后的字符串
        User user = userService.login(userName, MD5Utils.getPwd(passWord));
        HttpSession session = request.getSession();
        if (user != null) {
            if(user.isEnable()){
                session.setAttribute("user",user);
                String myUrl = "http://" + request.getServerName() + ":"
                        + request.getServerPort()           //端口号
                        + request.getContextPath() + "/buy?userId=" + user.getId();
                model.addAttribute("myUrl",myUrl);
                String userId = null;
                if(!"admin".equals(user.getUserName())){
                    userId = String.valueOf(user.getId());
                }
                List list = orderService.getUserOrderCount(new OrderSearchVo().setStartDate(simpleDateFormat.format(Calendar.getInstance().getTime())).setUserId(userId));
                BigInteger sum =BigInteger.ZERO;
                for(Object order:list){
                    BigInteger count = (BigInteger)((Object[])order)[1];
                    sum = sum.add(count);
                }
                model.addAttribute("sum",sum.longValue());
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