package reva.ide.codeactions.switches;

import org.eclipse.emf.ecore.EObject;
import reva.ide.utils.switches.RevaDslCustomSwitch;
import reva.revaDsl.PrintExpression;

import java.util.List;

public class RevaDslCodeActionSwitch extends RevaDslCustomSwitch {
    @Override
    public List<EObject> casePrintExpression(PrintExpression object) {
        return super.casePrintExpression(object);
    }
}
