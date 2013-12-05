package org.stjs.generator.check.statement;

import org.stjs.generator.GenerationContext;
import org.stjs.generator.JavascriptFileGenerationException;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.visitor.TreePathScannerContributors;
import org.stjs.generator.visitor.VisitorContributor;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.util.TreePath;

public class VariableFinalInLoopCheck implements VisitorContributor<VariableTree, Void, GenerationContext> {
	private static boolean isLoop(TreePath path) {
		Tree tree = path.getLeaf();
		return tree instanceof ForLoopTree || tree instanceof EnhancedForLoopTree || tree instanceof WhileLoopTree;
	}

	private static boolean isMethodOrClassDeclaration(TreePath path) {
		Tree tree = path.getLeaf();
		return tree instanceof MethodTree || tree instanceof ClassTree;
	}

	@Override
	public Void visit(TreePathScannerContributors<Void, GenerationContext> visitor, VariableTree tree, GenerationContext context, Void v) {
		if (!JavaNodes.isFinal(tree)) {
			return null;
		}
		for (TreePath p = context.getCurrentPath(); p != null; p = p.getParentPath()) {
			if (isLoop(p)) {
				//TODO add new SourcePosition(n)
				context.getChecks()
						.addError(
								new JavascriptFileGenerationException(context.getInputFile(), null,
										"To prevent unexpected behaviour in Javascript, final variables must be declared at method level and not inside loops"));
			}
			if (isMethodOrClassDeclaration(p)) {
				break;
			}
		}
		return null;
	}

}