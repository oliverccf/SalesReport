package br.com.salesreport.launcher;

import br.com.salesreport.utils.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Launcher {

    public static void main(String[] args) {
        new Launcher().start();
    }

    public void start() {
        new FileSalesSkanner(new File("/Users/oliveirafilho/temp/desafio/input"), new File("/Users/oliveirafilho/temp/desafio/output")).start();
    }

}
