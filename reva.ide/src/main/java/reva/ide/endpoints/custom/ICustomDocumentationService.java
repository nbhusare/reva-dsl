package reva.ide.endpoints.custom;

import com.google.inject.ImplementedBy;
import org.eclipse.xtext.ide.server.Document;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.CancelIndicator;

@ImplementedBy(CustomDocumentationEndpoint.class)
public interface ICustomDocumentationService {
  String documentation(
      Document document,
      XtextResource resource,
      DocumentationParams params,
      CancelIndicator cancelIndicator);
}
