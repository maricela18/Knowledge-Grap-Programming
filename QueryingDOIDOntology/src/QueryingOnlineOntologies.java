import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.rio.RDFFormat;

public class QueryingOnlineOntologies 
{

	public static void main(String[] args) 
	{
		String sparqlEndpoint = "http://sparql.bioontology.org/";
		Repository repo = new SPARQLRepository(sparqlEndpoint);
		repo.init();

		try 
		{
			Map<String, String> headers = new HashMap<String, String>();
	        headers.put("Accept", "SPARQL/JSON");
	        ((SPARQLRepository) repo).setAdditionalHttpHeaders(headers);
			RepositoryConnection conn = repo.getConnection();
			

			String queryString = "PREFIX obo: <http://purl.obolibrary.org/obo/> \n";
			queryString += "PREFIX oboInOwl: <http://www.geneontology.org/formats/oboInOwl#/> \n";
			queryString += "SELECT DISTINCT ?s ?p ?o \n";
			queryString += "WHERE { \n";
			// queryString += " ?s oboInOwl:id \"DOID:0080600\" . \n";
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
		catch (RDF4JException e) 
		{
			e.printStackTrace();
		}

	}

}
