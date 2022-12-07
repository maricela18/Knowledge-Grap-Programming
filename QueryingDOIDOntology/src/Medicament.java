
public class Medicament 
{
	private String nuiId; //For individual
	private String medicamentIRI; //Full reference
	private String label;
	private String units;
	private String strength;
	private String rxNormCUI;
	private String rxNormName;
	
	public Medicament(String nuiId, String medicamentIRI, String label, String units, 
			String strength, String rxNormCUI, String rxNormName) 
	{
		this.nuiId = nuiId;
		this.medicamentIRI = medicamentIRI;
		this.label = label;
		this.units = units;
		this.strength = strength;
		this.rxNormCUI = rxNormCUI;
		this.rxNormName = rxNormName;
	}

	public String getNuiId() {
		return nuiId;
	}

	public void setNuiId(String nuiId) {
		this.nuiId = nuiId;
	}

	public String getMedicamentIRI() {
		return medicamentIRI;
	}

	public void setMedicamentIRI(String medicamentIRI) {
		this.medicamentIRI = medicamentIRI;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getStrength() {
		return strength;
	}

	public void setStrength(String strength) {
		this.strength = strength;
	}

	public String getRxNormCUI() {
		return rxNormCUI;
	}

	public void setRxNormCUI(String rxNormCUI) {
		this.rxNormCUI = rxNormCUI;
	}

	public String getRxNormName() {
		return rxNormName;
	}

	public void setRxNormName(String rxNormName) {
		this.rxNormName = rxNormName;
	}

	@Override
	public String toString() 
	{
		return "Medicament [nuiId=" + nuiId + ", medicamentIRI=" + medicamentIRI + ", label=" + label + ", units="
				+ units + ", strength=" + strength + ", rxNormCUI=" + rxNormCUI + ", rxNormName=" + rxNormName + "]";
	}
	
	
	
}
