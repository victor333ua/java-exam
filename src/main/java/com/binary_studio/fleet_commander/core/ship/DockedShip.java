package com.binary_studio.fleet_commander.core.ship;

import com.binary_studio.fleet_commander.core.common.PositiveInteger;
import com.binary_studio.fleet_commander.core.exceptions.InsufficientPowergridException;
import com.binary_studio.fleet_commander.core.exceptions.NotAllSubsystemsFitted;
import com.binary_studio.fleet_commander.core.ship.contract.ModularVessel;
import com.binary_studio.fleet_commander.core.subsystems.contract.AttackSubsystem;
import com.binary_studio.fleet_commander.core.subsystems.contract.DefenciveSubsystem;

public final class DockedShip implements ModularVessel {

	private String name;

	private PositiveInteger shieldHP;

	private PositiveInteger hullHP;

	private PositiveInteger capacitor;

	private PositiveInteger capacitorRegeneration;

	private PositiveInteger pg;

	private PositiveInteger speed;

	private PositiveInteger size;

	public static DockedShip construct(String name, PositiveInteger shieldHP, PositiveInteger hullHP,
			PositiveInteger powergridOutput, PositiveInteger capacitorAmount, PositiveInteger capacitorRechargeRate,
			PositiveInteger speed, PositiveInteger size) {

		return new DockedShip(name, shieldHP,  hullHP,
				 powergridOutput,  capacitorAmount,  capacitorRechargeRate,
				 speed,  size);
	}

	private DockedShip(String name, PositiveInteger shieldHP, PositiveInteger hullHP,
					   PositiveInteger powergridOutput, PositiveInteger capacitorAmount, PositiveInteger capacitorRechargeRate,
					   PositiveInteger speed, PositiveInteger size){

		this.name = name;

		this.shieldHP = shieldHP;

		this.hullHP = hullHP;

		this.capacitor = capacitorAmount;

		this.capacitorRegeneration = capacitorRechargeRate;

		this.pg = powergridOutput;

		this.speed = speed;

		this.size = size;

	}

	@Override
	public void fitAttackSubsystem(AttackSubsystem subsystem) throws InsufficientPowergridException {
		int missingMW = subsystem.getPowerGridConsumption().value() - pg.value();
		if(missingMW > 0)
			throw new InsufficientPowergridException(missingMW);
	}

	@Override
	public void fitDefensiveSubsystem(DefenciveSubsystem subsystem) throws InsufficientPowergridException {
		Integer missingMW = subsystem.getPowerGridConsumption().value() - pg.value();
		if(missingMW > 0)
			throw new InsufficientPowergridException(missingMW);
		pg = PositiveInteger.of(pg.value() + missingMW);

	}

	public CombatReadyShip undock() throws NotAllSubsystemsFitted {
		// TODO: Ваш код здесь :)
		return null;
	}

}
