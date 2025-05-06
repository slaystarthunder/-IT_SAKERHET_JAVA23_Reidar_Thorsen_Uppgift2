package com.it_sakerhet_java23_reidar_thorsen;

import com.it_sakerhet_java23_reidar_thorsen.consoleview.ConsoleView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ItSakerhetJava23ReidarThorsenApplication {

	public static void main(String[] args) {

		ApplicationContext applicationContext = SpringApplication.run(ItSakerhetJava23ReidarThorsenApplication.class);
		ConsoleView consoleView = applicationContext.getBean(ConsoleView.class);
		consoleView.Menu();


	}



}
