package cn.tnar.parkingupload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ParkingUploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingUploadApplication.class, args);
	}
}
