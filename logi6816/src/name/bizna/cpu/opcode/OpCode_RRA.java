package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_RRA
    extends OpCode
{
  public OpCode_RRA(int mCode, InstructionCycles cycles)
  {
    super("ROR", "Rotate accumulator right one bit; update NZC.", mCode, cycles);
  }
}

