package org.sanchez.corcoles.ana.pruebasconcepto.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class App implements CommandLineRunner {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    //Generamos contraseñas encriptadas a partir de un valor.
    @Override
    public void run(String... args) throws Exception {
        String password = "12345";
        for (int i = 0; i < 4; i++) {
            String passwordEncriptado = passwordEncoder.encode(password);
            System.out.println(passwordEncriptado);
        }
    }
}
