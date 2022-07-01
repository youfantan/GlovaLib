package glovalib.tests;

import glovalib.events.EventApplicationStart;
import glovalib.events.EventApplicationStop;
import glovalib.events.EventBus;
import glovalib.events.SubscribeEvent;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class EventBusTest {
    @SubscribeEvent(event = EventApplicationStart.class)
    private static void startInfo(EventApplicationStart evt){
        System.out.println("Event Bus Start test");
    }
    @Test
    public void testApplicationStart() throws IOException {
        EventBus.Start();
        EventBus.Post(new EventApplicationStart());
        EventBus.Post(new EventApplicationStop());
    }
}
