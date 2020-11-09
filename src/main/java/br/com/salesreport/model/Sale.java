package br.com.salesreport.model;

import java.math.BigDecimal;
import java.util.List;

public record Sale(String id, String salesId, List<Item> items, String salesmanName) {

    public BigDecimal total() {
      return items.stream().map(Item::itemPrice).reduce(new BigDecimal("0.0"), BigDecimal::add, BigDecimal::add);
    }

}
