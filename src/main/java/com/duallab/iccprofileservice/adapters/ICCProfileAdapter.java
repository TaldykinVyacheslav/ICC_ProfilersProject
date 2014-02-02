package com.duallab.iccprofileservice.adapters;

import com.duallab.iccprofileservice.domain.ICCProfile;
import com.duallab.iccprofileservice.dto.ICCProfileDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Taldykin V.S.
 * @version 1.00 02.02.14 16:50
 */
public class ICCProfileAdapter {
    public ICCProfileDTO convertToDTO(ICCProfile iccProfile) {
        return populateDTO(new ICCProfileDTO(), iccProfile);
    }

    public List<ICCProfileDTO> convertToDTO(List<ICCProfile> iccProfileList) {
        List<ICCProfileDTO> iccProfileDTOList;

        iccProfileDTOList = new ArrayList<ICCProfileDTO>();
        for(int i = 0; i < iccProfileList.size(); i++) {
            iccProfileDTOList.add(new ICCProfileDTO());
            populateDTO(iccProfileDTOList.get(i), iccProfileList.get(i));
        }
        return iccProfileDTOList;
    }

    private ICCProfileDTO populateDTO(ICCProfileDTO iccProfileDTO, ICCProfile iccProfile) {
        iccProfileDTO.setId(iccProfile.getId());
        iccProfileDTO.setType(iccProfile.getType());
        iccProfileDTO.setNumComponents(iccProfile.getNumComponents());
        iccProfileDTO.setDescription(iccProfile.getDescription());
        return iccProfileDTO;
    }
}
