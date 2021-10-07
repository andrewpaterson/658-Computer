package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_SRA
    extends OpCode
{
  public OpCode_SRA(int mCode, InstructionCycles cycles)
  {
    super("LSR", "Shift accumulator right one bit; update NZC.", mCode, cycles);
  }
}

