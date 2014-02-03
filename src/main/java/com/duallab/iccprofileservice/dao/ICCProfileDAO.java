package com.duallab.iccprofileservice.dao;

import com.duallab.iccprofileservice.domain.ICCProfile;
import java.util.List;

/**
 * @version 1.00 11 Jan 2014
 * @author Toldykin Vyacheslav
 */
public interface ICCProfileDAO {
    public void addICCProfile(ICCProfile profiler);
    public ICCProfile getProfile(String id);
    public List<ICCProfile> listICCProfiles();
}
