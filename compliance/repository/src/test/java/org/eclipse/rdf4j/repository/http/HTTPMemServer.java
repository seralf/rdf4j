/*******************************************************************************
 * Copyright (c) 2015 Eclipse RDF4J contributors, Aduna, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/
package org.eclipse.rdf4j.repository.http;

import java.io.File;
import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.rdf4j.http.protocol.Protocol;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.config.RepositoryConfigException;
import org.eclipse.rdf4j.repository.manager.SystemRepository;
import org.eclipse.rdf4j.rio.RDFFormat;

/**
 * @author Herko ter Horst
 */
public class HTTPMemServer {

	private static final String HOST = "localhost";

	private static final int PORT = 18081;

	private static final String TEST_REPO_ID = "Test";

	private static final String TEST_INFERENCE_REPO_ID = "Test-RDFS";

	private static final String RDF4J_CONTEXT = "/rdf4j";

	private static final String SERVER_URL = "http://" + HOST + ":" + PORT + RDF4J_CONTEXT;

	public static final String REPOSITORY_URL = Protocol.getRepositoryLocation(SERVER_URL, TEST_REPO_ID);

	public static String INFERENCE_REPOSITORY_URL = Protocol.getRepositoryLocation(SERVER_URL, TEST_INFERENCE_REPO_ID);

	private final Server jetty;

	public HTTPMemServer() {
		System.clearProperty("DEBUG");

		jetty = new Server(PORT);

		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath(RDF4J_CONTEXT);
		// warPath configured in pom.xml maven-war-plugin configuration
		webapp.setWar("./target/rdf4j-server");
		jetty.setHandler(webapp);
	}

	public void start() throws Exception {
		File dataDir = new File(System.getProperty("user.dir") + "/target/datadir");
		dataDir.mkdirs();
		System.setProperty("org.eclipse.rdf4j.appdata.basedir", dataDir.getAbsolutePath());

		jetty.start();

		createTestRepositories();
	}

	public void stop() throws Exception {
		Repository systemRepo = new HTTPRepository(Protocol.getRepositoryLocation(SERVER_URL, SystemRepository.ID));
		try (RepositoryConnection con = systemRepo.getConnection()) {
			con.clear();
		}

		jetty.stop();
		System.clearProperty("org.mortbay.log.class");
	}

	private void createTestRepositories() throws RepositoryException, RepositoryConfigException {
		Repository systemRep = new HTTPRepository(Protocol.getRepositoryLocation(SERVER_URL, SystemRepository.ID));

		try (RepositoryConnection conn = systemRep.getConnection()) {
			conn.add(getClass().getResourceAsStream("/fixtures/memory.ttl"), "", RDFFormat.TURTLE);
			conn.add(getClass().getResourceAsStream("/fixtures/memory-rdfs.ttl"), "", RDFFormat.TURTLE);
		} catch (IOException e) {
			throw new RepositoryConfigException(e);
		}
	}
}
