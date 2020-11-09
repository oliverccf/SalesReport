package br.com.salesreport.filereader;

import br.com.salesreport.model.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Callable;

public abstract class FileReader implements Readable, Callable<Bundle> {

    protected File fileIn;

    public FileReader(File folderIn) {
        this.fileIn = folderIn;
    }

    @Override
    public abstract Bundle process() throws IOException;

    @Override
    public Bundle call() throws Exception {
        return process();
    }
}
