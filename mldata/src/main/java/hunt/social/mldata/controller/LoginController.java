package hunt.social.mldata.controller;

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

@Controller
public class LoginController {
	
	@Autowired
	private MLDataUserRepository repository;
	
	@PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginEntity loginEntity) {

        //validate login here
		if(loginEntity.getThirdPartyID() != null ){
			MLDataUser user = repository.findByThirdPartyID(loginEntity.getThirdPartyID());
			if(user != null){
				return new ResponseEntity<MLDataUser>(user, new HttpHeaders(), HttpStatus.OK);
			}else{
				Map<String, String> res = new HashMap<String, String>();
				return new ResponseEntity("Login Failed", new HttpHeaders(), HttpStatus.OK);
			}
			
		}
		
		if(loginEntity.getUsername() != null && loginEntity.getPassword() != null){
			MLDataUser user = repository.findByUsernameAndPassword(loginEntity.getUsername(), DigestUtils.md5Hex(loginEntity.getPassword()));
			if(user != null){
				return new ResponseEntity<MLDataUser>(user, new HttpHeaders(), HttpStatus.OK);
			}else{
				Map<String, String> res = new HashMap<String, String>();
				return new ResponseEntity("Login Failed", new HttpHeaders(), HttpStatus.OK);
			}
		}

        return new ResponseEntity("Login Failed!", new HttpHeaders(), HttpStatus.OK);

    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
