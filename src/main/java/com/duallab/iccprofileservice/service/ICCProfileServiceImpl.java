package com.duallab.iccprofileservice.service;

import com.duallab.iccprofileservice.adapters.ICCProfileAdapter;
import com.duallab.iccprofileservice.dao.ICCProfileDAO;
import com.duallab.iccprofileservice.domain.ICCProfile;
import com.duallab.iccprofileservice.dto.ICCProfileDTO;
import com.duallab.iccprofileservice.utils.ICCProfileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.color.ICC_Profile;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @version 1.00 11 Jan 2014
 * @author Toldykin Vyacheslav
 */
@Service
public class ICCProfileServiceImpl implements ICCProfileService {

    @Autowired
    private ICCProfileDAO iccProfileDAO;
    @Autowired
    private ICCProfileParser iccProfileParser;
    @Value("${directoryForUploadedFiles}")
    private String directoryForUploadedFiles;

    @Transactional
    @Override
    public List<ICCProfile> getICCProfiles() {
        return iccProfileDAO.listICCProfiles();
    }

    @Transactional
    @Override
    public ICCProfile addICCProfile(String iccProfileName, byte[] iccProfileBytes) throws IOException {
        ICCProfile iccProfile;
        saveICCProfileInFileSystem(iccProfileName, iccProfileBytes);
        iccProfile = parseICCProfile(iccProfileName);
        iccProfileDAO.addICCProfile(iccProfile);
        return iccProfile;
    }

    @Transactional
    @Override
    public ICCProfile getICCProfileById(String id) {
        ICCProfile iccProfile;
        iccProfile = iccProfileDAO.getProfile(id);
        return iccProfile;
    }

    private void saveICCProfileInFileSystem(String iccProfileName, byte[] iccProfileBytes)
            throws IOException {
        OutputStream profileOutputStream;
        profileOutputStream = new FileOutputStream(directoryForUploadedFiles + iccProfileName);
        profileOutputStream.write(iccProfileBytes);
        profileOutputStream.close();
    }

    private ICCProfile parseICCProfile(String iccProfileName) throws IOException {
        InputStream profileInputStream;
        ICC_Profile iccProfile;
        ICCProfile iccProfileObject;

        profileInputStream = new FileInputStream(directoryForUploadedFiles + iccProfileName);
        iccProfile = ICC_Profile.getInstance(profileInputStream);
        iccProfileObject = iccProfileParser.parse(iccProfileName, iccProfile);
        profileInputStream.close();
        return iccProfileObject;
    }
}
