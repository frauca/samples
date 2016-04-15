package frauca.mysamples.java.osgi.helloservice;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

	ServiceRegistration helloServiceRegistration;
    public void start(BundleContext context) throws Exception {
    	System.out.println("Activate Hello Service");
        HelloService helloService = new HelloServiceImpl(); //One instance per service
        helloServiceRegistration =context.registerService(HelloService.class.getName(), helloService, null);
    	/**HelloServiceFactory helloServiceFactory = new HelloServiceFactory();//many instances per service
        helloServiceRegistration =context.registerService(HelloService.class.getName(), helloServiceFactory, null);**/
    }
    public void stop(BundleContext context) throws Exception {
    	System.out.println("DeActivate Hello Service");
        helloServiceRegistration.unregister();
    }

}
