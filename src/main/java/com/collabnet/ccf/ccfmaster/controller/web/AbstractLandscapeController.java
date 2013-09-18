package com.collabnet.ccf.ccfmaster.controller.web;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;
import com.collabnet.ccf.ccfmaster.gp.model.GenericParticipantFacade;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;

public abstract class AbstractLandscapeController {

	protected static final String PARTICIPANT_MODEL_ATTRIBUTE = "participant";
	protected static final String LANDSCAPE_MODEL_ATTRIBUTE = "landscape";
	protected static final String PAGE_REQUEST_PARAM = "page";
	protected static final String PAGE_SIZE_REQUEST_PARAM = "size";
	protected static final String TF_URL_MODEL_ATTRIBUTE = "tfUrl";

	@Autowired
	protected CCFRuntimePropertyHolder ccfRuntimePropertyHolder;
	
	@Autowired(required= false)
	public GenericParticipantFacade genericParticipant;


	/**
	 * Paginate the {@code query}. Set first result and number of results to return according to {@code page} and {@code pageSize}.
	 * 
	 * As a side effect, sets the model attribute "maxPages" to the number of pages of size {@code pageSize}.
	 * @param query
	 * @param entityCount
	 * @param page
	 * @param pageSize
	 * @param model
	 */
	protected static <T> TypedQuery<T> paginate(
			TypedQuery<T> query,
			long entityCount,
			Integer page,
			Integer pageSize,
			Model model) {
		final int sizeNo = normalizePageSize(pageSize);
		final int pageNo = normalizePageNumber(page);
		updateModelWithMaxPages(model, entityCount, sizeNo);
		return paginateQuery(query, pageNo, sizeNo);
	}

	/**
	 * paginates the {@code query}. Sets the first result that should be
	 * returned and the number of results to return.
	 * 
	 * @param <T> type of entity returned by the query
	 * @param query the query
	 * @param page zero-based page number
	 * @param pageSize number of entries per page
	 * @return the modified query, so fluent api works.
	 *         Identical to {@code query}.
	 */
	protected static <T> TypedQuery<T> paginateQuery(final TypedQuery<T> query,
			final int page, final int pageSize) {
		return query.setFirstResult(page * pageSize).setMaxResults(pageSize);
	}

	/**
	 * sets the model attribute "maxPages" to the number of {@code pageSize}
	 * -sized pages into which {@code entityCount} entities can be divided.
	 * 
	 * @param model
	 * @param entityCount
	 * @param pageSize
	 */
	private static void updateModelWithMaxPages(Model model,
			final long entityCount, final int pageSize) {
		float nrOfPages = (float) entityCount / pageSize;
		model.addAttribute("maxPages",
				(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ?
						nrOfPages + 1 : nrOfPages));
	}

	/**
	 * @param page one-based page number
	 * @return valid, zero-based page number, defaults to 0
	 */
	private static int normalizePageNumber(Integer page) {
		final int pageNo = (page == null || page < 1) ? 
				0 : (page.intValue() - 1);
		return pageNo;
	}

	/**
	 * @return valid page size, defaults to {@link ControllerConstants#PAGINATION_SIZE}.
	 */
	private static int normalizePageSize(Integer pageSize) {
		final int sizeNo = (pageSize == null || pageSize < 1) ?
				ControllerConstants.PAGINATION_SIZE : pageSize.intValue();
		return sizeNo;
	}

	@ModelAttribute(PARTICIPANT_MODEL_ATTRIBUTE)
	public Participant populateParticipant() {
		Landscape landscape = Landscape.findAllLandscapes().get(0);
		return landscape.getParticipant();
	}

	@ModelAttribute(LANDSCAPE_MODEL_ATTRIBUTE)
	public Landscape populateLandscape() {
		return Landscape.findAllLandscapes().get(0);
	}

	@ModelAttribute(TF_URL_MODEL_ATTRIBUTE)
	public String populateTfUrl() {
		return ccfRuntimePropertyHolder.getTfUrl();
	}
	
}