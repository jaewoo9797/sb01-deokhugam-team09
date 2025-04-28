package com.codeit.sb01_deokhugam.ranking.poweruser.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.sb01_deokhugam.global.enumType.Period;
import com.codeit.sb01_deokhugam.ranking.poweruser.entity.PowerUser;

public interface PowerUserRepository extends JpaRepository<PowerUser, UUID> {

	long countByPeriod(Period period);
}
