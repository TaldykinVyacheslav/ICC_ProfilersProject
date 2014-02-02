package com.duallab.iccprofileservice.service;

import com.duallab.iccprofileservice.domain.ICCProfile;
import com.duallab.iccprofileservice.dto.ICCProfileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
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
