package com.duallab.iccprofileservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @version 1.00 11 Jan 2014
 * @author Toldykin Vyacheslav
 */
@Entity
@Table(name = "ICCPROFILES")
public class ICCProfile {

    public ICCProfile() {
    }

    public ICCProfile(String id, String type, Integer numComponents, String description) {
        this.id = id;
        this.type = type;
        this.numComponents = numComponents;
        this.description = description;
    }

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "NUM_COMPONENTS")
    private Integer numComponents;

    @Column(name = "DESCRIPTION")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNumComponents() {
        return numComponents;
    }

    public void setNumComponents(Integer numComponents) {
        this.numComponents = numComponents;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
