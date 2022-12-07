import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class DrugOntologyCreation 
{
	public static void main(String[] args) 
	{
		OntologyUtils util = new OntologyUtils();	
		List <String> relations = new ArrayList<String>();
		
		//Step 1. Read the list of medicaments and insert them into the ontology
		List<Medicament> listMed = util.getListOfMedicamentsNDFRT();
		
		//Step 2. Obtain the relationships for each medicament
		List<Property> listProps = util.getPropertiesOfMedicaments(listMed);
		//System.out.println(listProps);
		
		for(int i=0; i<listProps.size(); i++)
		{
			//System.out.println(listProps.get(i).toString());
			String rel = getRelation(listProps.get(i).getRelationship());
			String domain = listProps.get(i).getObjectValue();
			//if(rel.equalsIgnoreCase("effect_may_be_inhibited_by") )
			   relations.add(rel + " " + domain);
		}
		
		Iterator<String> iterator = relations.iterator();
		while(iterator.hasNext())
		{
		  System.out.println(iterator.next());
		}
		
	}
	
	public static String getRelation(String relation)
	{
		String res = "";
		StringTokenizer tokenized = new StringTokenizer(relation,"#");
		while(tokenized.hasMoreElements())
		{
			try
			{
				tokenized.nextToken();
			    res =  tokenized.nextToken();
			}
			catch(NoSuchElementException e)
			{
				res = "has_DoseForm";
			}	
		}
		return res;
	}
	
}
