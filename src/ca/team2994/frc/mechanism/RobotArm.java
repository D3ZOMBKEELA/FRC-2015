package ca.team2994.frc.mechanism;

import ca.team2994.frc.robot.Constants;
import ca.team2994.frc.robot.Motor;
import ca.team2994.frc.robot.Subsystems;

public class RobotArm {
	
	//Declare your speeds for different parts of the arm usage here
	public double FORWARD_SPEED = Constants.getConstantAsDouble(Constants.ARM_FORWARD_SPEED);
	public double REVERSE_SPEED = Constants.getConstantAsDouble(Constants.ARM_REVERSE_SPEED);
	public double PICKUP_SPEED = Constants.getConstantAsDouble(Constants.ARM_PICKUP_SPEED);
	public double DROPOFF_SPEED = Constants.getConstantAsDouble(Constants.ARM_DROPOFF_SPEED);
	public double LOAD_SPEED = Constants.getConstantAsDouble(Constants.ARM_LOAD_SPEED);
	public double UNLOAD_SPEED = Constants.getConstantAsDouble(Constants.ARM_UNLOAD_SPEED);
	public int DROPOFF_TIME = Constants.getConstantAsInt(Constants.ARM_DROPOFF_TIME);
	public int UNLOAD_TIME = Constants.getConstantAsInt(Constants.ARM_UNLOAD_TIME);
	
	private Motor m_leftArmMotor;
	private Motor m_rightArmMotor;
	
	// State variable to keep track of whether we are in a mode of automatic behaviour,
	// such as running the arm motor until a sensor is triggered.
	private int m_currentMode = 0;

	// Definitions of possible states of the robot arm
	//   0 - no automatic mode (manual)
	//   1 - pick up mode (automatically run motor until tote sensor triggered ON)
	//   2 - load mode (automatically run motor until tote sensor triggered OFF again)
	//
	// TODO: put this in a java.lang.enum instead
	private static final int MANUAL_MODE = 0;
	private static final int PICKUP_MODE = 1;
	private static final int LOAD_MODE = 2;
	
	// Note to Connor: Your challenge is to figure out how to change pickup() and load() to start the automatic
	//                 behavior so it switch into a certain mode when the operator presses a button, and keep
	//                 doing stuff on its own.
	//
	//                 Hint: you will need to use the state variable I made. You will probably also want to
	//                       create a method that gets called by teleopPeriodic() that will check to see if
	//                       the arm needs to do stuff, and do it.
	//
	//                 Have fun!
	// 
	//                 -Kevin
	//
	//                 This message will self destruct. (Delete it when you're done with it)
	
	public RobotArm(Motor leftArmMotor, Motor rightArmMotor) {
		m_leftArmMotor = leftArmMotor;
		m_rightArmMotor = rightArmMotor;
		
		m_currentMode = MANUAL_MODE;
	}

	// Stop the arms from moving
	public void stop() {
		m_leftArmMotor.set(0.0);
		m_rightArmMotor.set(0.0);
		
		// Whatever we were doing before.. we are no longer in any automatic mode
		m_currentMode = MANUAL_MODE;
	}

	public void forward() {
		//Move forward manually
		m_leftArmMotor.set(FORWARD_SPEED);
		m_rightArmMotor.set(FORWARD_SPEED * -1);
		m_currentMode = MANUAL_MODE;
	}

	public void reverse() {
		//Move in reverse manually
		m_leftArmMotor.set(REVERSE_SPEED * -1);
		m_rightArmMotor.set(REVERSE_SPEED);
		m_currentMode = MANUAL_MODE;
	}

	public void pickup() {
		// Use the sensor to know when to run motors when picking up totes
		if (Subsystems.toteDetectionSensor.get() == false) {
			m_leftArmMotor.set(PICKUP_SPEED);
			m_rightArmMotor.set(PICKUP_SPEED * -1);
		
			m_currentMode = PICKUP_MODE;
		}
		if (Subsystems.toteDetectionSensor.get()) {
			stop();
	
		}
	}

	public void dropoff() {
		//Drop off totes on ground
		m_leftArmMotor.set(DROPOFF_SPEED * -1);
		m_rightArmMotor.set(DROPOFF_SPEED);
		m_leftArmMotor.setExpiration(DROPOFF_TIME);
		m_rightArmMotor.setExpiration(DROPOFF_TIME);
		m_currentMode = PICKUP_MODE;
	}

	public void load() {
		//Load totes into storage area
		if (Subsystems.toteDetectionSensor.get()) {
			m_leftArmMotor.set(LOAD_SPEED);
			m_rightArmMotor.set(LOAD_SPEED * -1);
			m_currentMode = LOAD_MODE;
		}
		if (Subsystems.toteDetectionSensor.get() == false) {
			stop();
		}
	}

	public void unload() {
		//Unload totes from storage area
		m_leftArmMotor.set(UNLOAD_SPEED * -1);
		m_leftArmMotor.set(UNLOAD_SPEED);
		m_leftArmMotor.setExpiration(UNLOAD_TIME);
		m_rightArmMotor.setExpiration(UNLOAD_TIME);
		m_currentMode = LOAD_MODE;
	}
    public static void robotArm() {
    	if(Subsystems.controlGamepad.getNumberedButton(Constants.getConstantAsInt(Constants.GAMEPAD_ARM_STOP))) {
    		Subsystems.robotArm.stop();
    	}
    	else if(Subsystems.controlGamepad.getNumberedButton(Constants.getConstantAsInt(Constants.GAMEPAD_ARM_FORWARD))) {
    		Subsystems.robotArm.forward();
    	}
    	else if(Subsystems.controlGamepad.getNumberedButton(Constants.getConstantAsInt(Constants.GAMEPAD_ARM_REVERSE))) {
    		Subsystems.robotArm.reverse();
    	}
    	else if(Subsystems.controlGamepad.getNumberedButton(Constants.getConstantAsInt(Constants.GAMEPAD_ARM_PICKUP))) {
    		Subsystems.robotArm.pickup();
    	}
    	else if(Subsystems.controlGamepad.getNumberedButton(Constants.getConstantAsInt(Constants.GAMEPAD_ARM_DROPOFF))) {
    		Subsystems.robotArm.dropoff();
    	}
    	else if(Subsystems.controlGamepad.getNumberedButton(Constants.getConstantAsInt(Constants.GAMEPAD_ARM_LOAD))) {
    		Subsystems.robotArm.load();
    	}
    	else if(Subsystems.controlGamepad.getNumberedButton(Constants.getConstantAsInt(Constants.GAMEPAD_ARM_UNLOAD))) {
    		Subsystems.robotArm.unload();
    	}
    	else {
    		Subsystems.robotArm.stop();
    	}
    }
}
