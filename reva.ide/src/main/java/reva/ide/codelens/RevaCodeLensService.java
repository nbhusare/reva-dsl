package reva.ide.codelens;

import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.CodeLensParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.xtext.ide.server.Document;
import org.eclipse.xtext.ide.server.codelens.ICodeLensService;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.CancelIndicator;
import reva.ide.commands.RepeatPrintStatementArgs;
import reva.ide.utils.nodes.NodeUtils;
import reva.revaDsl.Model;
import reva.revaDsl.PrintExpression;

import java.util.List;

public class RevaCodeLensService implements ICodeLensService {
  @Override
  public List<? extends CodeLens> computeCodeLenses(
      Document document, XtextResource resource, CodeLensParams params, CancelIndicator indicator) {

    return ((Model) resource.getContents().get(0))
        .getExpressions().stream()
            .filter(expression -> expression instanceof PrintExpression)
            .map(
                expression ->
                    getRepeatPrintStatementCodeLens(
                        (PrintExpression) expression, document, resource.getURI().toString()))
            .toList();
  }

  private CodeLens getRepeatPrintStatementCodeLens(
      PrintExpression printExpression, Document document, String uri) {
    CodeLens codeLens = new CodeLens();
    codeLens.setCommand(getRepeatPrintStatementCommand(printExpression, document, uri));
    codeLens.setRange(NodeUtils.getRange(document, printExpression));

    return codeLens;
  }

  private Command getRepeatPrintStatementCommand(
      PrintExpression printExpression, Document document, String uri) {
    Command command = new Command();

    String printStatement = NodeModelUtils.getNode(printExpression).getText();

    command.setTitle(reva.ide.commands.Command.RepeatPrint.getTitle());
    command.setCommand(reva.ide.commands.Command.RepeatPrint.getCommand());

    RepeatPrintStatementArgs args =
        new RepeatPrintStatementArgs(
            printStatement, NodeUtils.getRange(document, printExpression), uri, null);
    command.setArguments(List.of(args));

    return command;
  }
}
