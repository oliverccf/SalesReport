package br.com.salesreport.report;

import br.com.salesreport.model.Bundle;
import br.com.salesreport.model.Sale;
import br.com.salesreport.utils.Log;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.concurrent.ExecutorService;

import static java.util.Comparator.comparing;

public class SummarySalesReport extends Report {

    private final ExecutorService executorService;
    private final Bundle bundle;

    public SummarySalesReport(Bundle bundle, File folderOut, ExecutorService executorService) {
        super(folderOut);
        this.bundle = bundle;
        this.executorService = executorService;
    }

    @Override
    protected void execute() {

        try {
            String report = """
                    ########################################
                    #         Relatório de vendas          # 
                    ########################################
                    Total Clientes               : %d
                    Total Vendedores             : %d
                    ID da venda mais cara        : %s
                    Pior Vendedor                : %s 
                    """.formatted(totalCustomers(), totalSellers(), idOfBigestSale(), worstSeller());

            try {
                String filename = "file:///./" + folderOut.getAbsolutePath() + "/relatorio_de_vendas" + LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HHmmss")) + ".txt";

                Files.writeString(Path.of(URI.create(filename)), report);
            } catch (IOException e) {
                Log.writeLog("""
                        Não foi possível escrever no diretório %s
                        Ver mensagem abaixo:
                        """, folderOut.getAbsolutePath(), e.getMessage());
            }

        } finally {
            this.executorService.shutdown();
        }

    }

    public int totalCustomers() {
        return bundle.customers().size();
    }

    public int totalSellers() {
        return bundle.salesmans().size();
    }

    public String idOfBigestSale() {
        return bundle.sales().stream().max(comparing(Sale::total)).orElse(new Sale("", "", Collections.emptyList(),
                "")).salesId();
    }

    public String worstSeller() {
        return bundle.sales().stream().min(comparing(Sale::total)).orElse(new Sale("", "",
                Collections.emptyList(), "")).salesmanName();
    }


}
