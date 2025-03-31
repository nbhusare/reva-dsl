package reva.ide.commands;

public enum Command {
  CreateVariable("Create Variable", "0001"),
  RepeatPrint("Repeat Statement", "0002");

  private final String title;
  private final String id;

  Command(String title, String id) {
    this.title = title;
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public String getId() {
    return id;
  }
}
