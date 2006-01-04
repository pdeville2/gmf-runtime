/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.commands;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.ContainerElement;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Element;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Gate;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.InputOutputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.InputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.OutputTerminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Terminal;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Wire;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.util.LogicSemanticType;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.ConfigureElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;

/**
 * Abstract superclass for commands that configure <code>Element</code> s in
 * the logic model with input and output terminals.
 * 
 * @author ldamus
 */
public abstract class ConfigureLogicElementCommand extends
		ConfigureElementCommand {

	/**
	 * The input terminal feature.
	 */
	private static final EReference TERMINALS = SemanticPackage.eINSTANCE
			.getElement_Terminals();

	/**
	 * Constructs a new configure command for logic elements.
	 * 
	 * @param request
	 *            the configure request
	 * @param configurableType
	 *            the kind of element that can be configured by this command
	 *            instance.
	 */
	protected ConfigureLogicElementCommand(ConfigureRequest request,
			EClass configurableType) {

		super(request);
		setEClass(configurableType);
	}

	/**
	 * Creates an input terminal in <code>logicElement</code>.
	 * 
	 * @param logicElement
	 *            the logic element
	 * @param id
	 *            the terminal identifier
	 * @param progressMonitor
	 *            the monitor to measure progress through long-running
	 *            operations
	 * @return the new terminal
	 */
	protected InputTerminal createInputTerminal(Element logicElement,
			String id, IProgressMonitor progressMonitor) {

		return (InputTerminal) createTerminal(LogicSemanticType.INPUT_TERMINAL,
				logicElement, id, progressMonitor);
	}

	/**
	 * Creates an output terminal in <code>logicElement</code>.
	 * 
	 * @param logicElement
	 *            the logic element
	 * @param id
	 *            the terminal identifier
	 * @param progressMonitor
	 *            the monitor to measure progress through long-running
	 *            operations
	 * @return the new terminal
	 */
	protected OutputTerminal createOutputTerminal(Element logicElement,
			String id, IProgressMonitor progressMonitor) {

		return (OutputTerminal) createTerminal(
				LogicSemanticType.OUTPUT_TERMINAL, logicElement, id,
				progressMonitor);
	}

	/**
	 * Creates an input/output terminal in <code>logicElement</code>.
	 * 
	 * @param logicElement
	 *            the logic element
	 * @param id
	 *            the terminal identifier
	 * @param progressMonitor
	 *            the monitor to measure progress through long-running
	 *            operations
	 * @return the new terminal
	 */
	protected InputOutputTerminal createInputOutputTerminal(
			Element logicElement, String id, IProgressMonitor progressMonitor) {

		return (InputOutputTerminal) createTerminal(
				LogicSemanticType.INPUT_OUTPUT_TERMINAL, logicElement, id,
				progressMonitor);
	}

	/**
	 * Creates a new terminal in the <code>logicElement</code>, and sets its
	 * identifier to <code>id</code>.
	 * 
	 * @param elementType
	 *            the type of terminal to create
	 * @param logicElement
	 *            the logic element
	 * @param id
	 *            the terminal identifier
	 * @param progressMonitor
	 *            the monitor to measure progress through long-running
	 *            operations
	 * @return the new terminal
	 */
	private Terminal createTerminal(IElementType elementType,
			Element logicElement, String id, IProgressMonitor progressMonitor) {

		Terminal terminal = createTerminal(elementType, logicElement,
				progressMonitor);

		if (terminal != null) {
			setTerminalId(elementType, terminal, id, progressMonitor);
		}
		return terminal;
	}

	/**
	 * Creates a new terminal in the <code>containmentFeature</code> of
	 * <code>logicElement</code>
	 * 
	 * 
	 * @param elementType
	 *            the type of terminal to create
	 * @param logicElement
	 *            the logic element
	 * 
	 * @param progressMonitor
	 *            the monitor to measure progress through long-running
	 *            operations
	 * @return the new terminal element, or <code>null</code> if it wasn't
	 *         created
	 */
	private Terminal createTerminal(IElementType terminalType,
			Element logicElement, IProgressMonitor progressMonitor) {

		Element result = createElement(logicElement, terminalType, TERMINALS,
				progressMonitor);

		if (result instanceof Terminal) {
			return (Terminal) result;
		}

		return null;
	}

	/**
	 * Creates a <code>Wire</code> from <code>source</code> to
	 * <code>target</code>.
	 * 
	 * @param source
	 *            the source terminal
	 * @param target
	 *            the target terminal
	 * @param progressMonitor
	 *            the monitor to measure progress through long-running
	 *            operations
	 * @return the new <code>Wire</code>, or <code>null</code> if none was
	 *         created
	 */
	protected Wire createWire(OutputTerminal source, InputTerminal target,
			IProgressMonitor progressMonitor) {

		CreateRelationshipRequest createRequest = new CreateRelationshipRequest(
				source, target, LogicSemanticType.WIRE);

		IElementType elementType = ElementTypeRegistry.getInstance()
				.getElementType(createRequest.getEditHelperContext());

		if (elementType != null) {
			ICommand createCommand = elementType.getEditCommand(createRequest);

			if (createCommand != null && createCommand.isExecutable()) {
				createCommand.execute(progressMonitor);
				CommandResult commandResult = createCommand.getCommandResult();

				if (isOK(commandResult)) {
					Object result = commandResult.getReturnValue();

					if (result instanceof Wire) {
						return (Wire) result;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Creates a <code>Gate</code> in the <code>container</code>.
	 * 
	 * @param container
	 *            the conainer element
	 * @param gateType
	 *            the kind of gate to create
	 * @param progressMonitor
	 *            progressMonitor the monitor to measure progress through
	 *            long-running operations
	 * @return the new <code>Gate</code>, or <code>null</code> if none was
	 *         created
	 */
	protected Gate createGate(ContainerElement container,
			IElementType gateType, IProgressMonitor progressMonitor) {

		Element result = createElement(container, gateType, null,
				progressMonitor);

		if (result instanceof Gate) {
			return (Gate) result;
		}
		return null;
	}

	/**
	 * Creates a new element.
	 * 
	 * @param container
	 *            the container for the new element
	 * @param type
	 *            the kind of new element to create
	 * @param containmentFeature
	 *            the feature in which to put the new element
	 * @param progressMonitor
	 *            progressMonitor the monitor to measure progress through
	 *            long-running operations
	 * @return the new <code>Element</code>, or <code>null</code> if none
	 *         was created
	 */
	private Element createElement(Element container, IElementType type,
			EReference containmentFeature, IProgressMonitor progressMonitor) {

		CreateElementRequest createRequest = new CreateElementRequest(
				container, type, containmentFeature);

		IElementType elementType = ElementTypeRegistry.getInstance()
				.getElementType(createRequest.getEditHelperContext());

		if (elementType != null) {
			ICommand createCommand = elementType.getEditCommand(createRequest);

			if (createCommand != null && createCommand.isExecutable()) {
				createCommand.execute(progressMonitor);
				CommandResult commandResult = createCommand.getCommandResult();

				if (isOK(commandResult)) {
					Object result = commandResult.getReturnValue();

					if (result instanceof Element) {
						return (Element) result;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Sets the <code>terminal</code> identifier to <code>id</code>.
	 * 
	 * @param elementType
	 *            the type of terminal
	 * @param terminal
	 *            the terminal element
	 * @param id
	 *            the terminal identifier
	 * @param progressMonitor
	 *            the monitor to measure progress through long-running
	 *            operations
	 */
	private void setTerminalId(IElementType elementType, Terminal terminal,
			String id, IProgressMonitor progressMonitor) {

		SetRequest setRequest = new SetRequest(terminal,
				SemanticPackage.eINSTANCE.getTerminal_Id(), id);

		ICommand setCommand = elementType.getEditCommand(setRequest);

		if (setCommand != null && setCommand.isExecutable()) {
			setCommand.execute(progressMonitor);
		}
	}

}