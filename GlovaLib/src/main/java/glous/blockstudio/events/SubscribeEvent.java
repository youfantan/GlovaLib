package glous.blockstudio.events;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SubscribeEvent {
    Class<? extends Event> event();
}
