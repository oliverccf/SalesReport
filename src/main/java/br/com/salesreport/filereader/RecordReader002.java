package br.com.salesreport.filereader;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.salesreport.model.Bundle;
import br.com.salesreport.model.Customer;
import br.com.salesreport.model.Salesman;

import java.math.BigDecimal;

import static br.com.salesreport.utils.Log.writeLog;
import static br.com.salesreport.utils.RecordFieldValidator.compareIfEqual;

public class RecordReader002 {

    public static void read(Bundle bundle, int lineIndex, String[] fields) {
        if (fields.length == 4 && compareIfEqual("002", fields[0])) {
            try {
                new CNPJValidator().assertValid(fields[1]);
                var customer = new Customer(fields[0], fields[1], fields[2], fields[3]);
                bundle.customers().add(customer);
            } catch (InvalidStateException e) {
                writeLog("""
                            CNPJ inválido no registro %s da linha %d
                        """, fields[0], lineIndex);
            }
        } else {
            if (fields.length != 4) {
                writeLog("""
                            Estrutura inválida para o registro %s na linha %d
                        """, fields[0], lineIndex);
            } else {
                writeLog("""
                            "Registro não identificado na linha %d"
                        """, lineIndex);
            }
        }
    }
}
