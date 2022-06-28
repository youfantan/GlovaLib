package glous.blockstudio.events;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class EventBus {
    private static Hashtable<Class<? extends Event>, List<Method>> EventHandlers=new Hashtable<>();
    public static void Start() throws IOException {
        //juage if in complete jar mode or in debug environment
        URL url=Thread.currentThread().getContextClassLoader().getResource("glous");
        assert url != null;
        if (url.getProtocol().equals("jar")){
            //complete jar
            String jarUrlStr=url.getPath().substring(0,url.getPath().indexOf("!"));
            URL jarUrl=new URL(jarUrlStr);
            JarFile file=new JarFile(jarUrl.getFile(),false);
            ScanJarClasses(file);
        } else{
            String path=Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
            ScanDirectoryClasses(path,new File(path).getAbsolutePath()+File.separatorChar);
        }
    }
    public static void Post(Event evt){
        Class<? extends Event> evtClass = evt.getClass();
        if (EventHandlers.containsKey(evtClass)) {
            List<Method> evtHandlers=EventHandlers.get(evtClass);
            for (Method m:
                    evtHandlers) {
                try {
                    m.setAccessible(true);
                    m.invoke(null,evt);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    private static void ScanJarClasses(JarFile file){
        Enumeration<JarEntry> entries=file.entries();
        while (entries.hasMoreElements()){
            JarEntry entry=entries.nextElement();
            String entryName;
            if (!entry.isDirectory()&&(entryName=entry.getName()).endsWith(".class")){
                //find this class in current classloader and get annotations by reflection
                String className;
                if (!entryName.contains("$")){
                    className=entryName.substring(0,entryName.indexOf(".class"));
                    className=className.replace("/",".");
                    GetSubscribe(className);
                }
            }
        }
    }
    private static void ScanDirectoryClasses(String url,String baseUrl){
        File file=new File(url);
        if (file.listFiles()!=null){
            for (File f :
                    file.listFiles()) {
                if (f.isDirectory()) {
                    ScanDirectoryClasses(f.getPath(),baseUrl);
                } else{
                    String entryName;
                    if ((entryName=f.getPath()).endsWith(".class")){
                        //find this class in current classloader and get annotations by reflection
                        String className;
                        if (!entryName.contains("$")){
                            className=entryName.substring(0,entryName.indexOf(".class"));
                            className=className.replace(baseUrl,"");
                            className=className.replace(File.separatorChar, '.');
                            GetSubscribe(className);
                        }
                    }
                }
            }
        }
    }

    private static void GetSubscribe(String className) {
        try {
            Class<?> cls=Class.forName(className);
            Method[] methods=cls.getDeclaredMethods();
            for (Method m :
                    methods) {
                SubscribeEvent event;
                if ((event=m.getAnnotation(SubscribeEvent.class))!=null){
                    Class<? extends Event> evtClass=event.event();
                    List<Method> evtHandlerMethods;
                    if (EventHandlers.containsKey(evtClass)){
                        evtHandlerMethods=EventHandlers.get(evtClass);
                    } else {
                        evtHandlerMethods=new ArrayList<>();
                    }
                    evtHandlerMethods.add(m);
                    EventHandlers.put(evtClass,evtHandlerMethods);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
