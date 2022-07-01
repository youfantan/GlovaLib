package glovalib.core.features;

import glovalib.events.EventApplicationStart;
import glovalib.events.SubscribeEvent;
import glovalib.wcas.WindowsConsoleAnsiSupport;

import java.io.File;
import java.util.Locale;

public class SysOut {
    @SubscribeEvent(event = EventApplicationStart.class)
    private static void initialize(EventApplicationStart evt){
        if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("windows")){
            //load ansi support
            System.load(new File("bin\\WindowsConsoleAnsiSupport.dll").getAbsolutePath());
            if (!WindowsConsoleAnsiSupport.TryStart()){
                UniversalOutputLine("Cannot load Windows Virtual ANSI support.Please make sure your OS Version is Windows 10(update 1809) or higher,otherwise console log may output abnormally.");
            }
        }
    }
    public static void White(String text){
        UniversalOutput("\033[37m"+text+"\033[37m");
    }
    public static void Red(String text){
        UniversalOutput("\033[31m"+text+"\033[37m");
    }
    public static void Green(String text){
        UniversalOutput("\033[32m"+text+"\033[37m");
    }
    public static void Yellow(String text){
        UniversalOutput("\033[33m"+text+"\033[37m");
    }
    public static void Blue(String text){
        UniversalOutput("\033[34m"+text+"\033[37m");
    }
    public static void Cyan(String text){
        UniversalOutput("\033[36m"+text+"\033[37m");
    }
    public static void Magenta(String text){
        UniversalOutput("\033[35m"+text+"\033[37m");
    }
    public static void Bit256(int bit,String text){
        UniversalOutput("\033[38;5;%sm".formatted(bit)+text+"\033[37m");
    }
    public static class RGBColor{
        public RGBColor(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
        public RGBColor(){}

        public int getR() {
            return r;
        }

        public RGBColor setR(int r) {
            this.r = r;
            return this;
        }

        public int getG() {
            return g;
        }

        public RGBColor setG(int g) {
            this.g = g;
            return this;
        }

        public int getB() {
            return b;
        }

        public RGBColor setB(int b) {
            this.b = b;
            return this;
        }

        private int r=255;
        private int g=255;
        private int b=255;

    }
    public static void RGB(RGBColor color,String text){
        UniversalOutput("\033[38;2;%s;%s;%sm".formatted(color.getR(),color.getG(),color.getB())+text+"\033[37m");
    }
    public static void UniversalOutput(String text){
        System.out.print(text);
    }
    public static void UniversalOutputLine(String text){
        System.out.println(text);
    }
}
