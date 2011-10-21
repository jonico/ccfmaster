package com.collabnet.ccf.ccfmaster.controller.web;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;


@RequestMapping("/admin/**")
@Controller
public class LandscapeCCFPropertiesController extends AbstractLandscapeController{

	private static final Logger log = LoggerFactory.getLogger(LandscapeCCFPropertiesController.class);
	private static final String PARAM_DIRECTION = "param_direction";
	private static final String RESTART = "restart";

	/**
	 * Controller method to display log template from participant to TF 
	 * 
	 */
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESLOGTEMPLATEPARTTOTF, method = RequestMethod.GET)
	public String displayCCFPropertiesLogTemplateParttoTF(Model model, HttpServletRequest request) {
		populateDirectionConfigModel(model,Directions.REVERSE);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESLOGTEMPLATEPARTTOTF;
	}

	/**
	 * Controller method to update log template 
	 * 
	 */
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_SAVECCFPROPERTIESLOGTEMPLATE, method = RequestMethod.POST)
	public String saveCCFPropertiesLogTemplate(
			@RequestParam(ControllerConstants.DIRECTION_CONFIG_ID) String direction_Config_Id,
			@RequestParam(ControllerConstants.DIRECTION_CONFIG_VERSION) String direction_Config_Version,
			@RequestParam(PARAM_DIRECTION) String paramdirection,
			@ModelAttribute("directionConfig") @Valid DirectionConfig directionConfig,
			BindingResult bindingResult,
			Model model, HttpServletRequest request) {
		Landscape landscape=ControllerHelper.findLandscape(model);
		Directions directions = ControllerConstants.FORWARD.equals(paramdirection) ? Directions.FORWARD : Directions.REVERSE;
		try{
			updateCCFPropertiesLogTemplate(direction_Config_Id,
					direction_Config_Version, directionConfig, landscape,
					directions);
			boolean hasRestart = Boolean.parseBoolean(request.getParameter(RESTART));
			if(hasRestart){
				LandscapeStatusController.restartCCFCoreStatus(model);
				FlashMap.setSuccessMessage(ControllerConstants.LOG_TEMPLATE_RESTART_SUCCESS_MESSAGE);
			}
		}
		catch(Exception exception){
			log.debug(exception.getMessage());
			FlashMap.setErrorMessage(ControllerConstants.LOG_TEMPLATE_SAVE_FAIL_MESSAGE, exception.getMessage());	
		}
		model.asMap().clear();
		if(paramdirection.equals(ControllerConstants.FORWARD)){
			return "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESLOGTEMPLATETFTOPART;
		}else{
			return "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESLOGTEMPLATEPARTTOTF;
		}

	}

	/**
	 * @param direction_Config_Id
	 * @param direction_Config_Version
	 * @param directionConfig
	 * @param landscape
	 * @param directions
	 */
	private void updateCCFPropertiesLogTemplate(String direction_Config_Id,
			String direction_Config_Version, DirectionConfig directionConfig,
			Landscape landscape, Directions directions) {
		Direction direction=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape,directions).getSingleResult();
		directionConfig.setId(Long.valueOf(direction_Config_Id));
		directionConfig.setVersion(Integer.parseInt(direction_Config_Version));
		directionConfig.setDirection(direction);
		directionConfig.merge();
		FlashMap.setSuccessMessage(ControllerConstants.LOG_TEMPLATE_SAVE_SUCCESS_MESSAGE);
	}

	/**
	 * Controller method to display log template from TF to participant  
	 * 
	 */	
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESLOGTEMPLATETFTOPART, method = RequestMethod.GET)
	public String displayCCFPropertiesLogTemplateTFtoPart(Model model, HttpServletRequest request) {
		populateDirectionConfigModel(model,Directions.FORWARD);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESLOGTEMPLATETFTOPART;

	}

	/**
	 * Helper method to populate direction config   
	 * 
	 */	
	public static void populateDirectionConfigModel(Model model, Directions directions){
		Landscape landscape=ControllerHelper.findLandscape(model);
		Direction direction=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, directions).getSingleResult();
		DirectionConfig directionConfig=DirectionConfig.findDirectionConfigsByDirectionAndName(direction,ControllerConstants.CCF_DIRECTION_LOGMESSAGETEMPLATE).getSingleResult();
		model.addAttribute("participant",landscape.getParticipant());
		model.addAttribute("directionConfigversion",directionConfig.getVersion());
		model.addAttribute("directionConfigid",directionConfig.getId());
		model.addAttribute("direction",directionConfig.getDirection());
		model.addAttribute("directionConfig", directionConfig);
		model.addAttribute("landscape",landscape);
		model.addAttribute("selectedLink",ControllerConstants.CCF_PROPERTIES);
	}


	/**
	 * Controller method to display synchronize behavior from participant to TF  
	 * 
	 */	
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCPARTTOTF, method = RequestMethod.GET)
	public String displayCCFPropertiesSyncParttoTF(Model model, HttpServletRequest request) {
		populateDirectionModel(model,Directions.REVERSE);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCPARTTOTF;
	}

	/**
	 * Controller method to save synchronize behavior  
	 * 
	 */	
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_SAVECCFPROPERTIESSYNC, method = RequestMethod.POST)
	public String saveCCFPropertiesSync(@RequestParam(ControllerConstants.DIRECTION_ID) String direction_Id,
			@RequestParam(ControllerConstants.DIRECTION_VERSION) String direction_Version,
			@RequestParam(PARAM_DIRECTION) String paramdirection,
			@ModelAttribute("syncDirection") Direction direction,
			Model model, HttpServletRequest request) {
		Landscape landscape=ControllerHelper.findLandscape(model);
		Directions directions = ControllerConstants.FORWARD.equals(paramdirection) ? Directions.FORWARD : Directions.REVERSE;
		try{
			direction.setId(Long.valueOf(direction_Id));
			direction.setVersion(Integer.parseInt(direction_Version));
			direction.setShouldStartAutomatically(direction.getShouldStartAutomatically());
			direction.setLandscape(landscape);
			direction.merge();
			setSuccessMessage(paramdirection, direction, landscape);
		}
		catch(Exception exception){
			log.debug(exception.getMessage());
			FlashMap.setErrorMessage(ControllerConstants.SYNC_SAVE_FAIL_MESSAGE, exception.getMessage());
		}
		model.asMap().clear();
		if(paramdirection.equals(ControllerConstants.FORWARD)){
			return "redirect:/" +UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCTFTOPART;
		}
		else{
			return "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCPARTTOTF;
		}
	}

	/**
	 * @param paramdirection
	 * @param direction
	 * @param landscape
	 */
	private void setSuccessMessage(String paramdirection, Direction direction,
			Landscape landscape) {
		String messageCode;
		if(direction.getShouldStartAutomatically()){
			if(ControllerConstants.FORWARD.equals(paramdirection)){
				messageCode= SystemKind.QC.equals(landscape.getParticipant().getSystemKind()) ? ControllerConstants.CCF_CORE_TF_TO_QC_START_AUTOMATICALLY_MESSAGE : ControllerConstants.CCF_CORE_TF_TO_SWP_START_AUTOMATICALLY_MESSAGE;
			}
			else{
				messageCode= SystemKind.QC.equals(landscape.getParticipant().getSystemKind()) ? ControllerConstants.CCF_CORE_QC_TO_TF_START_AUTOMATICALLY_MESSAGE : ControllerConstants.CCF_CORE_SWP_TO_TF_START_AUTOMATICALLY_MESSAGE;
			}
			FlashMap.setSuccessMessage(messageCode );
		}
		else{
			FlashMap.setSuccessMessage(ControllerConstants.SYNC_SAVE_SUCCESS_MESSAGE);
		}
	}


	/**
	 * Controller method to display synchronize behavior from TF to participant  
	 * 
	 */	
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCTFTOPART, method = RequestMethod.GET)
	public String displayCCFPropertiesSyncTFtoPart(Model model, HttpServletRequest request) {
		populateDirectionModel(model,Directions.FORWARD);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCTFTOPART;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setAutoGrowNestedPaths(false);
		binder.setDisallowedFields(new String[]{"syncDirection.landscape","landscape"});
	}

	/**
	 * Helper method to populate direction    
	 * 
	 */	
	public static void populateDirectionModel(Model model, Directions directions){
		Landscape landscape=ControllerHelper.findLandscape(model);
		Direction direction=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, directions).getSingleResult();
		model.addAttribute("participant",landscape.getParticipant());
		model.addAttribute("directionversion",direction.getVersion());
		model.addAttribute("directionid",direction.getId());
		model.addAttribute("direction",direction.getDirection());
		model.addAttribute("description",direction.getDescription());
		model.addAttribute("syncDirection",direction);
		model.addAttribute("landscape",direction.getLandscape()); 
		model.addAttribute("selectedLink",ControllerConstants.CCF_PROPERTIES);

	}


}
