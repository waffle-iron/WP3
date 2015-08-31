/*******************************************************************************
 * Copyright (c) 2015 UNIT Information Technologies R&D
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    A. Furkan Tanriverdi (UNIT) - initial API and implementation
 *******************************************************************************/
/**
 *
 * $Id$
 */
package useCase.validation;

import org.eclipse.emf.common.util.EList;

import useCase.Actor;
import useCase.Interest;

/**
 * A sample validator interface for {@link useCase.UseCase}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface UseCaseValidator {
	boolean validate();

	boolean validatePrimaryActor(EList<Actor> value);

	boolean validatePrimaryActor(Actor value);
	boolean validateOwnedStakeholderInterest(EList<Interest> value);
	boolean validateOwnedMainFlow(useCase.Process value);

	boolean validateOwnedAlternativeFlow(EList<useCase.Process> value);

	boolean validateMainFlow(useCase.Process value);
	boolean validateAlternativeFlows(EList<useCase.Process> value);
}