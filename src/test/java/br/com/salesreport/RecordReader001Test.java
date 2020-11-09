package br.com.salesreport;

import br.com.salesreport.filereader.RecordReader001;
import br.com.salesreport.filereader.RecordReader002;
import br.com.salesreport.model.Bundle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.ListResourceBundle;

public class RecordReader001Test {

    @Test
    public void readingGoodRecordTest() {

        String[] fields = "001ç17383980050çPedroç50000".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader001.read(bandle, 1, fields);

        assert bandle.salesmans().size() > 0;

    }

    @Test
    public void readingWrongCPFRecordTest() {

        String[] fields = "001ç173839800503244423423432432432432423çPedroç50000".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader001.read(bandle, 1, fields);

        assert bandle.salesmans().isEmpty();

    }

    @Test
    public void readingRecordWithoutIdentifyerTest() {

        String[] fields = "ç17383980050çPedroç50000".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader001.read(bandle, 1, fields);

        assert bandle.salesmans().isEmpty();

    }

    @Test
    public void readingRecordWithWrongNumericValueTest() {

        String[] fields = "001ç17383980050çPedroç50L0R".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader001.read(bandle, 1, fields);

        assert bandle.salesmans().isEmpty();

    }

    @Test
    public void readingRecordWithLessFieldsTest() {

        String[] fields = "001ç17383980050çPedro".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader001.read(bandle, 1, fields);

        assert bandle.customers().isEmpty();

    }

    @Test
    public void readingEmptyRecordTest() {

        String[] fields = "".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader001.read(bandle, 1, fields);

        assert bandle.salesmans().isEmpty();

    }



}
