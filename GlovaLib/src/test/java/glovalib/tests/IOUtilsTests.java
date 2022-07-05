package glovalib.tests;

import glovalib.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class IOUtilsTests {
    @Test
    public void testAll() throws IOException {
        File in=new File("test_in");
        IOUtils.writeFile(in,"Test String");
        File out=new File("test_out");
        IOUtils.copyFile(in,out);
        String out_string=IOUtils.readFileString(out);
        assert out_string.equals("Test String");
    }
}
