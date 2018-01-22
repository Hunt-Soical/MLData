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
import hunt.social.mldata.dao.MyWorkRepository;
import hunt.social.mldata.domain.Market;
import hunt.social.mldata.domain.MyWork;
import hunt.social.mldata.entity.MarketEntity;
import hunt.social.mldata.entity.MyWorkEntity;

@Controller
public class MyWorkController {

	@Autowired
	private MyWorkRepository myWorkRepository;
	
	@Autowired
	private MarketRepository marketRepository;
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@RequestMapping(value="/api/getMyWorkItems", method = RequestMethod.GET)
	public ResponseEntity<?> getMyWorkItems(@RequestParam("count") int count, @RequestParam("page") int page, @RequestParam("userId") String userId) {
		
		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "updateDate"));

		Pageable pageable = new PageRequest(page, count, sort);
		List<MyWork> marketItems = myWorkRepository.findByUserId(userId, pageable);
		
		List<String> workIds = new ArrayList<String>();
		for(MyWork mw : marketItems){
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

	@PostMapping("/api/createMyWork")
	public ResponseEntity<?> createMyWork(@RequestBody MyWorkEntity myWorkEntity) {
		if (myWorkEntity.getUserId() != null && myWorkEntity.getWorkId() != null) {
			try {
				MyWork checkwork = myWorkRepository.findByUserIdAndWorkId(myWorkEntity.getUserId(), myWorkEntity.getWorkId());
				if(checkwork != null ){
					return new ResponseEntity<String>("This work is exist", new HttpHeaders(), HttpStatus.BAD_REQUEST);
				}
				
				MyWork mywork = new MyWork();
				mywork.setCreateDate(new Date());
				mywork.setUpdateDate(new Date());
				mywork.setFinishTotal(0l);
				mywork.setUserId(myWorkEntity.getUserId());
				mywork.setWorkId(myWorkEntity.getWorkId());

				MyWork newMyWork = myWorkRepository.save(mywork);
				return new ResponseEntity<Object>(newMyWork, new HttpHeaders(), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<String>("save failed", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<String>("Missing value", new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/api/updateMyWork")
	public ResponseEntity<?> updateMyWork(@RequestBody MyWorkEntity myWorkEntity) {
		if (myWorkEntity.getUserId() != null && myWorkEntity.getWorkId() != null) {
			try {
				MyWork mywork = myWorkRepository.findByUserIdAndWorkId(myWorkEntity.getUserId(), myWorkEntity.getWorkId());
				
				if(mywork == null){
					return new ResponseEntity<String>("the work isn't exist", new HttpHeaders(), HttpStatus.BAD_REQUEST);
				}

				mywork.setFinishTotal(myWorkEntity.getFinishTotal());
				mywork.setUpdateDate(new Date());

				MyWork newMyWork = myWorkRepository.save(mywork);
				return new ResponseEntity<Object>(newMyWork, new HttpHeaders(), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<String>("save failed", new HttpHeaders(), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<String>("Missing value", new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
	}
	

	@RequestMapping(value = "/api/deleteMyWork/{userId}/{workId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMyWork(@PathVariable("userId") String userId, @PathVariable("workId") String workId) {
			try {
				myWorkRepository.deleteByUserIdAndWorkId(userId, workId);
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
