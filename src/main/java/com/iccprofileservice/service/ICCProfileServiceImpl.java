package com.iccprofileservice.service;

import com.iccprofileservice.dao.ICCProfileDAO;
import com.iccprofileservice.domain.ICCProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @version 1.00 11 Jan 2014
 * @author Toldykin Vyacheslav
 */
@Service
public class ICCProfileServiceImpl implements ICCProfileService {

    @Autowired
    public ICCProfileDAO iccProfileDAO;

    @Transactional
    @Override
    public List<ICCProfile> getICCProfiles() {
        return iccProfileDAO.listICCProfiles();
    }

    @Transactional
    @Override
    public void addICCProfile(ICCProfile profile) {
        iccProfileDAO.addICCProfile(profile);
    }

    @Transactional
    @Override
    public ICCProfile getProfile(String id) {
        return iccProfileDAO.getProfile(id);
    }
}
