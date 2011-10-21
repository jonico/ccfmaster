package org.springframework.mvc.extensions.flash;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * taken from https://github.com/SpringSource/spring-mvc-showcase until
 * https://jira.springsource.org/browse/SPR-6464 is fixed
 */
public final class FlashMap {

	static final String FLASH_MAP_ATTRIBUTE = FlashMap.class.getName();

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getCurrent(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Map<String, Object> flash = (Map<String, Object>) session.getAttribute(FLASH_MAP_ATTRIBUTE);
		if (flash == null) {
			flash = new HashMap<String, Object>();
			session.setAttribute(FLASH_MAP_ATTRIBUTE, flash);
		}
		return flash;
	}

	private FlashMap() {
	}

	public static void put(String key, Object value) {
		getCurrent(getRequest(RequestContextHolder.currentRequestAttributes()))
				.put(key, value);
	}

	public static void setInfoMessage(String info) {
		put(MESSAGE_KEY, new Message(MessageType.info, info));
	}

	public static void setWarningMessage(String warning) {
		put(MESSAGE_KEY, new Message(MessageType.warning, warning));
	}

	public static void setErrorMessage(String error) {
		put(MESSAGE_KEY, new Message(MessageType.error, error));
	}

	public static void setSuccessMessage(String success) {
		put(MESSAGE_KEY, new Message(MessageType.success, success));
	}

	public static void setInfoMessage(String info, String message) {
		put(MESSAGE_KEY, new Message(MessageType.info, info, message));
	}

	public static void setWarningMessage(String warning, String message) {
		put(MESSAGE_KEY, new Message(MessageType.warning, warning, message));
	}

	public static void setErrorMessage(String error, String message) {
		put(MESSAGE_KEY, new Message(MessageType.error, error, message));
	}

	private static HttpServletRequest getRequest(
			RequestAttributes requestAttributes) {
		return ((ServletRequestAttributes) requestAttributes).getRequest();
	}

	public static void setSuccessMessage(String success, String message) {
		put(MESSAGE_KEY, new Message(MessageType.success, success, message));
	}

	private static final String MESSAGE_KEY = "message";

	public static class MessageCode {

		private final MessageType type;

		private final String messageCode;

		public MessageCode(MessageType type, String text) {
			this.type = type;
			this.messageCode = text;
		}

		public MessageType getType() {
			return type;
		}
		
		public String getCssClass() {
			return (type == MessageType.error) ? "errorMessage" : "greenText";
		}

		public String getMessageCode() {
			return messageCode;
		}

		public String toString() {
			return type + ": " + messageCode;
		}

	}
	
	public static class Message extends MessageCode {
		private final String message;
		
		public Message(MessageType messageType, String messageCode) {
			this(messageType, messageCode, "");
		}
		
		public Message(MessageType messageType, String messageCode, String message) {
			super(messageType, messageCode);
			this.message = message;
		}
		
		public String getMessage() {
			return message;
		}
		
		public String toString() {
			return String.format("%s (%s)", super.toString(), message);
		}
	}

	public static enum MessageType {
		info, success, warning, error
	}

}