package com.binary_studio.fleet_commander.core.subsystems;

import com.binary_studio.fleet_commander.core.actions.attack.AttackAction;
import com.binary_studio.fleet_commander.core.actions.defence.RegenerateAction;
import com.binary_studio.fleet_commander.core.common.PositiveInteger;
import com.binary_studio.fleet_commander.core.subsystems.contract.DefenciveSubsystem;

public final class DefenciveSubsystemImpl implements DefenciveSubsystem {

	private final String name;
	private final PositiveInteger pqReq, capacitorConsumption;
	private final PositiveInteger impactReductionPercent;
	private final PositiveInteger shieldRegeneration;
	private final PositiveInteger hullRegeneration;


	private DefenciveSubsystemImpl (String name, PositiveInteger powergridConsumption,
									PositiveInteger capacitorConsumption, PositiveInteger impactReductionPercent,
									PositiveInteger shieldRegeneration, PositiveInteger hullRegeneration) {

		this.pqReq = powergridConsumption;
		this.capacitorConsumption = capacitorConsumption;
		this.name = name;
		this.impactReductionPercent = impactReductionPercent;
		this.shieldRegeneration = shieldRegeneration;
		this.hullRegeneration = hullRegeneration;
	}

	public static DefenciveSubsystemImpl construct(String name, PositiveInteger powergridConsumption,
			PositiveInteger capacitorConsumption, PositiveInteger impactReductionPercent,
			PositiveInteger shieldRegeneration, PositiveInteger hullRegeneration) throws IllegalArgumentException {

		if(name == null || name.isBlank()) throw new IllegalArgumentException ("Name should be not null and not empty");
		return new DefenciveSubsystemImpl (name, powergridConsumption,
				capacitorConsumption, impactReductionPercent,
				shieldRegeneration, hullRegeneration );
	}

	@Override
	public PositiveInteger getPowerGridConsumption() {
		return pqReq;
	}

	@Override
	public PositiveInteger getCapacitorConsumption() {
		return capacitorConsumption;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public AttackAction reduceDamage(AttackAction incomingDamage) {
		int incDam = incomingDamage.damage.value();
		int reducedDamage = (int)Math.ceil(incDam * (1. - impactReductionPercent.value()/100.));
		int minDamage = (int)Math.ceil(incDam * 0.05);
		if(reducedDamage < minDamage) reducedDamage = minDamage;

		return new AttackAction(PositiveInteger.of(reducedDamage),
								incomingDamage.attacker,
								incomingDamage.target,
								incomingDamage.weapon
								);
	}

	@Override
	public RegenerateAction regenerate() {
		// amounts of regeneration
		return new RegenerateAction(shieldRegeneration, hullRegeneration);
	}
}
