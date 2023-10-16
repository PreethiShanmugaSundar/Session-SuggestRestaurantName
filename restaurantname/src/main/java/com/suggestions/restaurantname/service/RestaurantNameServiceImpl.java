package com.suggestions.restaurantname.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.suggestions.restaurantname.model.RestaurantNameSuggestion;
import com.suggestions.restaurantname.model.Session;
import com.suggestions.restaurantname.model.User;
import com.suggestions.restaurantname.repository.RestaurantNameSuggestionRepository;
import com.suggestions.restaurantname.repository.SessionRepository;
import com.suggestions.restaurantname.repository.UserRepository;

@Service
public class RestaurantNameServiceImpl implements RestaurantNameService{
	
	@Autowired
	SessionRepository sessionRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RestaurantNameSuggestionRepository restaurantNameSuggestionRepo;
	
	/**
	 * This method check current date time with session expired date time,
	 * return true only if current date time is equals or after session expiry date time 
	 */
	@Override
	public boolean checkSessionExpired(int sessionId) {
		boolean isSessionExpired = false;
		Optional<Session> objOptioanlSession =  sessionRepo.findById(Long.valueOf(sessionId));

		if(!ObjectUtils.isEmpty(objOptioanlSession)){
			System.out.println("end::"+objOptioanlSession.get().getSessionEndDate()+"::start::"+objOptioanlSession.get().getSessionStartDate());
			if(objOptioanlSession.get().getSessionEndDate().equals(objOptioanlSession.get().getSessionStartDate()) 
					|| objOptioanlSession.get().getSessionEndDate().isBefore(objOptioanlSession.get().getSessionStartDate())) {
				System.out.println("SESSION EXPIRED");
				isSessionExpired = true;
			}
		}
		return isSessionExpired;
	}

	@Override
	public Session saveSessionDetails(Session session) {
		Session objSession =  sessionRepo.save(session);
		//Save participants details in restaurant table
		List<RestaurantNameSuggestion> participantList = getParticipantList(session);
		System.out.println("pariticpantList size ::"+participantList.size());
		restaurantNameSuggestionRepo.saveAll(participantList);
		
		return objSession;
	}

