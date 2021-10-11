package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

public class FetchAbsoluteAddressLow
    extends DataOperation
{
  public FetchAbsoluteAddressLow(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    int data = cpu.getPins().getData();
    cpu.setAddressLow(data);
  }

  @Override
  public String toString()
  {
    return "Read(AAL)";
  }
}

