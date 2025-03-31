package reva.ide.commands;

public enum Command {
  CreateVariable("Create Variable", "0001");

  private String title;

  Command(String title, String id) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
