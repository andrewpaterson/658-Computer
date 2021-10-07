package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_CLC
    extends OpCode
{
  public OpCode_CLC(int mCode, InstructionCycles cycles)
  {
    super("CLC", "Clear Carry Flag", mCode, cycles);
  }
}

