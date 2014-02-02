package com.duallab.iccprofileservice;

import com.duallab.iccprofileservice.domain.ICCProfile;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.runners.Parameterized;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.apache.commons.io.FilenameUtils;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.context.TestContextManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    private static final String RESOURCES_DIR = ".\\src\\test\\resources\\";
    private static final String PROFILES_ICC_SUBDIR = "iccprofiles\\";
    private static final String PROFILES_JSON_SUBDIR = "json\\";

    private MockMvc mockMvc;
    private File profileFile_icc;
    private File profileFile_json;
    private String profileName;
    private TestContextManager testContextManager;
    private ICCProfile iccProfileObject;

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
        profileFiles = (new File(RESOURCES_DIR + PROFILES_ICC_SUBDIR)).listFiles();
        for(File file : profileFiles) {
            parameters.add(new Object[] {FilenameUtils.removeExtension(file.getName())});
        }
        return parameters;
    }

    @Before
    public void setup() throws Exception {
        initializeTestContext();
        initializeResourceFiles();
        initializeICCProfileObject();
    }

    @Test
     public void uploadWithJsonResponseTest() throws Exception {
        MockMultipartFile multipartFile;

        multipartFile = new MockMultipartFile("profile", profileName + ".icc",
                null, (new FileInputStream(profileFile_icc)));
        mockMvc.perform(fileUpload("/iccprofiles").file(multipartFile)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$id").value(iccProfileObject.getId()))
                .andExpect(jsonPath("$type").value(iccProfileObject.getType()))
                .andExpect(jsonPath("$numComponents").value(iccProfileObject.getNumComponents()));
    }

    @Test
    public void uploadWithXmlResponseTest() throws Exception {
        MockMultipartFile multipartFile;

        multipartFile = new MockMultipartFile("profile", profileName + ".icc",
                null, (new FileInputStream(profileFile_icc)));
        mockMvc.perform(fileUpload("/iccprofiles").file(multipartFile)
                .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isCreated())
                .andExpect(xpath("/iccprofile/@id").string(iccProfileObject.getId()))
                .andExpect(xpath("/iccprofile/@type").string(iccProfileObject.getType()))
                .andExpect(xpath("/iccprofile/@numComponents").string(iccProfileObject.getNumComponents().toString()))
                .andExpect(xpath("/iccprofile/@description").string(iccProfileObject.getDescription()));
    }

    @Test
    public void getSpecificProfileTest() throws Exception {
        mockMvc.perform(get("/iccprofiles/" + iccProfileObject.getId() + "/")
                .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(xpath("/iccprofile/@id").string(iccProfileObject.getId()))
                .andExpect(xpath("/iccprofile/@type").string(iccProfileObject.getType()))
                .andExpect(xpath("/iccprofile/@numComponents").string(iccProfileObject.getNumComponents().toString()))
                .andExpect(xpath("/iccprofile/@description").string(iccProfileObject.getDescription()));
    }

    private void initializeResourceFiles() {
        profileFile_icc = new File(RESOURCES_DIR + PROFILES_ICC_SUBDIR + profileName + ".icc");
        profileFile_json = new File(RESOURCES_DIR + PROFILES_JSON_SUBDIR + profileName + ".json");
    }

    private void initializeTestContext() throws Exception {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    private void initializeICCProfileObject() throws IOException {
        iccProfileObject = (new ObjectMapper()).readValue(profileFile_json, ICCProfile.class);
    }
}