package reva.diagnostics;

public enum Diagnostic {
  RV001("RV001", "In-place value is defined: ");
  private String code;
  private String message;

  Diagnostic(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
