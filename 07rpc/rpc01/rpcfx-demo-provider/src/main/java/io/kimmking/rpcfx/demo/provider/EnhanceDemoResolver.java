package io.kimmking.rpcfx.demo.provider;

public class EnhanceDemoResolver extends DemoResolver {

    @Override
    public Object resolve(String serviceClass) {
        try {
            if (getApplicationContext().containsBean(serviceClass)) {
                return getApplicationContext().getBean(serviceClass);
            } else {
                Class<?> klass = Class.forName(serviceClass);
                return getApplicationContext().getBean(klass);
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("cannot find serviceClass: %s", serviceClass));
        }
    }
}
