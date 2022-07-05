package glovalib.utils;

import glovalib.core.features.Logger;

public class ProgressTip {
    private String tipString;
    private int tipDuring;
    private Object[] data;
    private boolean flag=true;
    public ProgressTip(String tipString,int tipDuring,Object[] initializeData){
        this.tipString=tipString;
        this.tipDuring=tipDuring;
        this.data=initializeData;
    }
    public void update(Object[] data){
        this.data=data;
    }
    public void stop(){
        System.out.printf(tipString, data);
        this.flag=false;
    }
    public void start(){
        while (flag){
            try {
                System.out.print(tipString.formatted(data)+"\r");
                Thread.sleep(tipDuring);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void startAsync(){
        new Thread(this::start).start();
    }
}
