package com.springmvc.service;

import com.springmvc.dao.OrderDao;
import com.springmvc.dao.OrderDaoImpl;
import com.springmvc.dao.UserDao;
import com.springmvc.vo.Order;
import com.springmvc.vo.OrderSearchVo;
import com.springmvc.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhang on 2017/10/8.
 */
@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderDaoImpl orderDaoImpl;

    public void save(Order order) {
        orderDao.saveAndFlush(order);
    }

    public Page<Order> getAll(OrderSearchVo orderSearchVo, Pageable pageable) {
        return orderDaoImpl.getOrderList(orderSearchVo, pageable);
    }

    public Order getById(Long id) {
        return orderDao.findOne(id);
    }

    public Order findByMobile(String mobile) {
        return orderDao.findByMobile(mobile);
    }

    public List getUserOrderCount(OrderSearchVo orderSearchVo ){
        return orderDaoImpl.getUserOrderCount(orderSearchVo);
    }
    public List getUserDateOrderCount(OrderSearchVo orderSearchVo ) {
        return orderDaoImpl.getUserDateOrderCount(orderSearchVo);
    }

    public void delete(List<Long> ids) {
        orderDaoImpl.delete(ids);
    }

}
