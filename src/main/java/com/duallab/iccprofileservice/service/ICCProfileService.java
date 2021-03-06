package com.duallab.iccprofileservice.service;

import com.duallab.iccprofileservice.domain.ICCProfile;
import java.io.IOException;
import java.util.List;

/**
 * @version 1.00 11 Jan 2014
 * @author Toldykin Vyacheslav
 */
public interface ICCProfileService {
    public ICCProfile addICCProfile(String iccProfileName, byte[] iccProfileBytes) throws IOException;
    public List<ICCProfile> getICCProfiles();
    public ICCProfile getICCProfileById(String id);
}
