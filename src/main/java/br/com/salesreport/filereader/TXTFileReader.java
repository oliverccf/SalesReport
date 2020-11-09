package br.com.salesreport.filereader;

import br.com.salesreport.model.Bundle;
import br.com.salesreport.utils.Counter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Stream;

import static br.com.salesreport.utils.Log.writeLog;

public class TXTFileReader extends FileReader {

    public TXTFileReader(File fileIn) throws FileNotFoundException {
        super(fileIn);
    }

    @Override
    public Bundle process() throws IOException {

        Stream<String> lines = Files.lines(this.fileIn.toPath());
        return read(lines);

    }

    private Bundle read(Stream<String> lines) {
        final var bundle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        final var counter = new Counter();
        lines.forEach(line -> {
            String[] fields = line.replaceAll("ç", "Ç").split("Ç");
            if (fields.length > 0) {
                if ("001".equals(fields[0])) {
                    RecordReader001.read(bundle, counter.getIndex(), fields);
                } else if ("002".equals(fields[0])) {
                    RecordReader002.read(bundle, counter.getIndex(), fields);
                } else if ("003".equals(fields[0])) {
                    RecordReader003.read(bundle, counter.getIndex(), fields);
                } else {
                    writeLog("""
                                Registro inválido ou desconhecido na linha %d
                            """, counter.getIndex());
                }
            }
        });
        createBackup();
        return bundle;
    }

    private void createBackup() {
        try {
            String diretoryName = "file:///./" + fileIn.getParent() + "/backup" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            Path destiny = Path.of(URI.create(diretoryName));
            Files.createDirectory(destiny);
            Files.move(this.fileIn.toPath(), Path.of(URI.create(diretoryName + "/" + this.fileIn.toPath().getFileName().toString())));
        } catch (IOException e) {
            writeLog("""
                        Incapaz de criar diretório de backup. Ver mensdagem abaixo
                        %d
                    """,e.getMessage());
        }
    }

}
