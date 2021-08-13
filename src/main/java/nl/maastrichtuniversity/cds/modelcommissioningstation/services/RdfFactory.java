package nl.maastrichtuniversity.cds.modelcommissioningstation.services;

import nl.maastrichtuniversity.cds.modelcommissioningstation.model.*;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class RdfFactory {
    private Logger logger = Logger.getLogger(this.getClass().toString());
    private Repository repo = null;
    RepositoryConnection conn = null;
    ValueFactory vf = SimpleValueFactory.getInstance();
    IRI context;

    String getHostname() {
        String hostname = "localhost";
        try {
            hostname = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return hostname;
    }

    /**
     * Clear the repository, or only the given context (which is the actual scope of the factory)
     * @param completeRepository boolean indicating to clean the whole repository (value = true) or only the current context/graph (value = false)
     */
    public void clearData(boolean completeRepository) {
        if (completeRepository) {
            logger.info("Clearing whole repository");
            this.conn.clear();
        } else {
            logger.info("Clearing context " + this.context.stringValue());
            this.conn.clear(this.context);
        }
    }

    /**
     * Add a remote location to the current repository
     * @param remoteLocation: string representation of the URL where the turtle file is located
     */
    public void addRemoteFile(String remoteLocation) throws IOException {
        IRI graphIRI = SimpleValueFactory.getInstance().createIRI(remoteLocation);
        this.conn.clear(graphIRI);
        URL documentURL = new URL(remoteLocation);
        RDFFormat format = Rio.getParserFormatForFileName(documentURL.toString()).orElse(RDFFormat.RDFXML);
        org.eclipse.rdf4j.model.Model results = Rio.parse(documentURL.openStream(), remoteLocation, format);

        this.conn.add(results, graphIRI);
    }

    /**
     * Return all statements within the set context. If context is not given, all statements in repository are returned.
     * @return List object containing Statement instances
     */
    public List<Statement> getAllStatementsInContext() {
        RepositoryResult<Statement> statements = this.conn.getStatements(null, null, null, this.context);
        List<Statement> returnStatements = new ArrayList<Statement>();

        while(statements.hasNext()) {
            returnStatements.add(statements.next());
        }

        return returnStatements;
    }

    void addStatement(IRI subject, IRI predicate, IRI object) {
        if (this.context != null) {
            this.conn.add(this.vf.createStatement(subject, predicate, object, this.context));
        } else {
            this.conn.add(this.vf.createStatement(subject, predicate, object));
        }
    }

    void addStatement(IRI subject, IRI predicate, Value object) {
        if (this.context != null) {
            this.conn.add(this.vf.createStatement(subject, predicate, object, this.context));
        } else {
            this.conn.add(this.vf.createStatement(subject, predicate, object));
        }
    }

    void addStatement(BNode subject, IRI predicate, Value object) {
        if (this.context != null) {
            this.conn.add(this.vf.createStatement(subject, predicate, object, this.context));
        } else {
            this.conn.add(this.vf.createStatement(subject, predicate, object));
        }
    }

    /**
     * Add multiple statements to current repository (and current context if given in class constructor)
     * @param statements List of statements to be added
     */
    public void addStatements(Iterable<Statement> statements) {
        this.conn.add(statements, this.context);
    }

    /**
     * Initialize an in-memory repository
     */
    void initializeRdfStore() {
        this.initializeRdfStore("memory", null, null, null, null);
    }

    /**
     * Initialize repository on given information
     * @param repoType: type of repository (rdf4j, sparql, memory or null)
     * @param repoUrl: Location of the repository
     * @param repoId: repository/database ID
     * @param repoUser: username
     * @param repoPass: password
     */
    void initializeRdfStore(String repoType, String repoUrl, String repoId, String repoUser, String repoPass) {
        switch (repoType) {
            case "rdf4j":
                HTTPRepository httpRepo = new HTTPRepository(repoUrl, repoId);
                if (!("".equals(repoUser) || repoUser == null)) {
                    httpRepo.setUsernameAndPassword(repoUser, repoPass);
                }
                this.repo = httpRepo;
                break;
            case "sparql":
                this.repo = new SPARQLRepository(repoUrl);
                break;
            case "memory":
            default:
                this.repo = new SailRepository(new MemoryStore());
                this.repo.init();
                break;
        }

        this.conn = repo.getConnection();
    }

    /**
     * Get list of class types for a given URI
     * @param uri: Resource (IRI) to get the class types for
     * @return List of IRI objects representing the object type
     */
    private List<IRI> getClassTypesForUri(Resource uri) {
        RepositoryResult<Statement> statementsType = this.conn.getStatements(uri, RDF.TYPE, null);
        List<IRI> classTypes = new ArrayList<IRI>();
        while(statementsType.hasNext()) {
            classTypes.add((IRI)statementsType.next().getObject());
        }

        return classTypes;
    }

    /**
     * This function determines the Class type in a given RdfFactory implementation
     * @param classTypes: List of IRIs representing the current uri
     * @param uri: the URI (as IRI) to represent and determine
     * @param allStatements: All statements concerning the given URI
     * @return RdfRepresentation to specify the given URI. This can be a SimpleRdfRepresentation object,
     * or another implementation of RdfRepresentation
     */
    public abstract RdfRepresentation determineClassType(List<IRI> classTypes, Resource uri, List<Statement> allStatements);

    /**
     * Get RDF object for a specific given URI
     * @param uri: IRI representation of the URI
     * @return RdfRepresentation implementation
     */
    public RdfRepresentation getObjectForUri(Resource uri) {
        RdfRepresentation returnObject = null;

        List<IRI> classTypes = this.getClassTypesForUri(uri);
        RepositoryResult<Statement> statementsModel = this.conn.getStatements(uri, null, null);
        List<Statement> allStatements = new ArrayList<Statement>();
        while(statementsModel.hasNext()) {
            allStatements.add(statementsModel.next());
        }

        returnObject = this.determineClassType(classTypes, uri, allStatements);

        if (returnObject == null) {
            returnObject = new SimpleRdfRepresentation(uri, allStatements, this);
        }

        return returnObject;
    }

    /**
     * Get RDF object for a specific given URI
     * @param uri: String representation of the URI
     * @return RdfRepresentation implementation
     */
    public RdfRepresentation getObjectForUri(String uri) {
        return getObjectForUri(SimpleValueFactory.getInstance().createIRI(uri));
    }
}