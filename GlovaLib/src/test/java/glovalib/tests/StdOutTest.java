package glovalib.tests;

import glovalib.utils.Logger;
import glovalib.utils.Console;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class StdOutTest {
    @Test
    public void testSysOut() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Console.initialize();
        Console.Blue("Blue\n");
        Console.Cyan("Cyan\n");
        Console.Green("Green\n");
        Console.Magenta("Magenta\n");
        Console.Red("Red\n");
        Console.Yellow("Yellow\n");
        for (int i = 0; i < 256; i++) {
            Console.Bit256(i,"Bit Of %d | ".formatted(i));
        }
        Random random=new Random();
        Console.UniversalOutputLine("");
        for (int i = 0; i < 9182; i++) {
            Console.RGB(new Console.RGBColor(random.nextInt(256),random.nextInt(256),random.nextInt(256)),"\u2588");
        }
    }
    @Test
    public void testLogger() throws InvocationTargetException, IllegalAccessException, IOException {
        Logger.initialize();
        Logger logger=Logger.getLogger(StdOutTest.class);
        logger.info("This is a INFO message");
        logger.debug("This is a DEBUG message");
        logger.trace("This is a TRACE message");
        logger.warn("This is a WARN message");
        logger.error("This is a ERROR message");
        logger.fatal("This is a FATAL message");
        Logger.stop();
    }
}
