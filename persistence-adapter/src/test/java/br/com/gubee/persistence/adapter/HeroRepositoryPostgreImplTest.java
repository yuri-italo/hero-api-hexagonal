package br.com.gubee.persistence.adapter;

import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.api.out.requests.RegisterHeroRequest;
import br.com.gubee.api.out.requests.RegisterPowerStatsRequest;
import br.com.gubee.api.out.requests.UpdateHeroRequestApiOut;
import br.com.gubee.persistence.adapter.configuration.JdbcConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(classes = {HeroRepositoryPostgreImpl.class, PowerStatsRepositoryPostgreImpl.class, JdbcConfiguration.class})
class HeroRepositoryPostgreImplTest {
    @Autowired
    HeroRepositoryPostgreImpl heroRepository;
    @Autowired
    PowerStatsRepositoryPostgreImpl powerStatsRepository;
    @BeforeEach
    void setUp() {
        dbCleaner();
    }

    @Test
    void registerHeroSucceeds() {
        // given
        RegisterPowerStatsRequest registerPowerStatsRequest = createRegisterPowerStatsRequest();
        RegisterHeroRequest registerHeroRequest = createRegisterHeroRequest();
        UUID powerStatsId = powerStatsRepository.registerPowerStats(registerPowerStatsRequest);

        // when
        UUID uuid = heroRepository.registerHero(registerHeroRequest,powerStatsId);

        // then
        assertNotNull(powerStatsId);
        assertNotNull(uuid);

        HeroModelApiOut createdHero = heroRepository.findById(uuid).get();
        assertEquals(uuid,createdHero.getId());
        assertEquals(registerHeroRequest.getName(),createdHero.getName());
        assertEquals(registerHeroRequest.getRace(),createdHero.getRace());
        assertEquals(powerStatsId,createdHero.getPowerStatsId());
        assertTrue(createdHero.isEnabled());
        assertNotNull(createdHero.getCreatedAt());
        assertNotNull(createdHero.getUpdatedAt());
    }

    @Test
    void findByIdSucceeds() {
        // given
        RegisterPowerStatsRequest registerPowerStatsRequest = createRegisterPowerStatsRequest();
        RegisterHeroRequest registerHeroRequest = createRegisterHeroRequest();
        UUID powerStatsId = powerStatsRepository.registerPowerStats(registerPowerStatsRequest);
        UUID uuid = heroRepository.registerHero(registerHeroRequest,powerStatsId);

        // when
        Optional<HeroModelApiOut> heroModelApiOut = heroRepository.findById(uuid);

        // then
        assertTrue(heroModelApiOut.isPresent());

        HeroModelApiOut foundHero = heroModelApiOut.get();
        assertEquals(uuid,foundHero.getId());
        assertEquals(registerHeroRequest.getName(),foundHero.getName());
        assertEquals(registerHeroRequest.getRace(),foundHero.getRace());
        assertEquals(powerStatsId,foundHero.getPowerStatsId());
        assertNotNull(foundHero.getCreatedAt());
        assertNotNull(foundHero.getUpdatedAt());
    }

    @Test
    void findByIdShouldReturnEmptyOptionalWhenIdNotExists() {
        // given
        UUID uuid = UUID.randomUUID();

        // when
        Optional<HeroModelApiOut> heroModelApiOut = heroRepository.findById(uuid);

        // then
        assertTrue(heroModelApiOut.isEmpty());
    }

    @Test
    void findManyByNameReturnsListWithTwoHeroes() {
        // given
        String search = "man";
        RegisterPowerStatsRequest registerPowerStatsRequest = createRegisterPowerStatsRequest();
        RegisterHeroRequest registerHeroRequest = createRegisterHeroRequest();
        UUID powerStatsId = powerStatsRepository.registerPowerStats(registerPowerStatsRequest);
        UUID uuid = heroRepository.registerHero(registerHeroRequest,powerStatsId);

        RegisterPowerStatsRequest registerPowerStatsRequest2 = createAnotherRegisterPowerStatsRequest();
        RegisterHeroRequest registerHeroRequest2 = createAnotherRegisterHeroRequest();
        UUID powerStatsId2 = powerStatsRepository.registerPowerStats(registerPowerStatsRequest2);
        UUID uuid2 = heroRepository.registerHero(registerHeroRequest2,powerStatsId2);

        // when
        List<HeroModelApiOut> heroes = heroRepository.findManyByName(search);

        // then
        assertNotNull(heroes);
        assertEquals(2,heroes.size());
        assertEquals(heroes.get(0).getId(),uuid);
        assertEquals(heroes.get(1).getId(),uuid2);
        assertTrue(heroes.get(0).getName().contains(search));
        assertTrue(heroes.get(1).getName().contains(search));
    }

