package reva.ls.server;

import org.eclipse.lsp4j.jsonrpc.MessageConsumer;
import org.eclipse.lsp4j.jsonrpc.MessageIssueException;
import org.eclipse.lsp4j.jsonrpc.MessageIssueHandler;
import org.eclipse.lsp4j.jsonrpc.json.MessageJsonHandler;
import org.eclipse.lsp4j.jsonrpc.messages.Message;

public class RevaWSMessageHandler {
  private MessageConsumer consumer;
  private MessageJsonHandler messageJsonHandler;
  private MessageIssueHandler issueHandler;

  public void setConfigs(
      MessageConsumer consumer, MessageJsonHandler jsonHandler, MessageIssueHandler issueHandler) {
    this.consumer = consumer;
    this.issueHandler = issueHandler;
    this.messageJsonHandler = jsonHandler;
  }

  public void onMessage(String content) {
    try {
      Message message = messageJsonHandler.parseMessage(content);
      consumer.consume(message);
    } catch (MessageIssueException exception) {
      issueHandler.handle(exception.getRpcMessage(), exception.getIssues());
    }
  }
}
