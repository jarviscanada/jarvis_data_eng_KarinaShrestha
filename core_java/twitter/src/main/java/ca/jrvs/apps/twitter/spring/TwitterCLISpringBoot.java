package ca.jrvs.apps.twitter.spring;


import ca.jrvs.apps.twitter.TwitterCLIApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @SpringBootApplication is a convenience annotation that adds all of the following:
 *
 * - @Configuration
 * - @EnableAutoConfiguration
 * - @ComponentScan
 */
@SpringBootApplication(scanBasePackages = "ca.jrvs.apps.twitter")
public class TwitterCLISpringBoot {

  private TwitterCLIApp app;

  @Autowired
  public TwitterCLISpringBoot(TwitterCLIApp app) {
    this.app = app;
  }

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(TwitterCLISpringBoot.class);

    //Turn off web
    app.setWebApplicationType(WebApplicationType.NONE);
    app.run(args);
  }

}
