package hunt.social.mldata.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import hunt.social.mldata.dao.MLDataUserRepository;
import hunt.social.mldata.domain.MLDataUser;
import hunt.social.mldata.entity.LoginEntity;
import hunt.social.mldata.entity.RegisterEntity;

@Controller
public class RegisterController {
	
	@Autowired
	private MLDataUserRepository repository;
	
	@PostMapping("/api/register")
    public ResponseEntity<?> register(@RequestBody RegisterEntity registerEntity) {

        //validate login here
		if(registerEntity.getThirdPartyID() != null ){
			MLDataUser user = repository.findByThirdPartyID(registerEntity.getThirdPartyID());
			if(user != null){
				return new ResponseEntity<MLDataUser>(user, new HttpHeaders(), HttpStatus.OK);
			}else{
				if(registerEntity.getUsername() != null){
					MLDataUser newUser = new MLDataUser();
					newUser.setAvator(registerEntity.getAvator());
					newUser.setCreateDate(new Date());
					newUser.setUpdateDate(new Date());
					newUser.setThirdPartyID(registerEntity.getThirdPartyID());
					newUser.setUsername(registerEntity.getUsername());
					if(registerEntity.getPassword() != null){
						newUser.setPassword(DigestUtils.md5Hex(registerEntity.getPassword()));
					}
					
					MLDataUser u = repository.save(newUser);
					return new ResponseEntity<MLDataUser>(u, new HttpHeaders(), HttpStatus.OK);
				}
				
				Map<String, String> res = new HashMap<String, String>();
				return new ResponseEntity("register Failed", new HttpHeaders(), HttpStatus.OK);
			}
			
		}
		
		

        return new ResponseEntity("Register Failed!", new HttpHeaders(), HttpStatus.OK);

    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
