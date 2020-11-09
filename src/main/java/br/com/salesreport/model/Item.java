package br.com.salesreport.model;

import java.math.BigDecimal;

public record Item(String itemId, String itemQuantity, BigDecimal itemPrice) {}
