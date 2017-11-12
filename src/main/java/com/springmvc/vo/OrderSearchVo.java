package com.springmvc.vo;

/**
 * Created by zhanglijun on 17-10-9.
 */
public class OrderSearchVo {
    private String name;
    private String mobile;
    private String province;
    private String city;
    private String addr;
    private String style;
    private String size;
    private String userId;//员工
    private String startDate;
    private String endDate;
    private String deliverId;
    private String deliverName;
    private String status;

    public String getName() {
        return name;
    }

    public OrderSearchVo setName(String name) {
        this.name = name;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public OrderSearchVo setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public String getProvince() {
        return province;
    }

    public OrderSearchVo setProvince(String province) {
        this.province = province;
        return this;
    }

    public String getCity() {
        return city;
    }

    public OrderSearchVo setCity(String city) {
        this.city = city;
        return this;
    }

    public String getAddr() {
        return addr;
    }

    public OrderSearchVo setAddr(String addr) {
        this.addr = addr;
        return this;
    }

    public String getStyle() {
        return style;
    }

    public OrderSearchVo setStyle(String style) {
        this.style = style;
        return this;
    }

    public String getSize() {
        return size;
    }

    public OrderSearchVo setSize(String size) {
        this.size = size;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public OrderSearchVo setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public OrderSearchVo setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public OrderSearchVo setEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getDeliverId() {
        return deliverId;
    }

    public OrderSearchVo setDeliverId(String deliverId) {
        this.deliverId = deliverId;
        return this;
    }

    public String getDeliverName() {
        return deliverName;
    }

    public OrderSearchVo setDeliverName(String deliverName) {
        this.deliverName = deliverName;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public OrderSearchVo setStatus(String status) {
        this.status = status;
        return this;
    }
}
