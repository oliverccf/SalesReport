package br.com.salesreport.model;

import java.util.List;

public record Bundle(List<Salesman> salesmans, List<Customer> customers, List<Sale> sales) {
}
