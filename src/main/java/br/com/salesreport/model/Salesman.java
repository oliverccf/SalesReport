package br.com.salesreport.model;

import java.math.BigDecimal;

public record Salesman(String id, String cpf, String name, BigDecimal salary) {
}
