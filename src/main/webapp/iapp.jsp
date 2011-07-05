<?xml version="1.0" encoding="utf-8" ?>
<jsp:root
 xmlns:jsp="http://java.sun.com/JSP/Page"
 xmlns:iapp="http://ccf.open.collab.net/iaf/tags"
 xmlns:sec="http://www.springframework.org/security/tags"
 version="2.0">
    <jsp:directive.page language="java"
        contentType="text/html; charset=ISO-8859-1" pageEncoding="utf-8" />
<sec:authorize access="hasRole('ROLE_IAF_USER')">
<iapp:viewbuttonbar />
</sec:authorize>
</jsp:root>