package reva.ide.contentassist.providers;

import com.google.inject.Inject;
import java.util.Optional;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ide.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ide.editor.contentassist.IIdeContentProposalAcceptor;
import org.eclipse.xtext.ide.editor.contentassist.IdeContentProposalCreator;
import org.eclipse.xtext.ide.editor.contentassist.IdeContentProposalPriorities;
import org.eclipse.xtext.nodemodel.INode;
import reva.config.RevaConfig;
import reva.ide.spi.RevaSpiService;
import reva.ide.utils.nodes.NodeUtils;
import reva.revaDsl.PrintExpression;
import reva.services.RevaDslGrammarAccess;

@RevaSpiService("reva.ide.contentassist.providers.RevaAbstractContentProposalProvider")
public class PrintExpressionContentProposalProvider extends RevaAbstractContentProposalProvider {
  @Inject RevaDslGrammarAccess grammarAccess;

  @Inject IdeContentProposalPriorities proposalPriorities;

  @Inject RevaConfig revaConfig;

  @Override
  public Class<? extends EObject> mappedTo() {
    return PrintExpression.class;
  }

  @Override
  public Boolean create(
      ContentAssistContext context,
      IIdeContentProposalAcceptor acceptor,
      IdeContentProposalCreator proposalCreator) {
    return true;
  }

  @Override
  protected boolean canCreateProposals(ContentAssistContext context) {
    EObject currentModel = context.getCurrentModel();
    if (!(currentModel instanceof PrintExpression)) {
      return false;
    }

    Optional<INode> nodeBeforeOffset =
        NodeUtils.getNonEmptyNodeBeforeOffset(context.getRootNode(), context.getOffset());

    if (nodeBeforeOffset.isEmpty()) {
      return false;
    }

    String nodeText = nodeBeforeOffset.get().getText();
    return grammarAccess.getPrintExpressionAccess().getPrintKeyword_1().getValue().equals(nodeText);
  }

  @Override
  protected String getContextName() {
    return PrintExpression.class.getSimpleName();
  }
}
