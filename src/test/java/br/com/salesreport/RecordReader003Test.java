package br.com.salesreport;

import br.com.salesreport.filereader.RecordReader001;
import br.com.salesreport.filereader.RecordReader003;
import br.com.salesreport.model.Bundle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class RecordReader003Test {

    @Test
    public void readingGoodRecordTest() {

        String[] fields = "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader003.read(bandle, 1, fields);

        assert bandle.sales().size() > 0;

    }

    @Test
    public void readingOnlyOneItemWithWrongCurrencyValueRecordTest() {

        String[] fields = "003ç10ç[1-10-100,2-30-2.50,3-40-3,10]çPedro".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader003.read(bandle, 1, fields);

        assert bandle.sales().size() > 0;

    }

    @Test
    public void readingAllItemWithWrongCurrencyValueRecordTest() {

        String[] fields = "003ç10ç[1-10-100,0,2-30-2,50,3-40-3,10]çPedro".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader003.read(bandle, 1, fields);

        assert bandle.sales().size() > 0;

    }

    @Test
    public void readingOnlyOnItemWithWrongCurrencyValueInvalidConversionRecordTest() {

        String[] fields = "003ç10ç[1-10-100,2-30-2.L0,3-40-3.10]çPedro".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader003.read(bandle, 1, fields);

        assert bandle.sales().size() > 0;

    }

    @Test
    public void readingAllItemWithWrongCurrencyValueInvalidConversionRecordTest() {

        String[] fields = "003ç10ç[1-10-1R0,2-30-A.50,3-40-3.L0]çPedro".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader003.read(bandle, 1, fields);

        assert bandle.sales().isEmpty();

    }

    @Test
    public void readingRecordWithoutIdentifyerTest() {

        String[] fields = "ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader003.read(bandle, 1, fields);

        assert bandle.sales().isEmpty();

    }

    @Test
    public void readingRecordWithLessFieldsTest() {

        String[] fields = "10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader003.read(bandle, 1, fields);

        assert bandle.customers().isEmpty();

    }

    @Test
    public void readingEmptyRecordTest() {

        String[] fields = "".replace("ç", "Ç").split("Ç");
        var bandle = new Bundle(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        RecordReader003.read(bandle, 1, fields);

        assert bandle.salesmans().isEmpty();

    }



}
