package reva.ide.codeactions;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionKind;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.xtext.ide.server.codeActions.ICodeActionService2;
import reva.diagnostics.Diagnostic;
import reva.ide.commands.CreateVariableArgs;
import reva.ide.utils.switches.ComposedSwitchFactory;

public class RevaCodeActionService implements ICodeActionService2 {
  @Inject ComposedSwitchFactory composedSwitchFactory;

  @Override
  public List<Either<Command, CodeAction>> getCodeActions(Options options) {
    composedSwitchFactory.getCodeActionComposedSwitch(options).getCodeActions(options);

    List<Either<Command, CodeAction>> codeActions = new ArrayList<>();

    options
        .getCodeActionParams()
        .getContext()
        .getDiagnostics()
        .forEach(
            diagnostic -> {
              String diagnosticCode = diagnostic.getCode().getLeft();

              if (diagnosticCode.equals(Diagnostic.RV001.getCode())) {
                codeActions.add(getCreateVariableCodeAction(options.getURI(), diagnostic));
              }
            });

    return codeActions;
  }

  private Either<Command, CodeAction> getCreateVariableCodeAction(
      String uri, org.eclipse.lsp4j.Diagnostic diagnostic) {
    CodeAction codeAction = new CodeAction();
    Command command = new Command();

    command.setCommand(reva.ide.commands.Command.CreateVariable.getId());
    command.setTitle(reva.ide.commands.Command.CreateVariable.getTitle());

    command.setArguments(
        List.of(new CreateVariableArgs("varX", diagnostic.getRange(), uri, diagnostic)));
    codeAction.setCommand(command);
    codeAction.setKind(CodeActionKind.QuickFix);

    return Either.forRight(codeAction);
  }
}
