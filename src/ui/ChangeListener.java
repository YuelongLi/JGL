package ui;

/**
 * Change listener for listening to updates
 * @author Yuelong Li
 */
public interface ChangeListener<T> {
	/**
	 * Called when the state of the listened target changes
	 * @param source the source target
	 */
	public void stateChanged(T source);
}
/*
 * © Copyright 2020 Cannot be used without authorization
 */