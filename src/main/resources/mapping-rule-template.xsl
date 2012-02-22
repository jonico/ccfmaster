<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
  version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:vmf="http://www.altova.com/MapForce/UDF/vmf"
  exclude-result-prefixes="vmf xs xsi xsl">
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
  <xsl:template match="/artifact">
    <artifact>
      <topLevelAttributes>
        <xsl:for-each select="topLevelAttributes/@*">
          <xsl:variable name="n"><xsl:value-of select="local-name(.)"/></xsl:variable>
          <xsl:variable name="v"><xsl:value-of select="string(.)"/></xsl:variable>
          <xsl:attribute name="{$n}"><xsl:value-of select="$v" /></xsl:attribute>
        </xsl:for-each>
        <!-- This one is for rules that produce top level attributes based on fields -->
        <xsl:apply-templates mode="topLevelAttribute" />
        <!-- This one is for rules that produce top level attributes based on top level attributes -->
        <xsl:apply-templates mode="topLevelAttribute" select="topLevelAttributes/@*" />
        <!-- {unconditional constant top level attribute mappings and custom xslt snippets that produce top level attributes}  -->
      </topLevelAttributes>
      <!-- This one is for rules that produce fields based on fields-->
      <xsl:apply-templates mode="element" />
      <!-- This one is for rules that produce fields based on top level attributes -->
      <xsl:apply-templates mode="element" select="topLevelAttributes/@*" />
      <!-- { unconditional constant element mappings and custom xslt snippets that produce fields} -->
    </artifact>
  </xsl:template>
  <!-- {topLevelAttributeMappings grouped by source}  -->
  <!-- {elementMappings grouped by source}  -->
  <xsl:template match="@*" mode="topLevelAttribute"/>
  <xsl:template match="@*" mode="element"/>
  <xsl:template match="text()" mode="topLevelAttribute"/>
  <xsl:template match="text()" mode="element"/>
</xsl:stylesheet>
