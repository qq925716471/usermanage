package com.springmvc.controller;

import com.google.zxing.WriterException;
import com.springmvc.service.UserService;
import com.springmvc.util.MatrixToImageWriter;
import com.springmvc.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by zhang on 2017/10/8.
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    private Page<User> pageVo;

    @RequestMapping("/user")
    public String toUserList(Integer pageIndex, Model model) {
        if (pageIndex == null) pageIndex = Integer.valueOf(0);
        PageRequest page = new PageRequest(pageIndex, 50);
        pageVo = userService.getAll(page);
        model.addAttribute("pageVo", pageVo);
        return "user";
    }

    @RequestMapping("/user/disable")
    @ResponseBody
    public String disable(@RequestParam("userId") Long userId) {
        User user = userService.getById(userId);
        user.setEnable(false);
        userService.save(user);
        return "success";
    }

    @RequestMapping("/user/updatePass")
    @ResponseBody
    public String updatePass(HttpServletRequest request,@RequestParam("oldPass") String oldPass,@RequestParam("passWord") String passWord) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        User dbUser = userService.getById(user.getId());
        if(!user.getPassWord().equals(oldPass)){
            return "oldPassError";
        }
        dbUser.setPassWord(passWord);
        userService.save(dbUser);
        return "success";
    }

    @RequestMapping("/user/enable")
    @ResponseBody
    public String enable(@RequestParam("userId") Long userId) {
        User user = userService.getById(userId);
        user.setEnable(true);
        userService.save(user);
        return "success";
    }

    @RequestMapping("/user/save")
    @ResponseBody
    public String save(@ModelAttribute("user") User user) {
        user.setEnable(true);
        userService.save(user);
        return "success";
    }

    @RequestMapping("/user/download")
    @ResponseBody
    public void download(@RequestParam("userId") Long userId, HttpServletRequest request, HttpServletResponse response) throws IOException, WriterException {
        User user = userService.getById(userId);
        String url = "http://" + request.getServerName() + ":"
                + request.getServerPort()           //端口号
                + request.getContextPath() + "/buy?userId=" + userId;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/force-download;charset=utf-8");// 设置强制下载不打开
        response.addHeader("Content-Disposition",
                "attachment;fileName=" + new String(user.getName().getBytes("UTF-8"),"iso8859-1")+".jpg");// 设置文件名

        MatrixToImageWriter.writeToStream(url, response.getOutputStream());
    }

    public Page<User> getPageVo() {
        return pageVo;
    }

    public void setPageVo(Page<User> pageVo) {
        this.pageVo = pageVo;
    }
}
