<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fn="http://www.w3.org/2005/xpath-functions" version="2.0" exclude-result-prefixes="xsl xs fn">
  <xsl:template match="node()">
    <mapping xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="11">  
      <component name="defaultmap1" blackbox="0" editable="1"> 
        <properties SelectedLanguage="xslt"/>  
        <structure> 
          <children> 
            <component name="constant" library="core" uid="12" kind="2"> 
              <properties/>  
              <targets> 
                <datapoint pos="0" key="73665400"/> 
              </targets>  
              <view ltx="413" lty="343" rbx="-206" rby="-497"/>  
              <data> 
                <constant value="nobody" datatype="string"/>  
                <wsdl/> 
              </data> 
            </component>  
            <component name="constant" library="core" uid="9" kind="2"> 
              <properties/>  
              <targets> 
                <datapoint pos="0" key="73618728"/> 
              </targets>  
              <view ltx="536" lty="384" rbx="-264" rby="-489"/>  
              <data> 
                <constant datatype="string"/>  
                <wsdl/> 
              </data> 
            </component>  
            <component name="equal" library="core" uid="10" kind="5"> 
              <properties/>  
              <sources> 
                <datapoint pos="0" key="83816352"/>  
                <datapoint pos="1" key="81786168"/> 
              </sources>  
              <targets> 
                <datapoint pos="0" key="81786272"/> 
              </targets>  
              <view ltx="526" lty="322" rbx="-129" rby="-388"/> 
            </component>  
            <component name="document" library="xml" uid="2" kind="14"> 
              <properties XSLTTargetEncoding="UTF-8" XSLTDefaultOutput="1"/>  
              <view ltx="746" lty="-9" rbx="1122" rby="918"/>  
              <data> 
                <root scrollposition="1"> 
                  <header> 
                    <namespaces> 
                      <namespace/> 
                    </namespaces> 
                  </header>  
                  <entry name="artifact" inpkey="84304768" expanded="1"> 
                    <entry name="topLevelAttributes" inpkey="76350456"> 
                      <entry name="artifactMode" type="attribute" inpkey="84306128"/>  
                      <entry name="artifactAction" type="attribute" inpkey="84293480"/>  
                      <entry name="sourceArtifactVersion" type="attribute" inpkey="84302576"/>  
                      <entry name="targetArtifactVersion" type="attribute" inpkey="83783792"/>  
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" inpkey="84302336"/>  
                      <entry name="targetArtifactLastModifiedDate" type="attribute" inpkey="84302048"/>  
                      <entry name="conflictResolutionPriority" type="attribute" inpkey="84301864"/>  
                      <entry name="artifactType" type="attribute" inpkey="84301600"/>  
                      <entry name="sourceSystemKind" type="attribute" inpkey="84301328"/>  
                      <entry name="sourceSystemId" type="attribute" inpkey="84301056"/>  
                      <entry name="sourceRepositoryKind" type="attribute" inpkey="84300784"/>  
                      <entry name="sourceRepositoryId" type="attribute" inpkey="84300512"/>  
                      <entry name="sourceArtifactId" type="attribute" inpkey="84300240"/>  
                      <entry name="targetSystemKind" type="attribute" inpkey="84299968"/>  
                      <entry name="targetSystemId" type="attribute" inpkey="84299696"/>  
                      <entry name="targetRepositoryKind" type="attribute" inpkey="84299424"/>  
                      <entry name="targetRepositoryId" type="attribute" inpkey="84299152"/>  
                      <entry name="targetArtifactId" type="attribute" inpkey="84298880"/>  
                      <entry name="depParentSourceRepositoryKind" type="attribute" inpkey="84298608"/>  
                      <entry name="depParentSourceRepositoryId" type="attribute" inpkey="84298336"/>  
                      <entry name="depParentSourceArtifactId" type="attribute" inpkey="84298096"/>  
                      <entry name="depChildSourceRepositoryKind" type="attribute" inpkey="84297856"/>  
                      <entry name="depChildSourceRepositoryId" type="attribute" inpkey="84297616"/>  
                      <entry name="depChildSourceArtifactId" type="attribute" inpkey="84309640"/>  
                      <entry name="depParentTargetRepositoryKind" type="attribute" inpkey="84297136"/>  
                      <entry name="depParentTargetRepositoryId" type="attribute" inpkey="84296896"/>  
                      <entry name="depParentTargetArtifactId" type="attribute" inpkey="84309400"/>  
                      <entry name="depChildTargetRepositoryKind" type="attribute" inpkey="84296656"/>  
                      <entry name="depChildTargetRepositoryId" type="attribute" inpkey="84309160"/>  
                      <entry name="depChildTargetArtifactId" type="attribute" inpkey="84296416"/>  
                      <entry name="errorCode" type="attribute" inpkey="84308920"/>  
                      <entry name="transactionId" type="attribute" inpkey="84296176"/>  
                      <entry name="includesFieldMetaData" type="attribute" inpkey="84308680"/>  
                      <entry name="sourceSystemTimezone" type="attribute" inpkey="84295920"/>  
                      <entry name="targetSystemTimezone" type="attribute" inpkey="84308384"/> 
                    </entry>  
                    <entry name="BG_ACTUAL_FIX_TIME" inpkey="76350560"/>  
                    <entry name="BG_DESCRIPTION" inpkey="76351392"/>  
                    <entry name="BG_DEV_COMMENTS" inpkey="76351808"/>  
                    <entry name="BG_ESTIMATED_FIX_TIME" inpkey="76351992"/>  
                    <entry name="BG_PRIORITY" inpkey="76352816"/>  
                    <entry name="BG_RESPONSIBLE" inpkey="76354144"/>  
                    <entry name="BG_STATUS" inpkey="76354696"/>  
                    <entry name="BG_SUMMARY" inpkey="76355328"/> 
                  </entry> 
                </root>  
                <document schema="{@targetSchemaName}">
                  <xsl:attribute name="instanceroot">{}artifact</xsl:attribute>
                </document>  
                <wsdl/> 
              </data> 
            </component>  
            <component name="if-else" library="core" uid="11" kind="4"> 
              <properties/>  
              <sources> 
                <datapoint pos="0" key="81786816"/>  
                <datapoint pos="1" key="81786920"/>  
                <datapoint pos="2" key="81787024"/> 
              </sources>  
              <targets> 
                <datapoint pos="0" key="84356144"/> 
              </targets>  
              <view ltx="623" lty="325" rbx="158" rby="-484"/> 
            </component>  
            <component name="value-map" library="core" uid="3" kind="23"> 
              <properties/>  
              <sources> 
                <datapoint pos="0" key="83815960"/> 
              </sources>  
              <targets> 
                <datapoint pos="0" key="83816064"/> 
              </targets>  
              <view ltx="503" lty="241" rbx="38" rby="-787"/>  
              <data> 
                <wsdl/>  
                <valuemap enableDefaultValue="1"> 
                  <valuemapTable> 
                    <entry from="5" to="1-Low"/>  
                    <entry from="4" to="2-Medium"/>  
                    <entry from="3" to="3-High"/>  
                    <entry from="2" to="4-Very High"/>  
                    <entry from="1" to="5-Urgent"/> 
                  </valuemapTable>  
                  <input name="input" type="string"/>  
                  <result name="result" type="string" defaultValue="1-Low"/> 
                </valuemap> 
              </data> 
            </component>  
            <component name="document" library="xml" uid="1" kind="14"> 
              <properties XSLTTargetEncoding="UTF-8"/>  
              <view ltx="-18" lty="-9" rbx="367" rby="918"/>  
              <data> 
                <root scrollposition="1"> 
                  <header> 
                    <namespaces> 
                      <namespace/> 
                    </namespaces> 
                  </header>  
                  <entry name="artifact" outkey="83816640" expanded="1"> 
                    <entry name="topLevelAttributes" outkey="84246208"> 
                      <entry name="artifactMode" type="attribute" outkey="84242512"/>  
                      <entry name="artifactAction" type="attribute" outkey="83816536"/>  
                      <entry name="sourceArtifactVersion" type="attribute" outkey="83818072"/>  
                      <entry name="targetArtifactVersion" type="attribute" outkey="83818176"/>  
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" outkey="83818280"/>  
                      <entry name="targetArtifactLastModifiedDate" type="attribute" outkey="83818472"/>  
                      <entry name="conflictResolutionPriority" type="attribute" outkey="83818664"/>  
                      <entry name="artifactType" type="attribute" outkey="83818768"/>  
                      <entry name="sourceSystemKind" type="attribute" outkey="83818872"/>  
                      <entry name="sourceSystemId" type="attribute" outkey="83818976"/>  
                      <entry name="sourceRepositoryKind" type="attribute" outkey="83819080"/>  
                      <entry name="sourceRepositoryId" type="attribute" outkey="83819184"/>  
                      <entry name="sourceArtifactId" type="attribute" outkey="83819288"/>  
                      <entry name="targetSystemKind" type="attribute" outkey="83819392"/>  
                      <entry name="targetSystemId" type="attribute" outkey="83819496"/>  
                      <entry name="targetRepositoryKind" type="attribute" outkey="83819600"/>  
                      <entry name="targetRepositoryId" type="attribute" outkey="83819704"/>  
                      <entry name="targetArtifactId" type="attribute" outkey="83819808"/>  
                      <entry name="depParentSourceRepositoryKind" type="attribute" outkey="83819912"/>  
                      <entry name="depParentSourceRepositoryId" type="attribute" outkey="83820104"/>  
                      <entry name="depParentSourceArtifactId" type="attribute" outkey="84269856"/>  
                      <entry name="depChildSourceRepositoryKind" type="attribute" outkey="84270048"/>  
                      <entry name="depChildSourceRepositoryId" type="attribute" outkey="84270240"/>  
                      <entry name="depChildSourceArtifactId" type="attribute" outkey="84270432"/>  
                      <entry name="depParentTargetRepositoryKind" type="attribute" outkey="84270624"/>  
                      <entry name="depParentTargetRepositoryId" type="attribute" outkey="84270816"/>  
                      <entry name="depParentTargetArtifactId" type="attribute" outkey="84271008"/>  
                      <entry name="depChildTargetRepositoryKind" type="attribute" outkey="84271200"/>  
                      <entry name="depChildTargetRepositoryId" type="attribute" outkey="84271392"/>  
                      <entry name="depChildTargetArtifactId" type="attribute" outkey="84271584"/>  
                      <entry name="errorCode" type="attribute" outkey="84271776"/>  
                      <entry name="transactionId" type="attribute" outkey="84271880"/>  
                      <entry name="includesFieldMetaData" type="attribute" outkey="84271984"/>  
                      <entry name="sourceSystemTimezone" type="attribute" outkey="84272176"/>  
                      <entry name="targetSystemTimezone" type="attribute" outkey="84272280"/> 
                    </entry>  
                    <entry name="actualHours" outkey="84287208"/>  
                    <entry name="assignedTo" outkey="83817344"/>  
                    <entry name="description" outkey="83817448"/>  
                    <entry name="estimatedHours" outkey="83817552"/>  
                    <entry name="priority" outkey="83817656"/>  
                    <entry name="status" outkey="83817760"/>  
                    <entry name="title" outkey="83817864"/>  
                    <entry name="CommentText" outkey="83817968"/> 
                  </entry> 
                </root>  
                <document schema="{@sourceSchemaName}">
                  <xsl:attribute name="instanceroot">{}artifact</xsl:attribute>
                </document>  
                <wsdl/> 
              </data> 
            </component> 
          </children>  
          <graph directed="1"> 
            <edges/>  
            <vertices> 
              <vertex vertexkey="73618728"> 
                <edges> 
                  <edge vertexkey="81786920" edgekey="84314696"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="73665400"> 
                <edges> 
                  <edge vertexkey="83816352" edgekey="84317256"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="81786272"> 
                <edges> 
                  <edge vertexkey="81786816" edgekey="84317528"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83816064"> 
                <edges> 
                  <edge vertexkey="76352816" edgekey="76469432"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83816536"> 
                <edges> 
                  <edge vertexkey="84293480" edgekey="84647376"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83816640"> 
                <edges> 
                  <edge vertexkey="84304768" edgekey="84593240"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83817344"> 
                <edges> 
                  <edge vertexkey="81786168" edgekey="84318208"/>  
                  <edge vertexkey="81787024" edgekey="84318344"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83817448"> 
                <edges> 
                  <edge vertexkey="76351392" edgekey="84265640"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83817552"> 
                <edges> 
                  <edge vertexkey="76351992" edgekey="76465968"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83817656"> 
                <edges> 
                  <edge vertexkey="83815960" edgekey="84318752"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83817760"> 
                <edges> 
                  <edge vertexkey="76354696" edgekey="76466048"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83817864"> 
                <edges> 
                  <edge vertexkey="76355328" edgekey="76471448"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83817968"> 
                <edges> 
                  <edge vertexkey="76351808" edgekey="76469832"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83818072"> 
                <edges> 
                  <edge vertexkey="84302576" edgekey="84644208"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83818176"> 
                <edges> 
                  <edge vertexkey="83783792" edgekey="84644264"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83818280"> 
                <edges> 
                  <edge vertexkey="84302336" edgekey="84647488"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83818472"> 
                <edges> 
                  <edge vertexkey="84302048" edgekey="84650264"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83818664"> 
                <edges> 
                  <edge vertexkey="84301864" edgekey="84650320"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83818768"> 
                <edges> 
                  <edge vertexkey="84301600" edgekey="84650376"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83818872"> 
                <edges> 
                  <edge vertexkey="84301328" edgekey="84649040"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83818976"> 
                <edges> 
                  <edge vertexkey="84301056" edgekey="84653576"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83819080"> 
                <edges> 
                  <edge vertexkey="84300784" edgekey="84644488"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83819184"> 
                <edges> 
                  <edge vertexkey="84300512" edgekey="84644544"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83819288"> 
                <edges> 
                  <edge vertexkey="84300240" edgekey="84644640"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83819392"> 
                <edges> 
                  <edge vertexkey="84299968" edgekey="84644736"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83819496"> 
                <edges> 
                  <edge vertexkey="84299696" edgekey="84644832"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83819600"> 
                <edges> 
                  <edge vertexkey="84299424" edgekey="84644928"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83819704"> 
                <edges> 
                  <edge vertexkey="84299152" edgekey="84645024"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83819808"> 
                <edges> 
                  <edge vertexkey="84298880" edgekey="84645120"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83819912"> 
                <edges> 
                  <edge vertexkey="84298608" edgekey="84645216"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83820104"> 
                <edges> 
                  <edge vertexkey="84298336" edgekey="84668304"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84242512"> 
                <edges> 
                  <edge vertexkey="84306128" edgekey="84644152"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84246208"> 
                <edges> 
                  <edge vertexkey="76350456" edgekey="84315024"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84269856"> 
                <edges> 
                  <edge vertexkey="84298096" edgekey="84667136"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84270048"> 
                <edges> 
                  <edge vertexkey="84297856" edgekey="84668248"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84270240"> 
                <edges> 
                  <edge vertexkey="84297616" edgekey="84669504"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84270432"> 
                <edges> 
                  <edge vertexkey="84309640" edgekey="84670600"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84270624"> 
                <edges> 
                  <edge vertexkey="84297136" edgekey="84671696"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84270816"> 
                <edges> 
                  <edge vertexkey="84296896" edgekey="84672792"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84271008"> 
                <edges> 
                  <edge vertexkey="84309400" edgekey="84673888"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84271200"> 
                <edges> 
                  <edge vertexkey="84296656" edgekey="84674984"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84271392"> 
                <edges> 
                  <edge vertexkey="84309160" edgekey="84676080"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84271584"> 
                <edges> 
                  <edge vertexkey="84296416" edgekey="84677176"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84271776"> 
                <edges> 
                  <edge vertexkey="84308920" edgekey="84678272"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84271880"> 
                <edges> 
                  <edge vertexkey="84296176" edgekey="84679544"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84271984"> 
                <edges> 
                  <edge vertexkey="84308680" edgekey="84682088"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84272176"> 
                <edges> 
                  <edge vertexkey="84295920" edgekey="84680816"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84272280"> 
                <edges> 
                  <edge vertexkey="84308384" edgekey="84646952"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84287208"> 
                <edges> 
                  <edge vertexkey="76350560" edgekey="76456952"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84356144"> 
                <edges> 
                  <edge vertexkey="76354144" edgekey="84248576"/> 
                </edges> 
              </vertex> 
            </vertices> 
          </graph> 
        </structure> 
      </component> 
    </mapping>
  </xsl:template>
</xsl:stylesheet>
