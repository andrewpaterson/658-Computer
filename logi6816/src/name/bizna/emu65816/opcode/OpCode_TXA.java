package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TXA
    extends OpCode
{
  public OpCode_TXA(int mCode, InstructionCycles cycles)
  {
    super("TXA", "Transfer Index X to Accumulator", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isMemory8Bit() && cpu.isIndex8Bit())
    {
      int value = Binary.getLowByte(cpu.getX());
      cpu.setA(Binary.setLowByte(cpu.getA(), value));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
    }
    else if (cpu.isMemory8Bit() && cpu.isIndex16Bit())
    {
      int value = Binary.getLowByte(cpu.getX());
      cpu.setA(Binary.setLowByte(cpu.getA(), value));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
    }
    else if (cpu.isMemory16Bit() && cpu.isIndex8Bit())
    {
      int value = Binary.getLowByte(cpu.getX());
      cpu.setA(value);
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
    }
    else
    {
      cpu.setA(cpu.getX());
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getA());
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

