package glovalib.utils;

public class Timer {
    private long start=0;
    private long end=0;
    public void start(){
        start=System.currentTimeMillis();
    }
    public void end(){
        end=System.currentTimeMillis();
    }
    public long getTimeMS(){
        return end-start;
    }
    public long getTimeS(){
        return getTimeMS()/1000;
    }
    public double getTimeM(){
        return (double) (getTimeS()/60);
    }
    public double getTimeH(){
        return getTimeM()/60;
    }
}
