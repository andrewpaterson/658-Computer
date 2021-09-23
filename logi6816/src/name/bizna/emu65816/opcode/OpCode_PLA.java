package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PLA
    extends OpCode
{
  public OpCode_PLA(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    if (cpu.accumulatorIs8BitWide())
    {
      cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), cpu.getStack().pull8Bit(cpu)));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue((byte) cpu.getA());
      cpu.addToProgramAddressAndCycles(1, 4);
    }
    else
    {
      cpu.setA(cpu.getStack().pull16Bit(cpu));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getA());
      cpu.addToProgramAddressAndCycles(1, 5);
    }
  }
}

