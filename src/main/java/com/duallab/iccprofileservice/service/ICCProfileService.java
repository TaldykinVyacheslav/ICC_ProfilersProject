package com.duallab.iccprofileservice.service;

import com.duallab.iccprofileservice.domain.ICCProfile;

import java.util.List;

/**
 * @version 1.00 11 Jan 2014
 * @author Toldykin Vyacheslav
 */
public interface ICCProfileService {
    public void addICCProfile(ICCProfile profile);
    public List<ICCProfile> getICCProfiles();
    public ICCProfile getProfile(String id);
}
