/******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile;

import java.awt.Graphics2D;
import java.awt.Point;

import org.apache.batik.transcoder.TranscoderException;

/**  
 * @author dhabib
 */
public class SetViewportOrg extends AbstractSetExtentViewport 
{
	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException 
	{
		context.setViewportOrigin( new Point( getX(), getY() ) );
	}

}
