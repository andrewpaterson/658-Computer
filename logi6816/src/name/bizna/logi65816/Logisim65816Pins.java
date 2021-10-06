package name.bizna.logi65816;

import com.cburch.logisim.instance.InstanceState;
import name.bizna.emu65816.Pins;

public class Logisim65816Pins
    extends Pins
{
  private InstanceState state;

  public Logisim65816Pins(InstanceState state)
  {
    this.state = state;
  }
}

