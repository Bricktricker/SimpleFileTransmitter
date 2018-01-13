package Utils;

public class Pair<T1, T2>{
		
	private T1 val1;
	private T2 val2;
	
	public Pair(T1 val1, T2 val2){
		this.val1 = val1;
		this.val2 = val2;
	}
	
	public T1 getFirst() {
		return val1;
	}
	
	public T2 getSecond() {
		return val2;
	}

	public void setFirst(T1 val1) {
		this.val1 = val1;
	}

	public void setSecond(T2 val2) {
		this.val2 = val2;
	}
	
	public String toString() {
		return "" + val1.toString() + " | " + val2.toString();
	}
}
