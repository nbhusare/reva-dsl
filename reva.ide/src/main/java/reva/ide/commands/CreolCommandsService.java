package reva.ide.commands;

import org.eclipse.lsp4j.ApplyWorkspaceEditParams;
import org.eclipse.lsp4j.ApplyWorkspaceEditResponse;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.xtext.ide.server.ILanguageServerAccess;
import org.eclipse.xtext.ide.server.commands.IExecutableCommandService;
import org.eclipse.xtext.util.CancelIndicator;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CreolCommandsService implements IExecutableCommandService {
  @Override
  public List<String> initialize() {
    return Arrays.stream(Command.values()).map(Command::getTitle).toList();
  }

  @Override
  public Object execute(
      ExecuteCommandParams params, ILanguageServerAccess access, CancelIndicator cancelIndicator) {

    if (params.getCommand().equals(Command.CreateVariable.getTitle())) {
      CreateVariableArgs args = (CreateVariableArgs) params.getArguments().get(0);
      Map<String, List<TextEdit>> changes = new HashMap<>();

      TextEdit textEdit = new TextEdit();
      textEdit.setNewText(args.getNewText());
      textEdit.setRange(args.getRange());

      changes.put(args.getUri(), Collections.singletonList(textEdit));

      WorkspaceEdit edit = new WorkspaceEdit();
      edit.setChanges(changes);
      ApplyWorkspaceEditParams editParams = new ApplyWorkspaceEditParams();
      editParams.setEdit(edit);
      CompletableFuture<ApplyWorkspaceEditResponse> response =
          access.getLanguageClient().applyEdit(editParams);
    }

    return null;
  }
}
