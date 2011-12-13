<?xml version="1.0"?>
<!-- Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache 
	License, Version 2.0 (the "License"); you may not use this file except in 
	compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
	Unless required by applicable law or agreed to in writing, software distributed 
	under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES 
	OR CONDITIONS OF ANY KIND, either express or implied. See the License for 
	the specific language governing permissions and limitations under the License. -->
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:stringutil="xalan://com.collabnet.ccf.core.utils.StringUtils"
	exclude-result-prefixes="xsl xs ccf stringutil" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://www.w3.org/2007/schema-for-xslt20.xsd">
	<xsl:template match='/ccf:artifact[@artifactType = "plainArtifact"]'>
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:copy-of select="@*" />
			<xsl:if test="@artifactAction = 'create'">
				<xsl:attribute name="artifactAction">ignore</xsl:attribute>
			</xsl:if>
			<xsl:apply-templates />
		</artifact>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="title"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">RQ_REQ_NAME</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="description"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">RQ_REQ_COMMENT</xsl:attribute>
			<xsl:text>&lt;html&gt;&lt;body&gt;</xsl:text>
			<!-- <xsl:value-of select="stringutil:someMethod(string(.))" /> -->
			<xsl:text>&lt;/body&gt;&lt;/html&gt;</xsl:text>
		</field>
	</xsl:template>
	<xsl:template match="text()" />
</xsl:stylesheet>
