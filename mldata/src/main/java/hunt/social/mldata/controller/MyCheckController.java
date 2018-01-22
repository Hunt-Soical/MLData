package hunt.social.mldata.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import hunt.social.mldata.dao.MarketRepository;
import hunt.social.mldata.dao.MyCheckRepository;
import hunt.social.mldata.dao.MyWorkRepository;
import hunt.social.mldata.domain.Market;
import hunt.social.mldata.domain.MyCheck;
import hunt.social.mldata.domain.MyWork;
import hunt.social.mldata.entity.MarketEntity;
import hunt.social.mldata.entity.MyWorkEntity;

@Controller
public class MyCheckController {

	@Autowired
	private MyCheckRepository myCheckRepository;
	
	@Autowired
	private MarketRepository marketRepository;
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@RequestMapping(value="/api/getMyCheckItems", method = RequestMethod.GET)
	public ResponseEntity<?> getMyCheckItems(@RequestParam("count") int count, @RequestParam("page") int page, @RequestParam("userId") String userId) {
		
		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "updateDate"));

		Pageable pageable = new PageRequest(page, count, sort);
		List<MyCheck> marketItems = myCheckRepository.findByUserId(userId, pageable);
		
		List<String> workIds = new ArrayList<String>();
		for(MyCheck mw : marketItems){
			workIds.add(mw.getWorkId());
		}
		
		List<Market> markets = marketRepository.findByIdIn(workIds);
		
		List<Map<String, Object>> works = new ArrayList<Map<String, Object>>();
		
		Map<String, Market> marketMap = new HashMap<String, Market>();
		for(int i = 0; i < markets.size(); i++){	
			marketMap.put(markets.get(i).getId(), markets.get(i));
		}
				
		for(int i = 0; i < marketItems.size(); i++){
			Map<String, Object> item = new HashMap<String, Object>();
			
			Market mm = marketMap.get(marketItems.get(i).getWorkId());
			if(mm == null){
				continue;
			}
			item.put("id", mm.getId());
			item.put("description", mm.getDescription());
			item.put("reward", mm.getReward());
			item.put("keywords", mm.getKeywords());
			item.put("total", mm.getTotal());
			item.put("createDate", dateFormat.format(mm.getCreateDate()));
			item.put("expirationDate", dateFormat.format(mm.getExpirationDate()));
			item.put("company", mm.getCompany());
			item.put("companyCode", mm.getCompanyCode());
			item.put("abbr", mm.getAbbr());
			item.put("finishTotal", marketItems.get(i).getFinishTotal());
			item.put("updateDate", dateFormat.format(marketItems.get(i).getUpdateDate()));
			
			item.put("getBatchAPI", mm.getGetBatchAPI());
			item.put("cancelAPI", mm.getCancelAPI());
			item.put("updateAPI", mm.getUpdateAPI());
			item.put("batchNumber", mm.getBatchNumber());
			item.put("labels", mm.getLabels());
			item.put("type", mm.getType());
			
			works.add(item);
		}

		Map<String, Object> res = new HashMap<String, Object>();
		res.put("result", works);
		return new ResponseEntity<Object>(res, new HttpHeaders(), HttpStatus.OK);

	}

	@PostMapping("/api/createMyCheck")
	public ResponseEntity<?> createMyCheck(@RequestBody MyWorkEntity myWorkEntity) {
		if (myWorkEntity.getUserId() != null && myWorkEntity.getWorkId() != null) {
			try {
				MyCheck checkwork = myCheckRepository.findByUserIdAndWorkId(myWorkEntity.getUserId(), myWorkEntity.getWorkId());
				if(checkwork != null ){
					return new ResponseEntity<String>("This check is exist", new HttpHeaders(), HttpStatus.BAD_REQUEST);
				}
				
				MyCheck mycheck = new MyCheck();
				mycheck.setCreateDate(new Date());
				mycheck.setUpdateDate(new Date());
				mycheck.setFinishTotal(0l);
				mycheck.setUserId(myWorkEntity.getUserId());
				mycheck.setWorkId(myWorkEntity.getWorkId());

				MyCheck newMyCheck = myCheckRepository.save(mycheck);
				return new ResponseEntity<Object>(newMyCheck, new HttpHeaders(), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<String>("save failed", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<String>("Missing value", new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/api/updateMyCheck")
	public ResponseEntity<?> updateMyCheck(@RequestBody MyWorkEntity myWorkEntity) {
		if (myWorkEntity.getUserId() != null && myWorkEntity.getWorkId() != null) {
			try {
				MyCheck mycheck = myCheckRepository.findByUserIdAndWorkId(myWorkEntity.getUserId(), myWorkEntity.getWorkId());
				
				if(mycheck == null){
					return new ResponseEntity<String>("the work isn't exist", new HttpHeaders(), HttpStatus.BAD_REQUEST);
				}

				mycheck.setFinishTotal(myWorkEntity.getFinishTotal());
				mycheck.setUpdateDate(new Date());

				MyCheck newMyCheck = myCheckRepository.save(mycheck);
				return new ResponseEntity<Object>(newMyCheck, new HttpHeaders(), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<String>("save failed", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<String>("Missing value", new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}
	

	@RequestMapping(value = "/api/deleteMyCheck/{userId}/{workId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMyWork(@PathVariable("userId") String userId, @PathVariable("workId") String workId) {
			try {
				myCheckRepository.deleteByUserIdAndWorkId(userId, workId);
				return new ResponseEntity<String>("Success", new HttpHeaders(), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<String>("save failed", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
	}
}
