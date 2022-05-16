package org.semanticweb.owlapi.functional.parser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.IIOException;
import java.io.IOException;

public class StringProviderTest {

    @Test
    public void TestRead(){
        StringProvider stringProvider = new StringProvider("This is for testing read function of StringProvider");
        char[] cbuf = new char[10];
        try {
            assert (stringProvider.read(cbuf, 2, 5) == 5);
            assert (stringProvider.read(cbuf, 2, 10) == 8);
            assert (stringProvider.read(cbuf, 0, 5) == 5);
            assert (stringProvider.read(cbuf, 0, 12) == 10);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
