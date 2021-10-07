package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_PLB
    extends OpCode
{
  public OpCode_PLB(int mCode, InstructionCycles cycles)
  {
    super("PLB", "Pull Data Bank Register from Stack", mCode, cycles);
  }
}

