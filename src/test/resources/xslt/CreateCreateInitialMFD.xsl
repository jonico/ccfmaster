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
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xslo="alias" xmlns="">
	<xsl:namespace-alias stylesheet-prefix="xslo" result-prefix="xsl"/>
	<xsl:param name="sourceSchema" />
	<xsl:param name="targetSchema" />
	
	<xsl:template match="/mapping" priority="2">
		<xslo:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns="" exclude-result-prefixes="xsl xs fn">
			<xslo:template match="node()">
				<xsl:apply-templates select="." mode="processDocument"/>
			</xslo:template>
		</xslo:stylesheet>
	</xsl:template>
	<xsl:template match="node()" priority="2" mode="processDocument">
		<xsl:copy>
			<xsl:copy-of select="@*"/>
			<xsl:apply-templates mode="processDocument"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="document" priority="3" mode="processDocument">
		<xsl:choose>
			<xsl:when test="@schema = $sourceSchema">
				<xsl:copy>
					<xsl:copy-of select="@*[not(name() = 'instanceroot')]"/>
					<xsl:attribute name="schema">{@sourceSchemaName}</xsl:attribute>
					<xslo:attribute name="instanceroot">
						<xsl:value-of select="@instanceroot"/>
					</xslo:attribute>
					<xsl:apply-templates mode="processDocument"/>
				</xsl:copy>
			</xsl:when>
			<xsl:when test="@schema = $targetSchema">
				<xsl:copy>
					<xsl:copy-of select="@*[not(name() = 'instanceroot')]"/>
					<xsl:attribute name="schema">{@targetSchemaName}</xsl:attribute>
					<xslo:attribute name="instanceroot">
						<xsl:value-of select="@instanceroot"/>
					</xslo:attribute>
					<xsl:apply-templates mode="processDocument"/>
				</xsl:copy>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:copy-of select="@*[not(name() = 'instanceroot')]"/>
					<xslo:attribute name="instanceroot">
						<xsl:value-of select="@instanceroot"/>
					</xslo:attribute>
					<xsl:apply-templates mode="processDocument"/>
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
