package reva.ide.commands;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Range;

public class CreateVariableArgs {
  private final Range range;
  private final String uri;
  private final String newText;
  private final String variableValue;

  public CreateVariableArgs(
      String newText, Range range, String uri, Diagnostic diagnostic, String variableValue) {
    this.newText = newText;
    this.range = range;
    this.uri = uri;
    this.variableValue = variableValue;
  }

  public String getUri() {
    return uri;
  }

  public String getNewText() {
    return newText;
  }

  public Range getRange() {
    return range;
  }

  public String getVariableValue() {
    return variableValue;
  }
}
