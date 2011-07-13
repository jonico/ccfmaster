package com.collabnet.ccf.ccfmaster.controller.web;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import com.collabnet.ccf.ccfmaster.web.model.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;

import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LogFile;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;




  

@RequestMapping("/admin/**")
@Controller
public class LandscapeLogsController {

	
	public static final String CONTENT = "content";
	private static final String END_INDEX = "endIndex";
	public static final String LAST_MODIFIED_TIME = "lastModifiedTime";
	public static final String FORWARD = "forward";
	public static final String START_INDEX = "startIndex";
	public static final String FILE_NAME = "fileName";
	public static final String DIRECTION = "direction";
	private static final Logger log = LoggerFactory.getLogger(LandscapeLogsController.class);
	
	/**
	 * Controller method to display TF to Participant logs list
	 * 
	 */
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFTOPARTICIPANTLOGS, method = RequestMethod.GET)
    public String displaytftoparticipantlogs(Model model, HttpServletRequest request) {
		log.info("in displaylogstftopart");
		RequestContext ctx = new RequestContext(request);
		Landscape landscape=ControllerHelper.findLandscape(model);
		List logFileList=null;
		Direction forwardDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.FORWARD).getSingleResult();
		try{
			logFileList=LogFile.findLogFilesByDirection(forwardDirection);
			
		}
		catch(Exception exception){
			model.addAttribute("connectionerror",ctx.getMessage(ControllerConstants.LOGLISTFAILMESSAGE)+ exception.getMessage());

		}
		model.addAttribute("logfilelist",logFileList);
		populateLogModel(model,landscape);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFTOPARTICIPANTLOGS;
		  
    }
	
	/**
	 * Controller method to display Participant to TF logs list
	 * 
	 */
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTTOTFLOGS, method = RequestMethod.GET)
    public String displayparticipanttotflogs(Model model, HttpServletRequest request) {
		log.info("in displaylogsparttotf");
		RequestContext ctx = new RequestContext(request);
		Landscape landscape=ControllerHelper.findLandscape(model);
		List logFileList=null;
		Direction reverseDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.REVERSE).getSingleResult();
		try{
			logFileList=LogFile.findLogFilesByDirection(reverseDirection);
		}
		catch(Exception exception){
			model.addAttribute("connectionerror",ctx.getMessage(ControllerConstants.LOGLISTFAILMESSAGE)+ exception.getMessage());

		}
		model.addAttribute("logfilelist",logFileList);
		populateLogModel(model,landscape);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTTOTFLOGS;
		  
    }
    
	/**
	 * Controller method to download log file
	 * 
	 */
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DOWNLOADLOGFILE, method = RequestMethod.GET)
    public void downloadlogs(Model model,HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("in Download Log");
		Direction direction=null;
		Landscape landscape=ControllerHelper.findLandscape(model);
		String paramdirection=request.getParameter(ControllerConstants.DIRECTION);
		if(paramdirection.equals(FORWARD)){
		 direction=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.FORWARD).getSingleResult();
		}
		else{
		 direction=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.REVERSE).getSingleResult();
		}
		LogFile log=LogFile.findLogFile(direction, request.getParameter(ControllerConstants.FILENAME));
		File file=log.logFile();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName()+"\"");
        OutputStream out = response.getOutputStream();
        response.setContentType("text/html; charset=utf-8");
        FileInputStream fi = new FileInputStream(file);
        IOUtils.copy(fi, out);
        out.flush();
        out.close(); 
       
	}
	
	
	/**
	 * Controller method to display log file
	 * 
	 */
	
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_SHOWLOGFILE, method = RequestMethod.GET)
    public String viewlog(Model model,HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("in show log");
		Direction direction=null;
		Landscape landscape=ControllerHelper.findLandscape(model);
		String paramDirection=request.getParameter(ControllerConstants.DIRECTION);
		if(paramDirection.equals(FORWARD)){
			direction=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.FORWARD).getSingleResult();
		}
		else{
		 	direction=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.REVERSE).getSingleResult();
		}	
		LogFile log=LogFile.findLogFile(direction, request.getParameter(ControllerConstants.FILENAME));
		File file=log.logFile();
	  	FileReader fr = new FileReader(file); 
        BufferedReader br = new BufferedReader(fr);
       	String line = null;
       	StringBuffer linebuffer=new StringBuffer();
       	while((line = br.readLine()) != null){
       		linebuffer.append(line);
       		linebuffer.append("<BR/>");     	
       	}
		
       	model.addAttribute("fileobject", linebuffer.toString());
       	model.addAttribute("filename",log.getName());
       	model.addAttribute("filesize",log.getSize());
       	model.addAttribute("filemodificationdate",log.getLastModifiedDate());
       	model.addAttribute("direction",paramDirection);
    	populateLogModel(model,landscape);
		return UIPathConstants.LANDSCAPESETTINGS_SHOWLOGS;
    }
	
	
	 /**
     * returns a tail of the log file starting at the given index. Renders JSON View as response for ajax request
	 * @return 
     */
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_TAILLOGFILE, method = RequestMethod.GET)
    public ModelAndView viewLogTail(Model model,HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info("in Tail log");
		Direction direction=null;
        String logName = request.getParameter(FILE_NAME);
        long startIndex=Long.valueOf(request.getParameter(START_INDEX));
        String paramdirection=request.getParameter("direction");
        try {
        	Landscape landscape=ControllerHelper.findLandscape(model);
        	if(paramdirection.equals(FORWARD)){
       		 direction=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.FORWARD).getSingleResult();
       		}
       		else{
       		 direction=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.REVERSE).getSingleResult();
       		}	LogFile fileLog=LogFile.findLogFile(direction, logName);
    		File file=fileLog.logFile();
    		Date logModifiedTime = fileLog.getLastModifiedDate();
    		
           // fetch any new content after the startIndex
            StringBuffer buffer = new StringBuffer();
            FileInputStream fis = null;
            InputStreamReader isr = null;
            try {
                fis = new FileInputStream(file);
                isr = new InputStreamReader(fis, "UTF8");
                isr.skip(startIndex);
                int ch;
                while ((ch = isr.read()) > -1) {
                    buffer.append((char)ch);
                }
                isr.close();
                fis.close();

            } catch (IOException e) {
            	log.debug(e.getMessage());
                if (isr != null) {
                    isr.close();
                }
                if (fis != null) {
                    fis.close();
                }
            }
            
            ModelAndView jsonModel=new ModelAndView();
            jsonModel.addObject(FILE_NAME, logName);
            jsonModel.addObject(DIRECTION, paramdirection);
            jsonModel.addObject(LAST_MODIFIED_TIME, logModifiedTime);
            jsonModel.addObject(START_INDEX, startIndex);
            jsonModel.addObject(END_INDEX, startIndex + buffer.length());
            jsonModel.addObject(CONTENT, buffer.toString());
            return JsonView.Render(jsonModel, response);  
        } catch (FileNotFoundException logDoesNotExist) {
        	 ModelAndView jsonModel=new ModelAndView();
             jsonModel.addObject(FILE_NAME, logName);
             jsonModel.addObject(DIRECTION, "");
             jsonModel.addObject(LAST_MODIFIED_TIME, 0);
             jsonModel.addObject(START_INDEX, 0);
             jsonModel.addObject(END_INDEX, 0);
             jsonModel.addObject(CONTENT, "");
             jsonModel.addObject("error", "File Not Found");
            return JsonView.Render(jsonModel, response);  
        }
	}
	
	
	/**
	 * Helper method to populate model object for log
	 * 
	 */
	public void populateLogModel(Model model,Landscape landscape){
		model.addAttribute("selectedLink", "logs");
		model.addAttribute("landscape",landscape);
		model.addAttribute("participant",landscape.getParticipant());
		
	}
	
	
}
