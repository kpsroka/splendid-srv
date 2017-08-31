package net.rk.splendid;

import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import net.rk.splendid.dao.entities.GameEntity;
import net.rk.splendid.dao.entities.OfyNoSelection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
  static {
    ObjectifyService.register(GameEntity.class);
    ObjectifyService.register(OfyNoSelection.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(Application.class, ObjectifyFilter.class);
  }
}
