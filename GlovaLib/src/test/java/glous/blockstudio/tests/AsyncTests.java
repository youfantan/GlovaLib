package glous.blockstudio.tests;

import glous.blockstudio.core.features.Logger;
import glous.blockstudio.core.features.async.AsyncFunctionQueue;
import glous.blockstudio.events.EventApplicationStart;
import glous.blockstudio.events.EventApplicationStop;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AsyncTests {
    @Test
    public void testAsyncFunctionQueue0() throws InterruptedException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> sysout=Class.forName("glous.blockstudio.core.features.Logger");
        Method initialize=sysout.getDeclaredMethod("initialize", EventApplicationStart.class);
        initialize.setAccessible(true);
        initialize.invoke(null,new EventApplicationStart());
        Logger testLogger=Logger.getLogger(this.getClass(),"QueueTestWorker");
        AsyncFunctionQueue queue=new AsyncFunctionQueue(64,128);
        for (int i = 0; i < 192; i++) {
            int finalI = i;
            if (!queue.submit(() -> {
                Logger logger=Logger.getLogger(this.getClass(),"QueueTestWorker");
                logger.trace("Current Worker Number: %d".formatted(finalI));
            })){
                testLogger.error("Error size  "+i+" of 128");
            }
        }
        queue.startLoopAsync();
        Thread.sleep(4000);
        queue.endLoop();
        Method stop=sysout.getDeclaredMethod("stop", EventApplicationStop.class);
        stop.setAccessible(true);
        stop.invoke(null,new EventApplicationStop());
    }
    @Test
    public void testAsyncFunctionQueue1() throws InterruptedException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> sysout=Class.forName("glous.blockstudio.core.features.Logger");
        Method initialize=sysout.getDeclaredMethod("initialize", EventApplicationStart.class);
        initialize.setAccessible(true);
        initialize.invoke(null,new EventApplicationStart());
        Logger testLogger=Logger.getLogger(this.getClass(),"QueueTestWorker");
        AsyncFunctionQueue queue=new AsyncFunctionQueue(64,128);
        for (int i = 0; i < 192; i++) {
            int finalI = i;
            if (!queue.submit(() -> {
                Logger logger=Logger.getLogger(this.getClass(),"QueueTestWorker");
                logger.trace("Current Worker Number: %d".formatted(finalI));
            })){
                testLogger.error("Error size  "+i+" of 128");
            }
        }
        new Thread(()->{
            try {
                Thread.sleep(4000);
                queue.endLoop();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).start();
        queue.startLoop();
        Method stop=sysout.getDeclaredMethod("stop", EventApplicationStop.class);
        stop.setAccessible(true);
        stop.invoke(null,new EventApplicationStop());
    }
}
