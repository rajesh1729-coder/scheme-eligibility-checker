package com.example.scheme;

import com.example.scheme.dto.UserProfile;
import com.example.scheme.model.Scheme;
import com.example.scheme.service.SchemeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

// End-to-end integration test verifying full application context boot, H2 seeding, and scheme matching.
@SpringBootTest
class SchemeEligibilityApplicationTests {

    @Autowired
    private SchemeService schemeService;

    @Test
    void contextLoadsAndSeedsSchemes() {
        List<Scheme> allSchemes = schemeService.getAllSchemes();
        assertNotNull(allSchemes);
        assertFalse(allSchemes.isEmpty(), "Schemes should be seeded from data.sql");
        assertTrue(allSchemes.size() >= 15, "At least 15 schemes should be loaded");
    }

    @Test
    void shouldEvaluateRealSeededSchemesForSCStudent() {
        // SC student, 26 years old, annual income 150000, Maharashtra
        UserProfile user = new UserProfile(26, 150000.0, "Maharashtra", "SC", "Student");
        List<Scheme> eligible = schemeService.checkEligibility(user);

        assertNotNull(eligible);
        assertFalse(eligible.isEmpty());

        List<String> eligibleNames = eligible.stream().map(Scheme::getName).toList();

        // APY: age 18-40, ALL -> eligible
        assertTrue(eligibleNames.contains("Atal Pension Yojana (APY)"));

        // PMJJBY: age 18-50 -> eligible
        assertTrue(eligibleNames.contains("Pradhan Mantri Jeevan Jyoti Bima Yojana (PMJJBY)"));

        // PMSBY: age 18-70 -> eligible
        assertTrue(eligibleNames.contains("Pradhan Mantri Suraksha Bima Yojana (PMSBY)"));

        // PM-SYM: age 18-40, income <= 180000 -> eligible
        assertTrue(eligibleNames.contains("PM-SYM (Shram Yogi Maan-dhan)"));

        // Post Matric Scholarship for SC: SC, income <= 250000 -> eligible
        assertTrue(eligibleNames.contains("Post Matric Scholarship for SC Students"));

        // Babu Jagjivan Ram Chhatrawas: SC, age >= 16, income <= 250000 -> eligible
        assertTrue(eligibleNames.contains("Babu Jagjivan Ram Chhatrawas Yojana"));

        // IGNOAPS: age >= 60 -> NOT eligible (user is 26)
        assertFalse(eligibleNames.contains("Indira Gandhi National Old Age Pension Scheme (IGNOAPS)"));

        // Ayushman Bharat 70+: age >= 70 -> NOT eligible (user is 26)
        assertFalse(eligibleNames.contains("Ayushman Bharat PM-JAY (Senior Citizen 70+ Expansion)"));

        // Yuva Nidhi: state Karnataka -> NOT eligible (user is in Maharashtra)
        assertFalse(eligibleNames.contains("Yuva Nidhi Scheme (Karnataka)"));
    }
}
