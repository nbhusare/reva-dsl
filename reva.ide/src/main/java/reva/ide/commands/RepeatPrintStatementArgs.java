package reva.ide.commands;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Range;

public class RepeatPrintStatementArgs {
  private final Range range;
  private final String uri;
  private final String statement;

  public RepeatPrintStatementArgs(
      String statement, Range range, String uri, Diagnostic diagnostic) {
    this.statement = statement;
    this.range = range;
    this.uri = uri;
  }

  public String getUri() {
    return uri;
  }

  public Range getRange() {
    return range;
  }

  public String getStatement() {
    return statement;
  }
}
