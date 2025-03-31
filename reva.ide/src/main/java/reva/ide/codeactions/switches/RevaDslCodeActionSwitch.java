package reva.ide.codeactions.switches;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionKind;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.xtext.ide.server.codeActions.ICodeActionService2;
import org.eclipse.xtext.xbase.XNumberLiteral;
import org.eclipse.xtext.xbase.XStringLiteral;
import reva.ide.commands.CreateVariableArgs;
import reva.ide.utils.switches.RevaDslCustomSwitch;
import reva.revaDsl.PrintExpression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RevaDslCodeActionSwitch extends RevaDslCustomSwitch {
  private List<Either<Command, CodeAction>> codeActions = new ArrayList<>();
  private ICodeActionService2.Options options;

  public RevaDslCodeActionSwitch(ICodeActionService2.Options options) {
    this.options = options;
  }

  @Override
  public List<EObject> casePrintExpression(PrintExpression printExpression) {
    if (printExpression.getExpression() instanceof XNumberLiteral
        || printExpression.getExpression() instanceof XStringLiteral) {
      return Collections.emptyList();
    }

    codeActions.add(getCreateVariableCodeAction(options.getURI(), null));

    return Collections.emptyList();
  }

  private Either<Command, CodeAction> getCreateVariableCodeAction(
      String uri, org.eclipse.lsp4j.Diagnostic diagnostic) {
    CodeAction codeAction = new CodeAction();
    Command command = new Command();

    command.setCommand(reva.ide.commands.Command.CreateVariable.getTitle());
    command.setTitle(reva.ide.commands.Command.CreateVariable.getTitle());

    command.setArguments(
        List.of(new CreateVariableArgs("varX", diagnostic.getRange(), uri, diagnostic)));
    codeAction.setCommand(command);
    codeAction.setKind(CodeActionKind.QuickFix);

    return Either.forRight(codeAction);
  }
}
