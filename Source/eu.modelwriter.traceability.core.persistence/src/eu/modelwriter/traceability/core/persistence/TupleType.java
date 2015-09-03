/**
 */
package eu.modelwriter.traceability.core.persistence;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tuple Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link eu.modelwriter.traceability.core.persistence.TupleType#getAtom <em>Atom</em>}</li>
 * </ul>
 *
 * @see eu.modelwriter.traceability.core.persistence.persistencePackage#getTupleType()
 * @model extendedMetaData="name='tuple_._type' kind='elementOnly'"
 * @generated
 */
public interface TupleType extends EObject {
  /**
   * Returns the value of the '<em><b>Atom</b></em>' containment reference list.
   * The list contents are of type {@link eu.modelwriter.traceability.core.persistence.AtomType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Atom</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Atom</em>' containment reference list.
   * @see eu.modelwriter.traceability.core.persistence.persistencePackage#getTupleType_Atom()
   * @model containment="true"
   *        extendedMetaData="kind='element' name='atom' namespace='##targetNamespace'"
   * @generated
   */
  EList<AtomType> getAtom();

} // TupleType