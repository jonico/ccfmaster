<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fn="http://www.w3.org/2005/xpath-functions" version="2.0" exclude-result-prefixes="xsl xs fn">
  <xsl:template match="node()">
    <mapping xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="11">  
      <component name="defaultmap1" blackbox="0" editable="1"> 
        <properties SelectedLanguage="xslt"/>  
        <structure> 
          <children> 
            <component name="constant" library="core" uid="4" kind="2"> 
              <properties/>  
              <targets> 
                <datapoint pos="0" key="73677168"/> 
              </targets>  
              <view ltx="387" lty="109" rbx="-94" rby="-46"/>  
              <data> 
                <constant datatype="string"/>  
                <wsdl/> 
              </data> 
            </component>  
            <component name="equal" library="core" uid="6" kind="5"> 
              <properties/>  
              <sources> 
                <datapoint pos="0" key="84418400"/>  
                <datapoint pos="1" key="83788504"/> 
              </sources>  
              <targets> 
                <datapoint pos="0" key="84356432"/> 
              </targets>  
              <view ltx="444" lty="87"/> 
            </component>  
            <component name="constant" library="core" uid="7" kind="2"> 
              <properties/>  
              <targets> 
                <datapoint pos="0" key="76366696"/> 
              </targets>  
              <view ltx="448" lty="149" rbx="-33" rby="-6"/>  
              <data> 
                <constant value="0" datatype="string"/>  
                <wsdl/> 
              </data> 
            </component>  
            <component name="if-else" library="core" uid="5" kind="4"> 
              <properties/>  
              <sources> 
                <datapoint pos="0" key="76354000"/>  
                <datapoint pos="1" key="76354104"/>  
                <datapoint pos="2" key="76353720"/> 
              </sources>  
              <targets> 
                <datapoint pos="0" key="83769280"/> 
              </targets>  
              <view ltx="528" lty="76" rbx="47" rby="-79"/> 
            </component>  
            <component name="constant" library="core" uid="8" kind="2"> 
              <properties/>  
              <targets> 
                <datapoint pos="0" key="84362640"/> 
              </targets>  
              <view ltx="391" lty="219" rbx="-90" rby="64"/>  
              <data> 
                <constant datatype="string"/>  
                <wsdl/> 
              </data> 
            </component>  
            <component name="constant" library="core" uid="10" kind="2"> 
              <properties/>  
              <targets> 
                <datapoint pos="0" key="84434296"/> 
              </targets>  
              <view ltx="452" lty="259" rbx="-29" rby="104"/>  
              <data> 
                <constant value="0" datatype="string"/>  
                <wsdl/> 
              </data> 
            </component>  
            <component name="equal" library="core" uid="9" kind="5"> 
              <properties/>  
              <sources> 
                <datapoint pos="0" key="84431848"/>  
                <datapoint pos="1" key="84432024"/> 
              </sources>  
              <targets> 
                <datapoint pos="0" key="84432152"/> 
              </targets>  
              <view ltx="448" lty="197" rbx="4" rby="110"/> 
            </component>  
            <component name="value-map" library="core" uid="3" kind="23"> 
              <properties/>  
              <sources> 
                <datapoint pos="0" key="73668944"/> 
              </sources>  
              <targets> 
                <datapoint pos="0" key="84276896"/> 
              </targets>  
              <view ltx="479" lty="296" rbx="14" rby="-732"/>  
              <data> 
                <wsdl/>  
                <valuemap enableDefaultValue="1"> 
                  <valuemapTable> 
                    <entry from="1-Low" to="5"/>  
                    <entry from="2-Medium" to="4"/>  
                    <entry from="3-High" to="3"/>  
                    <entry from="4-Very High" to="2"/>  
                    <entry from="5-Urgent" to="1"/> 
                  </valuemapTable>  
                  <input name="input" type="string"/>  
                  <result name="result" type="string" defaultValue="5"/> 
                </valuemap> 
              </data> 
            </component>  
            <component name="document" library="xml" uid="2" kind="14"> 
              <properties XSLTTargetEncoding="UTF-8" XSLTDefaultOutput="1"/>  
              <view ltx="689" rbx="1068" rby="1263"/>  
              <data> 
                <root scrollposition="1"> 
                  <header> 
                    <namespaces> 
                      <namespace/> 
                    </namespaces> 
                  </header>  
                  <entry name="artifact" inpkey="84277000" expanded="1"> 
                    <entry name="topLevelAttributes" inpkey="84277312"> 
                      <entry name="artifactMode" type="attribute" inpkey="84277208"/>  
                      <entry name="artifactAction" type="attribute" inpkey="84277104"/>  
                      <entry name="sourceArtifactVersion" type="attribute" inpkey="84278248"/>  
                      <entry name="targetArtifactVersion" type="attribute" inpkey="84278352"/>  
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" inpkey="84278456"/>  
                      <entry name="targetArtifactLastModifiedDate" type="attribute" inpkey="84278560"/>  
                      <entry name="conflictResolutionPriority" type="attribute" inpkey="84278752"/>  
                      <entry name="artifactType" type="attribute" inpkey="84278856"/>  
                      <entry name="sourceSystemKind" type="attribute" inpkey="84278960"/>  
                      <entry name="sourceSystemId" type="attribute" inpkey="84279064"/>  
                      <entry name="sourceRepositoryKind" type="attribute" inpkey="84279168"/>  
                      <entry name="sourceRepositoryId" type="attribute" inpkey="83809208"/>  
                      <entry name="sourceArtifactId" type="attribute" inpkey="83809312"/>  
                      <entry name="targetSystemKind" type="attribute" inpkey="83809416"/>  
                      <entry name="targetSystemId" type="attribute" inpkey="83809520"/>  
                      <entry name="targetRepositoryKind" type="attribute" inpkey="83809624"/>  
                      <entry name="targetRepositoryId" type="attribute" inpkey="83809728"/>  
                      <entry name="targetArtifactId" type="attribute" inpkey="83809832"/>  
                      <entry name="depParentSourceRepositoryKind" type="attribute" inpkey="83809936"/>  
                      <entry name="depParentSourceRepositoryId" type="attribute" inpkey="83810128"/>  
                      <entry name="depParentSourceArtifactId" type="attribute" inpkey="83810320"/>  
                      <entry name="depChildSourceRepositoryKind" type="attribute" inpkey="83810512"/>  
                      <entry name="depChildSourceRepositoryId" type="attribute" inpkey="83810704"/>  
                      <entry name="depChildSourceArtifactId" type="attribute" inpkey="83810896"/>  
                      <entry name="depParentTargetRepositoryKind" type="attribute" inpkey="83811168"/>  
                      <entry name="depParentTargetRepositoryId" type="attribute" inpkey="83811440"/>  
                      <entry name="depParentTargetArtifactId" type="attribute" inpkey="83811712"/>  
                      <entry name="depChildTargetRepositoryKind" type="attribute" inpkey="83811984"/>  
                      <entry name="depChildTargetRepositoryId" type="attribute" inpkey="84279400"/>  
                      <entry name="depChildTargetArtifactId" type="attribute" inpkey="84279672"/>  
                      <entry name="errorCode" type="attribute" inpkey="84279944"/>  
                      <entry name="transactionId" type="attribute" inpkey="84280208"/>  
                      <entry name="includesFieldMetaData" type="attribute" inpkey="84280392"/>  
                      <entry name="sourceSystemTimezone" type="attribute" inpkey="84280624"/>  
                      <entry name="targetSystemTimezone" type="attribute" inpkey="84280808"/> 
                    </entry>  
                    <entry name="actualHours" inpkey="84281576"/>  
                    <entry name="assignedTo" inpkey="84277416"/>  
                    <entry name="description" inpkey="84277520"/>  
                    <entry name="estimatedHours" inpkey="84277624"/>  
                    <entry name="priority" inpkey="84277728"/>  
                    <entry name="status" inpkey="84277832"/>  
                    <entry name="title" inpkey="84277936"/>  
                    <entry name="CommentText" inpkey="84278040"/>  
                    <entry name="QC-Id" inpkey="84278144"/> 
                  </entry> 
                </root>  
                <document schema="{@targetSchemaName}">
                  <xsl:attribute name="instanceroot">{}artifact</xsl:attribute>
                </document>  
                <wsdl/> 
              </data> 
            </component>  
            <component name="document" library="xml" uid="1" kind="14"> 
              <properties XSLTTargetEncoding="UTF-8"/>  
              <view rbx="367" rby="1262"/>  
              <data> 
                <root scrollposition="1"> 
                  <header> 
                    <namespaces> 
                      <namespace/> 
                    </namespaces> 
                  </header>  
                  <entry name="artifact" outkey="84304968" expanded="1"> 
                    <entry name="topLevelAttributes" outkey="84305712"> 
                      <entry name="artifactMode" type="attribute" outkey="84305488"/>  
                      <entry name="artifactAction" type="attribute" outkey="84305232"/>  
                      <entry name="sourceArtifactVersion" type="attribute" outkey="84307912"/>  
                      <entry name="targetArtifactVersion" type="attribute" outkey="84308144"/>  
                      <entry name="sourceArtifactLastModifiedDate" type="attribute" outkey="84308416"/>  
                      <entry name="targetArtifactLastModifiedDate" type="attribute" outkey="84308776"/>  
                      <entry name="conflictResolutionPriority" type="attribute" outkey="84309048"/>  
                      <entry name="artifactType" type="attribute" outkey="84309232"/>  
                      <entry name="sourceSystemKind" type="attribute" outkey="84309456"/>  
                      <entry name="sourceSystemId" type="attribute" outkey="84309712"/>  
                      <entry name="sourceRepositoryKind" type="attribute" outkey="84309952"/>  
                      <entry name="sourceRepositoryId" type="attribute" outkey="84310192"/>  
                      <entry name="sourceArtifactId" type="attribute" outkey="84310432"/>  
                      <entry name="targetSystemKind" type="attribute" outkey="84310672"/>  
                      <entry name="targetSystemId" type="attribute" outkey="84310912"/>  
                      <entry name="targetRepositoryKind" type="attribute" outkey="84311152"/>  
                      <entry name="targetRepositoryId" type="attribute" outkey="84311392"/>  
                      <entry name="targetArtifactId" type="attribute" outkey="84311632"/>  
                      <entry name="depParentSourceRepositoryKind" type="attribute" outkey="84311872"/>  
                      <entry name="depParentSourceRepositoryId" type="attribute" outkey="84312144"/>  
                      <entry name="depParentSourceArtifactId" type="attribute" outkey="84312416"/>  
                      <entry name="depChildSourceRepositoryKind" type="attribute" outkey="84312688"/>  
                      <entry name="depChildSourceRepositoryId" type="attribute" outkey="84312960"/>  
                      <entry name="depChildSourceArtifactId" type="attribute" outkey="84313232"/>  
                      <entry name="depParentTargetRepositoryKind" type="attribute" outkey="84313504"/>  
                      <entry name="depParentTargetRepositoryId" type="attribute" outkey="84313776"/>  
                      <entry name="depParentTargetArtifactId" type="attribute" outkey="84314048"/>  
                      <entry name="depChildTargetRepositoryKind" type="attribute" outkey="84314320"/>  
                      <entry name="depChildTargetRepositoryId" type="attribute" outkey="84314592"/>  
                      <entry name="depChildTargetArtifactId" type="attribute" outkey="84314864"/>  
                      <entry name="errorCode" type="attribute" outkey="84315136"/>  
                      <entry name="transactionId" type="attribute" outkey="84315400"/>  
                      <entry name="includesFieldMetaData" type="attribute" outkey="84315584"/>  
                      <entry name="sourceSystemTimezone" type="attribute" outkey="84315872"/>  
                      <entry name="targetSystemTimezone" type="attribute" outkey="84316112"/> 
                    </entry>  
                    <entry name="BG_ACTUAL_FIX_TIME" outkey="84334920"/>  
                    <entry name="BG_BUG_ID" outkey="84305968"/>  
                    <entry name="BG_DESCRIPTION" outkey="84306232"/>  
                    <entry name="BG_DEV_COMMENTS" outkey="84306432"/>  
                    <entry name="BG_ESTIMATED_FIX_TIME" outkey="84306672"/>  
                    <entry name="BG_PRIORITY" outkey="84306944"/>  
                    <entry name="BG_RESPONSIBLE" outkey="84307168"/>  
                    <entry name="BG_STATUS" outkey="84307424"/>  
                    <entry name="BG_SUMMARY" outkey="84307688"/> 
                  </entry> 
                </root>  
                <document schema="{@sourceSchemaName}">
                  <xsl:attribute name="instanceroot">{}artifact</xsl:attribute>
                </document>  
                <wsdl/> 
              </data> 
            </component>  
            <component name="if-else" library="core" uid="11" kind="4"> 
              <properties/>  
              <sources> 
                <datapoint pos="0" key="84436536"/>  
                <datapoint pos="1" key="84436760"/>  
                <datapoint pos="2" key="84436888"/> 
              </sources>  
              <targets> 
                <datapoint pos="0" key="84436992"/> 
              </targets>  
              <view ltx="532" lty="186" rbx="51" rby="31"/> 
            </component> 
          </children>  
          <graph directed="1"> 
            <edges/>  
            <vertices> 
              <vertex vertexkey="73677168"> 
                <edges> 
                  <edge vertexkey="84418400" edgekey="73840744"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="76366696"> 
                <edges> 
                  <edge vertexkey="76354104" edgekey="76468464"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="83769280"> 
                <edges> 
                  <edge vertexkey="84281576" edgekey="76470376"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84276896"> 
                <edges> 
                  <edge vertexkey="84277728" edgekey="84340008"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84304968"> 
                <edges> 
                  <edge vertexkey="84277000" edgekey="84341688"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84305232"> 
                <edges> 
                  <edge vertexkey="84277104" edgekey="84343184"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84305488"> 
                <edges> 
                  <edge vertexkey="84277208" edgekey="84341824"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84305712"> 
                <edges> 
                  <edge vertexkey="84277312" edgekey="84341960"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84305968"> 
                <edges> 
                  <edge vertexkey="84278144" edgekey="84342096"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84306232"> 
                <edges> 
                  <edge vertexkey="84277520" edgekey="84342232"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84306432"> 
                <edges> 
                  <edge vertexkey="84278040" edgekey="84342368"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84306672"> 
                <edges> 
                  <edge vertexkey="84432024" edgekey="76454712"/>  
                  <edge vertexkey="84436888" edgekey="76462416"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84306944"> 
                <edges> 
                  <edge vertexkey="73668944" edgekey="84342640"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84307168"> 
                <edges> 
                  <edge vertexkey="84277416" edgekey="84342776"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84307424"> 
                <edges> 
                  <edge vertexkey="84277832" edgekey="84342912"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84307688"> 
                <edges> 
                  <edge vertexkey="84277936" edgekey="84343048"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84307912"> 
                <edges> 
                  <edge vertexkey="84278248" edgekey="84343344"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84308144"> 
                <edges> 
                  <edge vertexkey="84278352" edgekey="84343528"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84308416"> 
                <edges> 
                  <edge vertexkey="84278456" edgekey="84343712"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84308776"> 
                <edges> 
                  <edge vertexkey="84278560" edgekey="84343896"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84309048"> 
                <edges> 
                  <edge vertexkey="84278752" edgekey="84344080"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84309232"> 
                <edges> 
                  <edge vertexkey="84278856" edgekey="84344264"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84309456"> 
                <edges> 
                  <edge vertexkey="84278960" edgekey="84344448"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84309712"> 
                <edges> 
                  <edge vertexkey="84279064" edgekey="84344632"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84309952"> 
                <edges> 
                  <edge vertexkey="84279168" edgekey="84344816"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84310192"> 
                <edges> 
                  <edge vertexkey="83809208" edgekey="84345000"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84310432"> 
                <edges> 
                  <edge vertexkey="83809312" edgekey="84345184"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84310672"> 
                <edges> 
                  <edge vertexkey="83809416" edgekey="84345368"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84310912"> 
                <edges> 
                  <edge vertexkey="83809520" edgekey="84345552"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84311152"> 
                <edges> 
                  <edge vertexkey="83809624" edgekey="84345736"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84311392"> 
                <edges> 
                  <edge vertexkey="83809728" edgekey="84345920"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84311632"> 
                <edges> 
                  <edge vertexkey="83809832" edgekey="84346104"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84311872"> 
                <edges> 
                  <edge vertexkey="83809936" edgekey="84346288"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84312144"> 
                <edges> 
                  <edge vertexkey="83810128" edgekey="84346472"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84312416"> 
                <edges> 
                  <edge vertexkey="83810320" edgekey="84346656"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84312688"> 
                <edges> 
                  <edge vertexkey="83810512" edgekey="84346840"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84312960"> 
                <edges> 
                  <edge vertexkey="83810704" edgekey="84347024"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84313232"> 
                <edges> 
                  <edge vertexkey="83810896" edgekey="84347208"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84313504"> 
                <edges> 
                  <edge vertexkey="83811168" edgekey="84347392"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84313776"> 
                <edges> 
                  <edge vertexkey="83811440" edgekey="84347576"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84314048"> 
                <edges> 
                  <edge vertexkey="83811712" edgekey="84347760"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84314320"> 
                <edges> 
                  <edge vertexkey="83811984" edgekey="84347944"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84314592"> 
                <edges> 
                  <edge vertexkey="84279400" edgekey="84348128"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84314864"> 
                <edges> 
                  <edge vertexkey="84279672" edgekey="84348312"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84315136"> 
                <edges> 
                  <edge vertexkey="84279944" edgekey="84348496"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84315400"> 
                <edges> 
                  <edge vertexkey="84280208" edgekey="84348680"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84315584"> 
                <edges> 
                  <edge vertexkey="84280392" edgekey="84348864"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84315872"> 
                <edges> 
                  <edge vertexkey="84280624" edgekey="84349048"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84316112"> 
                <edges> 
                  <edge vertexkey="84280808" edgekey="84349232"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84334920"> 
                <edges> 
                  <edge vertexkey="83788504" edgekey="76471720"/>  
                  <edge vertexkey="76353720" edgekey="76474408"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84356432"> 
                <edges> 
                  <edge vertexkey="76354000" edgekey="76476528"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84362640"> 
                <edges> 
                  <edge vertexkey="84431848" edgekey="76447032"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84432152"> 
                <edges> 
                  <edge vertexkey="84436536" edgekey="84449336"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84434296"> 
                <edges> 
                  <edge vertexkey="84436760" edgekey="76446792"/> 
                </edges> 
              </vertex>  
              <vertex vertexkey="84436992"> 
                <edges> 
                  <edge vertexkey="84277624" edgekey="76455488"/> 
                </edges> 
              </vertex> 
            </vertices> 
          </graph> 
        </structure> 
      </component> 
    </mapping>
  </xsl:template>
</xsl:stylesheet>
