package de.valendur.discordbot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.valendur.discordbot.tasks.GenericRepeatedTask;

public class RepeatedTaskExecutor {
	
	ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
	GenericRepeatedTask task;
    volatile boolean isStopIssued;
    
    public RepeatedTaskExecutor(GenericRepeatedTask task) {
    	this.task = task;
    	start();
    }
    public void start() {
    	repeatIn(task.getSeconds());
    }

    public void repeatIn(int repeatInSeconds)
    {
        Runnable taskWrapper = new Runnable(){

            @Override
            public void run() 
            {
        		task.execute();
        		repeatIn(repeatInSeconds);
            }

        };
        long delay = repeatInSeconds;
        executorService.schedule(taskWrapper, delay, TimeUnit.SECONDS);
    }

    public void stop()
    {
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            Logger.getLogger(RepeatedTaskExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
