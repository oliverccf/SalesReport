package br.com.salesreport.launcher;

import java.io.File;

public class Launcher {

    public static void main(String[] args) {
        new Launcher().start();
    }

    public void start() {
        new FileSalesSkanner(new File("/Users/oliveirafilho/temp/desafio/input"), new File("/Users/oliveirafilho/temp/desafio/output")).start();
    }

}
