package com.collabnet.ccf.ccfmaster.controller.api;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletResponse;

import org.apache.commons.io.LineIterator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.LogFile;
import com.collabnet.ccf.ccfmaster.server.domain.LogFileList;

@Controller
//@Scope("request")
@RequestMapping(value = Paths.LOG_FILE)
public class ApiLogFileController {
	
	@ModelAttribute
	public Direction populateDirection(@PathVariable("directionId") Direction direction) {
		return direction;
	}
	
	@RequestMapping(method = GET, headers="Accept=application/xml")
	public @ResponseBody LogFileList list(
			@ModelAttribute Direction direction) throws IOException {
		return new LogFileList(LogFile.findLogFilesByDirection(direction));
	}
	
	// regex is necessary to keep spring mvc from cutting off the file extension :-/
	@RequestMapping(value = "/{fileName:.+}", method = GET)
	public void show(
			@ModelAttribute Direction direction,
			@PathVariable("fileName") String fileName,
			@RequestParam(value="firstLine", required=false) Long startLine,
			@RequestParam(value="maxLines",  required=false) Long maxLines,
			ServletResponse response) throws IOException {
		LogFile logFile = LogFile.findLogFile(direction, fileName);
		response.setContentType("text/plain");
		Writer out = response.getWriter();
		LineIterator it;
		if (startLine != null) {
			it = logFile.linesFrom(startLine);
		} else {
			it = logFile.lines();
		}
		try {
			long line = 0;
			while (it.hasNext()) {
				if (maxLines != null && line >= maxLines) {
					break;
				}
				out.write(it.nextLine());
				out.write('\n');
				line++;
			}
		} finally {
			it.close();
		}
	}
	
}
