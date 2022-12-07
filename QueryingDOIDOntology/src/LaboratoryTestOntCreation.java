import java.util.List;

public class LaboratoryTestOntCreation 
{

	public static void main(String[] args) 
	{
		OntologyUtils util = new OntologyUtils();
		List<LaboratoryClass> lista = util.getLaboratoryTests();
		
		for(int i = 0; i < lista.size(); i++)
		{
			System.out.println("==================================================================");
			System.out.println("Id " + lista.get(i).getId());
			System.out.println("Name \n" + lista.get(i).getName());
			List<LaboratoryIndividual> individuals = lista.get(i).getIndividuals();
			for (int j = 0; j < individuals.size(); j++)
			{
				System.out.println("== Subclass Id " + individuals.get(j).getId());
				System.out.println("== Subclass Desc " + individuals.get(j).getDesc());
				System.out.println("");
			}
		}
		
		util.populateLaboratoryOntology("Ontologies/LaboratoryTest.owl", lista);
		
	}

}
