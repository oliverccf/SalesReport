package br.com.salesreport;

import br.com.salesreport.filereader.RecordReader001;
import br.com.salesreport.filereader.RecordReader002;
import br.com.salesreport.model.Bundle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class RecordReader002Test {

    @Test
    public void readingGoodRecordTest() {

        String[] fields = "002ç28841374000159çJose daSilvaçRural".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader002.read(bandle, 1, fields);

        assert bandle.customers().size() > 0;

    }

    @Test
    public void readingWrongCNPJRecordTest() {

        String[] fields = "002ç28841çJose daSilvaçRural".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader002.read(bandle, 1, fields);

        assert bandle.customers().isEmpty();

    }

    @Test
    public void readingRecordWithoutIdentifyerTest() {

        String[] fields = "ç28841374000159çJose daSilvaçRural".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader002.read(bandle, 1, fields);

        assert bandle.customers().isEmpty();

    }

    @Test
    public void readingRecordWithLessFieldsTest() {

        String[] fields = "002ç28841374000159çJose daSilva".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader002.read(bandle, 1, fields);

        assert bandle.customers().isEmpty();

    }

    @Test
    public void readingEmptyRecordTest() {

        String[] fields = "".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader002.read(bandle, 1, fields);

        assert bandle.customers().isEmpty();

    }



}
