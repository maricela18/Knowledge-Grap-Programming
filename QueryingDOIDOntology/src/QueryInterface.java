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

public class QueryInterface 
{

	public static void main(String[] args)
	{
	
	Repository repo = new SailRepository(new MemoryStore());
	repo.init();

	File file = new File("C:/Ontos/MascotasRDF.owl");
	String baseURI = "http://www.semanticweb.org/maricelaclaudiabravo/ontologies/2021/8/Mascotas#";

	try 
	{
		RepositoryConnection conn = repo.getConnection();
		conn.add(file, baseURI, RDFFormat.RDFXML);
		
		String queryString = "PREFIX grafo: <http://www.semanticweb.org/maricelaclaudiabravo/ontologies/2021/8/Mascotas#> \n";
		queryString += "SELECT distinct ?ind ?nombreMas ?edadMas ?razaMas \n";
		queryString += "WHERE { \n";
		queryString += "    ?c rdfs:subClassOf grafo:Mascota . \n";
		queryString += "    ?ind rdf:type ?c . \n";
		queryString += "    ?ind grafo:nombreMascota ?nombreMas . \n";
		queryString += "    ?ind grafo:edadMascota ?edadMas . \n";
		queryString += "    ?ind grafo:razaMascota ?razaMas . ";
		queryString += "}";

		System.out.println(queryString);
		TupleQuery query = conn.prepareTupleQuery(queryString);

		TupleQueryResult result = query.evaluate();

		while (result.hasNext()) 
		{
			BindingSet solution = result.next();

			String ind = solution.getValue("ind").toString();
			String nombreMas = solution.getValue("nombreMas").toString();
			String edadMas = solution.getValue("edadMas").toString();
			String razaMas = solution.getValue("razaMas").toString();

			System.out.println("================================================");
			System.out.println("Ind mascota " + ind);
			System.out.println("Nombre " + nombreMas);
			System.out.println("Edad " + edadMas);
			System.out.println("Raza " + razaMas);

		}
		System.out.println(" //////////////////////////////////////////////////////////////////////////////  ");

	}
	catch (RDF4JException | IOException e)
	{
		e.printStackTrace();
	}
	}
}
