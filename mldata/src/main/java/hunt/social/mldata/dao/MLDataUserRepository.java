package hunt.social.mldata.dao;

import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.repository.MongoRepository;

import hunt.social.mldata.domain.MLDataUser;

@EntityScan
public interface MLDataUserRepository extends MongoRepository<MLDataUser, String> {

    public MLDataUser findById(String id);
    public List<MLDataUser> findByUsername(String username);
    public MLDataUser findByUsernameAndPassword(String username, String password);
    public MLDataUser findByThirdPartyID(String tpi);
}
