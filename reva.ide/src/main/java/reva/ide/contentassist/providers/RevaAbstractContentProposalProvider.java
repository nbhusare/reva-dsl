package reva.ide.contentassist.providers;

import com.google.inject.Inject;
import org.eclipse.xtext.ide.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ide.editor.contentassist.ContentAssistEntry;
import org.eclipse.xtext.ide.editor.contentassist.IIdeContentProposalAcceptor;
import org.eclipse.xtext.ide.editor.contentassist.IdeContentProposalCreator;
import org.eclipse.xtext.ide.editor.contentassist.IdeContentProposalPriorities;
import org.eclipse.xtext.xbase.ide.contentassist.XbaseIdeContentProposalProvider;
import reva.config.RevaConfig;

import java.util.List;

public abstract class RevaAbstractContentProposalProvider extends XbaseIdeContentProposalProvider {
  @Inject private RevaConfig revaConfig;

  @Inject private IdeContentProposalPriorities proposalPriorities;

  public final Boolean createProposals(
      ContentAssistContext context,
      IIdeContentProposalAcceptor acceptor,
      IdeContentProposalCreator proposalCreator) {
    if (!canCreateProposals(context)) {
      return true;
    }

    generateSnippets(acceptor, context);

    return create(context, acceptor, proposalCreator);
  }

  protected abstract Boolean create(
      ContentAssistContext context,
      IIdeContentProposalAcceptor acceptor,
      IdeContentProposalCreator proposalCreator);

  protected abstract boolean canCreateProposals(ContentAssistContext context);

  protected abstract String getContextName();

  protected void generateSnippets(
      IIdeContentProposalAcceptor acceptor, ContentAssistContext context) {
    String snippetsPath = "completions." + getContextName() + ".snippets";
    List<Snippet> snippets = revaConfig.getBeansListSilent(Snippet.class, snippetsPath);

    snippets.stream()
        .map(snippet -> getContentAssistEntryFromSnippet(snippet, context))
        .forEach(
            contentAssistEntry ->
                acceptor.accept(
                    contentAssistEntry, proposalPriorities.getDefaultPriority(contentAssistEntry)));
  }

  private ContentAssistEntry getContentAssistEntryFromSnippet(
      Snippet snippet, ContentAssistContext context) {
    String insertText = snippet.getInsertText();

    ContentAssistEntry entry = new ContentAssistEntry();

    return entry;
  }
}
