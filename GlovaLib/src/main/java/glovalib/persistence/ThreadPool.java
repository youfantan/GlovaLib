package glovalib.persistence;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ThreadPool{
    private class ThreadPackage{
        public ThreadPackage(Thread handle, boolean waitForThread, Thread senderThread) {
            this.handle = handle;
            this.waitForThread = waitForThread;
            this.senderThread = senderThread;
        }

        public Thread getHandle() {
            return handle;
        }

        public void setHandle(Thread handle) {
            this.handle = handle;
        }

        public boolean isWaitForThread() {
            return waitForThread;
        }

        public void setWaitForThread(boolean waitForThread) {
            this.waitForThread = waitForThread;
        }

        public Thread getSenderThread() {
            return senderThread;
        }

        public void setSenderThread(Thread senderThread) {
            this.senderThread = senderThread;
        }

        private Thread handle;
        private boolean waitForThread;
        private Thread senderThread;
    }
    private LinkedList<ThreadPackage> threads=new LinkedList<>();
    private ThreadPackage[] queue;
    private int initializeSize;
    private int maxSize;
    private boolean stopFlag;
    private volatile int sizeofTobeNotifiedThread=0;
    private Thread poolThread;

    public ThreadPool(int initializeSize, int maxSize) {
        this.initializeSize = initializeSize;
        this.maxSize = maxSize;
        queue=new ThreadPackage[initializeSize];
        stopFlag=false;
    }

    public void start(){
        startDaemon();
        new Thread(()->{
            this.poolThread=Thread.currentThread();
            while (!stopFlag){
                if (!threads.isEmpty()){
                    for (int i = 0; i < initializeSize; i++) {
                        ThreadPackage tpkg=queue[i];
                        if (tpkg==null){
                            queue[i]=threads.pollFirst();
                            queue[i].getHandle().start();
                            break;
                        }
                    }
                }
                if (sizeofTobeNotifiedThread>0){
                    for (int i = 0; i < initializeSize; i++) {
                        ThreadPackage tpkg=queue[i];
                        if (tpkg != null && !tpkg.handle.isAlive() && tpkg.isWaitForThread()){
                            synchronized (tpkg.getSenderThread()){
                                tpkg.getSenderThread().notify();
                                sizeofTobeNotifiedThread--;
                                queue[i]=null;
                            }
                        }
                    }
                }
                synchronized (poolThread){
                    try {
                        poolThread.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    private void startDaemon(){
        new Thread(()->{
            while (!stopFlag){
                if (threads.size()>0||sizeofTobeNotifiedThread>0){
                    synchronized (poolThread){
                        poolThread.notify();
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void stop(){
        stopFlag=true;
    }

    public synchronized void addThread(Thread thread, boolean waitForThread){
        synchronized (poolThread){
            poolThread.notify();
        }
        if (threads.size()<=maxSize){
            threads.add(new ThreadPackage(thread,waitForThread,Thread.currentThread()));
            if (waitForThread){
                sizeofTobeNotifiedThread++;
                synchronized (Thread.currentThread()){
                    try {
                        Thread.currentThread().wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public synchronized List<ThreadPackage> getWorkingThreads(){
        List<ThreadPackage> threadPackages=new ArrayList<>();
        for (ThreadPackage tpkg :
                queue) {
            if (tpkg != null) {
                threadPackages.add(tpkg);
            }
        }
        return threadPackages;
    }
}
