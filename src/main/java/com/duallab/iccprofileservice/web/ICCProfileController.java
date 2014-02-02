package com.duallab.iccprofileservice.web;

import com.duallab.iccprofileservice.adapters.ICCProfileAdapter;
import com.duallab.iccprofileservice.domain.ICCProfile;
import com.duallab.iccprofileservice.dto.ICCProfileDTO;
import com.duallab.iccprofileservice.dto.ICCProfilesDTOContainer;
import com.duallab.iccprofileservice.service.ICCProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @version 1.00 11 Jan 2014
 * @author Toldykin Vyacheslav
 */
@Controller
public class ICCProfileController {

    @Autowired
    private ICCProfileService iccProfileService;
    @Autowired
    private ICCProfileAdapter iccProfileAdapter;

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/iccprofiles", method = RequestMethod.GET)
	public @ResponseBody
    ICCProfilesDTOContainer getProfileInfo(@RequestParam Map<String,String> requestParams,
                                           HttpServletRequest request) {
        ICCProfilesDTOContainer iccProfilesDTOContainer;
        List<ICCProfile> iccProfileList;
        List<ICCProfileDTO> iccProfileDTOList;

        iccProfileList = iccProfileService.getICCProfiles();
        iccProfileDTOList = iccProfileAdapter.convertToDTO(iccProfileList);
        iccProfilesDTOContainer = new ICCProfilesDTOContainer(iccProfileDTOList);
        for(ICCProfileDTO iccProfileDTO : iccProfilesDTOContainer.getIccProfileDTOList()) {
            iccProfileDTO.setUri(request.getRequestURL() + "/" + iccProfileDTO.getId() + "/");
            if(!"full".equals(requestParams.get("resultType"))) {
                iccProfileDTO.setDescription(null);
                iccProfileDTO.setType(null);
                iccProfileDTO.setNumComponents(null);
            }
        }
        return iccProfilesDTOContainer;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/iccprofiles/{profileID}", method = RequestMethod.GET)
    public @ResponseBody
    ICCProfileDTO getProfileInfo(@PathVariable String profileID,
                                 HttpServletRequest request) {
        ICCProfile iccProfile;
        ICCProfileDTO iccProfileDTO;
        iccProfile = iccProfileService.getICCProfileById(profileID);
        iccProfileDTO = iccProfileAdapter.convertToDTO(iccProfile);
        iccProfileDTO.setUri(request.getRequestURL() + "/" + iccProfileDTO.getId() + "/");
        return iccProfileDTO;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/iccprofiles", method = RequestMethod.POST)
    public @ResponseBody ICCProfileDTO
    uploadProfile(@RequestParam("profile") MultipartFile profile,
                  HttpServletRequest request) throws IOException {
        ICCProfileDTO iccProfileDTO;
        ICCProfile iccProfile;
        iccProfile = iccProfileService
                .addICCProfile(profile.getOriginalFilename(), profile.getBytes());
        iccProfileDTO = iccProfileAdapter.convertToDTO(iccProfile);
        iccProfileDTO.setUri(request.getRequestURL() + "/" + iccProfileDTO.getId() + "/");
        return iccProfileDTO;
    }
}