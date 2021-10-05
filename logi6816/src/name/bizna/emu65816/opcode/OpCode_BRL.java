package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_BRL
    extends OpCode
{
  public OpCode_BRL(int mCode, InstructionCycles cycles)
  {
    super("BRL", "Branch Always Long", mCode, cycles);
  }
}

