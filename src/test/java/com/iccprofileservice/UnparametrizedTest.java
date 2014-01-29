package com.iccprofileservice;

import com.iccprofileservice.domain.ICCProfile;
import com.iccprofileservice.utils.ICCProfileParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.awt.color.ICC_Profile;
import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @version 1.00 11 Jan 2014
 * @author Toldykin Vyacheslav
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/mvc-dispatcher-servlet.xml",
        "classpath:spring/root-context.xml", "classpath:spring/data.xml"})
public class UnparametrizedTest {
    private static final String PROFILES_DIR = ".\\src\\test\\resources\\iccprofiles\\";

    private MockMvc mockMvc;
    private ICCProfileParser parser;
    private File[] profileFiles;

    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
        profileFiles = (new File(PROFILES_DIR)).listFiles();
        parser = new ICCProfileParser();
    }

    @Test
    public void retrieveBriefProfilesInfo() throws Exception {
        ResultActions result;
        MockMultipartFile multipartFile;

        // UPLOAD ALL PROFILES ON SERVER
        for(File profileFile : profileFiles) {
            multipartFile = new MockMultipartFile("profile", profileFile.getName(),
                    null, (new FileInputStream(profileFile)));
            mockMvc.perform(fileUpload("/iccprofiles").file(multipartFile));
        }

        // GET INFORMATION IN XML ABOUT ALL THE PROFILES
        result = mockMvc.perform(get("/iccprofiles")
               .accept(MediaType.APPLICATION_XML))
               .andExpect(status().isOk());

        // CHECK RETRIEVED INFORMATION
        for(File profileFile : profileFiles) {
            ICC_Profile iccProfile;
            ICCProfile iccProfileObject;
            String xpathExpression;

            iccProfile = ICC_Profile.getInstance(new FileInputStream(profileFile));
            iccProfileObject = parser.parse(profileFile.getName(), "some URL", iccProfile);
            xpathExpression = "/iccprofiles/iccprofile[@id='" + iccProfileObject.getId() + "']/@%s";

            result
                .andExpect(xpath(String.format(xpathExpression, "id")).string(iccProfileObject.getId()));
        }
    }

    @Test
    public void retrieveFullProfilesInfo() throws Exception {
        ResultActions result;
        MockMultipartFile multipartFile;

        // UPLOAD ALL PROFILES ON SERVER
        for(File profileFile : profileFiles) {
            multipartFile = new MockMultipartFile("profile", profileFile.getName(),
                    null, (new FileInputStream(profileFile)));
            mockMvc.perform(fileUpload("/iccprofiles").file(multipartFile));
        }

        // GET INFORMATION IN XML ABOUT ALL THE PROFILES
        result = mockMvc.perform(get("/iccprofiles")
                .accept(MediaType.APPLICATION_XML)
                .param("resultType", "full"))
                .andExpect(status().isOk());

        // CHECK RETRIEVED INFORMATION
        for(File profileFile : profileFiles) {
            ICC_Profile iccProfile;
            ICCProfile iccProfileObject;
            String xpathExpression;

            iccProfile = ICC_Profile.getInstance(new FileInputStream(profileFile));
            iccProfileObject = parser.parse(profileFile.getName(), "some URL", iccProfile);
            xpathExpression = "/iccprofiles/iccprofile[@id='" + iccProfileObject.getId() + "']/@%s";

            result
                    .andExpect(xpath(String.format(xpathExpression, "id")).string(iccProfileObject.getId()))
                    .andExpect(xpath(String.format(xpathExpression, "type")).string(iccProfileObject.getType()))
                    .andExpect(xpath(String.format(xpathExpression, "numComponents")).string(iccProfileObject.getNumComponents().toString()))
                    .andExpect(xpath(String.format(xpathExpression, "description")).string(iccProfileObject.getDescription()));
        }
    }
}
