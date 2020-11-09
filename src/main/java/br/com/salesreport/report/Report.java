package br.com.salesreport.report;

import java.io.File;

public abstract class Report implements Runnable {

    protected File folderOut;

    public Report(File folderOut) {
        this.folderOut = folderOut;
    }

    protected abstract void execute();

    @Override
    public void run() {
        execute();
    }
}
