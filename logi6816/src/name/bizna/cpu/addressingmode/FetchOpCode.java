package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Pins65816;

public class FetchOpCode
    extends DataOperation
{
  public FetchOpCode()
  {
    super(true, true, true, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    Pins65816 pins = cpu.getPins();
    cpu.setOpCode(pins.getData());
  }

  @Override
  public String toString()
  {
    return "OpCode";
  }
}

