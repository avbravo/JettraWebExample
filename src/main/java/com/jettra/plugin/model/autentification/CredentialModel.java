package com.jettra.plugin.model.autentification;

import com.jettra.plugin.entity.autentification.Credential;
import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.ModelToRecordConversor;
import io.jettra.wui.core.annotations.PropertiesInRecord;
import io.jettra.wui.core.annotations.PropertiesLabel;
import io.jettra.wui.validations.NotNull;
import java.util.UUID;
import java.time.Instant;

@JettraViewModel
@ModelToRecordConversor(goal = Credential.class)
public class CredentialModel {

    @PropertiesInRecord
    @PropertiesLabel(value = "credential.id", label = "ID")
    private UUID id;

    @PropertiesInRecord
    @PropertiesLabel(value = "credential.userId", label = "User ID")
    private UUID userId;

    @NotNull
    @PropertiesInRecord
    @PropertiesLabel(value = "credential.username", label = "Username")
    private String username;

    @NotNull
    @PropertiesInRecord
    @PropertiesLabel(value = "credential.passwordHash", label = "Password Hash")
    private String passwordHash;

    @PropertiesInRecord
    @PropertiesLabel(value = "credential.active", label = "Active")
    private Boolean active;

    @PropertiesInRecord
    @PropertiesLabel(value = "credential.lastLogin", label = "Last Login")
    private Instant lastLogin;

    public CredentialModel() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Instant lastLogin) {
        this.lastLogin = lastLogin;
    }
}
