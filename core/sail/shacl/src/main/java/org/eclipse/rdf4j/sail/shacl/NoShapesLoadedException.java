/*******************************************************************************
 * Copyright (c) 2018 Eclipse RDF4J contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/

package org.eclipse.rdf4j.sail.shacl;

import org.eclipse.rdf4j.model.vocabulary.RDF4J;

/**
 * @deprecated since 3.0. This feature is for internal use only: its existence, signature or behavior may change without
 *             warning from one release to the next.
 */
@Deprecated
// FIXME should extend SailException
public class NoShapesLoadedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoShapesLoadedException() {
		super("Load shapes by adding them to named graph <" + RDF4J.SHACL_SHAPE_GRAPH
				+ "> in the first transaction after initialization!");
	}
}
