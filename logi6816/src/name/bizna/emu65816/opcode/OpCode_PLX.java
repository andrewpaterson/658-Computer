package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PLX
    extends OpCode
{
  public OpCode_PLX(int mCode, InstructionCycles cycles)
  {
    super("PLX", "Pull Index X from Stack", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isIndex8Bit())
    {
      int value = cpu.pull8Bit();
      cpu.setX(Binary.setLowByte(cpu.getX(), value));
      cpu.getCpuStatus().setSignAndZeroFlagFrom8BitValue(value);
      cpu.addToProgramAddressAndCycles(1, 4);
    }
    else
    {
      cpu.setX(cpu.pull16Bit());
      cpu.getCpuStatus().setSignAndZeroFlagFrom16BitValue(cpu.getX());
      cpu.addToProgramAddressAndCycles(1, 5);
    }
  }
}

