package eu.modelwriter.core.alloyinecore.ui.editor.completion.expression.provider;

import java.util.HashSet;
import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import eu.modelwriter.core.alloyinecore.recognizer.AlloyInEcoreParser.ExpressionContext;
import eu.modelwriter.core.alloyinecore.recognizer.AlloyInEcoreParser.TransposeContext;
import eu.modelwriter.core.alloyinecore.ui.editor.completion.expression.ExpressionCompletionTokens;
import eu.modelwriter.core.alloyinecore.ui.editor.completion.util.AbstractAIESuggestionProvider;

public class TransposeSuggestionProvider extends AbstractAIESuggestionProvider {

  @Override
  public Set<String> getStartSuggestions() {
    final Set<String> startSuggestions = new HashSet<>();
    startSuggestions.add(ExpressionCompletionTokens._transpose);
    return startSuggestions;
  }

  @Override
  protected void initParentProviders() {
    parents.add(spFactory.expressionSP());
  }

  @Override
  protected void initChildProviders() {
    children.add(spFactory.expressionSP());
  }

  @Override
  protected void computeSuggestions(final ParserRuleContext context, final ParseTree lastToken) {
    if (lastToken instanceof ParserRuleContext) {
      if (lastToken instanceof ExpressionContext) {
        // end of context.
        suggestions.addAll(getParentProviderSuggestions(context, lastToken));
      }
    } else if (lastToken instanceof TerminalNode) {
      if (lastToken.getText().equals(ExpressionCompletionTokens._transpose)) {
        suggestions.addAll(spFactory.expressionSP().getStartSuggestions());
      } else if (lastToken instanceof ErrorNode) {
        // suggestions.addAll(getChildProviderSuggestions(context, lastToken));
      }
    }
  }

  @Override
  protected boolean isCompatibleWithContext(final ParserRuleContext context) {
    return context instanceof TransposeContext;
  }

}
