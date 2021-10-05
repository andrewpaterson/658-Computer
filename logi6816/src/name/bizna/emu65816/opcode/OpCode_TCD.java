package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TCD
    extends OpCode
{
  public OpCode_TCD(int mCode, InstructionCycles cycles)
  {
    super("TCD", "Transfer C Accumulator to Direct Register", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.setDirectPage(cpu.getA());
    cpu.getCpuStatus().setSignAndZeroFlagFrom16BitValue(cpu.getDirectPage());
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

