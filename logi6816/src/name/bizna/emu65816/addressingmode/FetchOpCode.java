package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

public class FetchOpCode
    extends DataOperation
{
  public FetchOpCode()
  {
    super(true, true, true, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
    //Special case that is handled directly in the Cpu65816 class.
  }
}

