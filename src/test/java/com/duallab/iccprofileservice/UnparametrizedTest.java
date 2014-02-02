package com.duallab.iccprofileservice;

import com.duallab.iccprofileservice.domain.ICCProfile;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.bind.*;
import javax.xml.xpath.XPathExpressionException;

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
    private static final String RESOURCES_DIR = ".\\src\\test\\resources\\";
    private static final String PROFILES_ICC_SUBDIR = "iccprofiles\\";
    private static final String PROFILES_XML_SUBDIR = "xml\\";

    private MockMvc mockMvc;
    private File[] profileFiles_icc;
    private File[] profileFiles_xml;

    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() throws Exception {
        initializeTestContext();
        initializeProfileFiles();
        uploadICCProfilesOnServer();
    }

    @Test
    public void retrieveBriefProfilesInfoTest() throws Exception {
        ResultActions result;
        result = getBriefICCProfilesInfoInXml();
        checkReceivedBriefInformation(result);
    }

    @Test
    public void retrieveFullProfilesInfoTest() throws Exception {
        ResultActions result;
        result = getFullICCProfilesInfoInXml();
        checkReceivedFullInformation(result);
    }

    private void initializeTestContext() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    private void initializeProfileFiles() {
        profileFiles_icc = (new File(RESOURCES_DIR + PROFILES_ICC_SUBDIR)).listFiles();
        profileFiles_xml = (new File(RESOURCES_DIR + PROFILES_XML_SUBDIR)).listFiles();
    }

    private void uploadICCProfilesOnServer() throws Exception {
        MockMultipartFile multipartFile;

        for(File profileFile : profileFiles_icc) {
            multipartFile = new MockMultipartFile("profile", profileFile.getName(),
                    null, (new FileInputStream(profileFile)));
            mockMvc.perform(fileUpload("/iccprofiles").file(multipartFile));
        }
    }

    private ResultActions getFullICCProfilesInfoInXml() throws Exception {
        return mockMvc.perform(get("/iccprofiles")
                    .accept(MediaType.APPLICATION_XML)
                    .param("resultType", "full"))
                    .andExpect(status().isOk());
    }

    private ResultActions getBriefICCProfilesInfoInXml() throws Exception {
        return mockMvc.perform(get("/iccprofiles")
                .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());
    }

    private void checkReceivedBriefInformation(ResultActions result) throws Exception {
        ICCProfile iccProfileObject;
        String xpathExpression;
        for (int i = 0; i < profileFiles_icc.length; i++) {
            iccProfileObject = unmarshalICCProfileFromXmlFile(profileFiles_xml[i]);
            xpathExpression = "/iccprofiles/iccprofile[@id='" + iccProfileObject.getId() + "']/@%s";
            result
                    .andExpect(xpath(String.format(xpathExpression, "id")).string(iccProfileObject.getId()));
        }
    }

    private void checkReceivedFullInformation(ResultActions result) throws Exception {
        ICCProfile iccProfileObject;
        String xpathExpression;

        for (int i = 0; i < profileFiles_icc.length; i++) {
            iccProfileObject = unmarshalICCProfileFromXmlFile(profileFiles_xml[i]);
            xpathExpression = "/iccprofiles/iccprofile[@id='" + iccProfileObject.getId() + "']/@%s";

            result
                .andExpect(xpath(String.format(xpathExpression, "id")).string(iccProfileObject.getId()))
                .andExpect(xpath(String.format(xpathExpression, "type")).string(iccProfileObject.getType()))
                .andExpect(xpath(String.format(xpathExpression, "numComponents")).string(iccProfileObject.getNumComponents().toString()))
                .andExpect(xpath(String.format(xpathExpression, "description")).string(iccProfileObject.getDescription()));
        }
    }

    private ICCProfile unmarshalICCProfileFromXmlFile(File profileFileInXml) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(ICCProfile.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (ICCProfile)unmarshaller.unmarshal(profileFileInXml);
    }
}
