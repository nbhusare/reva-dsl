package reva.ide.commands;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.lsp4j.ApplyWorkspaceEditParams;
import org.eclipse.lsp4j.ApplyWorkspaceEditResponse;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.xtext.ide.server.ILanguageServerAccess;
import org.eclipse.xtext.ide.server.commands.IExecutableCommandService;
import org.eclipse.xtext.util.CancelIndicator;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RevaCommandsService implements IExecutableCommandService {
  private final Gson gson = new Gson();

  @Override
  public List<String> initialize() {
    return Arrays.stream(Command.values()).map(Command::getCommand).toList();
  }

  @Override
  public Object execute(
      ExecuteCommandParams params, ILanguageServerAccess access, CancelIndicator cancelIndicator) {

    if (params.getCommand().equals(Command.CreateVariable.getCommand())) {
      CreateVariableArgs args =
          gson.fromJson(((JsonObject) params.getArguments().get(0)), CreateVariableArgs.class);
      String variableValue = args.getVariableValue();

      Map<String, List<TextEdit>> changes = new HashMap<>();

      TextEdit variableReplace = new TextEdit();
      TextEdit variableCreate = new TextEdit();

      variableReplace.setNewText(args.getNewText());
      variableReplace.setRange(args.getRange());

      variableCreate.setNewText(
          "var " + args.getNewText() + " = " + variableValue + System.lineSeparator());
      variableCreate.setRange(new Range(new Position(0, 0), new Position(0, 0)));

      changes.put(args.getUri(), List.of(variableReplace, variableCreate));

      WorkspaceEdit edit = new WorkspaceEdit();
      edit.setChanges(changes);
      ApplyWorkspaceEditParams editParams = new ApplyWorkspaceEditParams();
      editParams.setEdit(edit);
      CompletableFuture<ApplyWorkspaceEditResponse> response =
          access.getLanguageClient().applyEdit(editParams);
    }

    if (params.getCommand().equals(Command.RepeatPrint.getCommand())) {
      RepeatPrintStatementArgs args =
          gson.fromJson(
              ((JsonObject) params.getArguments().get(0)), RepeatPrintStatementArgs.class);

      this.executeRepeatPrintCommand(args, access.getLanguageClient());
    }

    return null;
  }

  private void executeRepeatPrintCommand(
      RepeatPrintStatementArgs args, LanguageClient languageClient) {
    TextEdit repeatEdit = new TextEdit();

    String edit =
        "repeat {"
            + System.lineSeparator()
            + "    "
            + args.getStatement().trim()
            + System.lineSeparator()
            + "} 2 times"
            + System.lineSeparator();
    repeatEdit.setNewText(edit);
    repeatEdit.setRange(args.getRange());

    Map<String, List<TextEdit>> changes = new HashMap<>();
    changes.put(args.getUri(), Collections.singletonList(repeatEdit));

    WorkspaceEdit workspaceEdit = new WorkspaceEdit();
    workspaceEdit.setChanges(changes);

    ApplyWorkspaceEditParams editParams = new ApplyWorkspaceEditParams();
    editParams.setEdit(workspaceEdit);

    languageClient.applyEdit(editParams);
  }
}
