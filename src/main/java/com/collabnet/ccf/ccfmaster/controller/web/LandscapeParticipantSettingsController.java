package com.collabnet.ccf.ccfmaster.controller.web;



import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContext;

import com.collabnet.ccf.ccfmaster.server.core.QCMetaDataProvider;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.Timezone;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.helper.LandscapeParticipantSettingsHelper;
import com.collabnet.ccf.ccfmaster.web.model.ParticipantSettingsModel;
import com.collabnet.ccf.ccfmaster.web.validator.ParticipantSettingsValidator;
import com.danube.scrumworks.api2.client.Product;
import com.danube.scrumworks.api2.client.ScrumWorksAPIService;



@RequestMapping("/admin/**")
@Controller
public class LandscapeParticipantSettingsController extends AbstractLandscapeController{

	private static final String SCRUM_WORKS_API_BEAN_SERVICE = "ScrumWorksAPIBeanService";
	private static final String SCRUMWORKS_NAMESPACE = "http://api2.scrumworks.danube.com/";
	private static final String RESTART = "restart";
	private static final Logger log = LoggerFactory.getLogger(LandscapeParticipantSettingsController.class);
	LandscapeParticipantSettingsHelper landscapeParticipantSettingsHelper=new LandscapeParticipantSettingsHelper();
	
	private @Autowired
	QCMetaDataProvider qcMetaDataProvider;
	
	private ScrumWorksAPIService endpoint;
	
	/**
	 * Controller method to display participant settings 
	 * 
	 */ 
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTSETTINGS, method = RequestMethod.GET)
	public String displayParticipantSettings(Model model, HttpServletRequest request) {
		ParticipantSettingsModel participantSettingsModel=new ParticipantSettingsModel();
		Landscape landscape=ControllerHelper.findLandscape();
		Participant participant=landscape.getParticipant();
		landscapeParticipantSettingsHelper.populateParticipantSettingsModel(participantSettingsModel,model);
		landscapeParticipantSettingsHelper.makeModel(model, participantSettingsModel, landscape, participant);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTSETTINGS;
	}



	/**
	 * Controller method to save participant settings 
	 * 
	 */ 
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_SAVEPARTICIPANTSETTINGS, method = RequestMethod.POST)
	@Transactional
	public String saveParticipantSettings(@ModelAttribute("qcsettingsmodel") @Valid ParticipantSettingsModel participantSettingsModel,BindingResult bindingResult,Model model, HttpServletRequest request) {
		//validate participantSettingsModel
		ParticipantSettingsValidator participantSettingsValidator=new ParticipantSettingsValidator();	
		participantSettingsValidator.validate(participantSettingsModel, bindingResult);
		if (bindingResult.hasErrors()) {
			landscapeParticipantSettingsHelper.populateParticipantSettingsModel(participantSettingsModel,model);
			Landscape landscape=ControllerHelper.findLandscape();
			Participant participant=landscape.getParticipant();
			landscapeParticipantSettingsHelper.makeModel(model, participantSettingsModel, landscape, participant);
			return  UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTSETTINGS;
		} 
		else{
			try{  
				landscapeParticipantSettingsHelper.updateParticipantSettings(participantSettingsModel, model, request); 
				boolean hasRestart = Boolean.parseBoolean(request.getParameter(RESTART));
				if(hasRestart){
					LandscapeStatusController.restartCCFCoreStatus(model);
					FlashMap.setSuccessMessage(ControllerConstants.PARTICIPANT_RESTART_SUCCESS_MESSAGE);
				}
			}
			catch(Exception exception){
				log.debug("Error saving participant settings: " + exception.getMessage(), exception);
				FlashMap.setErrorMessage(ControllerConstants.PARTICIPANT_SAVE_FAIL_MESSAGE, exception.getMessage());
			}
			model.asMap().clear();
			return "redirect:/" +UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTSETTINGS;
		} 
	}
	

	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_QC_TEST_CONNECTION, method = RequestMethod.POST)
	public @ResponseBody String  testQCConnection(@RequestParam("url") String url,@RequestParam("username") String username,@RequestParam("password") String password , HttpServletRequest request) {
		RequestContext ctx = new RequestContext(request);
		String returnText;
		Landscape landscape=ControllerHelper.findLandscape();
		String connectionResult=qcMetaDataProvider.showVisibleDomains(landscape, url, username,
					password);
		if(connectionResult.contains("<success>")){
			returnText="<strong><font color='green'>"+ ctx.getMessage(ControllerConstants.QC_CONNECTION_SUCCESS_MESSAGE)+"</font></strong>";
		}
		else{
			returnText="<strong><font color='red'>"+ ctx.getMessage(ControllerConstants.QC_CONNECTION_FAILURE_MESSAGE)+connectionResult + "</font></strong>";
		}
		return returnText;
	}

	
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_SWP_TEST_CONNECTION, method = RequestMethod.POST)
	public @ResponseBody String  testSWPConnection(@RequestParam("url") String url, Model model, HttpServletRequest request) {
		RequestContext ctx = new RequestContext(request);
		String returnText;
		try{
		Service service = Service.create(new URL(url), new QName(
				SCRUMWORKS_NAMESPACE,
				SCRUM_WORKS_API_BEAN_SERVICE));
		endpoint = service.getPort(ScrumWorksAPIService.class);
		 String resync=request.getParameter("resync");
		if("true".equals(resync)){
			String resyncUserName=request.getParameter("resyncusername");
			String resyncPassword=request.getParameter("resyncpassword");
			setUserNameAndPassword(resyncUserName, resyncPassword);
		}
		else{
			String username=request.getParameter("username");
			String password=request.getParameter("password");
			setUserNameAndPassword(username, password);	
		}
		List<Product> product=endpoint.getProducts();
		if(product.isEmpty()){
			returnText="<strong><font color='red'> "+ctx.getMessage(ControllerConstants.SWP_CONNECTION_FAILURE_MESSAGE)+ "</font></strong>";
		}
		else{
			returnText="<strong><font color='green'>"+ctx.getMessage(ControllerConstants.SWP_CONNECTION_SUCCESS_MESSAGE)+ "</font></strong>";
		}
		}
		catch(Exception exception){
			returnText="<strong><font color='red'>"+ctx.getMessage(ControllerConstants.SWP_CONNECTION_FAILURE_MESSAGE)+exception.getMessage() + "</font></strong>";
		}
		return returnText;
	}
	
	/**
	 * 
	 * @param userName
	 * @param password
	 */
	private void setUserNameAndPassword(String userName, String password) {
		final BindingProvider bindingProvider = (BindingProvider) endpoint;
		Map<String, Object> requestContext = bindingProvider
				.getRequestContext();
		requestContext.put(BindingProvider.USERNAME_PROPERTY, userName);
		requestContext.put(BindingProvider.PASSWORD_PROPERTY, password);
	}
	
	@ModelAttribute("timezones")
	public java.util.Collection<Timezone> populateTimezones() {
		return Arrays.asList(Timezone.class.getEnumConstants());
	}

}
