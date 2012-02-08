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
			<xsl:for-each select='ccf:field[@fieldName="QA contact"]'>
				<xsl:if test="position()=last()">
					<xsl:variable name="QAcontact" as="xs:string" select="." />
					<field>
						<xsl:copy-of select="@*" />
						<xsl:attribute name="fieldName">BG_USER_06</xsl:attribute>
						<xsl:if test="$QAcontact = 'alex_qc'">
							<xsl:text>alex_qc</xsl:text>
						</xsl:if>
						<xsl:if test="$QAcontact = 'alice_qc'">
							<xsl:text>alice_qc</xsl:text>
						</xsl:if>
						<xsl:if test="$QAcontact = 'cecil_qc'">
							<xsl:text>cecil_qc</xsl:text>
						</xsl:if>
					</field>
				</xsl:if>
			</xsl:for-each>
			<xsl:apply-templates />
		</artifact>
	</xsl:template>
	<xsl:template match="/ccf:artifact[@artifactType = 'attachment']">
		<xsl:copy-of select="." />
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="actualHours"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_ACTUAL_FIX_TIME</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="estimatedHours"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_ESTIMATED_FIX_TIME</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="QC-Id"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_BUG_ID</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="title"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_SUMMARY</xsl:attribute>
			<xsl:value-of select="." />
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="description"]'>
		<field>
			<xsl:copy-of select="@*" />
			<xsl:attribute name="fieldName">BG_DESCRIPTION</xsl:attribute>
			<xsl:text>&lt;html&gt;&lt;body&gt;</xsl:text>
			<xsl:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/>
			<xsl:text>&lt;/body&gt;&lt;/html&gt;</xsl:text>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="status"]'>
		<xsl:variable name="statusValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">BG_STATUS</xsl:attribute>
			<xsl:if test="$statusValue = 'Open'">
				<xsl:text>Open</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Closed'">
				<xsl:text>Closed</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = 'Pending'">
				<xsl:text>Invalid</xsl:text>
			</xsl:if>
			<xsl:if test="$statusValue = ''">
				<xsl:text>Open</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="category"]'>
		<xsl:variable name="typeValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">BG_USER_02</xsl:attribute>
			<xsl:if test="$typeValue = 'DefectCat'">
				<xsl:text>DefectCat</xsl:text>
			</xsl:if>
			<xsl:if test="$typeValue = 'PatchCat'">
				<xsl:text>PatchCat</xsl:text>
			</xsl:if>
			<xsl:if test="$typeValue = 'TaskCat'">
				<xsl:text>TaskCat</xsl:text>
			</xsl:if>
			<xsl:if test="$typeValue = 'EnhancementCat'">
				<xsl:text>EnhancementCat</xsl:text>
			</xsl:if>
			<xsl:if test="$typeValue = 'FeatureCat'">
				<xsl:text>FeatureCat</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="id"]'>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">BG_USER_01</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Detected By"]'>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">BG_DETECTED_BY</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Detected in Version"]'>
		<xsl:variable name="detectedIn" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">BG_DETECTION_VERSION</xsl:attribute>
			<xsl:if test="$detectedIn = '1.0'">
				<xsl:text>1.0</xsl:text>
			</xsl:if>
			<xsl:if test="$detectedIn = '1.1'">
				<xsl:text>1.0</xsl:text>
			</xsl:if>
			<xsl:if test="$detectedIn = '1.2'">
				<xsl:text>2.0</xsl:text>
			</xsl:if>
			<xsl:if test="$detectedIn = '2.0'">
				<xsl:text>3.0</xsl:text>
			</xsl:if>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Severity"]'>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">BG_SEVERITY</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Detected On"]'>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">BG_DETECTION_DATE</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="priority"]'>
		<xsl:variable name="priorityValue" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">BG_PRIORITY</xsl:attribute>
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
	<xsl:template match='ccf:field[@fieldName="Test Date"]'>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">BG_USER_03</xsl:attribute>
			<xsl:value-of select="."/>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="assignedTo"]'>
		<xsl:variable name="assignedTo" as="xs:string" select="."/>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">BG_RESPONSIBLE</xsl:attribute>
			<xsl:choose>
				<xsl:when test="$assignedTo != 'nobody'">
					<xsl:value-of select="."/>
				</xsl:when>
			</xsl:choose>
		</field>
	</xsl:template>
	<xsl:template match='ccf:field[@fieldName="Comment Text"]'>
		<field>
			<xsl:copy-of select="@*"/>
			<xsl:attribute name="fieldName">BG_DEV_COMMENTS</xsl:attribute>
			<xsl:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/>
		</field>
	</xsl:template>
	<xsl:template match="text()"/>
</xsl:stylesheet>
