package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_BRL
    extends OpCode
{
  public OpCode_BRL(int mCode, InstructionCycles cycles)
  {
    super("BRL", "Branch Always Long", mCode, cycles);
  }
}

