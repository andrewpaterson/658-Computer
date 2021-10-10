package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Pins65816;

public class FetchAbsoluteAddressHigh
    extends DataOperation
{
  public FetchAbsoluteAddressHigh(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    Pins65816 pins = cpu.getPins();
    cpu.setAddressHigh(pins.getData());
  }

  @Override
  public String toString()
  {
    return "Read(AAH)";
  }
}

