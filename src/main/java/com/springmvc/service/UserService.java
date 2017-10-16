package com.springmvc.service;

import com.springmvc.dao.UserDao;
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
public class UserService {
    @Autowired
    private UserDao userDao;

    public void save(User user) {
        userDao.saveAndFlush(user);
    }

    public Page<User> getAll(Pageable pageable) {
        return userDao.findAll(pageable);
    }

    public List<User> getAll() {
        return userDao.findAll();
    }

    public User getById(Long id) {
        return userDao.findOne(id);
    }

    public User login(String userName,String passWord){
        return userDao.findByUserNameAndPass(userName,passWord);
    }


}
