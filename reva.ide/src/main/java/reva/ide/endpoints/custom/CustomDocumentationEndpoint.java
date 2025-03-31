package reva.ide.endpoints.custom;

import org.eclipse.xtext.ide.server.Document;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.CancelIndicator;

public class CustomDocumentationEndpoint implements ICustomDocumentationService {
  @Override
  public String documentation(
      Document document,
      XtextResource resource,
      DocumentationParams params,
      CancelIndicator cancelIndicator) {
    return "Test Documentation";
  }
}
