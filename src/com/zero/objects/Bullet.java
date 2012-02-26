package com.zero.objects;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.zero.main.Manager;

public class Bullet extends Entity {
	
	public static final float BULLET_SPEED_FACTOR = 0.1f;
	public static final float ALIVE_TIME = 0.2f;
	
	private float totalAliveTime = 0;
	private Entity shooter = null;
	
	
	public Bullet(TextureAtlas atlas, String name, Entity shooter) {
		super(atlas, name);
		this.shooter = shooter;
		
		createPhysicsBody();
		if (body != null) {
			body.setUserData(this);
		}
		
		manager.playSound("laser", 4f, 0.3f, false);
		body.setTransform(body.getPosition(), shooter.body.getAngle());
	}
	
	public void updatePosition(float delta) {
		body.applyLinearImpulse(getThrustVector(), body.getWorldCenter());
		
		totalAliveTime += delta;
		
		if (totalAliveTime >= ALIVE_TIME) {
			manager.removeEntity(this);
		}
	}

	private Vector2 getThrustVector() {
		double rads = body.getAngle() + Math.toRadians(90);
		
		//x + d * cos(a)  y + d.sin(a)
		double x = BULLET_SPEED_FACTOR * Math.cos(rads);
		double y = BULLET_SPEED_FACTOR * Math.sin(rads);
		
		Vector2 vector = new Vector2((float)x, (float)y).mul(-1);
		return vector;
	}
	
	@Override
	public void createPhysicsBody() {
		Float placementY = (-shooter.getHeight() / 2 / Manager.PTM) - (sprite.getHeight() / 2 / Manager.PTM);
		
		bodyDef = new BodyDef();
		bodyDef.position.set( shooter.body.getWorldPoint(new Vector2( 0, placementY )));
		bodyDef.type = BodyType.DynamicBody;
		body = manager.getWorld().createBody(bodyDef);
	
		PolygonShape shape = new PolygonShape();
		
		Float halfX = (float)sprite.getWidth() / Manager.PTM / 2;
		Float halfY = (float)sprite.getHeight() / Manager.PTM / 2;
		
		shape.setAsBox(halfX, halfY);
		  
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
	    fixtureDef.density = 0.01f;
	    fixtureDef.friction = 0f;
	    fixtureDef.restitution = 0.0f;
	    fixtureDef.isSensor = true;
	    body.createFixture(fixtureDef);
	    
	    shape.dispose();
		
        body.setLinearDamping(0f);
        body.setAngularDamping(0f);
        body.setFixedRotation(true);
        body.setBullet(true);
	}

	@Override
	public Boolean collision(Entity with) {
		if (with.equals(shooter)) {
			return false;
		}
		with.hit();
		return true;
	}

	@Override
	public void hit() {
	}
}
