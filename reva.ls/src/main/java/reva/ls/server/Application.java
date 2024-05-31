package reva.ls.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.lsp4j.services.LanguageServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;
import reva.ide.LanguageServerModule;

@SpringBootApplication
public class Application {
  private static final Injector injector = Guice.createInjector(new LanguageServerModule());

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public LanguageServer languageServer() {
    return injector.getInstance(LanguageServer.class);
  }

  @Bean
  public WebSocketHandler revaLSWebSocketHandler() {
    return new PerConnectionWebSocketHandler(RevaLSWebSocketHandler.class);
  }
}
