package reva.ls.server;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageClientAware;
import org.eclipse.lsp4j.services.LanguageServer;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class RevaLSWebSocketHandler extends TextWebSocketHandler {
  private final LanguageServer languageServer;

  private final RevaWSMessageHandler messageHandler;

  public RevaLSWebSocketHandler(LanguageServer languageServer) {
    this.languageServer = languageServer;
    this.messageHandler = new RevaWSMessageHandler();
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    messageHandler.onMessage(message.getPayload());
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    RevaWebSocketLauncherBuilder launcherBuilder = new RevaWebSocketLauncherBuilder();
    launcherBuilder
        .setSession(session)
        .setMessageHandler(this.messageHandler)
        .setLocalService(this.languageServer)
        .setRemoteInterface(LanguageClient.class);

    Launcher<LanguageClient> languageClientLauncher = launcherBuilder.create();

    if (this.languageServer instanceof LanguageClientAware) {
      ((LanguageClientAware) this.languageServer).connect(languageClientLauncher.getRemoteProxy());
    }
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) {
    languageServer.shutdown();
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    languageServer.shutdown();
  }
}
