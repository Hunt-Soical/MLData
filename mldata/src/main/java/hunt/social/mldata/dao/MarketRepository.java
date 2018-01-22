package hunt.social.mldata.dao;

import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.repository.MongoRepository;

import hunt.social.mldata.domain.Market;

@EntityScan
public interface MarketRepository extends MongoRepository<Market, String> {

    public Market findById(String id);
    public List<Market> findByIdIn(List<String> ids);
    public Long deleteById(String id);
}
