package com.teaminology.hp.web.spring;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.teaminology.hp.dao.exception.ApplicationException;
import com.teaminology.hp.data.TermInformationDetails;
import com.teaminology.hp.service.ITermsService;
import com.teaminology.hp.web.utils.PathConstants;
import com.teaminology.hp.web.utils.TermValidator;

@Controller
@RequestMapping(value = "/")
public class TermSearchController {

	private static final Logger logger = Logger
			.getLogger(TermSearchController.class);
	@Autowired
	private ITermsService termServices;
	

	@RequestMapping(value = "/{version}" + "/terms" + "/{termId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> getTerms(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable Integer termId,
			@PathVariable String version,
			@RequestParam(value = "source", required = false) String sourceLang,
			@RequestParam(value = "target", required = false) String targetLang,
			@RequestHeader("Auth-Token") String token)
					throws TermException, ApplicationException {

		logger.info("entered into  getTerms API ");
		logger.info("Request object"+request);
		ResponseEntity<Object> responseEntity = null;
		TermInformationDetails termInformationDetails = null;
		logger.info("validate token: "+token);
		
			// validate Token
			if (token == null || token.trim().isEmpty()) {
				throw new TermException(
						"Invalid Token,Please send valid token"); 		
			}
			
			if(!termServices.validateToken(token)){ 						 
				throw new TermException(
							"Invalid Token,Please send valid token"); 						 
			 }
			logger.info("valid token");
			if (!version.equals(PathConstants.version)) {
				responseEntity = new ResponseEntity<Object>(
						HttpStatus.NOT_FOUND);
			} else {
				String sourceLangerrorMessage ="";
				String targetLangErrorMessage = "";
				/*String sourceLangerrorMessage = TermValidator
						.isFieldNullOrEmpty("sourceLang", sourceLang);*/
				/*
				 * if (!sourceLangerrorMessage.equals("")) {
				 * 
				 * throw new TermException(
				 * "sourceLang cannot be empty or null"); }
				 * targetLangErrorMessage = TermValidator
				 * .isFieldNullOrEmpty("targetLang", targetLang); if
				 * (!targetLangErrorMessage.equals("")) { throw new
				 * TermException( "targetLang cannot be empty or null");
				 * }
				 */
	
				if (sourceLang != null){
					if(!sourceLang.trim().isEmpty()) {
					sourceLangerrorMessage = TermValidator
							.isAlphaCharacters("sourceLang", sourceLang);
					if (!sourceLangerrorMessage.equals("")) {
						throw new TermException(sourceLangerrorMessage);
					}
				}
				}
				if (targetLang != null) {
					if(!targetLang.trim().isEmpty()){
						targetLangErrorMessage = TermValidator
								.isAlphaCharacters("targetLang", targetLang);
						if (!targetLangErrorMessage.equals("")) {
							throw new TermException(targetLangErrorMessage);
						}
					}
				}
	
				if (sourceLang != null && targetLang != null
						&& !sourceLang.trim().isEmpty()
						&& !targetLang.trim().isEmpty()
						&& responseEntity == null) {
					termInformationDetails = termServices.searchTermDetails(
							termId, sourceLang, targetLang);
	                logger.debug(termInformationDetails); 
					responseEntity = new ResponseEntity<Object>(
							termInformationDetails, HttpStatus.OK);
				} else if (targetLang != null && !targetLang.equals("")) {
					if (sourceLang == null || sourceLang.equals("")) {
						throw new TermException(
								"source must be provided for the given target");
					}
				} else if (sourceLang != null
						&& !sourceLang.trim().isEmpty()) {
					termInformationDetails = termServices.searchTermDetails(
							termId, sourceLang, targetLang);
					logger.debug(termInformationDetails); 
					responseEntity = new ResponseEntity<Object>(
							termInformationDetails, HttpStatus.OK);
				}
				if (termId != null && sourceLang == null
						&& targetLang == null) {
					termInformationDetails = termServices.searchTermDetails(
							termId, sourceLang, targetLang);
					logger.debug(termInformationDetails); 
					responseEntity = new ResponseEntity<Object>(
							termInformationDetails, HttpStatus.OK);
	
				}
			}
					  
				
			 
		logger.info("exit from  getTerms API ");
		return responseEntity;
	}

