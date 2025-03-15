package game;

/**
 * Interface for managing game timers.
 * Provides methods to start, stop, and update the interval of a timer.
 */
public interface GameTimer {

    /**
     * Starts the timer.
     */
    void startTimer();

    /**
     * Stops the timer.
     */
    void stopTimer();

    /**
     * Updates the timer interval.
     *
     * @param newInterval The new interval in milliseconds.
     */
    void updateInterval(int newInterval);
}