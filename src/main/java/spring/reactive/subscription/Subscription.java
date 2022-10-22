package spring.reactive.subscription;

public interface Subscription {
	
	void request(long n);
	void cancel();
}
