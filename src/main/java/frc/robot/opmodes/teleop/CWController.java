package frc.robot.opmodes.teleop;
import edu.wpi.first.wpilibj.XboxController;

// adds better dpad controls
public class CWController extends XboxController {
    boolean wasUp, wasRight, wasDown, wasLeft, wasLT, wasRT;

    public CWController(int port) {super(port);}

    public boolean getUpButton() {
        return getPOV() == 315 || getPOV() == 0 || getPOV() == 45;
    }
    public boolean getRightButton() {
        return getPOV() == 45 || getPOV() == 90 || getPOV() == 135;
    }
    public boolean getDownButton() {
        return getPOV() == 135 || getPOV() == 180 || getPOV() == 225;
    }
    public boolean getLeftButton() {
        return getPOV() == 225 || getPOV() == 270 || getPOV() == 315;
    }

    public boolean getUpButtonPressed()    {return getUpButton()    && !wasUp;}
    public boolean getRightButtonPressed() {return getRightButton() && !wasRight;}
    public boolean getDownButtonPressed()  {return getDownButton()  && !wasDown;}
    public boolean getLeftButtonPressed()  {return getLeftButton()  && !wasLeft;}

    public boolean getUpButtonReleased()    {return !getUpButton()    && wasUp;}
    public boolean getRightButtonReleased() {return !getRightButton() && wasRight;}
    public boolean getDownButtonReleased()  {return !getDownButton()  && wasDown;}
    public boolean getLeftButtonReleased()  {return !getLeftButton()  && wasLeft;}

    // save the current dpad values to check for press/release
    public void update() {
        wasUp    = getUpButton();
        wasRight = getRightButton();
        wasDown  = getDownButton();
        wasLeft  = getLeftButton();
        wasLT = getLeftTriggerButton();
        wasRT = getRightTriggerButton();
    }

    public boolean getLeftTriggerButton()          {return getLeftTriggerAxis() > 0.5;}
    public boolean getRightTriggerButton()         {return getRightTriggerAxis() > 0.5;}
    public boolean getLeftTriggerButtonPressed()   {return getLeftTriggerButton() && !wasLT;}
    public boolean getRightTriggerButtonPressed()  {return getRightTriggerButton() && !wasRT;}
    public boolean getLeftTriggerButtonReleased()  {return !getLeftTriggerButton() && wasLT;}
    public boolean getRightTriggerButtonReleased() {return !getRightTriggerButton() && wasRT;}

    public boolean getM1Button()         {return getBackButton();}
    public boolean getM1ButtonPressed()  {return getBackButtonPressed();}
    public boolean getM1ButtonReleased() {return getBackButtonReleased();}
    public boolean getM2Button()         {return getStartButton();}
    public boolean getM2ButtonPressed()  {return getStartButtonPressed();}
    public boolean getM2ButtonReleased() {return getStartButtonReleased();}
}