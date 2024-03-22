package reva.ls.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageClientAware;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.websocket.WebSocketEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reva.ide.LanguageServerModule;

import java.util.Collection;

public class WebsocketLanguageServerEndpoint extends WebSocketEndpoint<LanguageClient> {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketLanguageServerEndpoint.class);
    static final Injector injector = Guice.createInjector(new LanguageServerModule());

    @Override
    protected void configure(Launcher.Builder<LanguageClient> builder) {
        // TODO: Enable tracing
        builder.setLocalService(injector.getInstance(LanguageServer.class));
        builder.setRemoteInterface(LanguageClient.class);
    }

    @Override
    protected void connect(Collection<Object> localServices, LanguageClient remoteProxy) {
        localServices
                .forEach(
                        a -> {
                            LOGGER.debug("connection to {}", a);
                            if (a instanceof LanguageClientAware) {
                                try {
                                    LanguageClientAware t = (LanguageClientAware) a;
                                    t.connect(remoteProxy);
                                } catch (Exception e) {
                                    LOGGER.error("error while connecting", e);
                                }
                            }
                        });
    }
}
