package br.nagualcode.kitchencustomer;

import org.springframework.boot.SpringApplication;

public class TestKitchencustomerApplication {

	public static void main(String[] args) {
		SpringApplication.from(KitchencustomerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
