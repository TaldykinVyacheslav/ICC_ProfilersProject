package com.iccprofileservice.dao;

import com.iccprofileservice.domain.ICCProfile;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @version 1.00 11 Jan 2014
 * @author Toldykin Vyacheslav
 */
@Repository
public class ICCProfileDAOImpl implements ICCProfileDAO {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addICCProfile(ICCProfile profiler) {
        sessionFactory.getCurrentSession().saveOrUpdate(profiler);
    }

    @Override
    public List<ICCProfile> listICCProfiles() {
        return sessionFactory.getCurrentSession().createCriteria(ICCProfile.class).list();
    }

    @Override
    public ICCProfile getProfile(String id) {
        return (ICCProfile)sessionFactory.getCurrentSession().get(ICCProfile.class, id);
    }
}
