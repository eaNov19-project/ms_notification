package ea.notification.notification;

import ea.notification.notification.controllers.EmailController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

@EnableFeignClients
@EnableCircuitBreaker
@SpringBootApplication
public class NotificationApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

    @Value("${APP_VERSION}")
    private static String appVersion;


	public static void main(String[] args) {
		LOGGER.info("Notification service (" + appVersion + "). Host: ");

		SpringApplication.run(NotificationApplication.class, args);
	}

}
