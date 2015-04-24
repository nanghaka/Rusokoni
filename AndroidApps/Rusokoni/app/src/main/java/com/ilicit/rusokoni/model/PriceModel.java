package com.ilicit.rusokoni.model;

/**
 * Created by Shaffic on 4/17/15.
 */
public class PriceModel {

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getMl_price() {
        return ml_price;
    }

    public void setMl_price(String ml_price) {
        this.ml_price = ml_price;
    }

    public String getMl_wholesaleprice() {
        return ml_wholesaleprice;
    }

    public void setMl_wholesaleprice(String ml_wholesaleprice) {
        this.ml_wholesaleprice = ml_wholesaleprice;
    }

    public String getMl_time_recorded() {
        return ml_time_recorded;
    }

    public void setMl_time_recorded(String ml_time_recorded) {
        this.ml_time_recorded = ml_time_recorded;
    }

    String prod_name;
    String ml_price;
    String ml_wholesaleprice;
    String ml_time_recorded;

}
