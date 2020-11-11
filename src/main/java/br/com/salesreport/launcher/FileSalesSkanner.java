package br.com.salesreport.launcher;

import br.com.salesreport.filereader.TXTFileReader;
import br.com.salesreport.report.SummarySalesReport;
import br.com.salesreport.utils.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class FileSalesSkanner {

    private final File filesInput;
    private final File filesOutput;

    public FileSalesSkanner(File filesInput, File filesOutput) {
        this.filesInput = filesInput;
        this.filesOutput = filesOutput;
    }

    public void start() {

        final var scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {

            var executorService = Executors.newFixedThreadPool(1);
            try {
                validateInputOutput();
                var files = Files.list(this.filesInput.toPath()).filter(path -> path.toFile().isFile() && path.getFileName().toString().endsWith(".txt")).collect(Collectors.toList());
                for (Path file : files) {
                    try {
                        var future = executorService.submit(new TXTFileReader(file.toFile()));
                        while (!future.isDone()) {
                            sleep(1);
                        }
                        var bundle = future.get();
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

            var InputMessageError = directoryInputInvalid ? "Diretório de entrada inválido" : "";
            var OutputMessageError = directoryOutputInvalid ? "Diretório de saída inválido" : "";

            var message = """
                       %s
                       %s
                    """.formatted(InputMessageError, OutputMessageError);

            throw new IOException(message);

        }
    }

}
