package reva.ide.codeactions.switches;

import reva.ide.utils.switches.RevaComposedSwitch;

public class CodeActionComposedSwitch extends RevaComposedSwitch {

  private final RevaDslCodeActionSwitch revaDslCodeActionSwitch;
  private final XBaseCodeActionSwitch xBaseCodeActionSwitch;

  public CodeActionComposedSwitch(
      XBaseCodeActionSwitch xBaseCodeActionSwitch,
      RevaDslCodeActionSwitch revaDslCodeActionSwitch) {
    super(xBaseCodeActionSwitch, revaDslCodeActionSwitch);
    this.xBaseCodeActionSwitch = xBaseCodeActionSwitch;
    this.revaDslCodeActionSwitch = revaDslCodeActionSwitch;
  }
  
  
}
