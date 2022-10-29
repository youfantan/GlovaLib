package glovalib.tests;

import glovalib.utils.Logger;
import glovalib.utils.SysOut;
import glovalib.events.EventApplicationStart;
import glovalib.events.EventApplicationStop;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

public class StdOutTest {
    @Test
    public void testSysOut() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //initialize SysOut by reflection
        Class<?> sysout=Class.forName("glovalib.utils.SysOut");
        Method initialize=sysout.getDeclaredMethod("initialize", EventApplicationStart.class);
        initialize.setAccessible(true);
        initialize.invoke(null,new EventApplicationStart());
        SysOut.Blue("Blue\n");
        SysOut.Cyan("Cyan\n");
        SysOut.Green("Green\n");
        SysOut.Magenta("Magenta\n");
        SysOut.Red("Red\n");
        SysOut.Yellow("Yellow\n");
        for (int i = 0; i < 256; i++) {
            SysOut.Bit256(i,"Bit Of %d | ".formatted(i));
        }
        Random random=new Random();
        SysOut.UniversalOutputLine("");
        for (int i = 0; i < 9182; i++) {
            SysOut.RGB(new SysOut.RGBColor(random.nextInt(256),random.nextInt(256),random.nextInt(256)),"\u2588");
        }
    }
    @Test
    public void testLogger() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> sysout=Class.forName("glovalib.utils.Logger");
        Method initialize=sysout.getDeclaredMethod("initialize", EventApplicationStart.class);
        initialize.setAccessible(true);
        initialize.invoke(null,new EventApplicationStart());
        Logger logger=Logger.getLogger(StdOutTest.class);
        logger.info("This is a INFO message");
        logger.debug("This is a DEBUG message");
        logger.trace("This is a TRACE message");
        logger.warn("This is a WARN message");
        logger.error("This is a ERROR message");
        logger.fatal("This is a FATAL message");
        Method stop=sysout.getDeclaredMethod("stop", EventApplicationStop.class);
        stop.setAccessible(true);
        stop.invoke(null,new EventApplicationStop());
    }
}
