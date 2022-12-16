package glovalib.command;

import glovalib.utils.Logger;

import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Scanner;
public class CommandResolver {
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public @interface Command {
        String name();
    }

    @Command(name = "UnnamedCommandExecutor")
    public abstract static class CommandExecutor {
        abstract public boolean execute(String[] args);
    }
    protected Hashtable<String, CommandExecutor> callbacks=new Hashtable<>();
    private boolean flag=true;
    private Logger logger=Logger.getLogger(CommandResolver.class);
    public void registerCommand(Class<? extends CommandExecutor> executor){
        String name=executor.getDeclaredAnnotation(Command.class).name();
        try {
            callbacks.put(name,executor.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    public void start(){
        new Thread(()->{
            Scanner scanner=new Scanner(System.in);
            while (flag){
                String[] command=scanner.nextLine().split(" ");
                if (callbacks.containsKey(command[0])){
                    if (callbacks.get(command[0]).execute(command)){
                        logger.error("An error occurred when executing command: %s".formatted(command[0]));
                    }
                } else{
                    logger.trace("Command not found: %s".formatted(command[0]));
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
    public void stop(){
        flag=false;
    }
}

