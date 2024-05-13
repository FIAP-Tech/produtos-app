package br.com.fiap.produtos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestprodutosAppApplication {

	@Bean
	@ServiceConnection
	PostgreSQLContainer postgreSQLContainer(){
		return new PostgreSQLContainer(DockerImageName.parse("postgres:latest"));
	}



	public static void main(String[] args) {
		SpringApplication.from(ProdutosAppApplication::main).with(TestprodutosAppApplication.class).run(args);
	}

}
