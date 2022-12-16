package glovalib.events;

import glovalib.io.ClassScanner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.*;

public class EventBus {
    private static Hashtable<Class<? extends Event>, List<Method>> EventHandlers=new Hashtable<>();
    public static void start(String[] roots) {
        ClassScanner scanner=new ClassScanner(roots,(clz -> {
            Method[] methods=clz.getDeclaredMethods();
            for (Method m :
                    methods) {
                m.setAccessible(true);
                SubscribeEvent event;
                if ((event=m.getDeclaredAnnotation(SubscribeEvent.class))!=null){
                    Class<? extends Event> evtClass=event.event();
                    if (EventHandlers.containsKey(evtClass)){
                        List<Method> mList=EventHandlers.get(evtClass);
                        mList.add(m);
                        EventHandlers.put(evtClass,mList);
                    } else{
                        List<Method> mList=new ArrayList<>();
                        mList.add(m);
                        EventHandlers.put(evtClass,mList);
                    }
                }
            }
        }));
        try {
            scanner.scan();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public static void post(Event event){
        List<Method> mList=EventHandlers.get(event.getClass());
        for (Method m :
                mList) {
            m.setAccessible(true);
            try {
                m.invoke(null,event);
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                System.out.println("CALLING METHOD: "+ m);
                e.printStackTrace();
            }
        }
    }
}