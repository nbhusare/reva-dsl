package reva.ls.server;

import org.eclipse.lsp4j.jsonrpc.Endpoint;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.jsonrpc.MessageConsumer;
import org.eclipse.lsp4j.jsonrpc.RemoteEndpoint;
import org.eclipse.lsp4j.jsonrpc.json.MessageJsonHandler;
import org.eclipse.lsp4j.jsonrpc.services.ServiceEndpoints;
import org.eclipse.lsp4j.services.LanguageClient;
import org.springframework.web.socket.WebSocketSession;

public class RevaWebSocketLauncherBuilder extends Launcher.Builder<LanguageClient> {
  private WebSocketSession session;

  private RevaWSMessageHandler messageHandler;

  @Override
  public Launcher<LanguageClient> create() {
    MessageJsonHandler jsonHandler = createJsonHandler();
    RemoteEndpoint remoteEndpoint = createRemoteEndpoint(jsonHandler);
    MessageConsumer messageConsumer = wrapMessageConsumer(remoteEndpoint);
    LanguageClient remoteProxy = createProxy(remoteEndpoint);

    this.messageHandler.setConfigs(messageConsumer, jsonHandler, remoteEndpoint);

    return createLauncher(null, remoteProxy, remoteEndpoint, null);
  }

  @Override
  protected RemoteEndpoint createRemoteEndpoint(MessageJsonHandler jsonHandler) {
    MessageConsumer outgoingMessageStream = new RevaWSMessageConsumer(jsonHandler, session);
    outgoingMessageStream = wrapMessageConsumer(outgoingMessageStream);
    Endpoint localEndpoint = ServiceEndpoints.toEndpoint(localServices);
    RemoteEndpoint remoteEndpoint =
        exceptionHandler == null
            ? new RemoteEndpoint(outgoingMessageStream, localEndpoint)
            : new RemoteEndpoint(outgoingMessageStream, localEndpoint, exceptionHandler);
    jsonHandler.setMethodProvider(remoteEndpoint);

    return remoteEndpoint;
  }

  public RevaWebSocketLauncherBuilder setSession(WebSocketSession session) {
    this.session = session;

    return this;
  }

  public RevaWebSocketLauncherBuilder setMessageHandler(RevaWSMessageHandler messageHandler) {
    this.messageHandler = messageHandler;

    return this;
  }
}
