package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_SED
    extends OpCode
{
  public OpCode_SED(int mCode, InstructionCycles cycles)
  {
    super("SED", "Set Decimal Mode", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.getCpuStatus().setDecimalFlag(true);
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

