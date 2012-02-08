<?xml version="1.0" encoding="UTF-8"?>
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
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xslo="alias">
	<xsl:namespace-alias stylesheet-prefix="xslo" result-prefix="xsl"/>
	<xsl:template match="/node()" priority="2">
		<xslo:stylesheet version="2.0" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns="" xmlns:stringutil="xalan://com.collabnet.ccf.core.utils.GATransformerUtil" exclude-result-prefixes="stringutil xsl xs fn ccf">
			<xsl:comment>
				<xsl:text>Automatically generated stylesheet to convert from a repository specific schema to the generic artifact format</xsl:text>
			</xsl:comment>
			<xslo:template match="node()" priority="1"/>
			<xslo:template match="/{local-name()}" priority="2">
				<artifact xmlns="http://ccf.open.collab.net/GenericArtifactV1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://ccf.open.collab.net/GenericArtifactV1.0 http://ccf.open.collab.net/files/documents/177/1972/genericartifactschema.xsd">
					<xslo:copy-of select="topLevelAttributes/@*"/>
					<xslo:apply-templates/>
				</artifact>
			</xslo:template>
			<xsl:apply-templates/>
		</xslo:stylesheet>
	</xsl:template>
	<xsl:template match="node()" priority="1"/>
	<xsl:template match="ccf:field" priority="2">
		<xslo:template match="{@alternativeFieldName}" priority="2">
			<field fieldType="{@fieldType}" fieldValueType="{@fieldValueType}" nullValueSupported="{@nullValueSupported}" minOccurs="{@minOccurs}" maxOccurs="{@maxOccurs}" fieldValueHasChanged="{@fieldValueHasChanged}" fieldAction="{@fieldAction}" xmlns="http://ccf.open.collab.net/GenericArtifactV1.0">
				<xslo:attribute name="fieldName"><xsl:value-of select="@fieldName"/></xslo:attribute>
				<xslo:attribute name="alternativeFieldName"><xsl:value-of select="@alternativeFieldName"/></xslo:attribute>
<!--				<xslo:copy-of select="@*"/>-->
				<xsl:choose>
					<xsl:when test='@nullValueSupported="true"'>
						<xslo:choose>
							<xslo:when test="text()!=''">
								<xslo:attribute name="fieldValueIsNull">false</xslo:attribute>
							</xslo:when>
							<xslo:otherwise>
								<xslo:attribute name="fieldValueIsNull">true</xslo:attribute>
							</xslo:otherwise>
						</xslo:choose>
					</xsl:when>
					<xsl:otherwise>
						<xslo:attribute name="fieldValueIsNull">false</xslo:attribute>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="@fieldValueType='HTMLString' and @fieldName != 'BG_DEV_COMMENTS' and @fieldName != 'RQ_DEV_COMMENTS'">
						<xsl:text>&lt;html&gt;&lt;body&gt;</xsl:text><xslo:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/><xsl:text>&lt;/body&gt;&lt;/html&gt;</xsl:text>
					</xsl:when>
					<xsl:when test="@fieldValueType='HTMLString' and (@fieldName = 'BG_DEV_COMMENTS' or @fieldName = 'RQ_DEV_COMMENTS')">
						<xslo:value-of select="stringutil:encodeHTMLToEntityReferences(string(.))"/>
					</xsl:when>
					<xsl:otherwise>
						<xslo:value-of select="." />
					</xsl:otherwise>
				</xsl:choose>
			</field>
		</xslo:template>
	</xsl:template>
</xsl:stylesheet>