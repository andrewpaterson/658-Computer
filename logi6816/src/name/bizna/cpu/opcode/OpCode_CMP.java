package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_CMP
    extends OpCode
{
  public OpCode_CMP(int mCode, InstructionCycles cycles)
  {
    super("CMP", "Compare Memory and Accumulator", mCode, cycles);
  }
}

