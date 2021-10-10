package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Pins65816;

public class FetchDirectOffset
    extends DataOperation
{
  public FetchDirectOffset(boolean notMemoryLock)
  {
    super(true, false, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    Pins65816 pins = cpu.getPins();
    cpu.setDirectOffset(pins.getData());
  }


  @Override
  public String toString()
  {
    return "Read(D0)";
  }
}

