package support.maple.daily;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DailyApplication {

  public static void main(String[] args) {
    System.setProperty("spring.profiles.default", "release");

    SpringApplication.run(DailyApplication.class, args);
  }

}