    @Test
    void findManyByNameReturnsListWithZeroHeroesWhenSearchHasNoMatch() {
        // given
        String search = "Spider";
        RegisterPowerStatsRequest registerPowerStatsRequest = createRegisterPowerStatsRequest();
        RegisterHeroRequest registerHeroRequest = createRegisterHeroRequest();
        UUID powerStatsId = powerStatsRepository.registerPowerStats(registerPowerStatsRequest);
        heroRepository.registerHero(registerHeroRequest,powerStatsId);

        RegisterPowerStatsRequest registerPowerStatsRequest2 = createAnotherRegisterPowerStatsRequest();
        RegisterHeroRequest registerHeroRequest2 = createAnotherRegisterHeroRequest();
        UUID powerStatsId2 = powerStatsRepository.registerPowerStats(registerPowerStatsRequest2);
        heroRepository.registerHero(registerHeroRequest2,powerStatsId2);

        // when
        List<HeroModelApiOut> heroes = heroRepository.findManyByName(search);

        // then
        assertNotNull(heroes);
        assertEquals(0,heroes.size());
        assertFalse(registerHeroRequest.getName().contains(search));
        assertFalse(registerHeroRequest2.getName().contains(search));
    }

    @Test
    void findByNameReturnsOneHeroWithNameMatchingSearch() {
        // given
        String search = "Batman";

        RegisterPowerStatsRequest registerPowerStatsRequest = createRegisterPowerStatsRequest();
        RegisterHeroRequest registerHeroRequest = createRegisterHeroRequest();
        UUID powerStatsId = powerStatsRepository.registerPowerStats(registerPowerStatsRequest);
        heroRepository.registerHero(registerHeroRequest, powerStatsId);


        // when
        Optional<HeroModelApiOut> heroModelApiOut = heroRepository.findByName(search);

        // then
        assertTrue(heroModelApiOut.isPresent());

        HeroModelApiOut hero = heroModelApiOut.get();
        assertTrue(hero.getName().toUpperCase().contains(search.toUpperCase()));
    }

    @Test
    void findByReturnsEmptyOptionalWhenSearchDoesNotMatchAnyHeroName() {
        // given
        String search = "Spider";

        RegisterPowerStatsRequest registerPowerStatsRequest = createRegisterPowerStatsRequest();
        RegisterHeroRequest registerHeroRequest = createRegisterHeroRequest();
        UUID powerStatsId = powerStatsRepository.registerPowerStats(registerPowerStatsRequest);
        heroRepository.registerHero(registerHeroRequest, powerStatsId);

        // when
        Optional<HeroModelApiOut> heroModelApiOut = heroRepository.findByName(search);

        // then
        assertTrue(heroModelApiOut.isEmpty());
        assertFalse(registerHeroRequest.getName().toUpperCase().contains(search.toUpperCase()));
    }

    @Test
    void findAllReturnsAllCreatedHeroes() {
        // given
        RegisterPowerStatsRequest registerPowerStatsRequest = createRegisterPowerStatsRequest();
        RegisterHeroRequest registerHeroRequest = createRegisterHeroRequest();
        UUID powerStatsId = powerStatsRepository.registerPowerStats(registerPowerStatsRequest);
        UUID uuid = heroRepository.registerHero(registerHeroRequest,powerStatsId);

        RegisterPowerStatsRequest registerPowerStatsRequest2 = createAnotherRegisterPowerStatsRequest();
        RegisterHeroRequest registerHeroRequest2 = createAnotherRegisterHeroRequest();
        UUID powerStatsId2 = powerStatsRepository.registerPowerStats(registerPowerStatsRequest2);
        UUID uuid2 = heroRepository.registerHero(registerHeroRequest2,powerStatsId2);

        // when
        List<HeroModelApiOut> heroes = heroRepository.findAll();

        // then
        assertNotNull(heroes);
        assertEquals(2,heroes.size());
        assertEquals(uuid,heroes.get(0).getId());
        assertEquals(uuid2,heroes.get(1).getId());
    }

