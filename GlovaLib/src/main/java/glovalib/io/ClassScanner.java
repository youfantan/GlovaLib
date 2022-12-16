package glovalib.io;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanner {
    public interface ScanCallback{
        void call(Class<?> clz);
    }
    private String[] packages;
    private ScanCallback callback;
    public ClassScanner(String[] packages, ScanCallback callback){
        this.packages=packages;
        this.callback=callback;
    }
    public void scan() throws IOException, URISyntaxException {
        for (String pkg :
                packages) {
            URL baseUrl = Thread.currentThread().getContextClassLoader().getResource(pkg.replace(".","/"));
            if (baseUrl!=null){
                if (baseUrl.getProtocol().equals("jar")){
                    System.out.println(new URL(baseUrl.getPath().substring(0,baseUrl.getPath().indexOf("!"))).getFile());
                    try (JarFile file=new JarFile(new URL(baseUrl.getPath().substring(0,baseUrl.getPath().indexOf("!"))).getFile(),false)){
                        Enumeration<JarEntry> entries=file.entries();
                        while (entries.hasMoreElements()){
                            JarEntry entry=entries.nextElement();
                            if (!entry.isDirectory() && entry.getName().endsWith(".class") && !entry.getName().contains("$") && entry.getName().replace("/",".").startsWith(pkg)){
                                String clzName=entry.getName().replace("/",".");
                                clzName=clzName.substring(0,clzName.indexOf(".class"));
                                try {
                                    Class<?> clz=Class.forName(clzName);
                                    callback.call(clz);
                                } catch (ClassNotFoundException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else if (baseUrl.getProtocol().equals("file")){
                    scanDirectory(new File(baseUrl.getFile()),pkg);
                    return;
                } else {
                    return;
                }
            }
        }
    }
    //For Debugging in IDE
    public void scanDirectory(File directory,String pkg){
        String pkgPath=pkg.replace('.',File.separatorChar);
        File[] files=directory.listFiles();
        if (files!=null){
            for (File f :
                    files) {
                if (f.isDirectory()) {
                    scanDirectory(f,pkg);
                } else {
                    String absPath=f.getAbsolutePath();
                    if (absPath.contains(pkgPath) && absPath.endsWith(".class") && !absPath.contains("$")){
                        absPath=absPath.substring(absPath.lastIndexOf(pkgPath));
                        absPath=absPath.replace(File.separatorChar,'.').substring(0,absPath.indexOf(".class"));
                        try {
                            Class<?> clz=Class.forName(absPath);
                            callback.call(clz);
                        } catch (ClassNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
