package com.example.restaurant.Domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order extends Entity<Integer>{
    private int Table_id;
    private List<Integer> menuitems;
    private LocalDateTime date;

    private OrderStatus status;


    public Order(int id,int table_id, List<Integer> menuitems, LocalDateTime date, OrderStatus status) {
        super.setId(id);
        this.Table_id = table_id;
        this.menuitems = menuitems;
        this.date = date;
        this.status = status;
    }

    public int getTable_id() {
        return Table_id;
    }

    public void setTable_id(int table_id) {
        Table_id = table_id;
    }

    public List<Integer> getMenuitems() {
        return menuitems;
    }

    public void setMenuitems(List<Integer> menuitems) {
        this.menuitems = menuitems;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Order order = (Order) o;
        return Table_id == order.Table_id && Objects.equals(menuitems, order.menuitems) && Objects.equals(date, order.date) && status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), Table_id, menuitems, date, status);
    }

    @Override
    public String toString() {
        return "Order{" +
                "Table_id=" + Table_id +
                ", menuitems=" + menuitems +
                ", date=" + date +
                ", status=" + status +
                '}';
    }
}
