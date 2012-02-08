<?xml version="1.0"?>
	<!--
		Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache
		License, Version 2.0 (the "License"); you may not use this file except
		in compliance with the License. You may obtain a copy of the License
		at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
		applicable law or agreed to in writing, software distributed under the
		License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
		CONDITIONS OF ANY KIND, either express or implied. See the License for
		the specific language governing permissions and limitations under the
		License.
	-->
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:stringutil="xalan://com.collabnet.ccf.core.utils.StringUtils"
	exclude-result-prefixes="xsl xs ccf stringutil" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.w3.org/1999/XSL/Transform http://www.w3.org/2007/schema-for-xslt20.xsd">
	<xsl:template match='/ccf:artifact[@artifactType = "plainArtifact"]'>
		<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
		</artifact>
	</xsl:template>
	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="." />
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="actualHours"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">RQ_USER_13</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="estimatedHours"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">RQ_USER_11</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Current Est. Effort"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">RQ_USER_12</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="remainingEffort"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">RQ_USER_14</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="id"]'>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">RQ_USER_08</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
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
			<xsl:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/>
			<xsl:text>&lt;/body&gt;&lt;/html&gt;</xsl:text>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="status"]'>
		<xsl:variable name="statusValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">RQ_USER_07</xsl:attribute>
			<xsl:if test="$statusValue = 'Accepted'">
				<xsl:text>Accepted</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Completed'">
				<xsl:text>Completed</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Draft'">
				<xsl:text>Draft</xsl:text>
			</xsl:if>	
			<xsl:if test="$statusValue = 'InProgress'">
				<xsl:text>InProgress</xsl:text>
			</xsl:if>	
			<xsl:if test="$statusValue = 'Reviewed'">
				<xsl:text>Reviewed</xsl:text>
			</xsl:if>		
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Team"]'>
		<xsl:variable name="teamValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">RQ_USER_18</xsl:attribute>
			<xsl:if test="$teamValue = 'Team Dev 1'">
				<xsl:text>Team Dev 1</xsl:text>
			</xsl:if>
			<xsl:if test="$teamValue = 'Team Dev 2'">
				<xsl:text>Team Dev 2</xsl:text>
			</xsl:if>
			<xsl:if test="$teamValue = 'Team Dev 3'">
				<xsl:text>Team Dev 3</xsl:text>
			</xsl:if>	
			<xsl:if test="$teamValue = 'Team QA 1'">
				<xsl:text>Team QA 1</xsl:text>
			</xsl:if>	
			<xsl:if test="$teamValue = 'Team QA 2'">
				<xsl:text>Team QA 2</xsl:text>
			</xsl:if>		
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="priority"]'>
		<xsl:variable name="priorityValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">RQ_REQ_PRIORITY</xsl:attribute>
			<xsl:if test="$priorityValue = '5'">
				<xsl:text>1-Low</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '4'">
				<xsl:text>2-Medium</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '3'">
				<xsl:text>3-High</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '2'">
				<xsl:text>4-Very High</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '1'">
				<xsl:text>5-Urgent</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Comment Text"]'>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">RQ_DEV_COMMENTS</xsl:attribute>
			<xsl:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/>
		</field>
	</xsl:template>
	<xsl:template match="text()"/>
</xsl:stylesheet>
