import java.io.File;
import java.io.IOException;

import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sail.*;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;

public class ReadOntologyasRDF 
{

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
	           RepositoryResult<Statement> statements = conn.getStatements(null, null, null, true);
	                 
	            	while (statements.hasNext())
	            	{
	                    Statement st = statements.next();
	                    System.out.println(st);
	                }
	            	
	            	statements.close();	
	            	conn.close();
	            
	        }
	        catch (RDF4JException e)
	        {
	        	e.printStackTrace();
	        }
	  }

}
