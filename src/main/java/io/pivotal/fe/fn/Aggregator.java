package io.pivotal.fe.fn;

import org.springframework.data.gemfire.function.annotation.GemfireFunction;
import org.springframework.stereotype.Component;

@Component
public class Aggregator {
	
	@GemfireFunction
	public Integer doSomething() {
		System.out.println("on server");
		return 1;
	}

}
