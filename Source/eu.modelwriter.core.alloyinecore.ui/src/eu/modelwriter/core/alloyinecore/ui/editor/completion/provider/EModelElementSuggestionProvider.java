package eu.modelwriter.core.alloyinecore.ui.editor.completion.provider;

import java.util.HashSet;
import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

import eu.modelwriter.core.alloyinecore.recognizer.AlloyInEcoreParser.EModelElementContext;
import eu.modelwriter.core.alloyinecore.ui.editor.completion.util.AbstractAIESuggestionProvider;

public class EModelElementSuggestionProvider extends AbstractAIESuggestionProvider {

  @Override
  public Set<ICompletionProposal> getStartSuggestions() {
    final Set<ICompletionProposal> startSuggestions = new HashSet<>();
    startSuggestions.addAll(
        spFactory.eAnnotationSP().getStartSuggestions());
    startSuggestions.addAll(
        spFactory.eNamedElementSP().getStartSuggestions());
    return startSuggestions;
  }

  @Override
  protected void computeSuggestions(final ParserRuleContext context, final ParseTree lastToken) {
    suggestions.addAll(getParentProviderSuggestions(context, lastToken));
  }

  @Override
  protected boolean isCompatibleWithContext(final ParserRuleContext context) {
    return context instanceof EModelElementContext;
  }

  @Override
  protected void initParentProviders() {
    addParent(spFactory.eAnnotationSP());
  }

  @Override
  protected void initChildProviders() {
    addChild(spFactory.eAnnotationSP());
    addChild(spFactory.eNamedElementSP());
  }

}
