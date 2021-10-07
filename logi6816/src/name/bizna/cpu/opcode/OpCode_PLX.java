package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_PLX
    extends OpCode
{
  public OpCode_PLX(int mCode, InstructionCycles cycles)
  {
    super("PLX", "Pull Index X from Stack", mCode, cycles);
  }
}

