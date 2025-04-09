package reva.ide.constructs;

import java.util.List;

public class ExpressionDetails {
  private String description;
  private List<ExpressionArgument> arguments;

  public ExpressionDetails() {}

  public ExpressionDetails(String description, List<ExpressionArgument> arguments) {
    this.description = description;
    this.arguments = arguments;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<ExpressionArgument> getArguments() {
    return arguments;
  }

  public void setArguments(List<ExpressionArgument> arguments) {
    this.arguments = arguments;
  }
}
