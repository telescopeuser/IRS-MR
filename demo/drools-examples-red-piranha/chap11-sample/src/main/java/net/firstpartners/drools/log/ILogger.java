package net.firstpartners.drools.log;

/**
 * Marks a class as being one that we can log to
 * @author paulbrowne
 *
 */
public interface ILogger {

	public void debug(String output);
	public void info(String output);
	public void exception(String output, Throwable t);
}
