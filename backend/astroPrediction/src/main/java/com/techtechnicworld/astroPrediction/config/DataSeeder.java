package com.techtechnicworld.astroPrediction.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.techtechnicworld.astroPrediction.entity.Role;
import com.techtechnicworld.astroPrediction.repository.RoleRepository;
import com.techtechnicworld.enums.RoleName;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements ApplicationRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) {
        seedRoles();
    }

    private void seedRoles() {
        Set<RoleName> all = new HashSet<>(Arrays.asList(RoleName.values()));
        Set<RoleName> existing = roleRepository.findExistingByNames(all);

        List<Role> missing = all.stream()
                .filter(r -> !existing.contains(r))
                .map(r -> Role.builder().name(r).build())
                .toList();

        if (!missing.isEmpty()) {
            roleRepository.saveAll(missing);
            missing.forEach(r -> log.info("Seeded role: {}", r.getName()));
        }
    }
}
