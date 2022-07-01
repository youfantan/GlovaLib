package glovalib.core.features;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface FeatureInfo {
    String name();
    String version();
    String author();
}
