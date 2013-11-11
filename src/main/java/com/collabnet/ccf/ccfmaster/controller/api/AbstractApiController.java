package com.collabnet.ccf.ccfmaster.controller.api;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class AbstractApiController<T> extends AbstractBaseApiController {
    static final Logger log = LoggerFactory
                                    .getLogger(AbstractApiController.class);

    @ResponseStatus(CREATED)
    @RequestMapping(method = POST, headers = "Accept=application/xml")
    public abstract T create(@RequestBody T requestBody,
            HttpServletResponse response);

    @ResponseStatus(NO_CONTENT)
    @RequestMapping(value = "/{id}", method = DELETE)
    public abstract void delete(@PathVariable("id") Long id,
            HttpServletResponse response);

    @RequestMapping(method = GET, headers = "Accept=application/xml")
    public abstract @ResponseBody
    List<T> list();

    @RequestMapping(value = "/{id}", method = GET, headers = "Accept=application/xml")
    public T show(@PathVariable("id") T id) {
        if (id == null) {
            throw new DataRetrievalFailureException(
                    "requested entity not found.");
        }
        return id;
    }

    /**
     * 
     * @param id
     * @param requestBody
     * @param response
     *            this parameter is necessary to prevent Spring MVC from trying
     *            to resolve a view. Alternatively, we could return @ResponseBody
     *            String
     */
    @RequestMapping(value = "/{id}", method = PUT)
    public abstract void update(@PathVariable("id") Long id,
            @RequestBody T requestBody, HttpServletResponse response);
}