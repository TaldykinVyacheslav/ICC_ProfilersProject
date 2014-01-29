package com.iccprofileservice;

import com.iccprofileservice.domain.ICCProfile;
import com.iccprofileservice.utils.ICCProfileParser;
import org.junit.runners.Parameterized;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.context.TestContextManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.context.ContextConfiguration;

import java.awt.color.ICC_Profile;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @version 1.00 11 Jan 2014
 * @author Toldykin Vyacheslav
 */
@RunWith(Parameterized.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/mvc-dispatcher-servlet.xml",
        "classpath:spring/root-context.xml", "classpath:spring/data.xml"})
public class ParametrizedTest {
    private static final String PROFILES_DIR = ".\\src\\test\\resources\\iccprofiles\\";

    private MockMvc mockMvc;
    private File profileFile;
    private ICCProfileParser parser;
    private String profileName;
    private TestContextManager testContextManager;

    @Autowired
    protected WebApplicationContext wac;

    public ParametrizedTest(String profileName) {
        this.profileName = profileName;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        List parameters;
        File[] profileFiles;

        parameters = new ArrayList();
        profileFiles = (new File(PROFILES_DIR)).listFiles();
        for(File file : profileFiles) {
            parameters.add(new Object[] {file.getName()});
        }

        return parameters;
    }

    @Before
    public void setup() throws Exception {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
        profileFile = new File(PROFILES_DIR + profileName);
        parser = new ICCProfileParser();
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
     public void uploadWithJsonResponse() throws Exception {
        MockMultipartFile multipartFile;
        ICC_Profile iccProfile;
        ICCProfile iccProfileObject;

        iccProfile = ICC_Profile.getInstance(new FileInputStream(profileFile));
        iccProfileObject = parser.parse(profileName, "some URL", iccProfile);
        multipartFile = new MockMultipartFile("profile", profileName,
                null, (new FileInputStream(profileFile)));

        mockMvc.perform(fileUpload("/iccprofiles").file(multipartFile)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$id").value(iccProfileObject.getId()))
                .andExpect(jsonPath("$type").value(iccProfileObject.getType()))
                .andExpect(jsonPath("$numComponents").value(iccProfileObject.getNumComponents()));
    }

    @Test
    public void uploadWithXmlResponse() throws Exception {
        MockMultipartFile multipartFile;
        ICC_Profile iccProfile;
        ICCProfile iccProfileObject;

        iccProfile = ICC_Profile.getInstance(new FileInputStream(profileFile));
        iccProfileObject = parser.parse(profileName, "some URL", iccProfile);
        multipartFile = new MockMultipartFile("profile", profileName,
                null, (new FileInputStream(profileFile)));

        mockMvc.perform(fileUpload("/iccprofiles").file(multipartFile)
                .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isCreated())
                .andExpect(xpath("/iccprofile/@id").string(iccProfileObject.getId()))
                .andExpect(xpath("/iccprofile/@type").string(iccProfileObject.getType()))
                .andExpect(xpath("/iccprofile/@numComponents").string(iccProfileObject.getNumComponents().toString()))
                .andExpect(xpath("/iccprofile/@description").string(iccProfileObject.getDescription()));
    }

    @Test
    public void getSpecificProfile() throws Exception {
        ICC_Profile iccProfile;
        ICCProfile iccProfileObject;

        iccProfile = ICC_Profile.getInstance(new FileInputStream(profileFile));
        iccProfileObject = parser.parse(profileFile.getName(), "some URL", iccProfile);

        mockMvc.perform(get("/iccprofiles/" + iccProfileObject.getId() + "/")
                .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(xpath("/iccprofile/@id").string(iccProfileObject.getId()))
                .andExpect(xpath("/iccprofile/@type").string(iccProfileObject.getType()))
                .andExpect(xpath("/iccprofile/@numComponents").string(iccProfileObject.getNumComponents().toString()))
                .andExpect(xpath("/iccprofile/@description").string(iccProfileObject.getDescription()));
    }
}