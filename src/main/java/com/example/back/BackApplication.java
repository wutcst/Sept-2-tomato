package com.example.back;

import com.example.back.sys.websocket.WebSocketConfig;
import com.example.back.sys.websocket.WebSocketServerUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan(basePackages = "com.example.back.*.mapper")
public class BackApplication {
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(BackApplication.class);
		ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
		WebSocketServerUtil.setApplicationContext(configurableApplicationContext);
	}

}
