package hunt.social.mldata.entity;

import java.util.List;


public class MarketEntity {

	public String id;
    public String description;
    public Double reward;
    public List<String> keywords;
    public Long total;
    public String expirationDate;
    public String company;
    public String companyCode;
    public String abbr;
    public String getBatchAPI;
    public String cancelAPI;
    public String updateAPI;
    public Long batchNumber;
    public List<String> labels;
    public Integer type;
    
    

    public MarketEntity() {}

    
    

	public String getId() {
		return id;
	}




	public void setId(String id) {
		this.id = id;
	}




	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public Double getReward() {
		return reward;
	}



	public void setReward(Double reward) {
		this.reward = reward;
	}



	public List<String> getKeywords() {
		return keywords;
	}



	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}



	public Long getTotal() {
		return total;
	}



	public void setTotal(Long total) {
		this.total = total;
	}


	public String getExpirationDate() {
		return expirationDate;
	}



	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}



	public String getCompany() {
		return company;
	}



	public void setCompany(String company) {
		this.company = company;
	}



	public String getAbbr() {
		return abbr;
	}



	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}



	public String getGetBatchAPI() {
		return getBatchAPI;
	}



	public void setGetBatchAPI(String getBatchAPI) {
		this.getBatchAPI = getBatchAPI;
	}



	public String getCancelAPI() {
		return cancelAPI;
	}



	public void setCancelAPI(String cancelAPI) {
		this.cancelAPI = cancelAPI;
	}



	public String getUpdateAPI() {
		return updateAPI;
	}



	public void setUpdateAPI(String updateAPI) {
		this.updateAPI = updateAPI;
	}



	public Long getBatchNumber() {
		return batchNumber;
	}



	public void setBatchNumber(Long batchNumber) {
		this.batchNumber = batchNumber;
	}



	public List<String> getLabels() {
		return labels;
	}



	public void setLabels(List<String> labels) {
		this.labels = labels;
	}



	public Integer getType() {
		return type;
	}



	public void setType(Integer type) {
		this.type = type;
	}




	public String getCompanyCode() {
		return companyCode;
	}




	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
	
}

