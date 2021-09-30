package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

public class Fetch_AAL
    extends DataOperation
{
  public Fetch_AAL(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
    cpu.setAddressLow(cpu.getPinData());
  }
}

