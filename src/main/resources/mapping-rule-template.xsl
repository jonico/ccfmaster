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
        <xsl:apply-templates mode="topLevelAttribute" />
        <xsl:apply-templates mode="topLevelAttribute" select="topLevelAttributes/@*" />
        <!-- {constantTopLevelAttributeMappings}  -->
      </topLevelAttributes>
      <xsl:apply-templates mode="element" />
      <!-- {constantElementMappings} -->
    </artifact>
  </xsl:template>
  <!-- {topLevelAttributeMappings}  -->
  <!-- {elementMappings}  -->
  <xsl:template match="@*" mode="topLevelAttribute"/>
  <xsl:template match="text()" mode="topLevelAttribute"/>
  <xsl:template match="text()" mode="element"/>
</xsl:stylesheet>
