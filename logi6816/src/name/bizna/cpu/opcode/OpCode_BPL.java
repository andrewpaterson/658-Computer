package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_BPL
    extends OpCode
{
  public OpCode_BPL(int mCode, InstructionCycles cycles)
  {
    super("BPL", "Branch if Result Plus (N=0)", mCode, cycles);
  }
}

