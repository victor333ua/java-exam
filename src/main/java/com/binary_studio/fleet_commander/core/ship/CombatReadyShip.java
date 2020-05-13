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
	private PositiveInteger hullValue;
	private PositiveInteger shieldValue;

	public CombatReadyShip(DockedShip vessel){
		this.vessel = vessel;
		capacitor = vessel.getCapacitor().value();
		hullValue = vessel.getHullHP();
		shieldValue = vessel.getShieldHP();
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

		AttackAction attackAction = vessel.getDefenciveSubsystem().reduceDamage(attack);

		PositiveInteger damage = attackAction.damage;

		if(shieldValue.value() > damage.value()) shieldValue = shieldValue.minus(damage);
		else {
			int iHullValue = hullValue.plus(shieldValue).value() - damage.value();
			if(iHullValue <= 0)
				return new AttackResult.Destroyed();
			hullValue = PositiveInteger.of(iHullValue);
			shieldValue = PositiveInteger.of(0);
		}
		return new AttackResult.DamageRecived(attackAction.weapon, attackAction.damage, attackAction.target);
	}

	@Override
	public Optional<RegenerateAction> regenerate() {
		// capacity consumption
		int cupConsuption = vessel.getDefenciveSubsystem().getCapacitorConsumption().value();
		if(capacitor < cupConsuption) return Optional.empty();

		// delta for regeneration
		RegenerateAction regenerateAction = vessel.getDefenciveSubsystem().regenerate();
		PositiveInteger deltaHull = regenerateAction.hullHPRegenerated;
		PositiveInteger deltaShield = regenerateAction.shieldHPRegenerated;
		PositiveInteger hull0 =  vessel.getHullHP();
		PositiveInteger shield0 = vessel.getShieldHP();

		capacitor -= cupConsuption;

		if(hullValue == vessel.getHullHP() && shieldValue == vessel.getShieldHP())
			return Optional.of(new RegenerateAction(PositiveInteger.of(0), PositiveInteger.of(0)));

		if(hullValue == hull0) deltaHull = PositiveInteger.of(0);
		else {
			if(hullValue.plus(deltaHull).value() > hull0.value()){
				deltaHull = hull0.minus(hullValue);
				hullValue = hull0;
			}
			else
				hullValue = hullValue.plus(deltaHull);
		}
		if(shieldValue == shield0) deltaShield = PositiveInteger.of(0);
		else {
			if(shieldValue.plus(deltaShield).value() > shield0.value()){
				deltaShield = shield0.minus(shieldValue);
				shieldValue = shield0;
			}
			else
				shieldValue = shieldValue.plus(deltaHull);
		}

		return Optional.of(new RegenerateAction(deltaShield, deltaHull));
	}
}
