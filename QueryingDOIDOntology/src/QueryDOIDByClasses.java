import java.io.File;
import java.io.IOException;

import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

public class QueryDOIDByClasses {

	public static void main(String[] args) throws IOException  
	 {
	        File dataDir = new File("C:/Ontos/");
	        Repository repo = new SailRepository(new MemoryStore());
	        repo.init();
	        
	        ValueFactory factory = repo.getValueFactory();
	        
	        File file = new File("C:/Ontos/doid.owl");
	        String baseURI = "http://purl.obolibrary.org/obo/doid.owl#";
	        
	        try 
	        {
	           RepositoryConnection conn = repo.getConnection();
	           conn.add(file, baseURI, RDFFormat.RDFXML);
	           //RepositoryResult<Statement> statements = conn.getStatements(null, null, null, true);

				String queryString = "PREFIX obo: <http://purl.obolibrary.org/obo/> \n";
				queryString += "PREFIX oboIn: <http://www.geneontology.org/formats/oboInOwl#/> \n";
				queryString += "SELECT ?s ?label ?def ";
				queryString += "WHERE { \n";
				queryString += "    ?s a owl:Class . ";
				queryString += "    ?s rdfs:subClassOf obo:DOID_4 . ";
				queryString += "    ?s rdfs:label ?label . ";
				queryString += "    ?s obo:IAO_0000115 ?def . ";
				queryString += "}";

				TupleQuery query = conn.prepareTupleQuery(queryString);
			
				TupleQueryResult result = query.evaluate();
				
				while (result.hasNext()) 
				{
						BindingSet solution = result.next();
						System.out.println("?s = " + solution.getValue("s"));
						System.out.println("?label = " + solution.getValue("label"));
						System.out.println("?def = " + solution.getValue("def"));
				}
			}
	        catch (RDF4JException e)
	        {
	        	e.printStackTrace();
	        }
	  }

}
