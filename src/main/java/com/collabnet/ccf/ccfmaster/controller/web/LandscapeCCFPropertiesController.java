package com.collabnet.ccf.ccfmaster.controller.web;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.RequestContext;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;




@RequestMapping("/landscapesettings/**")
@Controller
public class LandscapeCCFPropertiesController {

	
	private static final Logger log = LoggerFactory.getLogger(LandscapeCCFPropertiesController.class);
	
	/**
	 * Controller method to display log template from participant to TF 
	 * 
	 */
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESLOGTEMPLATEPARTTOTF, method = RequestMethod.GET)
    public String displayccfpropertieslogtemplateparttotf(Model model, HttpServletRequest request) {
		Landscape landscape=ControllerHelper.findLandscape(model);
		populateDirectionConfigModel(landscape,model,Directions.REVERSE);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESLOGTEMPLATEPARTTOTF;
		  
    }
    
	/**
	 * Controller method to update log template from participant to TF 
	 * 
	 */
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_SAVECCFPROPERTIESLOGTEMPLATEPARTTOTF, method = RequestMethod.POST)
    public String saveccfpropertieslogtemplateparttotf(@ModelAttribute("directionConfig") @Valid DirectionConfig directionConfig,BindingResult bindingResult,Model model, HttpServletRequest request) {
		Landscape landscape=ControllerHelper.findLandscape(model);
		RequestContext ctx = new RequestContext(request);
		try{
				Direction reverseDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.REVERSE).getSingleResult();
				directionConfig.setId(Long.valueOf(request.getParameter(ControllerConstants.DIRECTIONCONFIG_ID)));
				directionConfig.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.DIRECTIONCONFIG_VERSION)));
				directionConfig.setDirection(reverseDirection);
				directionConfig.merge();
				model.addAttribute("connectionmessage",ctx.getMessage(ControllerConstants.LOGTEMPLATESAVESUCCESSMESSAGE));
			}
		catch(Exception exception){
				model.addAttribute("connectionerror",ctx.getMessage(ControllerConstants.LOGTEMPLATESAVEFAILMESSAGE)+ exception.getMessage());
		}
		
		populateDirectionConfigModel(landscape,model,Directions.REVERSE);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESLOGTEMPLATEPARTTOTF;
		  
    }
	
	/**
	 * Controller method to display log template from TF to participant  
	 * 
	 */	
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESLOGTEMPLATETFTOPART, method = RequestMethod.GET)
    public String displayccfpropertieslogtemplatetftopart(Model model, HttpServletRequest request) {
		Landscape landscape=ControllerHelper.findLandscape(model);
		populateDirectionConfigModel(landscape,model,Directions.FORWARD);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESLOGTEMPLATETFTOPART;
		  
    }
	
	/**
	 * Controller method to update log template from TF to participant  
	 * 
	 */	
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_SAVECCFPROPERTIESLOGTEMPLATETFTOPART, method = RequestMethod.POST)
    public String saveccfpropertieslogtemplatetftopart(@ModelAttribute("directionConfig") @Valid DirectionConfig directionConfig,BindingResult bindingResult,Model model, HttpServletRequest request) {
		RequestContext ctx = new RequestContext(request);
		Landscape landscape=ControllerHelper.findLandscape(model);
		try{
		Direction forwardDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.FORWARD).getSingleResult();
		directionConfig.setId(Long.valueOf(request.getParameter(ControllerConstants.DIRECTIONCONFIG_ID)));
		directionConfig.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.DIRECTIONCONFIG_VERSION)));
		directionConfig.setDirection(forwardDirection);
		directionConfig.merge();
		model.addAttribute("connectionmessage",ctx.getMessage(ControllerConstants.LOGTEMPLATESAVESUCCESSMESSAGE));
		}
		catch(Exception exception){
			model.addAttribute("connectionerror",ctx.getMessage(ControllerConstants.LOGTEMPLATESAVEFAILMESSAGE)+ exception.getMessage());
		}
		populateDirectionConfigModel(landscape,model,Directions.FORWARD);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESLOGTEMPLATETFTOPART; 
	 }
	
	/**
	 * Helper method to populate direction config   
	 * 
	 */	
	public static void populateDirectionConfigModel(Landscape landscape,Model model, Directions directions){
		Direction direction=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, directions).getSingleResult();
		DirectionConfig directionConfig=DirectionConfig.findDirectionConfigsByDirectionAndName(direction,ControllerConstants.CCF_DIRECTION_LOGMESSAGETEMPLATE).getSingleResult();
		model.addAttribute("participant",landscape.getParticipant());
		model.addAttribute("directionConfigversion",directionConfig.getVersion());
		model.addAttribute("directionConfigid",directionConfig.getId());
		model.addAttribute("direction",directionConfig.getDirection());
		model.addAttribute("directionConfig", directionConfig);
		model.addAttribute("landscape",landscape);
		model.addAttribute("selectedLink",ControllerConstants.CCFPROPERTIES);
	}
	
	
	/**
	 * Controller method to display synchronize behavior from participant to TF  
	 * 
	 */	
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCPARTTOTF, method = RequestMethod.GET)
    public String displayccfpropertiessyncparttotf(Model model, HttpServletRequest request) {
		log.info("displayccfpropertiessyncparttotf");
		Landscape landscape=ControllerHelper.findLandscape(model);
		populateDirectionModel(landscape,model,Directions.REVERSE);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCPARTTOTF;
		  
    }
	
	/**
	 * Controller method to save synchronize behavior from participant to TF  
	 * 
	 */	
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_SAVECCFPROPERTIESSYNCPARTTOTF, method = RequestMethod.POST)
	public String saveccfpropertiessyncparttotf(@ModelAttribute("syncDirection") @Valid Direction direction,BindingResult bindingResult,Model model, HttpServletRequest request) {
		RequestContext ctx = new RequestContext(request);
		Landscape landscape=ControllerHelper.findLandscape(model);
		try{
			direction.setId(Long.valueOf(request.getParameter(ControllerConstants.DIRECTION_ID)));
			direction.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.DIRECTION_VERSION)));
			direction.setShouldStartAutomatically(direction.getShouldStartAutomatically());
			direction.setLandscape(landscape);
			direction.merge();
			if(direction.getShouldStartAutomatically()){
				if(landscape.getParticipant().getSystemKind().equals(SystemKind.QC)){
					model.addAttribute("syncautomessage",ctx.getMessage("ccfcoreqctotfstartautomaticallymessage"));
				}
				if(landscape.getParticipant().getSystemKind().equals(SystemKind.SWP)){
					model.addAttribute("syncautomessage",ctx.getMessage("ccfcoreswptotfstartautomaticallymessage"));
				}
			}
			model.addAttribute("connectionmessage",ctx.getMessage(ControllerConstants.SYNCSAVESUCCESSMESSAGE));
		}
		catch(Exception e){
			model.addAttribute("connectionerror",ctx.getMessage(ControllerConstants.SYNCSAVEFAILMESSAGE));
		}
		populateDirectionModel(landscape,model,Directions.REVERSE);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCPARTTOTF;
	}
	

	/**
	 * Controller method to display synchronize behavior from TF to participant  
	 * 
	 */	
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCTFTOPART, method = RequestMethod.GET)
    public String displayccfpropertiessynctftopart(Model model, HttpServletRequest request) {
		Landscape landscape=ControllerHelper.findLandscape(model);
		populateDirectionModel(landscape,model,Directions.FORWARD);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCTFTOPART;
	 }
	
	
	/**
	 * Controller method to save synchronize behavior from TF to participant  
	 * 
	 */	
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_SAVECCFPROPERTIESSYNCTFTOPART, method = RequestMethod.POST)
	@Transactional
    public String saveccfpropertiessynctftopart(@ModelAttribute("syncDirection") Direction direction,Model model, HttpServletRequest request) {
		RequestContext ctx = new RequestContext(request);
		Landscape landscape=ControllerHelper.findLandscape(model);
		try{
			direction.setId(Long.valueOf(request.getParameter(ControllerConstants.DIRECTION_ID)));
			direction.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.DIRECTION_VERSION)));
			direction.setLandscape(landscape);
			direction.setShouldStartAutomatically(direction.getShouldStartAutomatically());
			direction.merge();
			if(direction.getShouldStartAutomatically()){
				if(landscape.getParticipant().getSystemKind().equals(SystemKind.QC)){
					model.addAttribute("syncautomessage",ctx.getMessage("ccfcoretftoqcstartautomaticallymessage"));
				}
				if(landscape.getParticipant().getSystemKind().equals(SystemKind.SWP)){
					model.addAttribute("syncautomessage",ctx.getMessage("ccfcoretftoswpstartautomaticallymessage"));
				}
			}
			
			model.addAttribute("connectionmessage",ctx.getMessage(ControllerConstants.SYNCSAVESUCCESSMESSAGE));
		}
		catch(Exception e){
			model.addAttribute("connectionerror",ctx.getMessage(ControllerConstants.SYNCSAVEFAILMESSAGE));
		}
		populateDirectionModel(landscape,model,Directions.FORWARD);
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
	public static void populateDirectionModel(Landscape landscape,Model model, Directions directions){
		Direction direction=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, directions).getSingleResult();
		model.addAttribute("participant",landscape.getParticipant());
		model.addAttribute("directionversion",direction.getVersion());
		model.addAttribute("directionid",direction.getId());
		model.addAttribute("direction",direction.getDirection());
		model.addAttribute("description",direction.getDescription());
		model.addAttribute("syncDirection",direction);
		model.addAttribute("landscape",direction.getLandscape());
		model.addAttribute("selectedLink",ControllerConstants.CCFPROPERTIES);
	
	}
	
	
}
