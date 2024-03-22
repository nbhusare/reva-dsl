package reva.ide;

import org.eclipse.lsp4j.CompletionOptions;
import org.eclipse.lsp4j.DefinitionParams;
import org.eclipse.lsp4j.DocumentHighlight;
import org.eclipse.lsp4j.DocumentHighlightParams;
import org.eclipse.lsp4j.FoldingRange;
import org.eclipse.lsp4j.FoldingRangeRequestParams;
import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.HoverParams;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.xtext.ide.server.LanguageServerImpl;
import org.eclipse.xtext.ide.server.WorkspaceManager;
import org.eclipse.xtext.util.CancelIndicator;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Language Server implementation for Reva. This will be wrapping the Language server operations to
 * gracefully handle the runtime exceptions
 */
public class RevaLanguageServerImpl extends LanguageServerImpl {
    private WorkspaceManager workspaceManager;

    @Override
    public void setWorkspaceManager(WorkspaceManager manager) {
        super.setWorkspaceManager(manager);
        this.workspaceManager = manager;
    }

    @Override
    protected List<FoldingRange> foldingRange(
            FoldingRangeRequestParams params, CancelIndicator cancelIndicator) {
        try {
            return super.foldingRange(params, cancelIndicator);
        } catch (Throwable e) {
            // Gracefully handle the error.
            return Collections.emptyList();
        }
    }

    @Override
    protected Hover hover(HoverParams params, CancelIndicator cancelIndicator) {
        try {
            return super.hover(params, cancelIndicator);
        } catch (Throwable e) {
            // Gracefully handle the error.
            return null;
        }
    }

    @Override
    public void exit() {
    }

    @Override
    public CompletableFuture<Object> shutdown() {
        return CompletableFuture.completedFuture(new Object());
    }

    @Override
    public CompletableFuture<List<FoldingRange>> foldingRange(FoldingRangeRequestParams params) {
        return super.foldingRange(params);
    }

    @Override
    public CompletableFuture<List<? extends DocumentHighlight>> documentHighlight(
            DocumentHighlightParams params) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    @Override
    public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>>
    definition(DefinitionParams params) {
        return CompletableFuture.completedFuture(Either.forLeft(Collections.emptyList()));
    }

    @Override
    protected ServerCapabilities createServerCapabilities(InitializeParams params) {
        ServerCapabilities serverCapabilities = super.createServerCapabilities(params);
        CompletionOptions completionProvider = serverCapabilities.getCompletionProvider();
        List<String> triggerCharacters =
                Stream.concat(completionProvider.getTriggerCharacters().stream(), Stream.of(" "))
                        .collect(Collectors.toList());
        completionProvider.setTriggerCharacters(triggerCharacters);
        completionProvider.setResolveProvider(true);

        return serverCapabilities;
    }
}
