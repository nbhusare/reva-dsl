package reva.ide.codeactions.switches;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ide.server.codeActions.ICodeActionService2;
import reva.ide.utils.switches.RevaComposedSwitch;

import java.util.Collections;
import java.util.List;

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

  @Override
  public List<EObject> doSwitch(EObject eObject) {
    List<EObject> eObjectsToVisit = super.doSwitch(eObject);

    for (EObject objectToVisit : eObjectsToVisit) {
      super.doSwitch(objectToVisit);
    }

    return Collections.emptyList();
  }
  
  public void getCodeActions(ICodeActionService2.Options options) {
    EList<EObject> contents = options.getResource().getContents();
    contents.forEach(this::doSwitch);
  }
}
