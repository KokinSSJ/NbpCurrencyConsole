package pl.parser.nbp;

public class Position {
	
	private Currency currency;
	private String buyRate;
	private String sellRate;
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public String getBuyRate() {
		return buyRate;
	}
	public void setBuyRate(String buyRate) {
		this.buyRate = buyRate;
	}
	public String getSellRate() {
		return sellRate;
	}
	public void setSellRate(String sellRate) {
		this.sellRate = sellRate;
	}
	@Override
	public String toString() {
		return "Position [currency=" + currency + ", buyRate=" + buyRate + ", sellRate=" + sellRate + "]";
	}
	
	
	

}