	@ExceptionHandler(TermException.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(TermException ex) {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.BAD_REQUEST.value());
		error.setStatusMsg(ex.getErrorMessage());
		logger.debug(error);
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/{version}" + "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<AuthCredentials> validateUser(
			HttpServletRequest request, ModelMap map) {
		logger.info("entered into validateUser API");
		logger.info("Request object"+request);
		String token = null;
		AuthCredentials authCredentials = null;
		ResponseEntity<AuthCredentials> responseToken = null;
		SecurityContext contextHolder = SecurityContextHolder.getContext();
		logger.info("context object : "+contextHolder);
		Authentication authentication = contextHolder.getAuthentication();
		logger.info("authentication object : "+authentication);
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			logger.info("principal object : "+principal);
			logger.info("User object cast : "+(User) principal);
			String username = ((User) principal).getUsername();
			if (username != null) {
				token = UUID.randomUUID().toString();
				Boolean bool = termServices.insertAuthToken(token);
				logger.info("token status : "+ bool);
				authCredentials = new AuthCredentials();
				authCredentials.setUserName(username);
				authCredentials.setCompanyName("demo");
				authCredentials.setAuthToken(token);
				map.addAttribute("authCredentials", authCredentials);
				logger.debug("map contains token related info : "+map);
			}
			responseToken = new ResponseEntity<AuthCredentials>(
					authCredentials, HttpStatus.OK);
			logger.debug("responseToken : "+responseToken);
		}
		logger.info("exit from validateUser API");
		return responseToken;

	}

