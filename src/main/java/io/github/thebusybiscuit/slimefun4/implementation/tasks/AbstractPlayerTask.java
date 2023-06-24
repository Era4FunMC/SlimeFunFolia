package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import javax.annotation.Nonnull;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import java.util.concurrent.TimeUnit;

abstract class AbstractPlayerTask implements Runnable {

    protected final Player p;
    private ScheduledTask task;

    AbstractPlayerTask(@Nonnull Player p) {
        this.p = p;
    }

    private void setID(ScheduledTask task) {
        this.task = task;
    }

    public void schedule(long delay) {
        //setID(Bukkit.getScheduler().scheduleSyncDelayedTask(Slimefun.instance(), this, delay));
    }

    public void scheduleRepeating(long delay, long interval, Location location) {
        setID(Bukkit.getRegionScheduler().runAtFixedRate(Slimefun.instance(), location,a -> this.run(), delay, interval));
    }

    @Override
    public final void run() {
        if (isValid()) {
            executeTask();
        }
    }

    /**
     * This method cancels this {@link AbstractPlayerTask}.
     */
    public final void cancel() {
        if (this.task == null){
            return;
        }
        this.task.cancel();
    }

    /**
     * This method checks if this {@link AbstractPlayerTask} should be continued or cancelled.
     * It will also cancel this {@link AbstractPlayerTask} if it became invalid.
     * 
     * @return Whether this {@link AbstractPlayerTask} is still valid
     */
    protected boolean isValid() {
        if (!p.isOnline() || !p.isValid() || p.isDead() || !p.isSneaking()) {
            cancel();
            return false;
        }

        return true;
    }

    protected abstract void executeTask();
}
