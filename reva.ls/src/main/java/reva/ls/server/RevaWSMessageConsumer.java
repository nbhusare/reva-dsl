package reva.ls.server;

import java.io.IOException;
import org.eclipse.lsp4j.jsonrpc.JsonRpcException;
import org.eclipse.lsp4j.jsonrpc.MessageConsumer;
import org.eclipse.lsp4j.jsonrpc.MessageIssueException;
import org.eclipse.lsp4j.jsonrpc.json.MessageJsonHandler;
import org.eclipse.lsp4j.jsonrpc.messages.Message;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class RevaWSMessageConsumer implements MessageConsumer {
  private final MessageJsonHandler messageJsonHandler;
  private final WebSocketSession session;

  public RevaWSMessageConsumer(MessageJsonHandler messageJsonHandler, WebSocketSession session) {
    this.session = session;
    this.messageJsonHandler = messageJsonHandler;
  }

  @Override
  public void consume(Message message) throws MessageIssueException, JsonRpcException {
    try {
      if (!session.isOpen()) {
        return;
      }

      String content = messageJsonHandler.serialize(message);
      TextMessage textMessage = new TextMessage(content);
      session.sendMessage(textMessage);
    } catch (IOException exception) {
      // TODO: Log properly
      throw new JsonRpcException(exception);
    }
  }
}
