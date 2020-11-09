package br.com.salesreport.filereader;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.salesreport.model.Bundle;
import br.com.salesreport.model.Salesman;

import java.math.BigDecimal;

import static br.com.salesreport.utils.Log.writeLog;
import static br.com.salesreport.utils.RecordFieldValidator.compareIfEqual;

public class RecordReader001 {

    public static void read(Bundle bundle, int lineIndex, String[] fields) {
        if (fields.length == 4 && compareIfEqual("001", fields[0])) {
            try {
                new CPFValidator().assertValid(fields[1]);
                var salesman = new Salesman(fields[0], fields[1], fields[2], new BigDecimal(fields[3]));
                bundle.salesmans().add(salesman);
            } catch (InvalidStateException e) {
                writeLog("""
                            CPF inválido no registro %s da linha %d
                        """, fields[0], lineIndex++);
            } catch (NumberFormatException e) {
                writeLog("""
                            Valor numérico %s inválido no registro %s na coluna 3 linha %d
                        """, fields[3], fields[0], lineIndex++);
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
