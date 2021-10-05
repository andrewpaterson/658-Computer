package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TXY
    extends OpCode
{
  public OpCode_TXY(int mCode, InstructionCycles cycles)
  {
    super("TXY", "Transfer Index X to Index Y", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isIndex8Bit())
    {
      int value = Binary.getLowByte(cpu.getX());
      cpu.setY(Binary.setLowByte(cpu.getY(), value));
      cpu.getCpuStatus().setSignAndZeroFlagFrom8BitValue(value);
    }
    else
    {
      cpu.setY(cpu.getX());
      cpu.getCpuStatus().setSignAndZeroFlagFrom16BitValue(cpu.getY());
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

