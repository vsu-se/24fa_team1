package application;

public class Category {
	private String name;
	public Category(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public void setBuyerPremium(double buyerPremium) {
		// TODO Auto-generated method stub
		
	}

	public void setSellerCommission(double sellerCommission) {
		// TODO Auto-generated method stub
		
	}
}