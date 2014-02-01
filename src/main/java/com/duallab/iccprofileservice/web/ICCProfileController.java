package com.duallab.iccprofileservice.web;

import com.duallab.iccprofileservice.domain.ICCProfile;
import com.duallab.iccprofileservice.domain.ICCProfilesContainer;
import com.duallab.iccprofileservice.service.ICCProfileService;
import com.duallab.iccprofileservice.utils.ICCProfileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.awt.color.ICC_Profile;
import java.io.*;
import java.util.Map;

/**
 * @version 1.00 11 Jan 2014
 * @author Toldykin Vyacheslav
 */
@Controller
public class ICCProfileController {

    @Autowired
    private ICCProfileService iccProfileService;
    private ICCProfileParser parser;

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/iccprofiles", method = RequestMethod.GET)
	public @ResponseBody
    ICCProfilesContainer getProfileInfo(@RequestParam Map<String,String> requestParams) {
        ICCProfilesContainer iccProfilesContainer;

        iccProfilesContainer = new ICCProfilesContainer(iccProfileService.getICCProfiles());

        if(!"full".equals(requestParams.get("resultType"))) {
            for(ICCProfile iccProfile : iccProfilesContainer.getIccProfileList()) {
                iccProfile.setDescription(null);
                iccProfile.setType(null);
                iccProfile.setNumComponents(null);
            }
        }
        return iccProfilesContainer;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/iccprofiles/{profileID}", method = RequestMethod.GET)
    public @ResponseBody
    ICCProfile getProfileInfo(@PathVariable String profileID) {
        System.out.println("PROFILE ID IS: " + profileID);
        return iccProfileService.getProfile(profileID);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/iccprofiles", method = RequestMethod.POST)
    public @ResponseBody ICCProfile
    uploadProfile(@RequestParam("profile") MultipartFile profile, HttpServletRequest request)
            throws IOException {
        OutputStream profileOutputStream;
        InputStream profileInputStream;
        ICC_Profile iccProfile;
        ICCProfile iccProfileObject;
        File profilerFile;
        String profileUrl;

        parser = new ICCProfileParser();
        profilerFile = new File(".\\src\\main\\resources\\iccprofiles\\"
                + profile.getOriginalFilename());
        profileUrl = request.getRequestURL() + "/" + profile.getOriginalFilename() + "/";

        profileOutputStream = new FileOutputStream(profilerFile);
        profileOutputStream.write(profile.getBytes());
        profileOutputStream.close();

        profileInputStream = new FileInputStream(profilerFile);
        iccProfile = ICC_Profile.getInstance(profileInputStream);
        iccProfileObject = parser.parse(profile.getOriginalFilename(), profileUrl, iccProfile);
        profileInputStream.close();
        iccProfileService.addICCProfile(iccProfileObject);

        return iccProfileObject;
    }
}