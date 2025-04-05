package reva.ide.contentassist;

import com.google.inject.Inject;
import java.util.Collection;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.ide.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ide.editor.contentassist.IIdeContentProposalAcceptor;
import org.eclipse.xtext.xbase.ide.contentassist.XbaseIdeContentProposalProvider;
import reva.ide.contentassist.switches.ContentAssistComposedSwitch;
import reva.ide.utils.switches.ComposedSwitchFactory;

public class RevaContentProposalProvider extends XbaseIdeContentProposalProvider {
  @Inject ComposedSwitchFactory composedSwitchFactory;

  public void createProposals(
      Collection<ContentAssistContext> contexts, IIdeContentProposalAcceptor acceptor) {

    for (ContentAssistContext context : getFilteredContexts(contexts)) {
      ContentAssistComposedSwitch contentAssistComposedSwitch =
          composedSwitchFactory.getContentAssistComposedSwitch(acceptor, context);
      boolean customProposalCreationIsSuccessful =
          contentAssistComposedSwitch.computeCustomProposals(context.getCurrentModel());

      if (!customProposalCreationIsSuccessful) {
        for (AbstractElement element : context.getFirstSetGrammarElements()) {
          if (!acceptor.canAcceptMoreProposals()) {
            return;
          }
          createProposals(element, context, acceptor);
        }
      }
    }
  }

  @Override
  public boolean filterKeyword(Keyword keyword, ContentAssistContext context) {
    // TODO: Here we can filter out the allowed keywords
    return super.filterKeyword(keyword, context);
  }
}
