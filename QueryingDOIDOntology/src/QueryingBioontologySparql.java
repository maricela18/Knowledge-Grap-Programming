import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;

public class QueryingBioontologySparql 
{
	public static void main(String []args) 
	{

		Repository endpoint = new SPARQLRepository("http://sparql.bioontology.org/");
		try (RepositoryConnection conn = endpoint.getConnection()) 
		{
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

	}
	
}

