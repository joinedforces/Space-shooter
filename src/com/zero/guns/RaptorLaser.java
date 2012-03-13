package com.zero.guns;

import com.badlogic.gdx.math.Vector2;
import com.zero.ammunition.Ammunition;
import com.zero.ammunition.Beam;
import com.zero.main.ResourceCache;
import com.zero.ships.Ship;
import com.zero.spaceshooter.actors.ManagerActor;

public class RaptorLaser extends Gun {

	public RaptorLaser(Ship owner) {
		super(owner);
	}

	@Override
	public float getEnergyUse() {
		return 1f;
	}

	@Override
	public float getDamageModifier() {
		return 0.5f;
	}

	@Override
	public Class<? extends Ammunition> getAmmunitionClass() {
		return Beam.class;
	}

	@Override
	public Vector2 getNozzlePosition(float bulletHeight) {
		Vector2 position = new Vector2();
		position.x = 0f;
		position.y = - owner.getSize().y / ManagerActor.PTM / 2 - bulletHeight / ManagerActor.PTM;
		position = owner.getBody().getWorldPoint(position);
		
		return position;
	}

	@Override
	public float getNozzleAngle() {
		return owner.getBody().getAngle();
	}

	@Override
	public void loadSound() {
		shotSound = ResourceCache.getInstance().getSound("laser-sound");
	}

	@Override
	public void playSound() {
		long id = shotSound.play(0.3f);
		shotSound.setPitch(id, 4f);		
	}

	@Override
	public float getTimeToReload() {
		// TODO Auto-generated method stub
		return 0.2f;
	}
}
