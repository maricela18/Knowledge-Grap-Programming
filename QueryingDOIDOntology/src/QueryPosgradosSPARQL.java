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

public class QueryPosgradosSPARQL 
{

	public static void main(String[] args) 
	{
		Repository repo = new SailRepository(new MemoryStore());
		repo.init();

		File file = new File("C:/Ontos/Posgrados3RDF.owl");
		String baseURI = "http://www.posgradosmexico.org";

		try 
		{
			RepositoryConnection conn = repo.getConnection();
			conn.add(file, baseURI, RDFFormat.RDFXML);

			String queryString = "PREFIX pos:<http://www.posgradosmexico.org#> \n";
			queryString += "SELECT distinct ?doctorado ?campus ?lineas ?profesores \n";
			queryString += "WHERE { \n";
			queryString += "    ?doctorado rdf:type pos:Doctorado . \n";
			queryString += "    ?doctorado pos:seImparteEn ?campus . \n";
			queryString += "    ?doctorado pos:tieneLineaDeInvestigacion ?lineas . \n";
			queryString += "    ?doctorado pos:tieneProfesor ?profesores . \n";
			queryString += "}";

			System.out.println(queryString);
			TupleQuery query = conn.prepareTupleQuery(queryString);

			TupleQueryResult result = query.evaluate();

			while (result.hasNext()) 
			{
				BindingSet solution = result.next();

				String doctorado = solution.getValue("doctorado").toString();
				String campus = solution.getValue("campus").toString();
				String lineas = solution.getValue("lineas").toString();
				String profesores = solution.getValue("profesores").toString();
				
				System.out.println("================================================");
				System.out.println("Doctorado " + doctorado);
				System.out.println("Campus " + campus);
				System.out.println("Lineas " + lineas);
				System.out.println("Profesores " + profesores);
			}
			System.out.println(" //////////////////////////////////////////////////////////////////////////////  ");

		}
		catch (RDF4JException | IOException e)
		{
			e.printStackTrace();
		}


	}

}
