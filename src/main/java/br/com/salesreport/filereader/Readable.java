package br.com.salesreport.filereader;

import br.com.salesreport.model.Bundle;

import java.io.IOException;

public interface Readable {

    Bundle process() throws IOException;

}
