package glovalib.core.features.async;

import java.util.LinkedList;

public class AsyncFunctionQueue {
    public interface function{
        void execute();
    }
    private final LinkedList<function> functions=new LinkedList<>();
    private final Thread[] threads;
    private final int size;
    private final int maxSize;
    private boolean status;
    public AsyncFunctionQueue(int size,int maxSize){
        this.size=size;
        this.maxSize=maxSize;
        this.threads=new Thread[size];
        this.status=true;
    }
    public void startLoop() {
        while (status){
            if (functions.size()>0){
                for (int i = 0; i < size; i++) {
                    if (threads[i]==null||threads[i].isAlive()){
                        function f= functions.pollFirst();
                        assert f != null;
                        threads[i]=new Thread(f::execute);
                        threads[i].setName("AsyncFunctionWorker"+i);
                        threads[i].start();
                        if (functions.size()>0){
                            continue;
                        }
                        break;
                    }else {
                        threads[i]=null;
                    }
                }
            }
            synchronized (this){
                try {
                    this.wait();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public void startLoopAsync(){
        Thread thread=new Thread(this::startLoop);
        thread.setName("AsyncFunctionLoopWorker");
        thread.start();
    }
    public boolean submit(function function){
        if (functions.size()<maxSize){
            functions.add(function);
            synchronized (this){
                this.notify();
            }
            return true;
        }
        return false;
    }
    public void endLoop(){
        this.status=false;
        synchronized (this){
            this.notify();
        }
    }
}
