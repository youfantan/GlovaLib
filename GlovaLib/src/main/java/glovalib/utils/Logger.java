package glovalib.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.zip.GZIPOutputStream;

public class Logger {
    private static FileOutputStream fileOut;
    private static File logFile;
    private static final ByteArrayOutputStream writerStream=new ByteArrayOutputStream();
    public static void initialize() throws IOException {
        File logDir=new File("logs");
        if (!logDir.exists()){
            logDir.mkdir();
        }
        String fileName="logs"+File.separatorChar+getTime("yyyy-MM-dd-HH.mm.ss")+ ".log";
        File logFile=new File(fileName);
        logFile.createNewFile();
        Logger.logFile=logFile;
        fileOut =new FileOutputStream(logFile);
    }
    public static void stop(){
        try {
            fileOut.flush();
            fileOut.close();
            //compress file
            GZIPOutputStream out=new GZIPOutputStream(new FileOutputStream(logFile.getPath()+".gz"));
            BufferedInputStream in=new BufferedInputStream(new FileInputStream(logFile));
            ByteArrayOutputStream bytes=new ByteArrayOutputStream();
            byte[] buffer=new byte[1024];
            int bytesRead;
            while ((bytesRead=in.read(buffer))!=-1){
                bytes.write(buffer,0,bytesRead);
            }
            bytes.close();
            out.write(bytes.toByteArray());
            out.flush();
            out.close();
            in.close();
            //delete origin file
            logFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private enum LogLevel {
        INFO,
        WARN,
        ERROR,
        DEBUG,
        TRACE,
        FATAL
    }
    private String name;
    private static Hashtable<Class<?>,Logger> customInstances=new Hashtable<>();
    public static Logger getLogger(Class<?> cls){
        if (customInstances.containsKey(cls)){
            return customInstances.get(cls);
        }
        Logger logger=new Logger(cls.getSimpleName());
        customInstances.put(cls,logger);
        return logger;
    }
    private Logger(String name){
        this.name=name;
    }
    private static String getTime(String pattern){
        Date date=new Date();
        SimpleDateFormat formatter=new SimpleDateFormat(pattern);
        return formatter.format(date);
    }
    private synchronized void message(String text, LogLevel level){
        /*
        * Example Logger Format:
        * TIME [LEVEL/TYPE/NAME/THREAD] Message
        * 2022-6-24-15:48:02 [INFO] [Logger:LoggerThread] This is a logger message
        * */
        String format="%s [%s/%s/%s] %s%n";
        //print to console
        switch (level){
            case WARN -> Console.Yellow((format=format.formatted(getTime("yyyy-MM-dd HH:mm:ss"),"WARN",name,Thread.currentThread().getName(),text)));
            case DEBUG -> Console.Cyan((format=format.formatted(getTime("yyyy-MM-dd HH:mm:ss"),"DEBUG",name,Thread.currentThread().getName(),text)));
            case ERROR -> Console.Red((format=format.formatted(getTime("yyyy-MM-dd HH:mm:ss"),"ERROR",name,Thread.currentThread().getName(),text)));
            case FATAL -> Console.Red((format=format.formatted(getTime("yyyy-MM-dd HH:mm:ss"),"FATAL",name,Thread.currentThread().getName(),text)));
            case TRACE -> Console.Bit256(243,(format=format.formatted(getTime("yyyy-MM-dd HH:mm:ss"),"TRACE",name,Thread.currentThread().getName(),text)));
            case INFO -> Console.White((format=format.formatted(getTime("yyyy-MM-dd HH:mm:ss"),"INFO",name,Thread.currentThread().getName(),text)));
        }
        try {
            fileOut.write(format.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public void info(String text){
        message(text, LogLevel.INFO);
    }
    public void trace(String text){
        message(text,LogLevel.TRACE);
    }
    public void debug(String text){
        message(text,LogLevel.DEBUG);
    }
    public void warn(String text){
        message(text,LogLevel.WARN);
    }
    public void error(String text){
        message(text,LogLevel.ERROR);
    }
    public void fatal(String text){
        message(text,LogLevel.FATAL);
    }
}
