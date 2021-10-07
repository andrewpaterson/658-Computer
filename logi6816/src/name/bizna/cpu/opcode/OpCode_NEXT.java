package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_NEXT
    extends OpCode
{
  public OpCode_NEXT(InstructionCycles cycles)
  {
    super("Fetch Opcode", "Fetch Opcode", -1, cycles);
  }
}

