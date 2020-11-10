package br.com.salesreport.launcher;

import br.com.salesreport.filereader.TXTFileReader;
import br.com.salesreport.model.Bundle;
import br.com.salesreport.report.SummarySalesReport;
import br.com.salesreport.utils.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.Time;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class FileSalesSkanner {

    private File filesInput;
    private File filesOutput;

    public FileSalesSkanner(File filesInput, File filesOutput) {
        this.filesInput = filesInput;
        this.filesOutput = filesOutput;
    }

    public void start() {

        final var scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {

            ExecutorService executorService = Executors.newFixedThreadPool(1);
            try {
                validateInputOutput();
                List<Path> files = Files.list(this.filesInput.toPath()).filter(path -> path.toFile().isFile() && path.getFileName().toString().endsWith(".txt")).collect(Collectors.toList());
                for (Path file : files) {
                    try {
                        Future<Bundle> future = executorService.submit(new TXTFileReader(file.toFile()));
                        while (!future.isDone()) {
                            sleep(1);
                        }
                        Bundle bundle = future.get();
                        executorService.submit(new SummarySalesReport(bundle, this.filesOutput, executorService));
                    } catch (InterruptedException | ExecutionException e) {
                        if (e instanceof InterruptedException) {
                            Log.writeLog("""
                                    Execução interrompida para o arquivo %s, favor verificar mensagem abaixo:
                                    %s
                                    """, file.getFileName().toString(), e.getMessage());
                        } else if (e instanceof ExecutionException) {
                            Log.writeLog("""
                                    Ocorreu um erro durante a execução do processo para o arquivo %s. Favor verificar mensagem abaixo:
                                    %s
                                    """, file.getFileName().toString(), e.getMessage());
                        }
                    }
                }
            } catch (IOException e) {
                executorService.shutdownNow();
                if (e instanceof IOException) {
                    Log.writeLog("""
                            Diretório(s) inválido(s). Verifique a mensagem abaixo:
                            %s
                            """, e.getMessage());
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    public void validateInputOutput() throws IOException {

        var directoryInputInvalid = !Files.isDirectory(this.filesInput.toPath());
        var directoryOutputInvalid = !Files.isDirectory(this.filesOutput.toPath());

        if (directoryInputInvalid || directoryOutputInvalid) {

            String InputMessageError = directoryInputInvalid ? "Diretório de entrada inválido" : "";
            String OutputMessageError = directoryOutputInvalid ? "Diretório de saída inválido" : "";

            String message = """
                       %s
                       %s
                    """.formatted(InputMessageError, OutputMessageError);

            throw new IOException(message);

        }
    }

}
