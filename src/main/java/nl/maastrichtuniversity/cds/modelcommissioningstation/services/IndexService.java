package nl.maastrichtuniversity.cds.modelcommissioningstation.services;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class IndexService {
    public static final String INDEX_URL = "https://fairmodels.org/index.ttl";
    private final Repository repo;
    private final RepositoryConnection conn;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public IndexService() {
        this.repo = new SailRepository(new MemoryStore());
        this.repo.init();
        this.conn = repo.getConnection();

        this.reloadIndex();
        this.fetchReferencedFiles();
    }

    /**
     * Reload the index by removing the index triples from the RDF store,
     * and loading them again from the standard remote location.
     */
    public void reloadIndex() {
        this.logger.info("Clear and reload index");
        this.conn.clear(SimpleValueFactory.getInstance().createIRI(IndexService.INDEX_URL));
        this.addRemoteFile(IndexService.INDEX_URL);
    }

    /**
     * Add a remote location to the current repository
     * @param remoteLocation: string representation of the URL where the turtle file is located
     */
    private void addRemoteFile(String remoteLocation) {
        String query = "LOAD <" + remoteLocation + "> INTO GRAPH <" + remoteLocation + ">";
        this.conn.prepareUpdate(query).execute();
    }

    private void fetchReferencedFiles() {
        IRI predicateToSearch = SimpleValueFactory.getInstance().createIRI("https://fairmodels.org/ontology.owl#referencedInformation");
        RepositoryResult<Statement> statements = this.conn.getStatements(null, predicateToSearch,null);

        while(statements.hasNext()) {
            Statement stmt = statements.next();
            logger.debug(stmt.getSubject().toString() + " | " +
                    stmt.getPredicate().toString() + " | " +
                    stmt.getObject().toString());
            this.addRemoteFile(stmt.getObject().stringValue());
        }
    }

    public Map<String,String> getAllModels() {
        String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
        "PREFIX fml: <https://fairmodels.org/ontology.owl#> " +
        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
        "SELECT ?model ?label " +
        "WHERE { " +
	        "?model rdf:type fml:Model. " +
            "?model rdfs:label ?label. " +
        "}";

        Map<String, String> retResult = new HashMap<String, String>();
        TupleQueryResult result = this.conn.prepareTupleQuery(query).evaluate();
        while(result.hasNext()) {
            BindingSet bs = result.next();
            retResult.put(bs.getValue("model").stringValue(), bs.getValue("label").stringValue());
        }

        return retResult;
    }

}
