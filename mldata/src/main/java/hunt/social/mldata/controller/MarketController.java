package hunt.social.mldata.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import hunt.social.mldata.dao.MarketRepository;
import hunt.social.mldata.domain.Market;
import hunt.social.mldata.domain.MyWork;
import hunt.social.mldata.entity.MarketEntity;
import hunt.social.mldata.entity.MyWorkEntity;

@Controller
public class MarketController {

	@Autowired
	private MarketRepository repository;
	
	public static DateFormat df = new SimpleDateFormat("yyyyMMdd");

	@RequestMapping(value="/api/getMarketItem", method = RequestMethod.GET)
	public ResponseEntity<?> getMarketItem(@RequestParam("count") int count, @RequestParam("page") int page) {

		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "createDate"));

		Pageable pageable = new PageRequest(page, count, sort);
		Page<Market> marketItems = repository.findAll(pageable);

		Map<String, Object> res = new HashMap<String, Object>();
		res.put("result", marketItems.getContent());
		return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);

	}

	@PostMapping("/api/createMarket")
	public ResponseEntity<?> createMarket(@RequestBody MarketEntity marketEntity) {
 		
		if (marketEntity.getDescription() != null && marketEntity.getExpirationDate() != null
				&& marketEntity.getReward() != null && marketEntity.getTotal() != null && marketEntity.getCompany() != null && marketEntity.getAbbr() != null) {
			try {
				Market market = new Market();
				market.setCreateDate(new Date());
				market.setDescription(marketEntity.getDescription());
				market.setExpirationDate(df.parse(marketEntity.getExpirationDate()));
				market.setKeywords(marketEntity.getKeywords());
				market.setReward(marketEntity.getReward());
				market.setTotal(marketEntity.getTotal());
				market.setAbbr(marketEntity.getAbbr());
				market.setCompany(marketEntity.getCompany());
				market.setBatchNumber(marketEntity.getBatchNumber());
				market.setCancelAPI(marketEntity.getCancelAPI());
				market.setGetBatchAPI(marketEntity.getGetBatchAPI());
				market.setLabels(marketEntity.getLabels());
				market.setType(marketEntity.getType());
				market.setUpdateAPI(marketEntity.getUpdateAPI());
				market.setCompanyCode(marketEntity.getCompanyCode());

				Market newMarket = repository.save(market);
				return new ResponseEntity<Object>(newMarket, new HttpHeaders(), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<String>("save failed", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<String>("Missing value", new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/api/updateMarket")
	public ResponseEntity<?> updateMarket(@RequestBody MarketEntity marketEntity) {
		if (marketEntity.getId() != null) {
			try {
				Market market = repository.findById(marketEntity.getId());
				
				if(market == null){
					return new ResponseEntity<String>("the work isn't exist", new HttpHeaders(), HttpStatus.BAD_REQUEST);
				}

				if(marketEntity.getGetBatchAPI() != null){
					market.setGetBatchAPI(marketEntity.getGetBatchAPI());
				}
				
				if(marketEntity.getAbbr() != null){
					market.setAbbr(marketEntity.getAbbr());
				}
				
				if(marketEntity.getBatchNumber() != null){
					market.setBatchNumber(marketEntity.getBatchNumber());
				}
				
				if(marketEntity.getCancelAPI() != null){
					market.setCancelAPI(marketEntity.getCancelAPI());
				}
				
				if(marketEntity.getCompany() != null){
					market.setCompany(marketEntity.getCompany());
				}
				
				if(marketEntity.getCompanyCode() != null){
					market.setCompanyCode(marketEntity.getCompanyCode());
				}
				
				if(marketEntity.getDescription() != null){
					market.setDescription(marketEntity.getDescription());
				}
				
				if(marketEntity.getExpirationDate() != null){
					market.setExpirationDate(df.parse(marketEntity.getExpirationDate()));
				}
				
				if(marketEntity.getKeywords() != null){
					market.setKeywords(marketEntity.getKeywords());
				}
				
				if(marketEntity.getLabels() != null){
					market.setLabels(marketEntity.getLabels());
				}
				
				if(marketEntity.getReward() != null){
					market.setReward(marketEntity.getReward());
				}
				
				if(marketEntity.getTotal() != null){
					market.setTotal(marketEntity.getTotal());
				}
				
				if(marketEntity.getType() != null){
					market.setType(marketEntity.getType());
				}
				
				if(marketEntity.getUpdateAPI() != null){
					market.setUpdateAPI(marketEntity.getUpdateAPI());
				}
				

				Market newMarket = repository.save(market);
				return new ResponseEntity<Object>(newMarket, new HttpHeaders(), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<String>("save failed", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<String>("Missing value", new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}
	

	@RequestMapping(value = "/api/deleteMarket/{workId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMyWork(@PathVariable("workId") String workId) {
			try {
				repository.deleteById(workId);
				return new ResponseEntity<String>("Success", new HttpHeaders(), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<String>("save failed", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
