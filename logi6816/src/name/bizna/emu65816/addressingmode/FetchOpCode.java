package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class OpCodeData
    extends Data
{
  public OpCodeData()
  {
    super(true, true, true, true, true);
  }

  @Override
  public void executeRead(Cpu65816 cpu, int data)
  {
    //Special case that is handled directly in the Cpu65816 class.
  }
}

