package com.springmvc.vo;

import com.springmvc.util.ExcelAnnotation;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zhanglijun on 17-10-9.
 */
@Entity
@Table(name = "t_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    @ExcelAnnotation(name="客户姓名")
    private String name;
    @ExcelAnnotation(name="客户电话")
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "province")
    @ExcelAnnotation(name="省")
    private String province;
    @Column(name = "city")
    @ExcelAnnotation(name="市")
    private String city;
    @Column(name = "area")
    @ExcelAnnotation(name="区")
    private String area;
    @Column(name = "addr")
    @ExcelAnnotation(name="详细地址")
    private String addr;
    @Column(name = "style")
    @ExcelAnnotation(name="样式")
    private String style;
    @Column(name = "size")
    @ExcelAnnotation(name="型号")
    private String size;
    @Column(name = "user_id")
    private String userId;//员工
    @Column(name = "create_date")
    @ExcelAnnotation(name="创建时间")
    private Date createDate;
    @Column(name = "deliver_id")
    @ExcelAnnotation(name="快递单号")
    private String deliverId;
    @ExcelAnnotation(name="快递")
    @Column(name = "deliver_name")
    private String deliverName;
    @Column(name = "status")
    @ExcelAnnotation(name="状态")
    private String status;
    @Column(name = "user")
    @ExcelAnnotation(name="员工姓名")
    private String user;//员工姓名
    @Column(name = "note")
    @ExcelAnnotation(name="备注")
    private String note;//备注
    @Column(name = "ts")
    private String ts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDeliverId() {
        return deliverId;
    }

    public void setDeliverId(String deliverId) {
        this.deliverId = deliverId;
    }

    public String getDeliverName() {
        return deliverName;
    }

    public void setDeliverName(String deliverName) {
        this.deliverName = deliverName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}
