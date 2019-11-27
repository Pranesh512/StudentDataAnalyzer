package com.pranze.studentdataanalyzer.query;

public class Order {

    private final String orderBy;
    private final boolean desc;

    public Order(String orderBy, boolean desc) {
        this.orderBy = orderBy;
        this.desc = desc;
    }

    public static Order of(String orderBy, boolean desc) {
        return new Order(orderBy, desc);
    }

    public String getOrderBy() {
        return orderBy;
    }

    public boolean isDesc() {
        return desc;
    }
}
