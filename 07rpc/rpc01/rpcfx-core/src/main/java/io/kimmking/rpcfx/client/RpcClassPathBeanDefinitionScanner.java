package io.kimmking.rpcfx.client;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;

public class RpcClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

    public RpcClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
        super.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.contains("Service");
        });
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        beanDefinitions.forEach(beanDefinitionHolder -> {
            AbstractBeanDefinition definition = (AbstractBeanDefinition) beanDefinitionHolder.getBeanDefinition();
            definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
            definition.getConstructorArgumentValues().addGenericArgumentValue("localhost");
            definition.getConstructorArgumentValues().addGenericArgumentValue(8080);
            definition.setBeanClass(RpcProxyFactoryBean.class);
        });
        return beanDefinitions;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
//        return super.isCandidateComponent(beanDefinition);
        return true;
    }

}
