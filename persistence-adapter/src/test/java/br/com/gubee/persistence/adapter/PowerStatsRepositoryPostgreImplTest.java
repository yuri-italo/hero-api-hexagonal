package br.com.gubee.persistence.adapter;

import br.com.gubee.api.out.model.PowerStatsModelApiOut;
import br.com.gubee.api.out.requests.RegisterPowerStatsRequest;
import br.com.gubee.api.out.requests.UpdatePowerStatsRequestApiOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(properties = "spring.profiles.active=test")
//@ExtendWith(SpringExtension.class)
@Testcontainers
class PowerStatsRepositoryPostgreImplTest {
    @Autowired
    PowerStatsRepositoryPostgreImpl powerStatsRepository;

    @Autowired HeroRepositoryPostgreImpl heroRepository;

    @Container
    private static PostgreSQLContainer container = new PostgreSQLContainer("postgres:14.6");

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("jdbc.url", container::getJdbcUrl);
        registry.add("jdbc.username", container::getUsername);
        registry.add("jdbc.password", container::getPassword);
    }

    @BeforeEach
    void setUp() {
        dbCleaner();
    }

    @Test
    void registerPowerStatsSucceeds() {
        // given
        RegisterPowerStatsRequest request = createRegisterPowerStatsRequest();

        // when
        UUID uuid = powerStatsRepository.registerPowerStats(request);

        // then
        assertNotNull(uuid);

        PowerStatsModelApiOut createdPowerStats = powerStatsRepository.findById(uuid);
        assertEquals(request.getAgility(),createdPowerStats.getAgility());
        assertEquals(request.getDexterity(),createdPowerStats.getDexterity());
        assertEquals(request.getIntelligence(),createdPowerStats.getIntelligence());
        assertEquals(request.getStrength(),createdPowerStats.getStrength());
        assertNotNull(createdPowerStats.getCreatedAt());
        assertNotNull(createdPowerStats.getUpdatedAt());
    }

    @Test
    void findByIdSucceeds() {
        // given
        RegisterPowerStatsRequest request = createRegisterPowerStatsRequest();
        UUID uuid = powerStatsRepository.registerPowerStats(request);

        // when
        PowerStatsModelApiOut powerStats = powerStatsRepository.findById(uuid);

        // then
        assertNotNull(powerStats);

        assertEquals(request.getAgility(),powerStats.getAgility());
        assertEquals(request.getDexterity(),powerStats.getDexterity());
        assertEquals(request.getIntelligence(),powerStats.getIntelligence());
        assertEquals(request.getStrength(),powerStats.getStrength());
        assertNotNull(powerStats.getCreatedAt());
        assertNotNull(powerStats.getUpdatedAt());
    }

    @Test
    void findByIdReturnsNullWhenIdNotExists() {
        // given
        UUID uuid = UUID.randomUUID();

        // when
        PowerStatsModelApiOut powerStats = powerStatsRepository.findById(uuid);

        // then
        assertNull(powerStats);
    }

    @Test
    void findAllSucceeds() {
        // given
        RegisterPowerStatsRequest request = createRegisterPowerStatsRequest();
        powerStatsRepository.registerPowerStats(request);

        // when
        List<PowerStatsModelApiOut> allPowerStats = powerStatsRepository.findAll();

        // then
        assertNotNull(allPowerStats);
        assertEquals(1,allPowerStats.size());
    }

    @Test
    void updateSucceeds() {
        // given
        RegisterPowerStatsRequest request = createRegisterPowerStatsRequest();
        UUID uuid = powerStatsRepository.registerPowerStats(request);
        UpdatePowerStatsRequestApiOut updateRequest = createUpdatePowerStatsRequestApiOut();
        PowerStatsModelApiOut createdPowerStats = powerStatsRepository.findById(uuid);
        Instant createdAtBeforeUpdate = createdPowerStats.getCreatedAt();
        Instant updatedAtBeforeUpdate = createdPowerStats.getUpdatedAt();

        // when
        powerStatsRepository.update(uuid,updateRequest);

        // then
        assertNotNull(uuid);
        PowerStatsModelApiOut updatedPowerStats = powerStatsRepository.findById(uuid);
        assertEquals(updateRequest.getAgility(),updatedPowerStats.getAgility());
        assertEquals(updateRequest.getDexterity(),updatedPowerStats.getDexterity());
        assertEquals(updateRequest.getIntelligence(),updatedPowerStats.getIntelligence());
        assertEquals(updateRequest.getStrength(),updatedPowerStats.getStrength());
        assertEquals(createdAtBeforeUpdate,updatedPowerStats.getCreatedAt());
        assertNotSame(updatedAtBeforeUpdate,updatedPowerStats.getUpdatedAt());
    }

    @Test
    void delete() {
        // given
        RegisterPowerStatsRequest request = createRegisterPowerStatsRequest();
        UUID uuid = powerStatsRepository.registerPowerStats(request);

        // when
        powerStatsRepository.delete(uuid);

        // then
        assertNull(powerStatsRepository.findById(uuid));
    }

    private RegisterPowerStatsRequest createRegisterPowerStatsRequest() {
        return RegisterPowerStatsRequest.builder()
                .agility(10)
                .dexterity(10)
                .intelligence(10)
                .strength(10)
                .build();
    }

    private UpdatePowerStatsRequestApiOut createUpdatePowerStatsRequestApiOut() {
        return UpdatePowerStatsRequestApiOut.builder()
                .agility(9)
                .dexterity(8)
                .intelligence(7)
                .strength(6)
                .build();
    }

    private void dbCleaner() {
        heroRepository.findAll()
                .forEach(heroModelApiOut -> heroRepository.delete(heroModelApiOut.getId()));
        powerStatsRepository.findAll()
                .forEach(powerStatsModelApiOut -> powerStatsRepository.delete(powerStatsModelApiOut.getId()));
    }
}