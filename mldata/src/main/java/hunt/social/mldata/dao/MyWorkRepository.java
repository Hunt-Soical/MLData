package hunt.social.mldata.dao;

import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import hunt.social.mldata.domain.MyWork;


@EntityScan
public interface MyWorkRepository extends MongoRepository<MyWork, String> {

    public MyWork findById(String id);
    public MyWork findByUserIdAndWorkId(String userId, String workId);
    public List<MyWork> findByUserId(String userId, Pageable page);
    public Long deleteByUserIdAndWorkId(String userId, String workId);
}
