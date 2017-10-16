package com.springmvc.dao;

import com.springmvc.vo.Order;
import com.springmvc.vo.OrderSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Repository
@Transactional
public class OrderDaoImpl {
    @Autowired
    EntityManagerFactory entityManagerFactory;

    public Page<Order> getOrderList(OrderSearchVo orderSearchVo, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int limit = (pageNumber) * pageSize;
        int max = (pageNumber + 1) * pageSize;
        StringBuilder sb = new StringBuilder("select * from t_order where 1=1 ");
        StringBuilder countSb = new StringBuilder("select count(*) from t_order where 1=1 ");
        if (!StringUtils.isEmpty(orderSearchVo.getName())) {
            sb.append(" and name like ?1 ");
            countSb.append(" and name like ?1 ");
        }
        if (!StringUtils.isEmpty(orderSearchVo.getMobile())) {
            sb.append(" and mobile = ?2 ");
            countSb.append(" and mobile = ?2 ");
        }
        if (!StringUtils.isEmpty(orderSearchVo.getStartDate())) {
            sb.append(" and create_date > ?3 ");
            countSb.append(" and create_date > ?3 ");
        }
        if (!StringUtils.isEmpty(orderSearchVo.getEndDate())) {
            sb.append(" and create_date < ?4 ");
            countSb.append(" and create_date < ?4 ");
        }
        if (!StringUtils.isEmpty(orderSearchVo.getDeliverId())) {
            sb.append(" and deliver_id = ?5 ");
            countSb.append(" and deliver_id = ?5 ");
        }
        if (!StringUtils.isEmpty(orderSearchVo.getUserId())) {
            sb.append(" and user_id = ?6 ");
            countSb.append(" and user_id = ?6 ");
        }
        if (!StringUtils.isEmpty(orderSearchVo.getStatus())) {
            sb.append(" and status = ?7 ");
            countSb.append(" and status = ?7 ");
        }
        sb.append(" order by create_date");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query listQuery = entityManager.createNativeQuery(sb.toString(), Order.class);
        Query countQuery = entityManager.createNativeQuery(countSb.toString());

        if (!StringUtils.isEmpty(orderSearchVo.getName())) {
            listQuery.setParameter(1, orderSearchVo.getName());
            countQuery.setParameter(1, orderSearchVo.getName());
        }
        if (!StringUtils.isEmpty(orderSearchVo.getMobile())) {
            listQuery.setParameter(2, orderSearchVo.getMobile());
            countQuery.setParameter(2, orderSearchVo.getMobile());
        }
        if (!StringUtils.isEmpty(orderSearchVo.getStartDate())) {
            listQuery.setParameter(3, orderSearchVo.getStartDate());
            countQuery.setParameter(3, orderSearchVo.getStartDate());
        }
        if (!StringUtils.isEmpty(orderSearchVo.getEndDate())) {
            listQuery.setParameter(4, orderSearchVo.getEndDate());
            countQuery.setParameter(4, orderSearchVo.getEndDate());
        }
        if (!StringUtils.isEmpty(orderSearchVo.getDeliverId())) {
            listQuery.setParameter(5, orderSearchVo.getDeliverId());
            countQuery.setParameter(5, orderSearchVo.getDeliverId());
        }
        if (!StringUtils.isEmpty(orderSearchVo.getUserId())) {
            listQuery.setParameter(6, orderSearchVo.getUserId());
            countQuery.setParameter(6, orderSearchVo.getUserId());
        }
        if (!StringUtils.isEmpty(orderSearchVo.getStatus())) {
            listQuery.setParameter(7, orderSearchVo.getStatus());
            countQuery.setParameter(7, orderSearchVo.getStatus());
        }
        listQuery.setFirstResult(limit);
        listQuery.setMaxResults(max);
        List<Order> list = listQuery.getResultList();
        List countList = countQuery.getResultList();
        Long count = 0L;
        if (countList != null && !countList.isEmpty()) {
            count = Long.valueOf(String.valueOf(countList.get(0)));
        }
        PageImpl page = new PageImpl(list, pageable, count);
        entityManager.close();
        return page;
    }

