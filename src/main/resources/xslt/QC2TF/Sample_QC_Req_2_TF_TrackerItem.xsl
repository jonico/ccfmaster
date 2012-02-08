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
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:stringutil="xalan://com.collabnet.ccf.core.utils.GATransformerUtil"
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
	<xsl:template match='ccf:field[@fieldName="RQ_USER_13"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">actualHours</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="RQ_USER_11"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">estimatedHours</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="RQ_USER_12"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Current Est. Effort</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="RQ_USER_14"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">remainingEffort</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="." />
			</field>
	</xsl:template>
		<xsl:template match='ccf:field[@fieldName="RQ_REQ_ID"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">RQ ID</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:attribute name="fieldValueType">String</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="RQ_REQ_NAME"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">title</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="RQ_REQ_COMMENT"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">description</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:choose>
				<xsl:when test="@fieldValueType='HTMLString'">
					<xsl:value-of select="stringutil:stripHTML(string(.))" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="." />
				</xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="RQ_USER_07"]'>
		<xsl:variable name="statusValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">status</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
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
	<xsl:template match='ccf:field[@fieldName="RQ_USER_18"]'>
		<xsl:variable name="teamValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Team</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
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
		<xsl:template match='ccf:field[@fieldName="RQ_REQ_PRIORITY"]'>
		<xsl:variable name="priorityValue" as="xs:string" select="." />
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">priority</xsl:attribute>
			<xsl:attribute name="fieldType">mandatoryField</xsl:attribute>
			<xsl:if test="$priorityValue = '1-Low'">
				<xsl:text>5</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '2-Medium'">
				<xsl:text>4</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '3-High'">
				<xsl:text>3</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '4-Very High'">
				<xsl:text>2</xsl:text>
			</xsl:if>
			<xsl:if test="$priorityValue = '5-Urgent'">
				<xsl:text>1</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
		<xsl:template match='ccf:field[@fieldName="RQ_DEV_COMMENTS"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">Comment Text</xsl:attribute>
			<xsl:attribute name="fieldType">flexField</xsl:attribute>
			<xsl:choose>
				<xsl:when test="@fieldValueType='HTMLString'">
					<xsl:value-of select="stringutil:stripHTML(string(.))" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="." />
				</xsl:otherwise>
			</xsl:choose>
		</field>
	</xsl:template>
	<xsl:template match="text()" />
</xsl:stylesheet>