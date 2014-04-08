package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement
public class RepositoryMappingDirectionConfigList extends ForwardingList<RepositoryMappingDirectionConfig> {

    private List<RepositoryMappingDirectionConfig> repositoryMappingDirectionConfig;

    public RepositoryMappingDirectionConfigList() {
        this(new ArrayList<RepositoryMappingDirectionConfig>());
    }

    public RepositoryMappingDirectionConfigList(
            List<RepositoryMappingDirectionConfig> repositoryMappingDirectionConfig) {
        this.setRepositoryMappingDirectionConfig(repositoryMappingDirectionConfig);
    }

    public List<RepositoryMappingDirectionConfig> getRepositoryMappingDirectionConfig() {
        return repositoryMappingDirectionConfig;
    }

    public void setRepositoryMappingDirectionConfig(
            List<RepositoryMappingDirectionConfig> repositoryMappingDirectionConfig) {
        this.repositoryMappingDirectionConfig = repositoryMappingDirectionConfig;
    }

    @Override
    protected List<RepositoryMappingDirectionConfig> delegate() {
        return getRepositoryMappingDirectionConfig();
    }

}
