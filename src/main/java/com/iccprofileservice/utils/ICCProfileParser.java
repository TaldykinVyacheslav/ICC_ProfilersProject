package com.iccprofileservice.utils;

import com.iccprofileservice.domain.ICCProfile;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.nio.ByteBuffer;

/**
 * @version 1.00 11 Jan 2014
 * @author Toldykin Vyacheslav
 */
public class ICCProfileParser {
    public ICCProfile parse(String profileName, String uri, ICC_Profile iccProfile) {
        return new ICCProfile(profileName.replaceAll("\u0002", ""), uri, getProfileType(iccProfile),
                iccProfile.getNumComponents(), getProfileDescription(iccProfile));
    }

    private String getProfileType(ICC_Profile iccProfile) {
        String colorSpaceType;

        switch (iccProfile.getColorSpaceType()) {
            case ColorSpace.TYPE_GRAY:
                colorSpaceType = "GRAY";
                break;
            case ColorSpace.TYPE_CMYK:
                colorSpaceType = "CMYK";
                break;
            case ColorSpace.TYPE_RGB:
                colorSpaceType = "RGB";
                break;
            default:
                colorSpaceType = "UNKNOWN";
        }

        return colorSpaceType;
    }

    private String getProfileDescription(ICC_Profile iccProfile) {
        byte[] profileData;
        int profileDescriptionLength;
        String profileDescriptionString;

        profileData = iccProfile.getData(0x64657363);

        // PROCESSING ASCII SEQUENCE
        profileDescriptionLength = ByteBuffer.wrap(profileData, 8, 4).getInt();
        profileDescriptionString = new String(profileData, 12, profileDescriptionLength - 1);

        // PROCESSING UNICODE SEQUENCE
        if(profileDescriptionLength == 1) {
            profileDescriptionLength = ByteBuffer.wrap(profileData, 20, 4).getInt();
            profileDescriptionString = new String(profileData, 28, profileDescriptionLength);
        }

        return profileDescriptionString
                .replaceAll("\u0002", "")
                .replaceAll("\u0000", "");
    }
}
