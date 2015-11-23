/*
 * The MIT License
 *
 * Copyright (C) 2015 by René de Groot
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.jenkinsci.plugins.publishoveronedrive.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountInfo {

    @Expose
    private Long uid;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("name_details")
    @Expose
    private NameDetails nameDetails;
    @SerializedName("referral_link")
    @Expose
    private String referralLink;
    @Expose
    private String country;
    @Expose
    private String locale;
    @SerializedName("is_paired")
    @Expose
    private Boolean isPaired;
    @Expose
    private Team team;
    @SerializedName("quota_info")
    @Expose
    private QuotaInfo quotaInfo;

    /**
     * @return The uid
     */
    public Long getUid() {
        return uid;
    }

    /**
     * @param uid The uid
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * @return The displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName The display_name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return The nameDetails
     */
    public NameDetails getNameDetails() {
        return nameDetails;
    }

    /**
     * @param nameDetails The name_details
     */
    public void setNameDetails(NameDetails nameDetails) {
        this.nameDetails = nameDetails;
    }

    /**
     * @return The referralLink
     */
    public String getReferralLink() {
        return referralLink;
    }

    /**
     * @param referralLink The referral_link
     */
    public void setReferralLink(String referralLink) {
        this.referralLink = referralLink;
    }

    /**
     * @return The country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return The locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * @param locale The locale
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * @return The isPaired
     */
    public Boolean getIsPaired() {
        return isPaired;
    }

    /**
     * @param isPaired The is_paired
     */
    public void setIsPaired(Boolean isPaired) {
        this.isPaired = isPaired;
    }

    /**
     * @return The team
     */
    public Team getTeam() {
        return team;
    }

    /**
     * @param team The team
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * @return The quotaInfo
     */
    public QuotaInfo getQuotaInfo() {
        return quotaInfo;
    }

    /**
     * @param quotaInfo The quota_info
     */
    public void setQuotaInfo(QuotaInfo quotaInfo) {
        this.quotaInfo = quotaInfo;
    }

    public class NameDetails {

        @SerializedName("familiar_name")
        @Expose
        private String familiarName;
        @SerializedName("given_name")
        @Expose
        private String givenName;
        @Expose
        private String surname;

        /**
         * @return The familiarName
         */
        public String getFamiliarName() {
            return familiarName;
        }

        /**
         * @param familiarName The familiar_name
         */
        public void setFamiliarName(String familiarName) {
            this.familiarName = familiarName;
        }

        /**
         * @return The givenName
         */
        public String getGivenName() {
            return givenName;
        }

        /**
         * @param givenName The given_name
         */
        public void setGivenName(String givenName) {
            this.givenName = givenName;
        }

        /**
         * @return The surname
         */
        public String getSurname() {
            return surname;
        }

        /**
         * @param surname The surname
         */
        public void setSurname(String surname) {
            this.surname = surname;
        }

    }

    public class QuotaInfo {

        @Expose
        private Long shared;
        @Expose
        private Long quota;
        @Expose
        private Long normal;

        /**
         * @return The shared
         */
        public Long getShared() {
            return shared;
        }

        /**
         * @param shared The shared
         */
        public void setShared(Long shared) {
            this.shared = shared;
        }

        /**
         * @return The quota
         */
        public Long getQuota() {
            return quota;
        }

        /**
         * @param quota The quota
         */
        public void setQuota(Long quota) {
            this.quota = quota;
        }

        /**
         * @return The normal
         */
        public Long getNormal() {
            return normal;
        }

        /**
         * @param normal The normal
         */
        public void setNormal(Long normal) {
            this.normal = normal;
        }

    }

    public class Team {

        @Expose
        private String name;
        @SerializedName("team_id")
        @Expose
        private String teamId;

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The teamId
         */
        public String getTeamId() {
            return teamId;
        }

        /**
         * @param teamId The team_id
         */
        public void setTeamId(String teamId) {
            this.teamId = teamId;
        }

    }
}
