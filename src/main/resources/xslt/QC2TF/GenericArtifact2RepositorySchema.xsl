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
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:ccf="http://ccf.open.collab.net/GenericArtifactV1.0" xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns="" exclude-result-prefixes="xsl xs fn ccf">
	<xsl:variable name="rootnode" select="/node()" />
	<xsl:template match="/node()">
		<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
			elementFormDefault="qualified" attributeFormDefault="unqualified">
			<xs:element name="{local-name()}">
				<xs:annotation>
					<xs:documentation>This element contains all field names of your repository.
					</xs:documentation>
				</xs:annotation>
				<xs:complexType>
					<xs:sequence>
						<xs:element name="topLevelAttributes" minOccurs="1"
							maxOccurs="1">
							<xs:annotation>
								<xs:documentation>This element contains all top level attributes. You should always map this field to the corresponding element of the target artifact.
								</xs:documentation>
							</xs:annotation>
							<xs:complexType>
								<xs:attribute name="artifactMode" use="required">
									<xs:annotation>
										<xs:documentation>The value of this attribute could be either “complete” or “changedFieldsOnly” – which denotes how the source system has formed this artifact. Typically, source systems are expected to provide a complete artifact, but in some cases, this may not be possible or might be very imperformant (e. g. updated meta-data for
attachments), and when that happens, the source system should set this mode value to “changedFieldsOnly".</xs:documentation>
									</xs:annotation>
									<xs:simpleType>
										<xs:restriction base="xs:string">
											<xs:enumeration value="changedFieldsOnly"/>
											<xs:enumeration value="complete"/>
										</xs:restriction>
									</xs:simpleType>
								</xs:attribute>
								<xs:attribute name="artifactAction" use="required">
									<xs:annotation>
										<xs:documentation>This attribute defines if this artifact should be "create"d, "update"d, "delete"d, "resync"hed or "ignore"d by the target system. As long the artifact has not yet passed the entity service (that may change the value), the value will typically be "create" or "delete", since the source system does not know whether the artifact has been already created in the target system. The "ignore" action will cause most components to just pass the artifact without any modifications to the next component. However, some special components can still do some actions (like updating the synchronization status table) if this action occurs. The "resync" action is used to show that this artifact shipment is only done because an immediate resync after artifact creation was requested. The five possible values of action are "create", "update", "delete", "resync" and "ignore".</xs:documentation>
									</xs:annotation>
									<xs:simpleType>
										<xs:restriction base="xs:string">
											<xs:enumeration value="delete"/>
											<xs:enumeration value="update"/>
											<xs:enumeration value="create"/>
											<xs:enumeration value="resync"/>
											<xs:enumeration value="ignore"/>
											<xs:enumeration value="resync"/>
										</xs:restriction>
									</xs:simpleType>
								</xs:attribute>
								<xs:attribute name="sourceArtifactVersion" type="xs:string" use="required">
									<xs:annotation>
										<xs:documentation>This attribute contains the version of the artifact in the source system. There is one reserved value "unknown" that is used if the source system does not support version control.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="targetArtifactVersion" type="xs:string" use="required">
									<xs:annotation>
										<xs:documentation>This attribute contains the version of the artifact in the target system. There is one
					reserved value "unknown" that is used if the version in the target system is not yet known (i. e. the artifact has not yet been written into the target system in this synchronization cycle).</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="sourceArtifactLastModifiedDate" type="xs:string" use="required">
									<xs:annotation>
										<xs:documentation>This attribute contains the date when this
					artifact was lastly updated in the source system.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="targetArtifactLastModifiedDate" type="xs:string" use="required">
									<xs:annotation>
										<xs:documentation>This attribute contains the date when this
					artifact was lastly updated in the target system.  There is one reserved value "unknown" that is used if the last modification date in the target system is not yet known (i. e. the artifact has not yet been written into the target system in this synchronization cycle).</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="conflictResolutionPriority" type="xs:string" use="required">
									<xs:annotation>
										<xs:documentation>If a conflict is detected in the target system, the value of this attribute will be used to determine, whether the target artifact should be overriden or not. Reserved values are "quarantineArtifact", "alwaysIgnore" and "alwaysOverride".</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="artifactType" use="required">
									<xs:annotation>
										<xs:documentation>This attribute determines, whether this is a plain artifact that contains actual data, whether it is a dependency or association between two artifacts or whether it is an attachment that belongs to a parent artifact. Supported values: "plainArtifact", "dependency" (dependency or association) and "attachment".</xs:documentation>
									</xs:annotation>
									<xs:simpleType>
										<xs:restriction base="xs:string">
											<xs:enumeration value="plainArtifact"/>
											<xs:enumeration value="dependency"/>
											<xs:enumeration value="attachment"/>
										</xs:restriction>
									</xs:simpleType>
								</xs:attribute>
								<xs:attribute name="sourceSystemKind" type="xs:string" use="required">
									<xs:annotation>
										<xs:documentation>This attribute contains the nature of the source system of the artifact, e. g. SourceForge EnterpriseEdition 4.4, CollabNet Enterprise Edition 5.1, HP Quality Center 9.1, Conigma CCM.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="sourceSystemId" type="xs:string" use="required">
									<xs:annotation>
										<xs:documentation>This attribute contains the id of the source system. This id (typically an URL), together with the source system kind, should be unique in the whole system landscape.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="sourceRepositoryKind" type="xs:string" use="required">
									<xs:annotation>
										<xs:documentation>This attribute contains the nature of the source repository of the artifact, e. g. SourceForge EnterpriseEdition 4.4 Tracker, SourceForge EnterpriseEdition 4.4 Tracker Dependency, HP Quality Center 9.1 Defect, Conigma CCM SWFM_CR.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="sourceRepositoryId" type="xs:string" use="required">
									<xs:annotation>
										<xs:documentation>This attribute contains the id of the source repository where this artifact is coming from. This id, together with the source system id, the source system kind and the source repository kind should be sufficient to determine the source repository. There is one reserved value "unknown" that is used if the artifact cannot be directly associated to a source repository (this might be the case for dependencies, associations and attachments).</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="sourceArtifactId" type="xs:string" use="required">
									<xs:annotation>
										<xs:documentation>This attribute contains the id of the artifact in the source system. This id, together with the source system id, the source system kind, the source repository kind and the source repository id should be sufficient to indentify the artifact in the whole system landscape.There is one reserved value named "unknown" that should be used, if the artifact has no direct id in the source system (this might be the case for dependencies, associations and attachments).</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="targetSystemKind" type="xs:string" use="required">
									<xs:annotation>
										<xs:documentation>This attribute contains the nature of the target system of the artifact, e. g. SourceForge EnterpriseEdition 4.4, CollabNet Enterprise Edition 5.1, HP Quality Center 9.1, Conigma CCM.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="targetSystemId" type="xs:string" use="required">
									<xs:annotation>
										<xs:documentation>This attribute contains the id of the target system. This id (typically an URL), together with the target system kind, should be unique in the whole system landscape.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="targetRepositoryKind" type="xs:string" use="required">
									<xs:annotation>
										<xs:documentation>This attribute contains the nature of the target repository of the artifact, e. g. SourceForge EnterpriseEdition 4.4 Tracker, SourceForge EnterpriseEdition 4.4 Task Dependency, HP Quality Center 9.1 Defect Attachment, Conigma CCM SWFM_CR.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="targetRepositoryId" type="xs:string" use="required">
									<xs:annotation>
										<xs:documentation>This attribute contains the id of the target repository where this artifact  should go. This id, together with the target system id, the target system kind and the target repository kind should be sufficient to determine the target repository. There is one reserved value "unknown" that should be used if the artifact cannot be directly associated to a target repository this might be the case for dependencies , associations or attachments).</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="targetArtifactId" type="xs:string" use="required">
									<xs:annotation>
										<xs:documentation>This attribute contains the id of the artifact in the target system. This id, together with the target system id, the target system kind, the target repository kind and the target repository id should be sufficient to indentify the artifact in the whole system landscape. There is one reserved value "unknown" that should be used if the artifact cannot be associated to a target id (this might be the case for dependencies, associations and attachments), the target artifact id is unknown at this stage and/or it has not been created yet in the target repository. Typically, the value will be "unknown"until this artifact passed the entity service.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="depParentSourceRepositoryKind" type="xs:string" use="optional">
									<xs:annotation>
										<xs:documentation>This attribute contains the nature of the source repository of the parent artifact that takes part in this association/dependency respectively the nature of the source repository of the parent of this attachment respectively the nature of the parent artifact of the plain artifact.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="depParentSourceRepositoryId" type="xs:string" use="optional">
									<xs:annotation>
										<xs:documentation>This attribute contains the id of the source repository where the parent artifact that takes part in this association/dependency respectively the parent artifact of this attachment respectively the parent of this plain artifact comes from.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="depParentSourceArtifactId" type="xs:string" use="optional">
									<xs:annotation>
										<xs:documentation>This attribute contains the id of the parent artifact that takes part in the defined association/dependency , respectively the id of the parent of this attachment in the source system respectively the id of the parent of this plain artifact in the source system. If this is a plain artifact and the value of this attribute is "none", the artifact does not have any parent. If the attribute is not set or set to "unkown", you cannot assume that the plain artifact does not have a parent.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="depChildSourceRepositoryKind" type="xs:string" use="optional">
									<xs:annotation>
										<xs:documentation>This attribute is only used if the artifact type is "dependency", therefore defines a dependency or association. In this case, it contains the nature of the source repository of the child artifact that takes part in this association or dependency.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="depChildSourceRepositoryId" type="xs:string" use="optional">
									<xs:annotation>
										<xs:documentation>This attribute is only used if the artifact type is "dependency", therefore defines a dependency or association. In this case, it contains the id of the source repository where the child artifact that takes part in this association or dependency is coming from.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="depChildSourceArtifactId" type="xs:string" use="optional">
									<xs:annotation>
										<xs:documentation>This attribute is only used if the artifact type is "dependency", therefore defines a dependency or association. In this case, this attribute contains the id of the child artifact that takes part in the defined association or dependency in the source system.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="depParentTargetRepositoryKind" type="xs:string" use="optional">
									<xs:annotation>
										<xs:documentation>This attribute contains the nature of the target repository of the parent artifact that takes part in this association/dependency respectively the nature of the target repository of the parent artifact of this attachment respectively the nature of the target repository of the parent artifact of this plain artifact. Typically, the value will be "unknown" until this artifact passes the entity service.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="depParentTargetRepositoryId" type="xs:string" use="optional">
									<xs:annotation>
										<xs:documentation>This attribute contains the id of the target repository where the parent artifact that takes part in this association/dependency respectively the parent artifact of this attachment respectively the parent artifact of this plain artifact should go. This id, together with the target system id, should be sufficient to determine the target repository. Typically, the value will be "unknown" until this artifact passes the entity service.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="depParentTargetArtifactId" type="xs:string" use="optional">
									<xs:annotation>
										<xs:documentation>This attribute contains the id of the parent artifact that takes part in the defined association/dependency respectively the id of the parent artifact of this attachment respectively the id of the parent artifact of this plain artifact in the target system. Typically, the value will be "unknown" until this artifact passes the entity service.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="depChildTargetRepositoryKind" type="xs:string" use="optional">
									<xs:annotation>
										<xs:documentation>This attribute is only used if the artifact type is "dependency", therefore defines a dependency or association. In this case, it contains the nature of the target repository of the child artifact that takes part in this association or dependency. Typically, the value will be "unknown" until this artifact passes the entity service.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="depChildTargetRepositoryId" type="xs:string" use="optional">
									<xs:annotation>
										<xs:documentation>This attribute is only used if the artifact type is "dependency", therefore defines a dependency or association. In this case, it contains the id of the target repository where the child artifact that takes part in this association or dependency should go. Typically, the value will be "unknown" until this artifact passes the entity service.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="depChildTargetArtifactId" type="xs:string" use="optional">
									<xs:annotation>
										<xs:documentation>This attribute is only used if the artifact type is "dependency", therefore defines a dependency or association. In this case, this attribute contains the id of the child artifact that takes part in the defined association or dependency in the target system. Typically, the value will be "unknown" until this artifact passes the entity service.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="errorCode" type="xs:string" use="required">
									<xs:annotation>
										<xs:documentation>This attribute indicates whether an error occured during processing. The default value for this field is "ok". If this field is set to another value but "ok", components not specialized in handling errors should just ignore the artifact, so the artifactAction attribute should be set to "ignore".</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="transactionId" type="xs:string" use="required">
									<xs:annotation>
										<xs:documentation>This attribute will contain the hospital id if the artifact has been quarantined and is getting replayed. The default value of this attribute is "unknown".</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="includesFieldMetaData" use="required">
									<xs:annotation>
										<xs:documentation>This attribute indicates whether field specific meta-data like cardinality, nillability and alternative names will be shipped with this artifact. In most cases, this data will be omitted due to performance reasons, however this data might be necessary for purposes like schema-generation and graphical mapping facilities. The only allowed values are "true" and "false".</xs:documentation>
									</xs:annotation>
									<xs:simpleType>
										<xs:restriction base="xs:string">
											<xs:enumeration value="true"/>
											<xs:enumeration value="false"/>
										</xs:restriction>
									</xs:simpleType>
								</xs:attribute>
								<xs:attribute name="sourceSystemTimezone" type="xs:string" use="optional">
									<xs:annotation>
										<xs:documentation>This attribute is used to indicate the time zone of the source system. If this time zone is unknown, use "unkown" as value.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
								<xs:attribute name="targetSystemTimezone" type="xs:string" use="optional">
									<xs:annotation>
										<xs:documentation>This attribute is used to indicate the time zone of the target system. If this time zone is unknown, use "unkown" as value.</xs:documentation>
									</xs:annotation>
								</xs:attribute>
							</xs:complexType>
						</xs:element>
						<xsl:apply-templates />
					</xs:sequence>
				</xs:complexType>
			</xs:element>
		</xs:schema>
	</xsl:template>
	<xsl:template match="ccf:field">
		<xsl:choose>
			<xsl:when test='$rootnode/@includesFieldMetaData="true"'>
				<xs:element name="{@alternativeFieldName}" minOccurs="{@minOccurs}"
					maxOccurs="{@maxOccurs}" type="xs:string">
					<xs:annotation>
						<xsl:choose>
							<xsl:when test="text() != ''">
								<xs:documentation><xsl:value-of select="text()"/></xs:documentation>
							</xsl:when>
							<xsl:otherwise>
								<xs:documentation>Field name: <xsl:value-of
							select="@fieldName" /> Field type: <xsl:value-of select="@fieldType" /> Field value type: <xsl:value-of select="@fieldValueType" /> Nillable: <xsl:value-of
							select="@nullValueSupported" />
								</xs:documentation>
							</xsl:otherwise>
						</xsl:choose>
					</xs:annotation>
					<!--
						<xs:complexType> <xs:simpleContent> <xs:extension
						base="xs:string"> <xs:attribute name="fieldValueHasChanged"
						use="required"> <xs:annotation> <xs:documentation>This attribute
						may have the values "true" or "false" to state whether this
						property has been changed since the last update. This attribute
						can be used by the target system as a hint which fields to
						update.</xs:documentation> </xs:annotation> <xs:simpleType>
						<xs:restriction base="xs:string"> <xs:enumeration value="true"/>
						<xs:enumeration value="false"/> </xs:restriction> </xs:simpleType>
						</xs:attribute> <xs:attribute name="fieldAction" use="required">
						<xs:annotation> <xs:documentation>This attribute determines
						whether the content of the value tag should be "append"ed,
						"replace"d, or "delete"d. Valid values are "append", "replace" and
						"delete". Note that most systems will only support the
						"replace"-action and that source systems usually do not know
						anything about the characteristics of the target system.
						</xs:documentation> </xs:annotation> <xs:simpleType>
						<xs:restriction base="xs:string"> <xs:enumeration value="append"/>
						<xs:enumeration value="replace"/> <xs:enumeration value="delete"/>
						</xs:restriction> </xs:simpleType> </xs:attribute> </xs:extension>
						</xs:simpleContent> </xs:complexType>
					-->
				</xs:element>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>