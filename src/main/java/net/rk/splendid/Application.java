/*
 * Copyright 2017 K. P. Sroka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.rk.splendid;

import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import net.rk.splendid.dao.entities.GameEntity;
import net.rk.splendid.dao.entities.OfyNoSelection;
import net.rk.splendid.game.FactoryGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import java.util.Properties;

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
    return application
        .sources(Application.class, ObjectifyFilter.class)
        .sources(FactoryGenerator.class)
        .properties(characterEncodingProperties());
  }

  private Properties characterEncodingProperties() {
    Properties characterEncodingProperties = new Properties();
    characterEncodingProperties.setProperty("spring.http.encoding.charset", "UTF-8");
    characterEncodingProperties.setProperty("spring.http.encoding.enabled", "true");
    characterEncodingProperties.setProperty("spring.http.encoding.force", "true");
    characterEncodingProperties.setProperty("spring.mandatory-file-encoding", "UTF-8");
    return characterEncodingProperties;
  }
}
