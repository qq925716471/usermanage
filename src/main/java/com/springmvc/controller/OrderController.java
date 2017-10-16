package com.springmvc.controller;

import com.springmvc.service.OrderService;
import com.springmvc.service.UserService;
import com.springmvc.util.ExcelFactory;
import com.springmvc.util.VerifyCodeUtils;
import com.springmvc.vo.Order;
import com.springmvc.vo.OrderSearchVo;
import com.springmvc.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by zhang on 2017/10/8.
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    Page<Order> orderList;
    List<User> userList;

    @RequestMapping("/list")
    public String toList(Model model, Integer pageIndex,HttpServletRequest request, @ModelAttribute("orderSearch") OrderSearchVo orderSearchVo) {
        if (pageIndex == null) pageIndex = Integer.valueOf(0);
        PageRequest page = new PageRequest(pageIndex, 20);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(!"admin".equals(user.getUserName())){
            orderSearchVo.setUserId(user.getId().toString());
        }
        orderList = orderService.getAll(orderSearchVo, page);
        userList = userService.getAll();
        model.addAttribute("userList",userList);
        model.addAttribute("orderList",orderList);
        model.addAttribute("totalCount",orderList.getTotalPages());
        model.addAttribute("pageIndex",pageIndex);
        return "orderList";
    }

    @RequestMapping("/export")
    public void export(Integer pageIndex,HttpServletRequest request,HttpServletResponse response, @ModelAttribute("orderSearch") OrderSearchVo orderSearchVo) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-Disposition",
                "attachment;fileName=" + new String("订单详情".getBytes("UTF-8"),"iso8859-1")+".xls");// 设置文件名
        if (pageIndex == null) pageIndex = Integer.valueOf(0);
        PageRequest page = new PageRequest(pageIndex,Integer.MAX_VALUE);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(!"admin".equals(user.getUserName())){
            orderSearchVo.setUserId(user.getId().toString());
        }
        orderList = orderService.getAll(orderSearchVo, page);
        userList = userService.getAll();
        ExcelFactory.createExcel(orderList.getContent(),Order.class,response.getOutputStream());
    }

    @RequestMapping("/count")
    public String toCount(Model model, @ModelAttribute("orderSearch") OrderSearchVo orderSearchVo) {
        List list = orderService.getUserOrderCount(orderSearchVo);
        //List list2 = orderService.getUserDateOrderCount(orderSearchVo);
        userList = userService.getAll();
        model.addAttribute("list",list);
        return "orderCount";
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(Model model, @ModelAttribute("order") Order order) {
        Order dbOrder = orderService.getById(order.getId());
        dbOrder.setStatus(order.getStatus());
        dbOrder.setDeliverId(order.getDeliverId());
        dbOrder.setDeliverName(order.getDeliverName());
        orderService.save(dbOrder);
        return "success";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(Model model, @RequestParam("orderIds") List<Long> orderIds) {
        orderService.delete(orderIds);
        return "success";
    }

    public Page<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(Page<Order> orderList) {
        this.orderList = orderList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
