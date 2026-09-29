
package com.projeto_java.cafeteria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URL;

@SpringBootApplication
public class CadastroCafeApplication extends Application {

    private ConfigurableApplicationContext contextoSpring;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() {
        contextoSpring = new SpringApplicationBuilder(
                CadastroCafeApplication.class
        )
                .headless(false)
                .run();
    }

    @Override
    public void start(Stage palco) throws Exception {

        URL localFXML = getClass().getResource("/main.fxml");

        if (localFXML == null) {
            throw new IllegalStateException(
                    "Arquivo main.fxml não encontrado em src/main/resources."
            );
        }

        FXMLLoader carregador = new FXMLLoader(localFXML);
        carregador.setControllerFactory(contextoSpring::getBean);

        Scene cena = new Scene(carregador.load());

        palco.setTitle("Cafeteria Maragato");
        palco.setScene(cena);
        palco.setMinWidth(1000);
        palco.setMinHeight(700);
        palco.show();
    }

    @Override
    public void stop() {
        if (contextoSpring != null) {
            contextoSpring.close();
        }
    }
}
