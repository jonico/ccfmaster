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
		<xslo:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns="" exclude-result-prefixes="xsl xs fn ccf">
			<xsl:comment>
				<xsl:text>Automatically generated stylesheet to convert from the generic artifact format to a repository specific schema</xsl:text>
			</xsl:comment>
			<xslo:template match="node()" priority="1"/>
			<xslo:template match="/ccf:artifact" priority="2">
				<xsl:element name="{local-name()}">
					<xsl:element name="topLevelAttributes">
						<xslo:copy-of select="@* [not(name()='xmlns:xsi' or name() ='xsi:schemaLocation')] "/>
					</xsl:element> 
					<xslo:apply-templates/>
				</xsl:element>
			</xslo:template>
			<xsl:apply-templates/>
		</xslo:stylesheet>
	</xsl:template>
	<xsl:template match="node()" priority="1"/>
	<xsl:template match="ccf:field" priority="2">
		<xslo:template match="ccf:field[@fieldName='{@fieldName}' and @fieldType='{@fieldType}']" priority="2">
			<xsl:element name="{@alternativeFieldName}">
				<!--<xslo:copy-of select="@fieldValueHasChanged"/>
				<xslo:copy-of select="@fieldAction"/>-->
				<xslo:value-of select="text()"/>
			</xsl:element>
		</xslo:template>
	</xsl:template>
</xsl:stylesheet>