	/*
	 * @RequestMapping(value = "/{version}" + "/terms", method =
	 * RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	 * 
	 * @ResponseBody public ResponseEntity<Object> getAllTerms(@PathVariable
	 * String version,
	 * 
	 * @RequestParam(value = "limit", required = false) Integer limit,
	 * 
	 * @RequestParam(value = "offset", required = false) Integer offSet,
	 * 
	 * @RequestParam(value = "source", required = true) String sourceLang,
	 * 
	 * @RequestParam(value = "target", required = true) String targetLang,
	 * 
	 * @RequestParam(value = "latestChange", required = false) String
	 * lastChange,
	 * 
	 * @RequestHeader("Auth-Token") String token,
	 * 
	 * @ModelAttribute("authCredentials") AuthCredentials authCredentials)
	 * throws ApplicationException,TermException { ResponseEntity<Object>
	 * responseEntity = null; List<TermInformation> termInformations=null; if
	 * (!"".equals(token)) { System.out.println(authCredentials.getAuthToken());
	 * if (authCredentials != null) { if
	 * (token.equals(authCredentials.getAuthToken())) { if
	 * (!version.equals(PathConstants.version)) { responseEntity = new
	 * ResponseEntity<Object>( HttpStatus.NOT_FOUND); } else { String
	 * sourceLangerrorMessage = TermValidator .isFieldNullOrEmpty("source",
	 * sourceLang); String targetLangErrorMessage = ""; if
	 * (!sourceLangerrorMessage.equals("")) {
	 * 
	 * throw new TermException( "source cannot be empty or null"); }
	 * targetLangErrorMessage = TermValidator .isFieldNullOrEmpty("target",
	 * targetLang); if (!targetLangErrorMessage.equals("")) { throw new
	 * TermException( "target cannot be empty or null"); }
	 * 
	 * if (!sourceLang.equals("")) { sourceLangerrorMessage = TermValidator
	 * .isAlphaCharacters("source", sourceLang); if
	 * (!sourceLangerrorMessage.equals("")) { throw new
	 * TermException(sourceLangerrorMessage); } } if (!targetLang.equals("")) {
	 * targetLangErrorMessage = TermValidator .isAlphaCharacters("target",
	 * targetLang); if (!targetLangErrorMessage.equals("")) { throw new
	 * TermException(targetLangErrorMessage); } }
	 * 
	 * if (!sourceLang.equals("") && !targetLang.equals("") && responseEntity ==
	 * null) { if(limit==null && offSet==null){ limit=10; offSet=0; }
	 * termInformations
	 * =termServices.getAllTerms(limit,lastChange,offSet,sourceLang,targetLang);
	 * 
	 * responseEntity = new ResponseEntity<Object>( termInformations,
	 * HttpStatus.OK); } }
	 * 
	 * 
	 * }else { throw new TermException("Invalid Token,Please send valid token");
	 * } } }else { throw new TermException("Token not available"); } return
	 * responseEntity; }
	 */
	@RequestMapping(value = "/{version}" + "/terms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> getAllTermsDetails(
			@PathVariable String version,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "offset", required = false) Integer offSet,
			@RequestParam(value = "source", required = false) String sourceLang,
			@RequestParam(value = "target", required = false) String targetLang,
			@RequestParam(value = "latestChange", required = false) String lastChange,
			@RequestHeader("Auth-Token") String token)
					throws ApplicationException, TermException {
		logger.info("entered into getAllTermsDetails");
		logger.info("Request object");
		ResponseEntity<Object> responseEntity = null;
		List<TermInformationDetails> termInformationDetailsList = null;
		
		// validate Token
		logger.info("validate token: "+token);
		if (token == null || token.trim().isEmpty()) {
			throw new TermException(
					"Invalid Token,Please send valid token"); 		
		}
		
		if(!termServices.validateToken(token)){ 						 
			throw new TermException(
						"Invalid Token,Please send valid token"); 						 
		 }
		logger.info("valid token");
		
		
		if (!version.equals(PathConstants.version)) {
			responseEntity = new ResponseEntity<Object>(
					HttpStatus.NOT_FOUND);
		} else {	 
			String sourceLangerrorMessage ="";
			String targetLangErrorMessage = "";
			/*
			 * String sourceLangerrorMessage = TermValidator
			 * .isFieldNullOrEmpty("source", sourceLang); String
			 * targetLangErrorMessage = "";;
			 */
			if (sourceLang != null) {
				if(!sourceLang.trim().isEmpty()){
				sourceLangerrorMessage = TermValidator
						.isAlphaCharacters("sourceLang", sourceLang);
				if (!sourceLangerrorMessage.equals("")) {
					throw new TermException(sourceLangerrorMessage);
				}
				}
			}
			if (targetLang != null) {
				if(!targetLang.trim().isEmpty()){
					targetLangErrorMessage = TermValidator
							.isAlphaCharacters("targetLang", targetLang);
					if (!targetLangErrorMessage.equals("")) {
						throw new TermException(targetLangErrorMessage);
					}
				}
			}
			if (limit == null && offSet == null) {
				limit = 10;
				offSet = 0;
			} else if (limit != 0) {
				if (offSet == null || offSet < 0)
					offSet = 0;
			}
			if (responseEntity == null) {
				if (targetLang != null && !targetLang.equals("")) {
					if (sourceLang == null || sourceLang.equals("")) {
						throw new TermException(
								"Source must be provided for the given Target");
					}
				}
				termInformationDetailsList = termServices.getAllTerms(limit,
						lastChange, offSet, sourceLang, targetLang);
	            logger.debug("termInformationResponse data : "+termInformationDetailsList);
				responseEntity = new ResponseEntity<Object>(
						termInformationDetailsList, HttpStatus.OK);
	
			}	
		}
		logger.info("exit from getAllTermsDetails");
		return responseEntity;
	}

}