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

public class QuryingCOVIDConcepts {

	public static void main(String[] args) 
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
				queryString += "PREFIX oboInOwl: <http://www.geneontology.org/formats/oboInOwl#/> \n";
				queryString += "SELECT DISTINCT ?s ?p ?o \n";
				queryString += "WHERE { \n";
				//queryString += "    ?s oboInOwl:id \"DOID:0080600\" . \n";
				queryString += "    ?s rdfs:subClassOf obo:DOID_0080599 . \n";
				queryString += "    ?s ?p ?o . \n";
				queryString += "}";

				System.out.println(queryString);
				
				TupleQuery query = conn.prepareTupleQuery(queryString);
			
				TupleQueryResult result = query.evaluate();
				
				while (result.hasNext()) 
				{
						BindingSet solution = result.next();
						System.out.println("=====================================================================");
						System.out.println("?sujeto = " + solution.getValue("s"));
						System.out.println("?predicado = " + solution.getValue("p"));
						System.out.println("?objeto = " + solution.getValue("o"));
				}
			}
	        catch (RDF4JException | IOException e)
	        {
	        	e.printStackTrace();
	        }
	  }


}
