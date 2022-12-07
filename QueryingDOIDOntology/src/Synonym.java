
public class Synonym 
{
	private String identifier;
	private String meaning;
	
	
	public Synonym(String identifier, String meaning) {
		this.identifier = identifier;
		this.meaning = meaning;
	}


	public String getIdentifier() {
		return identifier;
	}


	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}


	public String getMeaning() {
		return meaning;
	}


	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}


	@Override
	public String toString() {
		return this.identifier + " hasSynonym " + this.meaning;
	}
	
	
	
}
