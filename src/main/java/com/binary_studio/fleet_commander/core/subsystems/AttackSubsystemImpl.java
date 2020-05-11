package com.binary_studio.fleet_commander.core.subsystems;

import com.binary_studio.fleet_commander.core.common.Attackable;
import com.binary_studio.fleet_commander.core.common.PositiveInteger;
import com.binary_studio.fleet_commander.core.subsystems.contract.AttackSubsystem;

import static java.lang.Math.min;

public final class AttackSubsystemImpl implements AttackSubsystem {

	private final PositiveInteger pqReq, capacitorConsumption, optimalSpeed, optimalSize, baseDamage;
	private final String name;

	public static AttackSubsystemImpl construct(String name, PositiveInteger powergridRequirments,
			PositiveInteger capacitorConsumption, PositiveInteger optimalSpeed, PositiveInteger optimalSize,
			PositiveInteger baseDamage) throws IllegalArgumentException {

		if(name == null || name.isBlank()) throw new IllegalArgumentException ("Name should be not null and not empty");
		return new AttackSubsystemImpl (name, powergridRequirments,
				 capacitorConsumption,  optimalSpeed,
				 optimalSize,
				 baseDamage);
	}

	private AttackSubsystemImpl (String name, PositiveInteger pgReq,
								 PositiveInteger capacitorConsumption, PositiveInteger optimalSpeed,
								 PositiveInteger optimalSize,
								 PositiveInteger baseDamage) {
		this.pqReq = pgReq;
		this.capacitorConsumption = capacitorConsumption;
		this.optimalSpeed = optimalSpeed;
		this.optimalSize = optimalSize;
		this.baseDamage = baseDamage;
		this.name = name;
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
	public PositiveInteger attack(Attackable target) {
		double sizeReductionModifier, speedReductionModifier;

		int targetSpeed = target.getCurrentSpeed().value();
		int targetSize = target.getSize().value();

		sizeReductionModifier = (targetSize >= optimalSize.value()) ?  1 :
				(double)targetSize / optimalSize.value();
		speedReductionModifier = (targetSpeed <= optimalSpeed.value()) ?  1 :
				(double)optimalSpeed.value() / (2 * targetSpeed);

		return PositiveInteger.of((int)Math.ceil(baseDamage.value() * min(sizeReductionModifier, speedReductionModifier)));
	}

	@Override
	public String getName() {
		return name;
	}

}
