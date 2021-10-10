package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Pins65816;

public class FetchAbsoluteAddressBank
    extends DataOperation
{
  public FetchAbsoluteAddressBank(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    Pins65816 pins = cpu.getPins();
    cpu.setAddressBank(pins.getData());
  }

  @Override
  public String toString()
  {
    return "Read(AAB)";
  }
}

