package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class FetchImmediateDataLow
    extends DataBusCycleOperation
{
  public FetchImmediateDataLow()
  {
    super(false, true, true, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setImmediateLow(cpu.getPinData());
  }
}

