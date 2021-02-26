/*package com.aslcittaditorino.SIMI.services;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

public interface ConfirmationService {
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public Long getDaysExpiringConfirmations();
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public void setDaysExpiringConfirmations(Long days);
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public int getNumberExpiringConfirmations();
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public List<ConfirmationDTO> getExpiringConfirmations(Long days);
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public List<ConfirmationDTO> getConfirmations(boolean active);
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public void confirm(Long id, Optional<ConfirmationDTO> body);
}
*/