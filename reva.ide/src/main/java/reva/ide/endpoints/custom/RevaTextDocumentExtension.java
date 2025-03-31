package reva.ide.endpoints.custom;

import com.google.inject.Inject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;
import org.eclipse.xtext.ide.server.ILanguageServerAccess;
import org.eclipse.xtext.ide.server.ILanguageServerExtension;
import org.eclipse.xtext.ide.server.UriExtensions;
import org.eclipse.xtext.ide.server.concurrent.RequestManager;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.CancelIndicator;

import java.util.concurrent.CompletableFuture;

@JsonSegment("revaTextDocument")
public class RevaTextDocumentExtension implements ILanguageServerExtension {

    @Inject
    private UriExtensions uriExtensions;

    @Inject private RequestManager requestManager;

    @Inject private IResourceServiceProvider.Registry languagesRegistry;

    private ILanguageServerAccess languageServerAccess;

    @JsonRequest("documentation")
    public CompletableFuture<String> documentation(DocumentationParams params) {
        return requestManager.runRead(cancelIndicator -> documentation(cancelIndicator, params));
    }

    private String documentation(
            CancelIndicator cancelIndicator, DocumentationParams params) {
        URI uri = uriExtensions.toUri(params.getTextDocument().getUri());
        ICustomDocumentationService revaDocumentationService =
                getService(uri, ICustomDocumentationService.class);

        if (revaDocumentationService == null) {
            return "";
        }

        return languageServerAccess.doSyncRead(
                uriExtensions.toUriString(uri),
                context -> {
                    if (!(context.getResource() instanceof XtextResource)) {
                        return "";
                    }

                    return revaDocumentationService.documentation(
                            context.getDocument(),
                            (XtextResource) context.getResource(),
                            params,
                            cancelIndicator);
                });
    }

    @Override
    public void initialize(ILanguageServerAccess languageServerAccess) {
        this.languageServerAccess = languageServerAccess;
    }

    protected <Service> Service getService(URI uri, Class<Service> type) {
        return getService(getResourceServiceProvider(uri), type);
    }

    protected <Service> Service getService(
            IResourceServiceProvider resourceServiceProvider, Class<Service> type) {
        if (resourceServiceProvider == null) {
            return null;
        }
        return resourceServiceProvider.get(type);
    }

    protected IResourceServiceProvider getResourceServiceProvider(URI uri) {
        return languagesRegistry.getResourceServiceProvider(uri);
    }
}
