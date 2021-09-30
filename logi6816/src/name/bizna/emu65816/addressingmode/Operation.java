package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

public abstract class Operation
    implements BusCycleParameter
{
  public abstract void execute(Cpu65816 cpu, OpCode opCode);

  @Override
  public boolean isOperation()
  {
    return true;
  }

  @Override
  public boolean isAddress()
  {
    return false;
  }

  public boolean isData()
  {
    return false;
  }
}

