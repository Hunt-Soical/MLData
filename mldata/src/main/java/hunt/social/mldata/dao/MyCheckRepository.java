package hunt.social.mldata.dao;

import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import hunt.social.mldata.domain.MyCheck;



@EntityScan
public interface MyCheckRepository extends MongoRepository<MyCheck, String> {

    public MyCheck findById(String id);
    public MyCheck findByUserIdAndWorkId(String userId, String workId);
    public List<MyCheck> findByUserId(String userId, Pageable page);
    public Long deleteByUserIdAndWorkId(String userId, String workId);
}
