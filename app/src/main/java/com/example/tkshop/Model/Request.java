package com.example.tkshop.Model;

import java.io.Serializable;
import java.util.List;

public class Request implements Serializable {
    private String phone;
    private String name;
    private String address;
    private int total;
    private int status;
    private String dateCreated;
    private String dateConfirm;
    private String dateCanceled;
    private String dateDelivery;
    private String dateSuccess;
    private List<Order> list;
    private String massage;
    private String phone_status;
    private String cancellation_reason;

    public Request() {
    }

    public Request(String phone, String name, String address, int total,String dateCreated,List<Order> list, String massage) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.list = list;
        this.massage = massage;
        this.dateCreated = dateCreated;
        this.dateCanceled = "";
        this.dateConfirm = "";
        this.dateDelivery = "";
        this.dateSuccess = "";
        this.status = 0; // 0 - chờ xác nhận , 1 - xác nhận , 2 - hủy , 3 - đang giao - 4 thành công
        this.phone_status = this.phone+"_"+this.status;
        cancellation_reason = "";
    }

    public String getDateConfirm() {
        return dateConfirm;
    }

    public void setDateConfirm(String dateConfirm) {
        this.dateConfirm = dateConfirm;
    }

    public String getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(String dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public String getDateSuccess() {
        return dateSuccess;
    }

    public void setDateSuccess(String dateSuccess) {
        this.dateSuccess = dateSuccess;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateCanceled() {
        return dateCanceled;
    }

    public void setDateCanceled(String dateCanceled) {
        this.dateCanceled = dateCanceled;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public List<Order> getList() {
        return list;
    }

    public void setList(List<Order> list) {
        this.list = list;
    }

    public String getPhone_status() {
        return phone_status;
    }

    public void setPhone_status(String phone_status) {
        this.phone_status = phone_status;
    }

    public String getCancellation_reason() {
        return cancellation_reason;
    }

    public void setCancellation_reason(String cancellation_reason) {
        this.cancellation_reason = cancellation_reason;
    }
}


