package com.duallab.iccprofileservice.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @version 1.00 11 Jan 2014
 * @author Toldykin Vyacheslav
 */
@XmlRootElement(name = "iccprofiles")
public class ICCProfilesDTOContainer {
    List<ICCProfileDTO> iccProfileDTOList;

    public ICCProfilesDTOContainer() {
    }

    public ICCProfilesDTOContainer(List<ICCProfileDTO> iccProfileDTOList) {
        this.iccProfileDTOList = iccProfileDTOList;
    }

    public List<ICCProfileDTO> getIccProfileDTOList() {
        return iccProfileDTOList;
    }

    @XmlElement(name = "iccprofile")
    public void setIccProfileDTOList(List<ICCProfileDTO> iccProfileDTOList) {
        this.iccProfileDTOList = iccProfileDTOList;
    }
}
