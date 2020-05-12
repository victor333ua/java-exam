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

	//---------------------------------------------------------

	private AttackSubsystem attackSubsystem = null;
	private DefenciveSubsystem defenciveSubsystem = null;

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

	public String getName() {
		return name;
	}

	public PositiveInteger getShieldHP() {
		return shieldHP;
	}

	public PositiveInteger getHullHP() {
		return hullHP;
	}

	public PositiveInteger getCapacitor() {
		return capacitor;
	}

	public PositiveInteger getCapacitorRegeneration() {
		return capacitorRegeneration;
	}

	public PositiveInteger getPg() {
		return pg;
	}

	public PositiveInteger getSpeed() {
		return speed;
	}

	public PositiveInteger getSize() {
		return size;
	}

	public AttackSubsystem getAttackSubsystem() {
		return attackSubsystem;
	}

	public DefenciveSubsystem getDefenciveSubsystem() {
		return defenciveSubsystem;
	}

	@Override
	public void fitAttackSubsystem(AttackSubsystem subsystem) throws InsufficientPowergridException {

		if (subsystem == null){
			if (attackSubsystem != null)
				pg = pg.plus(attackSubsystem.getPowerGridConsumption());
			attackSubsystem = null;
			return;
		}
	// refit
		if(attackSubsystem != null)
			pg = pg.plus(attackSubsystem.getPowerGridConsumption());

	// does it have enough power
		int missingMW = subsystem.getPowerGridConsumption().value() - pg.value();
		if(missingMW > 0)
			throw new InsufficientPowergridException(missingMW);

		pg = PositiveInteger.of(pg.value() - missingMW);
		attackSubsystem = subsystem;
	}

	@Override
	public void fitDefensiveSubsystem(DefenciveSubsystem subsystem) throws InsufficientPowergridException {

	// disconnect
		if (subsystem == null){
			if (defenciveSubsystem != null)
				pg = pg.plus(defenciveSubsystem.getPowerGridConsumption());
			defenciveSubsystem = null;
			return;
		}

	// refit
		if(defenciveSubsystem != null)
			pg = pg.plus(defenciveSubsystem.getPowerGridConsumption());

	// does it have enough power
		int missingMW = subsystem.getPowerGridConsumption().value() - pg.value();
		if(missingMW > 0)
			throw new InsufficientPowergridException(missingMW);

		defenciveSubsystem = subsystem;
		pg = PositiveInteger.of(pg.value() - missingMW);
	}

	public CombatReadyShip undock() throws NotAllSubsystemsFitted {
		if(defenciveSubsystem == null & attackSubsystem == null) throw NotAllSubsystemsFitted.bothMissing();
		if(defenciveSubsystem == null & attackSubsystem != null) throw NotAllSubsystemsFitted.defenciveMissing();
		if(attackSubsystem == null) throw NotAllSubsystemsFitted.attackMissing();

		return new CombatReadyShip(this);
	}

}
