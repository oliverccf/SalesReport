package br.com.salesreport.filereader;

import br.com.salesreport.model.Bundle;
import br.com.salesreport.model.Item;
import br.com.salesreport.model.Sale;
import br.com.salesreport.utils.Counter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static br.com.salesreport.utils.Log.writeLog;
import static br.com.salesreport.utils.StringComparator.compareIfEqual;

public class RecordReader003 {

    public static void read(Bundle bundle, int lineIndex, String[] fields) {
        if (fields.length == 4 && compareIfEqual("003", fields[0])) {
            List<Item> items = new ArrayList<>();
            var itemsSectionList = fields[2].split(",");
            if (itemsSectionList.length > 0) {
                var counter = new Counter();
                for (String itemSection : itemsSectionList) {
                    counter.getIndex();
                    var itemsFields = itemSection.split("-");
                    if (itemsFields.length == 3) {
                        try {
                            var item = new Item(itemsFields[0], itemsFields[1], new BigDecimal(itemsFields[2].replace("[", "").replace("]", "")));
                            items.add(item);
                        } catch (NumberFormatException e) {
                            writeLog("""
                                        Valor numérico %s inválido na coluna 2 subcoluna 3 na linha %d
                                    """, itemsFields[2], lineIndex);
                        }
                    } else {
                        writeLog("""
                                    Registro do Item %d na linha %d com má formatação
                                """, counter.getIndex(), lineIndex);
                    }
                }
            }
            if (!items.isEmpty()) {
                var sale = new Sale(fields[0], fields[1], items, fields[3]);
                bundle.sales().add(sale);
            } else {
                writeLog("""
                            Estrutura do registro dos itens com má formatação. Registro %s linha %d
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
