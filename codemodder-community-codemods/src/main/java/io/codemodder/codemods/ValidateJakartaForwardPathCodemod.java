package io.codemodder.codemods;

import com.contrastsecurity.sarif.Result;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import io.codemodder.*;
import io.codemodder.ast.ASTTransforms;
import io.codemodder.providers.sarif.semgrep.SemgrepScan;
import java.util.List;
import javax.inject.Inject;

/**
 * Makes sure that internal Jakarta forwards don't go to places they shouldn't (e.g.,
 * /WEB-INF/web.xml.)
 */
@Codemod(
    id = "pixee:java/validate-jakarta-forward-path",
    author = "arshan@pixee.ai",
    reviewGuidance = ReviewGuidance.MERGE_WITHOUT_REVIEW)
public final class ValidateJakartaForwardPathCodemod
    extends SarifPluginJavaParserChanger<Expression> {

  @Inject
  public ValidateJakartaForwardPathCodemod(
      @SemgrepScan(ruleId = "validate-jakarta-forward-path") final RuleSarif sarif) {
    super(sarif, Expression.class);
  }

  @Override
  public boolean onResultFound(
      final CodemodInvocationContext context,
      final CompilationUnit cu,
      final Expression path,
      final Result result) {
    ASTTransforms.addStaticImportIfMissing(
        cu, "io.github.pixee.security.jakarta.PathValidator.validateDispatcherPath");
    Node parent = path.getParentNode().get();
    MethodCallExpr validateCall = new MethodCallExpr("validateDispatcherPath", path);
    parent.replace(path, validateCall);
    return true;
  }

  @Override
  public List<DependencyGAV> dependenciesRequired() {
    return List.of(DependencyGAV.JAVA_SECURITY_TOOLKIT);
  }
}
