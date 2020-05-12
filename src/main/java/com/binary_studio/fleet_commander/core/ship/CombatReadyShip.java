package com.binary_studio.fleet_commander.core.ship;

import java.util.Optional;

import com.binary_studio.fleet_commander.core.actions.attack.AttackAction;
import com.binary_studio.fleet_commander.core.actions.defence.AttackResult;
import com.binary_studio.fleet_commander.core.actions.defence.RegenerateAction;
import com.binary_studio.fleet_commander.core.common.Attackable;
import com.binary_studio.fleet_commander.core.common.PositiveInteger;
import com.binary_studio.fleet_commander.core.ship.contract.CombatReadyVessel;

public final class CombatReadyShip implements CombatReadyVessel {

	private DockedShip vessel = null;
	private int capacitor;

	public CombatReadyShip(DockedShip vessel){
		this.vessel = vessel;
		capacitor = vessel.getCapacitor().value();
	}

	@Override
	public void endTurn() {
		int capacitor0 = vessel.getCapacitor().value();
		capacitor += vessel.getCapacitorRegeneration().value();
		if(capacitor > capacitor0) capacitor = capacitor0;
	}

	@Override
	public void startTurn() {

	}

	@Override
	public String getName() {
		return vessel.getName();
	}

	@Override
	public PositiveInteger getSize() {
		return vessel.getSize();
	}

	@Override
	public PositiveInteger getCurrentSpeed() {
		return vessel.getSpeed();
	}

	@Override
	public Optional<AttackAction> attack(Attackable target) {
		// capacity consumption
		int cupConsuption = vessel.getAttackSubsystem().getCapacitorConsumption().value();
		if(capacitor < cupConsuption) return Optional.empty();
		capacitor -= cupConsuption;

		return Optional.of(new AttackAction(vessel.getAttackSubsystem().attack(target),
								this, target, vessel.getAttackSubsystem()));
	}

	@Override
	public AttackResult applyAttack(AttackAction attack) {

		return null;
	}

	@Override
	public Optional<RegenerateAction> regenerate() {
		// TODO: Ваш код здесь :)
		return null;
	}

}