    public List getUserOrderCount(OrderSearchVo orderSearchVo) {
        StringBuilder sb = new StringBuilder("select user ,count(*) count from t_order where 1=1 ");

        if (!StringUtils.isEmpty(orderSearchVo.getStartDate())) {
            sb.append(" and create_date > ?1 ");
        }
        if (!StringUtils.isEmpty(orderSearchVo.getEndDate())) {
            sb.append(" and create_date < ?2 ");
        }
        if (!StringUtils.isEmpty(orderSearchVo.getUserId())) {
            sb.append(" and user_id = ?3 ");
        }
        if (!StringUtils.isEmpty(orderSearchVo.getStatus())) {
            sb.append(" and status = ?4 ");
        }
        sb.append("group by user_id");
        EntityManager manager = entityManagerFactory.createEntityManager();
        Query listQuery = manager.createNativeQuery(sb.toString());

        if (!StringUtils.isEmpty(orderSearchVo.getStartDate())) {
            listQuery.setParameter(3, orderSearchVo.getStartDate());
        }
        if (!StringUtils.isEmpty(orderSearchVo.getEndDate())) {
            listQuery.setParameter(4, orderSearchVo.getEndDate());
        }
        if (!StringUtils.isEmpty(orderSearchVo.getUserId())) {
            listQuery.setParameter(6, orderSearchVo.getUserId());
        }
        if (!StringUtils.isEmpty(orderSearchVo.getStatus())) {
            listQuery.setParameter(7, orderSearchVo.getStatus());
        }
        List list = listQuery.getResultList();
        manager.close();
        return list;
    }


    public List getUserDateOrderCount(OrderSearchVo orderSearchVo) {
        StringBuilder sb = new StringBuilder("select DATE_FORMAT(create_date,'%Y-%m-%d') date,count(*) count from t_order where 1=1 ");

        if (!StringUtils.isEmpty(orderSearchVo.getStartDate())) {
            sb.append(" and create_date > ?1 ");
        }
        if (!StringUtils.isEmpty(orderSearchVo.getEndDate())) {
            sb.append(" and create_date < ?2 ");
        }
        if (!StringUtils.isEmpty(orderSearchVo.getUserId())) {
            sb.append(" and user_id = ?3 ");
        }
        if (!StringUtils.isEmpty(orderSearchVo.getStatus())) {
            sb.append(" and status = ?4 ");
        }
        sb.append("group by date ");
        EntityManager manager = entityManagerFactory.createEntityManager();
        Query listQuery = manager.createNativeQuery(sb.toString());

        if (!StringUtils.isEmpty(orderSearchVo.getStartDate())) {
            listQuery.setParameter(3, orderSearchVo.getStartDate());
        }
        if (!StringUtils.isEmpty(orderSearchVo.getEndDate())) {
            listQuery.setParameter(4, orderSearchVo.getEndDate());
        }
        if (!StringUtils.isEmpty(orderSearchVo.getUserId())) {
            listQuery.setParameter(6, orderSearchVo.getUserId());
        }
        if (!StringUtils.isEmpty(orderSearchVo.getStatus())) {
            listQuery.setParameter(7, orderSearchVo.getStatus());
        }
        List list = listQuery.getResultList();
        manager.close();
        return list;
    }

    @Modifying
    public void delete(List<Long> ids) {
        EntityManager manager = entityManagerFactory.createEntityManager();
        EntityTransaction tx = manager.getTransaction();
        try {
            tx.begin();
            String sql = "delete from t_order where id in (?1)";
            Query listQuery = manager.createNativeQuery(sql);
            listQuery.setParameter(1, ids);
            listQuery.executeUpdate();
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            manager.close();
        }
    }
}