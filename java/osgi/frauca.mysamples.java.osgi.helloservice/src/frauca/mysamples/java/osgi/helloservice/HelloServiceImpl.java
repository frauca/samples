package frauca.mysamples.java.osgi.helloservice;

public class HelloServiceImpl implements HelloService {

	@Override
	public String sayHello() {
		System.out.println("Inside HelloServiceImple.sayHello()");
        return "Say Hello";
	}

}
