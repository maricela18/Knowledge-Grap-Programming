import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;

public class QueryingEndpoint 
{

	public static void main(String[] args) 
	{
		Repository repo = new SPARQLRepository("http://sparql.bioontology.org/");
		repo.init();
		
		try(RepositoryConnection conn = repo.getConnection())
		{
		    TupleQuery query = conn.prepareTupleQuery(QueryLanguage.SPARQL,
		        "PREFIX : <http://example.org/>"
		        + " CONSTRUCT {"
		        + " :myThing :linksToASubject ?s ."
		        + " :myThing :linksToAPredicate ?p ."
		        + " :myThing :linksToAObject ?o ."
		        + " } WHERE { SELECT  ?s ?p ?o WHERE { ?s  ?p  ?o } LIMIT 1 }");
		    
		    query.setMaxExecutionTime(0);
		    
		    try(TupleQueryResult result = query.evaluate())
		    {
		        while(result.hasNext()) 
		        {
		            BindingSet bindingSet = result.next();
		            System.out.println(bindingSet);
		        }
		    };
		    
		};
		repo.shutDown();
	}
}
