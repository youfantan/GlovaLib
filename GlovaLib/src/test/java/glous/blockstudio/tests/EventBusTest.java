package glous.blockstudio.tests;

import glous.blockstudio.events.EventApplicationStart;
import glous.blockstudio.events.EventApplicationStop;
import glous.blockstudio.events.EventBus;
import glous.blockstudio.events.SubscribeEvent;
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
