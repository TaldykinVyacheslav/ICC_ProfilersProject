package com.duallab.iccprofileservice.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Taldykin V.S.
 * @version 1.00 02.02.14 16:20
 */
@XmlRootElement(name = "iccprofile")
public class ICCProfileDTO {

    public ICCProfileDTO() {
    }

    public ICCProfileDTO(String id, String uri, String type, Integer numComponents, String description) {
        this.id = id;
        this.uri = uri;
        this.type = type;
        this.numComponents = numComponents;
        this.description = description;
    }

    private String id;

    private String uri;

    private String type;

    private Integer numComponents;

    private String description;

    public String getId() {
        return id;
    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    @XmlAttribute(name = "uri")
    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    @XmlAttribute(name = "type")
    public void setType(String type) {
        this.type = type;
    }

    public Integer getNumComponents() {
        return numComponents;
    }

    @XmlAttribute(name = "numComponents")
    public void setNumComponents(Integer numComponents) {
        this.numComponents = numComponents;
    }

    public String getDescription() {
        return description;
    }

    @XmlAttribute(name = "description")
    public void setDescription(String description) {
        this.description = description;
    }
}
