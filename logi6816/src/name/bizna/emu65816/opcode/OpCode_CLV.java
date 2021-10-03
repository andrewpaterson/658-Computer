package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_CLV
    extends OpCode
{
  public OpCode_CLV(int mCode, InstructionCycles cycles)
  {
    super("CLV", "Clear Overflow Flag", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.getCpuStatus().setOverflowFlag(false);
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

