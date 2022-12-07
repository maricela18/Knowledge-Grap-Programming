
public class Symtom 
{
	private String symId; //for individual
	private String symName;
	private String symIRI; //Full reference
	
	public Symtom(String symId, String symName, String symIRI) {
		this.symId = symId;
		this.symName = symName;
		this.symIRI = symIRI;
	}

	public String getSymId() {
		return symId;
	}

	public void setSymId(String symId) {
		this.symId = symId;
	}

	public String getSymName() {
		return symName;
	}

	public void setSymName(String symName) {
		this.symName = symName;
	}

	public String getSymIRI() {
		return symIRI;
	}

	public void setSymIRI(String symIRI) {
		this.symIRI = symIRI;
	}

	@Override
	public String toString() {
		return symId + "  Name " + symName + "  Full reference " + symIRI;
	}
	
	

}