    @Test
    void findAllReturnsEmptyListWhenThereIsNoHeroesCreated() {
        // when
        List<HeroModelApiOut> heroes = heroRepository.findAll();

        // then
        assertNotNull(heroes);
        assertEquals(0,heroes.size());
    }


    @Test
    void updateSucceeds() {
        // given
        RegisterPowerStatsRequest registerPowerStatsRequest = createRegisterPowerStatsRequest();
        RegisterHeroRequest registerHeroRequest = createRegisterHeroRequest();
        UUID powerStatsId = powerStatsRepository.registerPowerStats(registerPowerStatsRequest);
        UUID uuid = heroRepository.registerHero(registerHeroRequest,powerStatsId);
        HeroModelApiOut createdHero = heroRepository.findById(uuid).get();

        UpdateHeroRequestApiOut request = createUpdateHeroRequestApiOut();

        // when
        heroRepository.update(uuid,request);

        // then
        HeroModelApiOut updatedHero = heroRepository.findById(uuid).get();
        assertEquals(uuid,updatedHero.getId());
        assertEquals(request.getName(),updatedHero.getName());
        assertEquals(request.getRace(),updatedHero.getRace());
        assertNotNull(updatedHero.getCreatedAt());
        assertNotSame(createdHero.getUpdatedAt(), updatedHero.getUpdatedAt());
    }

    @Test
    void deleteSucceeds() {
        // given
        RegisterPowerStatsRequest registerPowerStatsRequest = createRegisterPowerStatsRequest();
        RegisterHeroRequest registerHeroRequest = createRegisterHeroRequest();
        UUID powerStatsId = powerStatsRepository.registerPowerStats(registerPowerStatsRequest);
        heroRepository.registerHero(registerHeroRequest,powerStatsId);

        RegisterPowerStatsRequest registerPowerStatsRequest2 = createAnotherRegisterPowerStatsRequest();
        RegisterHeroRequest registerHeroRequest2 = createAnotherRegisterHeroRequest();
        UUID powerStatsId2 = powerStatsRepository.registerPowerStats(registerPowerStatsRequest2);
        UUID uuid2 = heroRepository.registerHero(registerHeroRequest2,powerStatsId2);

        // when
        heroRepository.delete(uuid2);

        // then
        assertEquals(1,heroRepository.findAll().size());
        assertTrue(heroRepository.findById(uuid2).isEmpty());
    }

    private RegisterHeroRequest createRegisterHeroRequest() {
        return RegisterHeroRequest.builder()
                .name("Batman")
                .race("HUMAN")
                .build();
    }

    private RegisterHeroRequest createAnotherRegisterHeroRequest() {
        return RegisterHeroRequest.builder()
                .name("Superman")
                .race("DIVINE")
                .build();
    }

    private RegisterPowerStatsRequest createRegisterPowerStatsRequest() {
        return RegisterPowerStatsRequest.builder()
                .agility(10)
                .dexterity(9)
                .intelligence(8)
                .strength(7)
                .build();
    }

    private RegisterPowerStatsRequest createAnotherRegisterPowerStatsRequest() {
        return RegisterPowerStatsRequest.builder()
                .agility(10)
                .dexterity(10)
                .intelligence(10)
                .strength(10)
                .build();
    }

    private UpdateHeroRequestApiOut createUpdateHeroRequestApiOut() {
        HeroModelApiOut heroModelApiOut = HeroModelApiOut.builder()
                .name("Thor")
                .race("ALIEN")
                .enabled(false)
                .build();

        return new UpdateHeroRequestApiOut(heroModelApiOut);
    }

    private void dbCleaner() {
        heroRepository.findAll().forEach(heroModelApiOut -> heroRepository.delete(heroModelApiOut.getId()));
    }
}