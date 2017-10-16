package com.springmvc.dao;

import com.springmvc.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The Interface UserJpaDao.
 * @author abel
 */
@Repository
public interface UserDao extends JpaRepository<User, Long> {

    /**
     * Find by name.
     *
     * @param name the name
     * @return the user
     */
    User findByName(String name);
    @Query("from User u where u.userName=:userName and u.passWord=:passWord")
    User findByUserNameAndPass(@Param("userName") String userName, @Param("passWord") String passWord);


}