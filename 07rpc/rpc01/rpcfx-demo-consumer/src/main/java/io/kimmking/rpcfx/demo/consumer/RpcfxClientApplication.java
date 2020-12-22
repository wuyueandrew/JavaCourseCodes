package io.kimmking.rpcfx.demo.consumer;

import io.kimmking.rpcfx.client.RpcImportBeanDefinitionRegistrar;
import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.GenericApplicationContext;

@Import(RpcImportBeanDefinitionRegistrar.class)
@SpringBootApplication
public class RpcfxClientApplication {

	// 二方库
	// 三方库 lib
	// nexus, userserivce -> userdao -> user
	//

//	public static void main(String[] args) {
//
//		// UserService service = new xxx();
//		// service.findById
//
//		UserService userService = Rpcfx.create(UserService.class, "http://localhost:8080/");
//		User user = userService.findById(1);
//		System.out.println("find user id=1 from server: " + user.getName());
//
//		OrderService orderService = Rpcfx.create(OrderService.class, "http://localhost:8080/");
//		Order order = orderService.findOrderById(1992129);
//		System.out.println(String.format("find order name=%s, amount=%f",order.getName(),order.getAmount()));
//
//		// 新加一个OrderService
//
////		SpringApplication.run(RpcfxClientApplication.class, args);
//	}

//	public static void main(String[] args) {
//		ProxyFactory proxyFactory = new ProxyFactory(UserService.class, OrderService.class);
//		proxyFactory.setTargetClass(UserService.class);
//		proxyFactory.addAdvice((MethodInterceptor) methodInvocation -> {
//			System.out.println("=====");
//			return new User();
//		});
//		UserService userService = (UserService)proxyFactory.getProxy();
//		User user = userService.findById(1);
//		System.out.println("=====");
//	}

//	@Bean
//	public RpcImportBeanDefinitionRegistrar rpcImportBeanDefinitionRegistrar() {
//		return new RpcImportBeanDefinitionRegistrar("http://localhost:8080/", "io.kimmking.rpcfx.demo.api");
//	}


	public static void main(String[] args) {
		ApplicationContext ac = new AnnotationConfigApplicationContext(RpcfxClientApplication.class);
		UserService us = ac.getBean(UserService.class);
		User user = us.findById(1);
		System.out.println("find user id=1 from server: " + user.getName());
		System.out.println("=====");
	}

}
