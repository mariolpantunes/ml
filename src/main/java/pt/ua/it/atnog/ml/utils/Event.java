package pt.ua.it.atnog.ml.utils;

public class Event {
	private boolean done;

	public Event(boolean done) {
		this.done = done;
	}

	public Event() {
		done = false;
	}

	public boolean done() {
		return done;
	}
}
