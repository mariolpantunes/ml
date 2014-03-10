package pt.ua.it.atnog.ml.optimization.genetic.selection;

public class Pair<Type> {
	private Type first, second;
	
	public Pair (Type first, Type second) {
		this.first = first;
		this.second = second;
	}
	
	public Type first() {
		return first;
	}
	
	public Type second() {
		return second;
	}
	
	public String toString() {
		return "first: "+first.toString()+"\nsecond: "+second.toString();
	}
}
