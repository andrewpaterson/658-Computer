package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_TDC
    extends OpCode
{
  public OpCode_TDC(int mCode, InstructionCycles cycles)
  {
    super("TDC", "Transfer Direct Register to C Accumulator", mCode, cycles);
  }
}

