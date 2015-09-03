/**
 *
 * $Id$
 */
package eu.modelwriter.traceability.core.persistence.validation;


/**
 * A sample validator interface for {@link eu.modelwriter.traceability.core.persistence.SourceType}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface SourceTypeValidator {
  boolean validate();

  boolean validateValue(String value);
  boolean validateContent(String value);
  boolean validateFilename(String value);
}