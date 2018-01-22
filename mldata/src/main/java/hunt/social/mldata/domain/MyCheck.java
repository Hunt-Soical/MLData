package hunt.social.mldata.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import hunt.social.mldata.util.JsonDateSerializer;

@JsonAutoDetect
@Document(collection = "MyCheck")
public class MyCheck {

    @Id
    public String id;

    public String userId;
    public String workId;
    public Long finishTotal;
    public Date createDate;
    public Date updateDate;

    public MyCheck() {}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}



	public String getWorkId() {
		return workId;
	}



	public void setWorkId(String workId) {
		this.workId = workId;
	}



	public Long getFinishTotal() {
		return finishTotal;
	}



	public void setFinishTotal(Long finishTotal) {
		this.finishTotal = finishTotal;
	}


	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getCreateDate() {
		return createDate;
	}



	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getUpdateDate() {
		return updateDate;
	}



	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}

