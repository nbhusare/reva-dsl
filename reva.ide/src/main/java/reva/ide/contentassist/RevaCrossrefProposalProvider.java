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
    return super.queryScope(scope, crossReference, context);
  }

  private static boolean isJvmOperation(IEObjectDescription objectDesc) {
    return objectDesc != null && objectDesc.getEObjectOrProxy() instanceof JvmOperation;
  }
}
