package com.springmvc.controller;

import com.springmvc.service.OrderService;
import com.springmvc.service.UserService;
import com.springmvc.util.VerifyCodeUtils;
import com.springmvc.vo.Order;
import com.springmvc.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by zhang on 2017/10/8.
 */
@Controller
public class BuyController {
    static {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
    }

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/buy")
    public String tobuy(Integer pageIndex) {
        return "buy";
    }

    @RequestMapping("/doBuy")
    @ResponseBody
    public String dobuy(HttpServletRequest request, @ModelAttribute("order") Order order, @RequestParam("imageCode") String imageCode) {
        HttpSession session = request.getSession(true);
        String code = (String) session.getAttribute("verCode");
        if (StringUtils.isEmpty(code)) {
            return "codeError";
        }
        if (!code.equalsIgnoreCase(imageCode)) {
            return "codeError";
        }
        if(orderService.findByTs(order.getTs())!=null||orderService.findByMobile(order.getMobile())!=null){
            return "repeat";
        }
        session.removeAttribute("verCode");
        if (!StringUtils.isEmpty(order.getUserId())) {
            User user = userService.getById(Long.valueOf(order.getUserId()));
            if (user != null) {
                order.setUser(user.getName());
            }
        }
        order.setStatus("待发货");
        order.setCreateDate(Calendar.getInstance().getTime());
        orderService.save(order);
        return "success";
    }

    @RequestMapping("/regImgCode")
    public void regImgCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        //生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        //存入会话session
        HttpSession session = request.getSession(true);
        //删除以前的
        session.removeAttribute("verCode");
        session.setAttribute("verCode", verifyCode.toLowerCase());
        //生成图片
        int w = 120, h = 40;
        VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
    }

}
