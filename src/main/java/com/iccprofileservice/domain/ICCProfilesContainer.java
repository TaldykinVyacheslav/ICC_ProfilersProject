package com.iccprofileservice.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @version 1.00 11 Jan 2014
 * @author Toldykin Vyacheslav
 */
@XmlRootElement(name = "iccprofiles")
public class ICCProfilesContainer {
    List<ICCProfile> iccProfileList;

    public ICCProfilesContainer() {
    }

    public ICCProfilesContainer(List<ICCProfile> iccProfileList) {
        this.iccProfileList = iccProfileList;
    }

    public List<ICCProfile> getIccProfileList() {
        return iccProfileList;
    }

    @XmlElement(name = "iccprofile")
    public void setIccProfileList(List<ICCProfile> iccProfileList) {
        this.iccProfileList = iccProfileList;
    }
}
