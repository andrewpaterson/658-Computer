package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_TSC
    extends OpCode
{
  public OpCode_TSC(int mCode, InstructionCycles cycles)
  {
    super("TSC", "Transfer Stack Pointer to C Accumulator", mCode, cycles);
  }
}

