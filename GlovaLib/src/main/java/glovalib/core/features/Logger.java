package glovalib.core.features;

import glovalib.events.EventApplicationStart;
import glovalib.events.EventApplicationStop;
import glovalib.events.SubscribeEvent;

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
    @SubscribeEvent(event = EventApplicationStart.class)
    public static void initialize(EventApplicationStart evt) throws IOException {
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
    @SubscribeEvent(event = EventApplicationStop.class)
    public static void stop(EventApplicationStop evt){
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
    private String type;
    private static Hashtable<Class<? extends Feature>,Logger> instances=new Hashtable<>();
    private static Hashtable<Class<?>,Logger> customInstances=new Hashtable<>();
    public static Logger getLogger(Class<? extends Feature> feature){
        if (instances.containsKey(feature)){
            return instances.get(feature);
        }
        String name=feature.getAnnotation(FeatureInfo.class).name();
        Logger logger=new Logger(name,"Feature");
        instances.put(feature,logger);
        return logger;
    }
    public static Logger getLogger(Class<?> cls,String name){
        if (customInstances.containsKey(cls)){
            return customInstances.get(cls);
        }
        Logger logger=new Logger(name,"Custom");
        customInstances.put(cls,logger);
        return logger;
    }
    private Logger(String name,String type){
        this.name=name;
        this.type=type;
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
        * 2022-6-24-15:48:02 [INFO/Feature/Logger/LoggerThread] This is a logger message
        * */
        String format="%s [%s/%s/%s/%s] %s%n";
        //print to console
        switch (level){
            case WARN -> SysOut.Yellow((format=format.formatted(getTime("yyyy-MM-dd HH:mm:ss"),"WARN",type,name,Thread.currentThread().getName(),text)));
            case DEBUG -> SysOut.Cyan((format=format.formatted(getTime("yyyy-MM-dd HH:mm:ss"),"DEBUG",type,name,Thread.currentThread().getName(),text)));
            case ERROR -> SysOut.Red((format=format.formatted(getTime("yyyy-MM-dd HH:mm:ss"),"ERROR",type,name,Thread.currentThread().getName(),text)));
            case FATAL -> SysOut.Red((format=format.formatted(getTime("yyyy-MM-dd HH:mm:ss"),"FATAL",type,name,Thread.currentThread().getName(),text)));
            case TRACE -> SysOut.Bit256(243,(format=format.formatted(getTime("yyyy-MM-dd HH:mm:ss"),"TRACE",type,name,Thread.currentThread().getName(),text)));
            case INFO -> SysOut.White((format=format.formatted(getTime("yyyy-MM-dd HH:mm:ss"),"INFO",type,name,Thread.currentThread().getName(),text)));
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
    public static class LogWriter extends Writer{

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            Logger logger=Logger.getLogger(LogWriter.class,"LogWriter");
            String msgBuf=new String(cbuf);
            logger.message(msgBuf,LogLevel.DEBUG);
            writerStream.write(CharArrayToByteArray(cbuf),off,len);
        }

        @Override
        public void flush() throws IOException {
            writerStream.flush();
        }

        @Override
        public void close() throws IOException {
            writerStream.close();
        }
    }
    private static final LogWriter writerInstance=new LogWriter();
    private static byte[] CharArrayToByteArray(char[] arr){
        byte[] arr0=new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            arr0[i]=(byte) arr[i];
        }
        return arr0;
    }

    public static LogWriter getWriterInstance() {
        return writerInstance;
    }
}
