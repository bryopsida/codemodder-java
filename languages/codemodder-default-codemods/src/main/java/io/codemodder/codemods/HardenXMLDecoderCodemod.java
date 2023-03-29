package io.codemodder.codemods;

import com.contrastsecurity.sarif.Result;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import io.codemodder.*;
import io.codemodder.ast.ASTTransforms;
import io.codemodder.providers.sarif.semgrep.SemgrepJavaParserChanger;
import io.codemodder.providers.sarif.semgrep.SemgrepScan;
import io.github.pixee.security.XMLDecoderSecurity;
import java.util.List;
import javax.inject.Inject;

/** Adds gadget filtering logic to {@link java.beans.XMLDecoder} streams. */
@Codemod(
    id = "pixee:java/harden-xmldecoder-stream",
    author = "arshan@pixee.ai",
    reviewGuidance = ReviewGuidance.MERGE_WITHOUT_REVIEW)
public final class HardenXMLDecoderCodemod extends SemgrepJavaParserChanger<ObjectCreationExpr> {

  @Inject
  public HardenXMLDecoderCodemod(
      @SemgrepScan(ruleId = "harden-xmldecoder-stream") final RuleSarif sarif) {
    super(sarif, ObjectCreationExpr.class);
  }

  @Override
  public boolean onSemgrepResultFound(
      final CodemodInvocationContext context,
      final CompilationUnit cu,
      final ObjectCreationExpr newXmlDecoderCall,
      final Result result) {
    final Expression firstArgument = newXmlDecoderCall.getArgument(0);
    ASTTransforms.addImportIfMissing(cu, XMLDecoderSecurity.class);
    MethodCallExpr safeExpr =
        new MethodCallExpr(new NameExpr(XMLDecoderSecurity.class.getSimpleName()), "hardenStream");
    safeExpr.setArguments(NodeList.nodeList(firstArgument));
    newXmlDecoderCall.setArgument(0, safeExpr);
    return true;
  }

  @Override
  public List<DependencyGAV> dependenciesRequired() {
    return List.of(DependencyGAV.JAVA_SECURITY_TOOLKIT);
  }
}
