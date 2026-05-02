package unicam.hackhub.presentation.dto.response;

import java.time.LocalDate;
import java.util.List;

public record TeamResponse(
        String name,
        String creatorEmail,
        List<String> memberEmails,
        Integer numMembers,
        Double balance,
        LocalDate registrationDate
) {}
