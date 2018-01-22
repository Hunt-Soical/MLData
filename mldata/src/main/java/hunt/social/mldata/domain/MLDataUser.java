package hunt.social.mldata.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "MLDataUser")
public class MLDataUser {

    @Id
    public String id;

    public String username;
    public String password;
    public String thirdPartyID;
    public String avator;
    public Date createDate;
    public Date updateDate;
    
    

    public MLDataUser() {}

    

    public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getThirdPartyID() {
		return thirdPartyID;
	}



	public void setThirdPartyID(String thirdPartyID) {
		this.thirdPartyID = thirdPartyID;
	}



	public String getAvator() {
		return avator;
	}



	public void setAvator(String avator) {
		this.avator = avator;
	}



	public Date getCreateDate() {
		return createDate;
	}



	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	public Date getUpdateDate() {
		return updateDate;
	}



	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}



	@Override
    public String toString() {
        return String.format(
                "User[id=%s, username='%s', avator='%s']",
                id, username, avator);
    }

}

