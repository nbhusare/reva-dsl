package reva.ide.hover;

import com.google.inject.Inject;
import java.util.Optional;
import org.eclipse.lsp4j.MarkupContent;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.ide.server.Document;
import org.eclipse.xtext.ide.server.hover.HoverContext;
import org.eclipse.xtext.ide.server.hover.HoverService;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.resource.XtextResource;
import reva.config.RevaConfig;
import reva.ide.constructs.ExpressionDetails;

public class RevaHoverService extends HoverService {
  @Inject RevaConfig revaConfig;

  @Override
  protected MarkupContent getMarkupContent(HoverContext ctx) {
    return toMarkupContent(getKind(ctx), getContents(ctx));
  }

  @Override
  protected HoverContext createContext(Document document, XtextResource resource, int offset) {
    IParseResult parseResult = resource.getParseResult();
    if (parseResult == null) {
      return null;
    }

    ILeafNode leafNode = NodeModelUtils.findLeafNodeAtOffset(parseResult.getRootNode(), offset);
    if (leafNode != null && leafNode.getGrammarElement() instanceof org.eclipse.xtext.Keyword) {
      return new HoverContext(
          document, resource, offset, leafNode.getTextRegion(), leafNode.getGrammarElement());
    }

    return super.createContext(document, resource, offset);
  }

  private String getContents(HoverContext ctx) {
    if (ctx.getElement() == null || !(ctx.getElement() instanceof Keyword)) {
      return "";
    }

    Optional<ExpressionDetails> expressionDetails =
        revaConfig.getBean(
            ExpressionDetails.class, "constructs." + ((Keyword) ctx.getElement()).getValue());
    return expressionDetails.isPresent() ? expressionDetails.get().getDescription() : "";
  }
}
