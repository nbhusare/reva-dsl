package reva.ide.contentassist;

import com.google.common.collect.Lists;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.CrossReference;
import org.eclipse.xtext.common.types.JvmEnumerationType;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.ide.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.xbase.ide.contentassist.XbaseIdeCrossrefProposalProvider;

import java.util.stream.Collectors;

public class RevaCrossrefProposalProvider extends XbaseIdeCrossrefProposalProvider {
  @Override
  protected Iterable<IEObjectDescription> queryScope(
      IScope scope, CrossReference crossReference, ContentAssistContext context) {
    return Lists.newArrayList(scope.getAllElements()).stream()
        .filter(
            ieObjDesc -> {
              EObject eObjectOrProxy = ieObjDesc.getEObjectOrProxy();
              if ((eObjectOrProxy instanceof JvmFormalParameter)
                  || (eObjectOrProxy instanceof JvmGenericType)) {
                return false;
              }
              if (!isJvmOperation(ieObjDesc)) {
                return true;
              }
              JvmOperation jvmOperation = (JvmOperation) eObjectOrProxy;
              String identifier = jvmOperation.getIdentifier();
              return !identifier.startsWith("org.eclipse.xtext.xbase.lib.")
                  && !identifier.contains("java.lang.")
                  && jvmOperation.getVisibility() == JvmVisibility.PUBLIC
                  && !(jvmOperation.eContainer() instanceof JvmEnumerationType);
            })
        .collect(Collectors.toList());
  }

  private static boolean isJvmOperation(IEObjectDescription objectDesc) {
    return objectDesc != null && objectDesc.getEObjectOrProxy() instanceof JvmOperation;
  }
}
