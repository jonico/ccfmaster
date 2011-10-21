package com.collabnet.ccf.ccfmaster.controller.web;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LogFile;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.model.JsonView;


@RequestMapping("/admin/**")
@Controller
public class LandscapeLogsController extends AbstractLandscapeController {


	private static final long MAX_FILE_SIZE = 102400; //to read only 100KB of data
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
	public String displayTFtoParticipantLogs(Model model, HttpServletRequest request) {
		Landscape landscape=ControllerHelper.findLandscape(model);
		List<LogFile> logFileList=new ArrayList<LogFile>();
		Direction forwardDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.FORWARD).getSingleResult();
		try{
			logFileList=LogFile.findLogFilesByDirection(forwardDirection);
		}
		catch(Exception exception){
			FlashMap.setErrorMessage(ControllerConstants.LOGLISTFAILMESSAGE, exception.getMessage());
		}
		model.addAttribute("logfilelist",logFileList);
		populateLogModel(model);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFTOPARTICIPANTLOGS;

	}

	/**
	 * Controller method to display Participant to TF logs list
	 * 
	 */
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTTOTFLOGS, method = RequestMethod.GET)
	public String displayParticipanttoTFLogs(Model model, HttpServletRequest request) {
		Landscape landscape=ControllerHelper.findLandscape(model);
		List<LogFile> logFileList=null;
		Direction reverseDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.REVERSE).getSingleResult();
		try{
			logFileList=LogFile.findLogFilesByDirection(reverseDirection);
		}
		catch(Exception exception){
			FlashMap.setErrorMessage(ControllerConstants.LOGLISTFAILMESSAGE, exception.getMessage());
		}
		model.addAttribute("logfilelist",logFileList);
		populateLogModel(model);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTTOTFLOGS;

	}

	/**
	 * Controller method to download log file
	 * 
	 */
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DOWNLOADLOGFILE, method = RequestMethod.GET)
	public void downloadLogs(@RequestParam(ControllerConstants.DIRECTION) String paramdirection , @RequestParam("filename") String logName,Model model, HttpServletResponse response)throws IOException{
		OutputStream out=null;
		FileInputStream fi=null;
		try{
			LogFile log = getLogFile(paramdirection, logName, model);
			File file=log.logFile();
			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName()+"\"");
			out = response.getOutputStream();
			response.setContentType("text/html; charset=utf-8");
			fi = new FileInputStream(file);
			IOUtils.copy(fi, out);
			out.flush();
			out.close();
			fi.close();
		}
		catch(IOException exception){
			log.error(exception.getMessage());
		}
		finally{
			if(out !=null){
				out.close(); 
			}
			if(fi!=null){
				fi.close();	
			}
		}

	}

	/**
	 * @param paramdirection
	 * @param logName
	 * @param model
	 * @return
	 * @throws IOException
	 */
	private LogFile getLogFile(String paramdirection, String logName, Model model)
	throws IOException {
		Landscape landscape=ControllerHelper.findLandscape(model);
		Directions directions = FORWARD.equals(paramdirection) ? Directions.FORWARD : Directions.REVERSE;
		Direction 	direction=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, directions).getSingleResult();
		LogFile log=LogFile.findLogFile(direction,logName);
		return log;
	}


	/**
	 * Controller method to display log file
	 * 
	 */

	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_SHOWLOGFILE, method = RequestMethod.GET)
	public String viewLog(@RequestParam(ControllerConstants.DIRECTION) String paramDirection , @RequestParam("filename") String logName,Model model,HttpServletRequest request, HttpServletResponse response) throws Exception{
		LogFile log = getLogFile(paramDirection, logName, model);
		StringBuffer linebuffer=readFiletoString(log);
		model.addAttribute("fileobject", linebuffer.toString());
		return UIPathConstants.LANDSCAPESETTINGS_SHOWLOGS;
	}

	/**
	 * @param paramDirection
	 * @param model
	 * @param log
	 */
	private void makeLogModel(String paramDirection, Model model, LogFile log) {
		model.addAttribute("filename",log.getName());
		model.addAttribute("filesize",log.getSize());
		model.addAttribute("filemodificationdate",log.getLastModifiedDate());
		model.addAttribute("direction",paramDirection);
	}


	/**
	 * returns a tail of the log file starting at the given index. Renders JSON
	 * View as response for ajax request
	 * 
	 * @return
	 */
	@RequestMapping(value = "/" + UIPathConstants.LANDSCAPESETTINGS_TAILLOGFILE, method = RequestMethod.GET)
	public ModelAndView viewLogTail(
			@RequestParam(ControllerConstants.DIRECTION) String paramDirection,
			@RequestParam("fileName") String logName, Model model,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		long startIndex= Long.valueOf(request.getParameter(START_INDEX));
		try {
			LogFile log = getLogFile(paramDirection, logName, model);
			File file = log.logFile();
			Date logModifiedTime = log.getLastModifiedDate();
			StringBuffer buffer = getFileContent(startIndex, file);
			makeLogModel(paramDirection, model, log);
			ModelAndView jsonModel = populateJsonModel(paramDirection, logName,
					startIndex, logModifiedTime, buffer);
			return JsonView.render(jsonModel, response);
		} catch (FileNotFoundException logDoesNotExist) {
			ModelAndView jsonModel = populateJsonModel("", logName, 0L, null,null);
			jsonModel.addObject("error", "File Not Found");
			return JsonView.render(jsonModel, response);
		}
	}

	/**
	 * @param startIndex
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private StringBuffer getFileContent(long startIndex, File file)
	throws IOException {
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
				buffer.append((char) ch);
			}
			isr.close();
			fis.close();
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
		finally{
			if (isr != null) {
				isr.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
		return buffer;
	}

	/**
	 * @param paramdirection
	 * @param logName
	 * @param startIndex
	 * @param logModifiedTime
	 * @param buffer
	 * @return
	 */
	private ModelAndView populateJsonModel(String paramDirection,
			String logName, long startIndex, Date logModifiedTime,
			StringBuffer buffer) {
		ModelAndView jsonModel=new ModelAndView();
		jsonModel.addObject(FILE_NAME, logName);
		jsonModel.addObject(DIRECTION, paramDirection);
		jsonModel.addObject(LAST_MODIFIED_TIME, logModifiedTime);
		jsonModel.addObject(START_INDEX, startIndex);
		jsonModel.addObject(END_INDEX, startIndex + buffer.length());
		jsonModel.addObject(CONTENT, buffer.toString());
		return jsonModel;
	}

	/**
	 * Controller method to view raw log file
	 * 
	 */  
	@RequestMapping(value = "/"	+ UIPathConstants.LANDSCAPESETTINGS_PROCESSRAWLOGFILE, method = RequestMethod.GET)
	public String viewRawLogFile(
			@RequestParam(ControllerConstants.DIRECTION) String paramDirection,
			@RequestParam("filename") String fileName , Model model,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		LogFile log = getLogFile(paramDirection, fileName, model);
		int skippedKiloBytes=0;
		long filesize=log.logFile().length();
		if(filesize > MAX_FILE_SIZE){
			skippedKiloBytes=((int)filesize-(int)MAX_FILE_SIZE)/1024;
		}
		StringBuffer linebuffer = readPartofFiletoString(log);
		response.setContentType("text/html; charset=utf-8");
		model.addAttribute("xmlcontent", linebuffer.toString());
		model.addAttribute("skippedkilobytes",skippedKiloBytes);
		makeLogModel(paramDirection, model, log);
		return UIPathConstants.LANDSCAPESETTINGS_VIEWRAWLOGFILE;
	}

	/**
	 * @param log
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private StringBuffer readFiletoString(LogFile logfile)
	throws FileNotFoundException, IOException {
		FileReader fr =null;
		BufferedReader br =null;
		StringBuffer linebuffer=null;
		try{
			File file = logfile.logFile();
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line = null;
			linebuffer = new StringBuffer();
			while ((line = br.readLine()) != null) {
				linebuffer.append(line);
				linebuffer.append("\r\n\n");
			}
			br.close();
			fr.close();
		}
		catch(IOException exception){
			log.debug(exception.getMessage());
			throw exception;
		}
		finally{
			if(br !=null){
			br.close();
			}
			if(fr !=null){
			fr.close();	
			}
		}

		return linebuffer;
	}

	/**
	 * @param log
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private StringBuffer readPartofFiletoString(LogFile logfile)
	throws FileNotFoundException, IOException {

		RandomAccessFile  randomAccessFile=null;
		StringBuffer linebuffer=null;
		try{
			File file = logfile.logFile();
			long filesize = file.length();
			randomAccessFile=new RandomAccessFile(file, "r");
			//If file size is greater than 100KB.Skip the remaining bytes and read only last 100 KB of data
			if(filesize > MAX_FILE_SIZE){
				randomAccessFile.skipBytes((int)filesize-(int)MAX_FILE_SIZE);
			}
			String line = null;
			linebuffer = new StringBuffer();
			int fileLineNr = 1;
			int skipLine=1;
			while ((line = randomAccessFile.readLine()) != null) {
				//skip the first line,assuming if its incomplete line after skipping
				if(fileLineNr++<=skipLine){
					continue;
				}
				linebuffer.append(line);
				linebuffer.append("\r\n\n");
			}
			randomAccessFile.close();
		}
		catch(IOException exception){
			log.debug(exception.getMessage());
			throw exception;
		}
		finally{
			if(randomAccessFile !=null){
				randomAccessFile.close();
			}
		}

		return linebuffer;
	}


	/**
	 * Helper method to populate model object for log
	 * 
	 */
	public void populateLogModel(Model model){
		Landscape landscape=ControllerHelper.findLandscape(model);
		model.addAttribute("selectedLink", "logs");
		model.addAttribute("landscape",landscape);
		model.addAttribute("participant",landscape.getParticipant());

	}


}
