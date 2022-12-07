import java.io.File;
import java.io.IOException;

import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

public class QueryingWISDM3 {

	public static void main(String[] args)
	{
	
	Repository repo = new SailRepository(new MemoryStore());
	repo.init();

	File file = new File("C:/Ontos/Sensors/WISDM3RDF.owl");
	String baseURI = "http://www.semanticweb.org/maricelaclaudiabravo/ontologies/2021/6/WISDM#";
	
	try 
	{
		RepositoryConnection conn = repo.getConnection();
		conn.add(file, baseURI, RDFFormat.RDFXML);
		
		String queryString = "PREFIX grafo: <http://www.semanticweb.org/maricelaclaudiabravo/ontologies/2021/6/WISDM#> \n";
		queryString += "SELECT distinct ?patient ?cgm ?manufacturer \n";
		queryString += "WHERE { \n";
		queryString += "    ?patient rdf:type grafo:Patient . \n";
		queryString += "    ?patient grafo:patientHasCGM ?cgm . \n";
		queryString += "    ?cgm grafo:hasDeviceManufacturer ?manufacturer . ";
		queryString += "}";

		System.out.println(queryString);
		TupleQuery query = conn.prepareTupleQuery(queryString);

		TupleQueryResult result = query.evaluate();

		while (result.hasNext()) 
		{
			BindingSet solution = result.next();

			String patient = solution.getValue("patient").toString();
			String cgm = solution.getValue("cgm").toString();
			String manufacturer = solution.getValue("manufacturer").toString();

			System.out.println("================================================");
			System.out.println("Patient id " + patient);
			System.out.println("CGM " + cgm);
			System.out.println("Manufacturer " + manufacturer);
			System.out.println("Manufacturer info == " + QueryingDBPedia.DBPediaInformation(manufacturer));

		}
		System.out.println(" //////////////////////////////////////////////////////////////////////////////  ");

	}
	catch (RDF4JException | IOException e)
	{
		e.printStackTrace();
	}
	}

}
