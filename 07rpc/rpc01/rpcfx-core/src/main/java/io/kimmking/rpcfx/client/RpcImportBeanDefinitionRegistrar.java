package io.kimmking.rpcfx.client;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;


public class RpcImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    private String url = "http://localhost:8080/";

    private String packagePath = "io.kimmking.rpcfx.demo.api";

//    public RpcImportBeanDefinitionRegistrar(String url, String packagePath) {
//        this.url = url;
//        this.packagePath = packagePath;
//    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        RpcClassPathBeanDefinitionScanner scanner = new RpcClassPathBeanDefinitionScanner(beanDefinitionRegistry, false);
        scanner.setResourceLoader(resourceLoader);
//        scanner.registerDefaultFilters();
//        try {
//            Class klass = Class.forName("io.kimmking.rpcfx.demo.api.UserService");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        scanner.doScan(packagePath);
    }

}
