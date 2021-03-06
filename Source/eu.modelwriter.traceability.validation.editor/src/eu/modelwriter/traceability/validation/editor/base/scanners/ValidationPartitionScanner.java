package eu.modelwriter.traceability.validation.editor.base.scanners;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

import eu.modelwriter.traceability.validation.editor.base.rules.ModelRule;

public class ValidationPartitionScanner extends RuleBasedPartitionScanner {
  public final static String VALIDATION_COMMENT = "__validation_comment";
  public final static String VALIDATION_MODEL = "__validation_model";

  public final static String[] PARTITION_TYPES =
      new String[] {ValidationPartitionScanner.VALIDATION_COMMENT,
          ValidationPartitionScanner.VALIDATION_MODEL, IDocument.DEFAULT_CONTENT_TYPE};

  public ValidationPartitionScanner() {
    final IToken comment = new Token(VALIDATION_COMMENT);
    final IToken model = new Token(VALIDATION_MODEL);

    final IPredicateRule[] rules = new IPredicateRule[3];

    rules[0] = new MultiLineRule("/**", "**/", comment);
    rules[1] = new EndOfLineRule("--", comment);
    rules[2] = new ModelRule(model);

    this.setPredicateRules(rules);
  }
}