	@Override
	public String validateSessionDetails(Session session) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); 
		if(!ObjectUtils.isEmpty(session.getCreatedBy())) {
			List<User> userList = userRepo.findByEmpNo(Integer.valueOf(session.getCreatedBy()));
			System.out.println("isUserExist::"+userList.size());
			if(userList.size() == 0) {
				return "Please enter valid Emp No";
			}
		}
		if(!ObjectUtils.isEmpty(session.getSessionStartDate())) {
			 
			LocalDateTime currentDateTime = LocalDateTime.now();
			if(currentDateTime.isAfter(session.getSessionStartDate())) {
				return "Session Start Date/time should not be past date/time";
			}
		}
		if(!ObjectUtils.isEmpty(session.getSessionEndDate())) {
			LocalDateTime currentDateTime = LocalDateTime.now();
			if(currentDateTime.isAfter(session.getSessionEndDate())) {
				return "Session End date/time should not be past date/time";
			}else if(session.getSessionStartDate().isAfter(session.getSessionEndDate())) {
				return "Session End date/time should not be before start date/time";
			}
		}
		if(!ObjectUtils.isEmpty(session.getParticipantNames())) {
			List<String> employeeNoList = new ArrayList<>();
			if(session.getParticipantNames().contains(";")) {
				String[] participantEmpNoArr = session.getParticipantNames().split(";");
				System.out.println("participantEmpNoArr::"+participantEmpNoArr.toString());
				
				employeeNoList = Arrays.asList(participantEmpNoArr);
			}else {
				employeeNoList.add(session.getParticipantNames());
			}
			if(!CollectionUtils.isEmpty(employeeNoList)) {
				for(String empNo : employeeNoList) {
					System.out.println("Inside for empNo::"+empNo);
					List<User> userList = userRepo.findByEmpNo(Integer.valueOf(empNo));
					System.out.println("isUserExist participant::"+userList.size());
					if(userList.size() == 0) {
						return "Emp No : "+empNo+" doesn't exist";
					}
				}
			}
			session.setEmployeeNoList(employeeNoList);
		}
		
		return "";
	}

	@Override
	public String validateEmployeeNo(RestaurantNameSuggestion restaurantNameSuggestion) {
		String errMsg = "";
		if(restaurantNameSuggestion.getEmpNo() > 0) {
			List<User> userList = userRepo.findByEmpNo(restaurantNameSuggestion.getEmpNo());
			System.out.println("isUserExist::"+userList.size());
			if(userList.size() == 0) {
				errMsg = "Please enter valid employee number";
			}else {
				System.out.println("isUserExist::session id :"+restaurantNameSuggestion.getSessionId());
				RestaurantNameSuggestion  restaurantNameSuggestionFromDB = restaurantNameSuggestionRepo.findBySessionIdAndEmpNo(restaurantNameSuggestion.getSessionId(),restaurantNameSuggestion.getEmpNo());;
				
				if(ObjectUtils.isEmpty(restaurantNameSuggestionFromDB)) {
					errMsg = "Employee Number : "+restaurantNameSuggestion.getEmpNo()+" not authorised for this session"; 
				}else {
					System.out.println("is admin::"+restaurantNameSuggestionFromDB.isAdmin());
					restaurantNameSuggestion.setAdmin(restaurantNameSuggestionFromDB.isAdmin());
				}
			}
			
		}else {
			errMsg = "Please enter valid employee number";
		}
		return errMsg;
	}
	
	private List<RestaurantNameSuggestion> getParticipantList(Session session){
		List<RestaurantNameSuggestion> participantList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(session.getEmployeeNoList())) {	
			for(String empNo : session.getEmployeeNoList()) {
				RestaurantNameSuggestion objSuggestion = new RestaurantNameSuggestion();
				objSuggestion.setEmpNo(Integer.valueOf(empNo));
				objSuggestion.setSessionId(session.getSessionId());
				objSuggestion.setAdmin(false);
				System.out.println("inside getParticipantList::empNo::"+empNo+"::sessionId::"+session.getSessionId());
				if(!participantList.contains(objSuggestion)) {
					participantList.add(objSuggestion);
				}
			}	
		}
		RestaurantNameSuggestion objSuggestion = new RestaurantNameSuggestion();
		objSuggestion.setEmpNo(Integer.valueOf(session.getCreatedBy()));
		objSuggestion.setAdmin(true);
		objSuggestion.setSessionId(session.getSessionId());
		System.out.println("inside getParticipantList::empNo::"+session.getCreatedBy()+"::sessionId::"+session.getSessionId());
		if(!participantList.contains(objSuggestion)) {
			participantList.add(objSuggestion);
		}
		return participantList;
	}

	@Override
	public List<RestaurantNameSuggestion> findParticipantList(int sessionId) {
		return restaurantNameSuggestionRepo.findBySessionId(sessionId);
	}

	@Override
	public void saveRestaurantNameSuggestion(RestaurantNameSuggestion restaurantNameSuggestion) {
		System.out.println("inside saveRestaurantNameSuggestion::empNo::"+restaurantNameSuggestion.getEmpNo()+"::sessionId::"+restaurantNameSuggestion.getSessionId());
		RestaurantNameSuggestion  restaurantNameSuggestionFromDB = restaurantNameSuggestionRepo.findBySessionIdAndEmpNo(restaurantNameSuggestion.getSessionId(),restaurantNameSuggestion.getEmpNo());
		restaurantNameSuggestionFromDB.setRestaurantName(restaurantNameSuggestion.getRestaurantName());
		System.out.println("inside saveRestaurantNameSuggestion::RestaurantName"+restaurantNameSuggestionFromDB.getRestaurantName());
		restaurantNameSuggestion.setAdmin(restaurantNameSuggestionFromDB.isAdmin());
		restaurantNameSuggestionRepo.save(restaurantNameSuggestionFromDB);
	}

	@Override
	public String endSession(RestaurantNameSuggestion restaurantNameSuggestion) {
		String restaurantName = "";
		Random rndm = new Random();
		System.out.println("inside endSession:sessionId::"+restaurantNameSuggestion.getSessionId());
		
		List<RestaurantNameSuggestion> restaurantNameList = restaurantNameSuggestionRepo.findRestaurantNameBySessionId(restaurantNameSuggestion.getSessionId());
		System.out.println("inside endSession:restaurantNameList::"+restaurantNameList.size());
		List<String> nameList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(restaurantNameList)) {
			for(RestaurantNameSuggestion name : restaurantNameList) {
				if(!ObjectUtils.isEmpty(name.getRestaurantName())) {
					nameList.add(name.getRestaurantName());
				}
			}
		}
		restaurantName = nameList.get(rndm.nextInt(nameList.size()));
		System.out.println("inside endSession:restaurantName::"+restaurantName);
		
		//update session end date and restaurant name
		Optional<Session> session = sessionRepo.findById(Long.valueOf(restaurantNameSuggestion.getSessionId()));
		if(!session.isEmpty()) {
			session.get().setSessionEndDate(LocalDateTime.now());
			session.get().setRestaurantNameChosen(restaurantName);
			sessionRepo.save(session.get());
		}
		
		return restaurantName;
	}
}
