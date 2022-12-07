import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;

public class QueryingDBPedia 
{
	public static String DBPediaInformation(String key) 
	{
		String res = "";

		Repository endpoint = new SPARQLRepository("http://dbpedia.org/sparql");
		try (RepositoryConnection conn = endpoint.getConnection()) 
		{
			String queryString = "PREFIX grafo: <http://www.semanticweb.org/maricelaclaudiabravo/ontologies/2021/6/WISDM#> \n";
			queryString += "SELECT * \n";
			queryString += "WHERE { \n";
			queryString += "?s rdfs:label " + key + "@en . \n";
			queryString += "}";

			System.out.println(queryString);

			TupleQueryResult result = conn.prepareTupleQuery(queryString).evaluate();
			int tam = 0;
			while (result.hasNext()) 
			{
				BindingSet solution = result.next();
				res = solution.getValue("s").toString();
				System.out.println("================================================");
				System.out.println("S " + res);
				tam++;
			}
			
			
			System.out.println("Number of resources found: " + tam);
		}

		return res;
	}

}
