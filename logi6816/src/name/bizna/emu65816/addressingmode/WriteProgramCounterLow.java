package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class WriteProgramCounterHigh
    extends DataBusCycleOperation
{
  public WriteProgramCounterHigh()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setPinData(Binary.getHighByte(cpu.getProgramCounter().getOffset()));
  }
}

